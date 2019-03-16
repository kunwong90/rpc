package com.learn.rpc.config.springsupport;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RpcNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        //registerBeanDefinitionParser("elementname1", new RpcBeanDefinitionParser(Hero.class));
        registerBeanDefinitionParser("consumer", new ConsumerConfigBeanDefinitionParser(ConsumerConfigBean.class));
    }
}