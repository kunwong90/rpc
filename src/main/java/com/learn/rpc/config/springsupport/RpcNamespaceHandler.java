package com.learn.rpc.config.springsupport;

import com.learn.rpc.config.RegistryConfig;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RpcNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("consumer", new ConsumerConfigBeanDefinitionParser(ConsumerConfigBean.class));
        registerBeanDefinitionParser("registry", new RegistryConfigBeanDefinitionParser(RegistryConfig.class));
    }
}