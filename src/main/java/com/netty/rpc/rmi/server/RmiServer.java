package com.netty.rpc.rmi.server;

import com.netty.rpc.rmi.MySocketFactory;
import com.netty.rpc.rmi.service.impl.PersionServiceImpl;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;

/**
 * 基于BIO，JDK动态代理，Jdk序列化
 * @author ex-pengfx
 */
public class RmiServer {
    public static void main(String[] args) throws Exception{
        PersionServiceImpl persion = new PersionServiceImpl();
        LocateRegistry.createRegistry(8090);
        //指定通信端口防止防火墙拦截
        RMISocketFactory.setSocketFactory(new MySocketFactory());
        Naming.bind("rmi://localhost:8090/hello",persion);
        System.out.println("server start....");
    }
}
