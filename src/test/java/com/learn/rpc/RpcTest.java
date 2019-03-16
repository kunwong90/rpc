package com.learn.rpc;

import com.learn.rpc.config.springsupport.ConsumerConfigBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcTest {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:rpc-test.xml");
        ConsumerConfigBean consumerConfigBean = ac.getBean(ConsumerConfigBean.class);
        System.out.println(consumerConfigBean.getId());
        System.out.println(consumerConfigBean.getFullInterface());
    }
}
