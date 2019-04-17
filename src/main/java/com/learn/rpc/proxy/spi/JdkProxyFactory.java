package com.learn.rpc.proxy.spi;


import com.learn.rpc.core.extension.SpiMeta;
import com.learn.rpc.proxy.ProxyFactory;
import com.learn.rpc.proxy.RefererInvocationHandler;
import com.learn.rpc.register.ServiceDiscovery;

import java.lang.reflect.Proxy;

@SpiMeta(name = "jdk")
public class JdkProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> clz, ServiceDiscovery serviceDiscovery, String implCode) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new RefererInvocationHandler<>(clz, serviceDiscovery, implCode));
    }
}