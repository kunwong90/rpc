package com.learn.rpc.test;

public interface HelloService {

    String hello(String name);

    User saveAndReturUser(User user);
}
