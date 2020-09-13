package com.netty.rpc.common.entity;

import java.io.Serializable;

/**
 * 请求response对象
 * @author 86136
 */
public class AresResponse implements Serializable {

    /**
     * UUID,唯一标识一次返回值
     */
    private String uniqueKey;
    /**
     * 客户端指定的服务超时时间
     */
    private long invokeTimeout;
    /**
     * 接口调用返回的结果对象
     */
    private Object result;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
