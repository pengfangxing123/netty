package com.netty.rpc.regist.provider;



import com.alibaba.dubbo.remoting.zookeeper.StateListener;
import com.alibaba.dubbo.remoting.zookeeper.curator.CuratorZookeeperClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netty.rpc.common.entity.InvokerService;
import com.netty.rpc.common.entity.ProviderService;
import com.netty.rpc.regist.IRegisterCenter4Governance;
import com.netty.rpc.regist.RpcZookeeperClient;
import com.netty.rpc.utils.IPHelper;
import com.netty.rpc.utils.PropertyConfigeHelper;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心实现
 * @author 86136
 */
public class RegisterCenterProvider implements  IRegisterCenterProvider, IRegisterCenter4Governance {
    private static final Logger logger = LoggerFactory.getLogger(RegisterCenterProvider.class);

    private static RegisterCenterProvider registerCenter = new RegisterCenterProvider();

    /**
     * 服务提供者列表,Key:服务提供者接口  value:服务提供者服务方法列表
     */
    private static final Map<String, List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();

    private static String ROOT_PATH = "/config_register";
    public static String PROVIDER_TYPE = "provider";

    public RpcZookeeperClient client;

    private RegisterCenterProvider() {
        client=new RpcZookeeperClient(this);
    }

    public static RegisterCenterProvider singleton() {
        return registerCenter;
    }



    @Override
    public void registerProvider(List<ProviderService> serviceMetaData) throws Exception {
        if(CollectionUtils.isEmpty(serviceMetaData)){
            logger.info("[注册中心]-[注册提供者元数据信息]---没有需要注册的数据");
            return ;
        }
        logger.info("[注册中心]-[注册提供者元数据信息]---开始注册");

        //将当前对象的元数据放到Map中，用来调用的时候找到对应的执行对象和zookeeper重连补偿
        ProviderService service = serviceMetaData.get(0);
        String serviceItfKey = service.getServiceItf().getName();
        List<ProviderService> providers = providerServiceMap.get(serviceItfKey);
        if (providers == null) {
            providers = Lists.newArrayList();
        }
        providers.addAll(serviceMetaData);
        providerServiceMap.put(serviceItfKey, providers);

        //执行注册
        doRegister(serviceItfKey,service);
        logger.info("[注册中心]-[注册提供者元数据信息]---注册完成");
    }

    /**
     * zookeeper重连补偿动作
     * zookeeper连接丢失，临时节点会被删除，重连后重新注册
     */
    @Override
    public void regist() {
        logger.info("[注册中心]-[zookeeper重连补偿]---开始注册");
        Map<String, List<ProviderService>> map=this.getProviderServiceMap();
        if(CollectionUtils.isEmpty(map)){
            logger.info("[注册中心]-[zookeeper重连补偿]---没有已注册的数据");
            return ;
        }
        map.forEach((key,val)->{
            if(CollectionUtils.isEmpty(val)){
                logger.info("[注册中心]-[zookeeper重连补偿]---{},没有provderService信息",key);
                return;
            }
            ProviderService providerService = val.get(0);
            try {
                doRegister(key, providerService);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        logger.info("[注册中心]-[zookeeper重连补偿]---注册完成");
    }

    /**
     * 注册
     * @param key
     * @param providerService
     * @throws Exception
     */
    private void doRegister(String key, ProviderService providerService) throws Exception {
        String appKey = providerService.getAppKey();
        String zkPath = ROOT_PATH + "/" + appKey;

        //创建服务提供者
        String groupName = providerService.getGroupName();
        String servicePath = zkPath + "/" + groupName + "/" + key + "/" + PROVIDER_TYPE;

        //创建当前服务器节点
        //服务端口
        int serverPort = providerService.getServerPort();
        //服务权重
        int weight = providerService.getWeight();
        //服务工作线程
        int workerThreads = providerService.getWorkerThreads();
        String localIp = IPHelper.localIp();
        String currentServiceIpNode = servicePath + "/" + localIp + "|" + serverPort + "|" + weight + "|" + workerThreads + "|" + groupName;


        if(!client.checkEx(currentServiceIpNode)){
            //create(currentServiceIpNode,true);
            client.creatingParentEph(currentServiceIpNode);
        }

    }


    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return providerServiceMap;
    }

    @Override
    public Pair<List<ProviderService>, List<InvokerService>> queryProvidersAndInvokers(String serviceName, String appKey) {
        return null;
    }
}
