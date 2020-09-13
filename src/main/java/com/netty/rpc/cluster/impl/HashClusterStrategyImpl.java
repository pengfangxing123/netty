package com.netty.rpc.cluster.impl;

import com.netty.rpc.cluster.ClusterStrategy;
import com.netty.rpc.common.entity.ProviderService;
import com.netty.rpc.utils.IPHelper;

import java.util.List;

/**
 * 软负载哈希算法实现
 *
 * @author liyebing created on 17/4/23.
 * @version $Id$
 */
public class HashClusterStrategyImpl implements ClusterStrategy {

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //获取调用方ip
        String localIP = IPHelper.localIp();
        //获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        //获取服务列表大小
        int size = providerServices.size();

        return providerServices.get(hashCode % size);
    }
}
