package com.netty.rpc.regist.provider;

import com.netty.rpc.common.entity.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * 注册中心接口
 * 对外提供注册，以及获取当前节点注册的method信息
 * @author 86136
 */
public interface IRegisterCenterProvider {
    /**
     * 服务端将服务提供者信息注册到zk对应的节点下
     *
     * @param serviceMetaData
     */
    public void registerProvider(final List<ProviderService> serviceMetaData) throws Exception;


    /**
     * 服务端获取服务提供者信息
     * <p/>
     * 注:返回对象,Key:服务提供者接口  value:服务提供者服务方法列表
     *
     * @return
     */
    public Map<String, List<ProviderService>> getProviderServiceMap();
}
