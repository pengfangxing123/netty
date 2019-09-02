package com.netty.dynamicProxy;

import java.net.URL;

/**
 * @author fangxing.peng
 */
public class ClassLoaderTest {
    public static void main(String[] args) {

        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for(URL url : urls){
            System.out.println(url.toExternalForm());
        }
    }

}
