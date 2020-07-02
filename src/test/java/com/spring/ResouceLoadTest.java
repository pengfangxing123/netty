package com.spring;

import org.junit.Test;
import org.springframework.core.io.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * 资源加载器测试类
 * @author 86136
 */
public class ResouceLoadTest {
    @Test
    public void testPatternResolver() throws Exception {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("classpath*:");
        String filename = resources[0].getFilename();
    }


    /**
     * 测试defaultResourceLoad
     */
    @Test
    public void testDefaultResourceLoad(){
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource fileResource1 = resourceLoader.getResource("D:/Person.class");
        System.out.println("fileResource1 is FileSystemResource:" + (fileResource1 instanceof FileSystemResource));
        System.out.println("fileResource1 is exists:" + fileResource1.exists());//返回false

        Resource fileResource2 = resourceLoader.getResource("/Users/chenming673/Documents/spark.txt");
        System.out.println("fileResource2 is ClassPathResource:" + (fileResource2 instanceof ClassPathResource));

        Resource urlResource1 = resourceLoader.getResource("file:/Users/chenming673/Documents/spark.txt");
        System.out.println("urlResource1 is UrlResource:" + (urlResource1 instanceof UrlResource));

        Resource urlResource2 = resourceLoader.getResource("http://www.baidu.com");
        System.out.println("urlResource2 is exists:" + urlResource2.exists());
        System.out.println("urlResource1 is urlResource:" + (urlResource2 instanceof  UrlResource));

        Resource resource = resourceLoader.getResource("settings.jar");
        System.out.println("resource is FileSystemResource:" + (resource instanceof FileSystemResource));
        System.out.println("resource is exists:" + resource.exists());
    }

    /**
     * 测试FileSystemResourceLoader
     * 用DefaultResourceLoader会返回classPathResource，从classpath处找，显然时不存在的
     * 所以用FileSystemResourceLoader
     */
    @Test
    public void testFileSystemResourceLoader(){
        ResourceLoader resourceLoader = new FileSystemResourceLoader();

        Resource fileResource1 = resourceLoader.getResource("D:/Person.class");
        System.out.println("fileResource1 is FileSystemResource:" + (fileResource1 instanceof FileSystemResource));
        System.out.println("fileResource1 is exists:" + fileResource1.exists());//返回false
    }

    /**
     * 测试ClassRelativeResourceLoader
     * 从传入的class找资源
     * 测试的时候注意记得把file.txt放到编译后的目录
     */
    @Test
    public void testClassRelativeResourceLoader() throws IOException {
        ResourceLoader resourceLoader = new ClassRelativeResourceLoader(this.getClass());

        Resource fileResource1 = resourceLoader.getResource("file.txt");
        System.out.println("fileResource1 is FileSystemResource:" + (fileResource1 instanceof ClassPathResource));
        System.out.println(fileResource1.getFile().getPath());//D:\project\idea\netty\target\test-classes\com\spring\file.txt
        System.out.println("fileResource1 is exists:" + fileResource1.exists());//返回TRUE
    }
}
