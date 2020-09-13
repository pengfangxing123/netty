package com.netty.rpc.revoker;

import com.netty.rpc.netty.NettyServer;
import com.netty.rpc.regist.revoker.RegisterCenterInvoker;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 服务调用工厂类
 * @author 86136
 */
public class RevokerFactoryBean implements FactoryBean, InitializingBean {


    /**
     * 服务接口
     */
    private Class<?> targetInterface;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 服务bean
     */
    private Object serviceObject;
    /**
     * 负载均衡策略
     */
    private String clusterStrategy;
    /**
     * 服务提供者唯一标识
     */
    private String remoteAppKey;
    /**
     * 服务分组组名
     */
    private String groupName = "default";


    @Override
    public Object getObject() throws Exception {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //仿照dubbo写，可以提供一个延时初始化serviceObject的判断，这里没实现
        get();
    }

    public synchronized Object get() {
        if (serviceObject == null) {
            init();
        }
        return serviceObject;
    }

    private void init() {
        //获取当前interface对应的provider信息,并根据provider信息初始化连接
        RegisterCenterInvoker registerCenter = RegisterCenterInvoker.singleton();
        try {
            registerCenter.doRefer(targetInterface,remoteAppKey,groupName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RevokerProxyBean proxyBean = new RevokerProxyBean(targetInterface, timeout, clusterStrategy);
        serviceObject = proxyBean.getProxy();
    }

    public Class<?> getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public String getClusterStrategy() {
        return clusterStrategy;
    }

    public void setClusterStrategy(String clusterStrategy) {
        this.clusterStrategy = clusterStrategy;
    }

    public String getRemoteAppKey() {
        return remoteAppKey;
    }

    public void setRemoteAppKey(String remoteAppKey) {
        this.remoteAppKey = remoteAppKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
