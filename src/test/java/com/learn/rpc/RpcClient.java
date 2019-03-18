package com.learn.rpc;

import com.learn.rpc.annotation.Reference;
import com.learn.rpc.test.HelloService;
import com.learn.rpc.test.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class RpcClient extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    @Reference
    private HelloService helloService;

    @Reference(implCode = "helloServiceI2mpl")
    private HelloService helloService1;


    @Test
    public void helloTest() {

        User user = new User();
        user.setAge(10);
        user.setId(UUID.randomUUID().toString());
        user.setName("张三");
        user.setAddress("南京市玄武区");
        String result = helloService.hello("tom");
        LOGGER.info("result = {}", result);

        String result2 = helloService1.hello("jim");
        LOGGER.info("result = {}", result2);
    }
}