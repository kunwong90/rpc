
package com.learn.rpc.proxy;

import com.learn.rpc.core.extension.Spi;

@Spi
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz);

}
