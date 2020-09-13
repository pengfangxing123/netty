package com.netty.rpc.regist;


import com.netty.rpc.common.entity.InvokerService;
import com.netty.rpc.common.entity.ProviderService;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 服务治理接口
 *
 * @author liyebing created on 17/4/26.
 * @version $Id$
 */
public interface IRegisterCenter4Governance {

    /**
     * 获取服务提供者列表与服务消费者列表
     *
     * @param serviceName
     * @param appKey
     * @return
     */
    Pair<List<ProviderService>, List<InvokerService>> queryProvidersAndInvokers(String serviceName, String appKey);

    /**
     * 临时节点，断开补偿操作
     */
    void regist();
}
