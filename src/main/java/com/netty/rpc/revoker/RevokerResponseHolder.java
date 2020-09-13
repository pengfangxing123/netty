package com.netty.rpc.revoker;

import com.google.common.collect.Maps;
import com.netty.rpc.common.entity.AresResponse;
import com.netty.rpc.common.entity.AresResponseWrapper;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author liyebing created on 17/2/1.
 * @version $Id$
 */
public class RevokerResponseHolder {

    /**
     * 服务返回结果Map
     */
    private static final Map<String, AresResponseWrapper> responseMap = Maps.newConcurrentMap();
    /**
     * 清除过期的返回结果
     */
    private static final ExecutorService removeExpireKeyExecutor = Executors.newSingleThreadExecutor();

    static {
        //删除超时未获取到结果的key,防止内存泄露
        removeExpireKeyExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        for (Map.Entry<String, AresResponseWrapper> entry : responseMap.entrySet()) {
                            boolean isExpire = entry.getValue().isExpire();
                            if (isExpire) {
                                responseMap.remove(entry.getKey());
                            }
                            Thread.sleep(10);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 初始化返回结果容器,requestUniqueKey唯一标识本次调用
     *
     * @param requestUniqueKey
     */
    public static void initResponseData(String requestUniqueKey) {
        responseMap.put(requestUniqueKey, AresResponseWrapper.of());
    }


    /**
     * 将Netty调用异步返回结果放入阻塞队列
     *
     * @param response
     */
    public static void putResultValue(AresResponse response) {
        long currentTime = System.currentTimeMillis();
        AresResponseWrapper responseWrapper = responseMap.get(response.getUniqueKey());
        //这里要加一个对responseWrapper判空的操作，因为如果getValue(String requestUniqueKey, long timeout)方法
        // 超时会从responseMap删除该AresResponseWrapper
        responseWrapper.setResponseTime(currentTime);
        responseWrapper.getResponseQueue().add(response);
        responseMap.put(response.getUniqueKey(), responseWrapper);
    }


    /**
     * 从阻塞队列中获取Netty异步返回的结果值
     *
     * @param requestUniqueKey
     * @param timeout
     * @return
     */
    public static AresResponse getValue(String requestUniqueKey, long timeout) {
        AresResponseWrapper responseWrapper = responseMap.get(requestUniqueKey);
        try {
            return responseWrapper.getResponseQueue().poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            responseMap.remove(requestUniqueKey);
        }
    }


}
