package com.netty.rpc.rmi.client;

import com.netty.rpc.rmi.service.PersionService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author ex-pengfx
 */
public class RmiClient {
    public static void main(String[] args) throws Exception {
        PersionService persionService = (PersionService) Naming.lookup("rmi://localhost:8090/hello");
        System.out.println(persionService.sayHello());
    }
}
