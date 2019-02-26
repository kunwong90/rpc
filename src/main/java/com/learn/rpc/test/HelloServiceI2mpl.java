
package com.learn.rpc.test;

import com.learn.rpc.annotation.RpcProvide;

@RpcProvide(value = HelloService.class, implCode = "helloServiceI2mpl")
public class HelloServiceI2mpl implements HelloService {



    @Override
    public String hello(String name) {
        return "Hello " + name;
    }


    @Override
    public User saveAndReturUser(User user) {
        return null;
    }
}