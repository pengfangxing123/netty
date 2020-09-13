package com.netty.rpc.provder;

import com.google.common.collect.Lists;
import com.netty.rpc.common.entity.ProviderService;
import com.netty.rpc.netty.NettyServer;
import com.netty.rpc.regist.provider.RegisterCenterProvider;
import com.netty.rpc.utils.IPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务发布
 */
public class ProviderBean implements InitializingBean , ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ProviderBean.class);

    private ApplicationContext applicationContext;
    /**
     * 服务实现得接口
     */
    private Class<?> serviceItf;
    /**
     * 服务实现对象
     */
    private Object serviceObject;

    /**
     * 服务端口
     */
    private String serverPort;
    /**
     * 服务超时时间
     */
    private long timeout;

    /**
     * 服务提供者唯一标识
     */
    private String appKey;

    /**
     * 服务分组组名
     */
    private String groupName = "default";

    /**
     * 服务提供者权重,默认为1 ,范围为[1-100]
     */
    private int weight = 1;

    /**
     * 服务端线程数,默认10个线程
     */
    private int workerThreads = 10;

    private ApplicationConfig applicationConfig;

    private transient volatile boolean exported;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(exported){
            logger.info("[ProviderBean]-[bean服务导出]---{}已经导出",serviceItf.getName());
            return;
        }
        //这个为啥这样获取，而不是放到对象中属性填充时直接放入，是为了避免每个对象在配置中都要指定applicationConfig实例
        applicationConfig = (ApplicationConfig) applicationContext.getBean("applicationConfig");

        //启动Netty服务端
        NettyServer.singleton().start(applicationConfig.getServerPort());

        //获取该接口，元数据
        List<ProviderService> providerServiceList = buildProviderServiceInfos();

        //注册到zk,元数据注册中心
        RegisterCenterProvider.singleton().registerProvider(providerServiceList);

        exported=true;
    }

    private List<ProviderService> buildProviderServiceInfos() {

        List<ProviderService> providerList = Lists.newArrayList();

        //获取接口得全部方法(不能获取实现类的，因为实现类可能有其他的方法)
        Method[] declaredMethods = serviceItf.getDeclaredMethods();
        for(Method method:declaredMethods){
            ProviderService providerService = new ProviderService();
            providerService.setServiceItf(serviceItf);
            providerService.setServiceObject(serviceObject);
            providerService.setServerIp(IPHelper.localIp());
            providerService.setServerPort(Integer.parseInt(applicationConfig.getServerPort()));
            providerService.setTimeout(applicationConfig.getTimeout());
            providerService.setServiceMethod(method);
            providerService.setWeight(weight);
            providerService.setWorkerThreads(workerThreads);
            providerService.setAppKey(applicationConfig.getAppKey());
            providerService.setGroupName(groupName);
            providerList.add(providerService);
        }
        return providerList;
    }


    public Class<?> getServiceItf() {
        return serviceItf;
    }

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
