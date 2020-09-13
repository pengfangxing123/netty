package com.netty.classload.load;

import com.netty.dynamicProxy.CglibProxyTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 86136
 */
public class LoadResourceDemo {

    public static void main(String[] args) throws IOException {
        //当前类(class)所在的包目录
        System.out.println(CglibProxyTest.class.getResource(""));
        //根目录
        System.out.println(LoadResourceDemo.class.getResource("/settings.jar"));

        //这个linux下可能路径不一样
        System.out.println(Object.class.getResource("/settings.jar"));
        URL resource = Object.class.getResource("/settings.jar");
        System.out.println(new UrlResource(resource).exists());

        //根目录
        System.out.println(LoadResourceDemo.class.getClassLoader().getResource("settings.jar"));
        //不能以/ 开头
        System.out.println(LoadResourceDemo.class.getClassLoader().getResource("/settings.jar"));

        Enumeration<URL> resources = LoadResourceDemo.class.getClassLoader().getResources("");
        Set<URL> set = new HashSet<>();
        while (resources.hasMoreElements()){
            set.add(resources.nextElement());
        }
        System.out.println(set);
    }
}
