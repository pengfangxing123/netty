package com.netty.rpc.regist.revoker;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netty.rpc.common.entity.InvokerService;
import com.netty.rpc.common.entity.ProviderService;
import com.netty.rpc.regist.IRegisterCenter4Governance;
import com.netty.rpc.regist.RpcZookeeperClient;
import com.netty.rpc.revoker.NettyChannelPoolFactory;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 86136
 */
public class RegisterCenterInvoker implements IRegisterCenterInvoker , IRegisterCenter4Governance {
    private static final Logger logger = LoggerFactory.getLogger(RegisterCenterInvoker.class);

    private static RegisterCenterInvoker registerCenter = new RegisterCenterInvoker();

    private static final Map<String, List<ProviderService>> serviceMetaDataMap = Maps.newConcurrentMap();
    private static String ROOT_PATH = "/config_register";
    public static String PROVIDER_TYPE = "provider";
    public static String INVOKER_TYPE = "consumer";

    public RpcZookeeperClient client;
    private NettyChannelPoolFactory channelPoolFactory;

    public RegisterCenterInvoker() {
        client=new RpcZookeeperClient(this);
        channelPoolFactory=NettyChannelPoolFactory.singleton();
    }

    public static RegisterCenterInvoker singleton() {
        return registerCenter;
    }


    public void doRefer(final Class type ,String remoteAppKey ,String groupName) throws Exception {
        //获取对应interface的provider
        final Map<String, List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();

        //从ZK获取服务提供者列表
        String zkPath = ROOT_PATH + "/" + remoteAppKey;
        String key = type.getName();
        String servicePath = zkPath + "/" + groupName + "/" + key + "/" + PROVIDER_TYPE;

        //防止provider没注册，没有这个目录，不过下面的addChildListener把没有这个节点的异常catch了，不加也没大多关系
        client.create(servicePath, false);
        List<String> childrenPaths = client.addChildListener(servicePath, new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                List<String> childrenPaths = client.addChildListener(event.getPath(), this);
                nodeNotify(event.getPath(),childrenPaths,type);
            }
        });

        nodeNotify(servicePath,childrenPaths, type);
    }

    public void nodeNotify(String parentPath,List<String> childrenPaths,Class type) {

        if(CollectionUtils.isEmpty(childrenPaths)){
            logger.info("[invoker注册中心]-[处理注册中心provider信息]---{}没有找到provider信息",parentPath);
        }else {
            //获取已有的provider信息
            List<ProviderService> oldList = serviceMetaDataMap.get(type.getName());
            if(CollectionUtils.isEmpty(oldList)){
                oldList = Lists.newArrayList();
            }

            Set<String> collect = oldList.stream().map(p -> p.getServerIp() + p.getServerPort()).collect(Collectors.toSet());
            List<ProviderService> newList = Lists.newArrayList();
            List<ProviderService> addList = Lists.newArrayList();

            childrenPaths.forEach(p -> {
                String serverIp = StringUtils.split(p, "|")[0];
                String serverPort = StringUtils.split(p, "|")[1];

                int weight = Integer.parseInt(StringUtils.split(p, "|")[2]);
                int workerThreads = Integer.parseInt(StringUtils.split(p, "|")[3]);
                String group = StringUtils.split(p, "|")[4];

                ProviderService providerService = new ProviderService();
                providerService.setServiceItf(type);
                providerService.setServerIp(serverIp);
                providerService.setServerPort(Integer.parseInt(serverPort));
                providerService.setWeight(weight);
                providerService.setWorkerThreads(workerThreads);
                providerService.setGroupName(group);
                newList.add(providerService);
                if(collect.contains(serverIp+serverPort)){
                    logger.info("[invoker注册中心]-[处理注册中心provider信息]---{}：{}该provider已经初始化",parentPath,
                            serverIp+serverPort);

                }else{
                    addList.add(providerService);
                }

                serviceMetaDataMap.put(type.getName(),newList);
            });

            if(addList.size()>0){
                logger.info("[invoker注册中心]-[处理注册中心provider信息]---处理{}新增provider，共[{}]个",parentPath, addList.size());
                channelPoolFactory.initChannelPool(addList);
            }else{
                logger.info("[invoker注册中心]-[处理注册中心provider信息]---{}：没有新增provider",parentPath);
            }
        }
    }


    @Override
    public Map<String, List<ProviderService>> getServiceMetaDataMap4Consume() {
        return serviceMetaDataMap;
    }

    @Override
    public void registerInvoker(InvokerService invoker) {

    }

    @Override
    public Pair<List<ProviderService>, List<InvokerService>> queryProvidersAndInvokers(String serviceName, String appKey) {
        return null;
    }

    @Override
    public void regist() {

    }
}
