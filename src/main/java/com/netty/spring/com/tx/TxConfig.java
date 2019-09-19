package com.netty.spring.com.tx;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @author Administrator
 */
@EnableTransactionManagement
@Configuration
public class TxConfig {

    /**
     * @EventListener 实现对事件监听
     * 等同于实现ApplicationListen接口
     * @param event
     */
    @EventListener(value=ApplicationEvent.class)
    public void listen(ApplicationEvent event){
        System.out.println("@EventListener 收到事件");
    }

    @Bean
    public DataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("123.com");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/oauth?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
       return new DataSourceTransactionManager(dataSource);
    }
}
