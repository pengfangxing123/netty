package com.netty.classload.service.imp;

import com.netty.classload.service.SPIService;

/**
 * @author 86136
 */
public class SpiImpl1 implements SPIService {

    @Override
    public void execute() {
        System.out.println("SpiImpl1 execute.....");
    }
}
