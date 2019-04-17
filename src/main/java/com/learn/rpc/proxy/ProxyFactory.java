
package com.learn.rpc.proxy;

import com.learn.rpc.core.extension.Spi;
import com.learn.rpc.register.ServiceDiscovery;

@Spi
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz, ServiceDiscovery serviceDiscovery, String implCode);

}
