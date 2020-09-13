package com.netty.rpc.service.imp;

import com.netty.rpc.service.PersonService;

/**
 * @author 86136
 */
public class PersonServiceImp implements PersonService {

    @Override
    public String getPersion(String name){
        return "远程服务persion"+name;
    }
}
