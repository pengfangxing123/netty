package com.netty.rpc.main;

import com.netty.rpc.service.PersonService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 86136
 */
public class ClientReference {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rpc/ares-client.xml");
        PersonService personService = (PersonService) applicationContext.getBean("personService");
        String name = personService.getPersion("NAME");
        System.out.println(name);
    }
}
