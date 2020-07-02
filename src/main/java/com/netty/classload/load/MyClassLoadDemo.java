package com.netty.classload.load;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author 86136
 */
public class MyClassLoadDemo {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, MalformedURLException {
        MyClassLoader mcl = new MyClassLoader();
        //Class<?> c1 = Class.forName("cn.com.jrj.vtmatch.firstcapitalmanage.entity.Person", true, mcl);
        Class<?> c1 = mcl.loadClass("cn.com.jrj.vtmatch.firstcapitalmanage.entity.Person");
        Object obj = c1.newInstance();
        System.out.println(obj);
        System.out.println(obj.getClass().getClassLoader());
    }

    public static class MyClassLoader extends ClassLoader
    {
        public MyClassLoader()
        {

        }

        public MyClassLoader(ClassLoader parent)
        {
            super(parent);
        }

        //这里为什么不直接重写loadClass ，直接调用findClass去加载呢
        //因为加载Person的时候，会初始化它的父类，Object，而我们自定义的classLoad是不允许加载全类名java开头的
        //所以还是得让走双亲委派模式，去让bootstrap加载器去加载
//        @Override
//        public Class<?> loadClass(String name) throws ClassNotFoundException {
//            return findClass(name);
//        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException
        {
            File file = getClassFile(name);
            try
            {
                byte[] bytes = getClassBytes(file);
                //name 是为了在资源中定位的
                Class<?> c = this.defineClass(name, bytes, 0, bytes.length);
                return c;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return super.findClass(name);
        }

        /**
         * 这里是获取资源文件
         * @param name
         * @return
         */
        private File getClassFile(String name)
        {
            File file = new File("D:/Person.class");
            return file;
        }

        private byte[] getClassBytes(File file) throws Exception
        {
            // 这里要读入.class的字节，因此要使用字节流
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            WritableByteChannel wbc = Channels.newChannel(baos);
            ByteBuffer by = ByteBuffer.allocate(1024);

            while (true)
            {
                int i = fc.read(by);
                if (i == 0 || i == -1)
                    break;
                by.flip();
                wbc.write(by);
                by.clear();
            }

            fis.close();

            return baos.toByteArray();
        }
    }

}
