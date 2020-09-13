package com.netty.rpc.regist;

import com.netty.rpc.regist.provider.RegisterCenterProvider;
import com.netty.rpc.utils.PropertyConfigeHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * zookeeperClient
 * @author 86136
 */
public class RpcZookeeperClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcZookeeperClient.class);

    private static String ZK_SERVICE = PropertyConfigeHelper.getZkService();
    private static int ZK_SESSION_TIME_OUT = PropertyConfigeHelper.getZkConnectionTimeout();
    private static int ZK_CONNECTION_TIME_OUT = PropertyConfigeHelper.getZkConnectionTimeout();

    private Object clientLock = new Object[0];
    private volatile CuratorFramework client = null;

    private IRegisterCenter4Governance registCenter;


    public RpcZookeeperClient(IRegisterCenter4Governance registCenter) {
        initCurator();
        this.registCenter = registCenter;
    }

    public void initCurator() {
        client = CuratorFrameworkFactory.newClient(ZK_SERVICE,
                ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT,
                //new RetryNTimes(Integer.MAX_VALUE, 1000)
                new ExponentialBackoffRetry(5000, 3)
        );
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if (newState == ConnectionState.LOST) {
                    logger.info("[注册中心]-[client状态监听]---连接丢失");
                } else if (newState == ConnectionState.CONNECTED) {
                    logger.info("[注册中心]-[client状态监听]---成功连接");
                } else if (newState == ConnectionState.RECONNECTED) {
                    logger.info("[注册中心]-[client状态监听]---重连成功");
                    //临时节点补偿操作
                    registCenter.regist();
                }
            }
        });
        client.start();
        ;
    }

    /**
     * dubbo初始化client 的代码
     * CuratorZookeeperClient是吃哟client的对象
     * @param serviceMetaData
     */
//    public CuratorZookeeperClient(URL url) {
//        super(url);
//        try {
//            // 创建 CuratorFramework 构造器
//            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
//                    .connectString(url.getBackupAddress())
//                    .retryPolicy(new RetryNTimes(1, 1000))
//                    .connectionTimeoutMs(5000);
//            String authority = url.getAuthority();
//            if (authority != null && authority.length() > 0) {
//                builder = builder.authorization("digest", authority.getBytes());
//            }
//            // 构建 CuratorFramework 实例
//            client = builder.build();
//            // 添加监听器
//            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
//                @Override
//                public void stateChanged(CuratorFramework client, ConnectionState state) {
//                    if (state == ConnectionState.LOST) {
//                        CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
//                    } else if (state == ConnectionState.CONNECTED) {
//                        CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
//                    } else if (state == ConnectionState.RECONNECTED) {
//                        CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
//                    }
//                }
//            });
//
//            // 启动客户端
//            client.start();
//        } catch (Exception e) {
//            throw new IllegalStateException(e.getMessage(), e);
//        }
//    }

    /**
     * 检查节是否存在
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean checkEx(String path) throws Exception {
        return client.checkExists().forPath(path) != null;
    }

    /**
     * 递归创建节点
     *
     * @param path
     */
    public void creatingParent(String path) throws Exception {
        try {
            client.create().creatingParentsIfNeeded().forPath(path);
        } catch (KeeperException.NodeExistsException e) {

        }
    }

    /**
     * 递归创建临时节点
     *
     * @param path
     */
    public void creatingParentEph(String path) throws Exception {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.NodeExistsException e) {

        }
    }


    /**
     * 递归创建，因为
     *
     * @param path
     * @param ephemeral
     * @throws Exception
     */
    public void create(String path, boolean ephemeral) throws Exception {
        int i = path.lastIndexOf('/');
        if (i > 0) {
            create(path.substring(0, i), false);
        }
        if (ephemeral) {
            createEphemeral(path);
        } else {
            createPersistent(path);
        }
    }

    /**
     * 创建节点
     *
     * @param path
     * @throws Exception
     */
    public void createPersistent(String path) throws Exception {
        try {
            client.create().forPath(path);
        } catch (KeeperException.NodeExistsException e) {

        }
    }

    /**
     * 创建临时节点
     *
     * @param path
     * @throws Exception
     */
    public void createEphemeral(String path) throws Exception {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            //如果已经有该节点了，catch异常，忽略
        }
    }

    /**
     * 获取子节点
     *
     * @param path
     * @throws Exception
     */
    public void getChildren(String path) throws Exception {
        client.getChildren().forPath(path);
    }

    /**
     * 添加watch ，返回path的子节点
     *
     * @param path
     * @param listener
     * @return
     */
    public List<String> addChildListener(String path, CuratorWatcher listener) {
        try {
            return client.getChildren().usingWatcher(listener).forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}