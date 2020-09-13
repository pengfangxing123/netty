package com.netty.rpc.rmi.service.impl;

import com.netty.rpc.rmi.service.PersionService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author ex-pengfx
 */
public class PersionServiceImpl extends UnicastRemoteObject implements PersionService  {

    public PersionServiceImpl() throws RemoteException {
    }

    @Override
    public String sayHello() throws RemoteException {
        System.out.println("666666");
        return "hello rmi";
    }
}
