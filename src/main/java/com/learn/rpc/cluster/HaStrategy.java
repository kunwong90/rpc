package com.learn.rpc.cluster;


import com.learn.rpc.core.extension.Scope;
import com.learn.rpc.core.extension.Spi;
import com.learn.rpc.init.Request;
import com.learn.rpc.init.Response;
import com.learn.rpc.init.URL;

/**
 * 
 * Ha strategy.
 *
 */
@Spi(scope = Scope.PROTOTYPE)
public interface HaStrategy<T> {

    void setUrl(URL url);

    Response call(Request request, LoadBalance<T> loadBalance);

}
