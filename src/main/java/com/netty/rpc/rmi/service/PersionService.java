package com.netty.rpc.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author ex-pengfx
 */
public interface PersionService extends Remote {
    String sayHello () throws RemoteException;
}

