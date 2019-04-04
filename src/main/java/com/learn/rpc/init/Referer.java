package com.learn.rpc.init;

import com.learn.rpc.core.extension.Scope;
import com.learn.rpc.core.extension.Spi;

/**
 * 
 * Refer to a service.
 *
 */
@Spi(scope = Scope.PROTOTYPE)
public interface Referer<T> extends Caller<T> {

    /**
     * 当前使用该referer的调用数
     * 
     * @return
     */
    int activeRefererCount();

    /**
     * 获取referer的原始service url
     * 
     * @return
     */
    URL getServiceUrl();
}
