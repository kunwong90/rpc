
package com.learn.rpc.proxy.spi;



import com.learn.rpc.core.extension.SpiMeta;
import com.learn.rpc.proxy.ProxyFactory;

@SpiMeta(name = "common")
public class CommonProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> clz) {
        return null;
    }
}
