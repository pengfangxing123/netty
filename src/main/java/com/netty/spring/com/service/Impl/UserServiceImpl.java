package com.netty.spring.com.service.Impl;

import com.netty.spring.com.Dao.UserDao;
import com.netty.spring.com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public void insert() {
        userDao.insert();
        int i=1/0;
        System.out.println("success");
    }

    @Override
    public void select(UserService user) {
        System.out.println("sss");
        user.insert();
    }
}
