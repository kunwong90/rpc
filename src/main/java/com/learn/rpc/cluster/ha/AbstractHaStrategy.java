package com.learn.rpc.cluster.ha;

import com.learn.rpc.cluster.HaStrategy;
import com.learn.rpc.init.URL;

public abstract class AbstractHaStrategy<T> implements HaStrategy<T> {

    protected URL url;

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

}
