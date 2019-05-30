package com.netty.lock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author fangxing.peng
 */
public class MyDataSource {

    private LinkedList<Connection> pool =new LinkedList<>();

    private static final int INIT_COUNTS=10;

    private static final int MAX_COUNTS=20;

//    static {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public MyDataSource(){
        try {
            for(int i=0;i<INIT_COUNTS;i++){
                Connection connection = DriverManager.getConnection("url", "user", "password");
                pool.add(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        Connection ret=null;
        synchronized (pool){
            while (pool.size()<=0){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ret=pool.getFirst();
        }
        return ret;

    }

    public void release(Connection connection){
        if(connection!=null){
            synchronized (pool){
                pool.addLast(connection);
                this.notifyAll();
            }
        }
    }
}
