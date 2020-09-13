package com.netty.rpc.provder;

/**
 * @author 86136
 */
public class ApplicationConfig {
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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
