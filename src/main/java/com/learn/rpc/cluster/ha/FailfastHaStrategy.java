package com.learn.rpc.cluster.ha;

import com.learn.rpc.cluster.LoadBalance;
import com.learn.rpc.core.extension.SpiMeta;
import com.learn.rpc.init.Referer;
import com.learn.rpc.init.Request;
import com.learn.rpc.init.Response;

/**
 * 
 * Fail fast ha strategy.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-5-22
 */
@SpiMeta(name = "failfast")
public class FailfastHaStrategy<T> extends AbstractHaStrategy<T> {

    @Override
    public Response call(Request request, LoadBalance<T> loadBalance) {
        Referer<T> refer = loadBalance.select(request);
        return refer.call(request);
    }
}
