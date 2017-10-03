/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.learn.rpc.test;

import com.learn.rpc.annotation.RpcProvide;
import org.springframework.beans.factory.annotation.Autowired;

@RpcProvide(value = HelloService.class, implCode = "helloServiceI2mpl")
public class HelloServiceI2mpl implements HelloService {

    @Autowired
    private RedisDao redisDao;

    public String hello(String name) {
        return "Hello " + name;
    }


    @Override
    public User saveAndReturUser(User user) {
        return redisDao.saveAndGetUser(user);
    }
}