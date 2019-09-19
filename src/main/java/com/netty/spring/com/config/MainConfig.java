package com.netty.spring.com.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Administrator
 */
@Configuration
@ComponentScan("com.netty.spring.com")
//@ComponentScan("com.netty.spring.com.aspect")
@EnableAspectJAutoProxy
public class MainConfig {

}
