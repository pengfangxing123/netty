package com.netty.spring.com.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 */
@Repository
public class UserDao {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    public void insert(){
        String sql="insert INTO `test_user` (`age` ,`name`) VALUES (?,?)";
        jdbcTemplate.update(sql,"40","深圳");
    }
}
