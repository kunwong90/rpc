
package com.learn.rpc.test;

import com.learn.rpc.annotation.RpcProvide;

@RpcProvide(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public User saveAndReturUser(User user) {
        return null;
    }
}