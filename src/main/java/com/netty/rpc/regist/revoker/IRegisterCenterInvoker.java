package com.netty.rpc.regist.revoker;

import com.netty.rpc.common.entity.InvokerService;
import com.netty.rpc.common.entity.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * 消费端注册中心接口
 * 对外提供获取注册信息
 * @author 86136
 */
public interface IRegisterCenterInvoker {

    /**
     * 消费端获取服务提供者信息
     *
     * @return
     */
    public Map<String, List<ProviderService>> getServiceMetaDataMap4Consume();


    /**
     * 消费端将消费者信息注册到zk对应的节点下
     *
     * @param invoker
     */
    public void registerInvoker(final InvokerService invoker);


}
