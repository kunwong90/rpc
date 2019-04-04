package com.learn.rpc.cluster;

import com.learn.rpc.init.Referer;
import com.learn.rpc.init.Request;

import java.util.List;

public interface LoadBalance<T> {

    void onRefresh(List<Referer<T>> referers);

    Referer<T> select(Request request);

    void selectToHolder(Request request, List<Referer<T>> refersHolder);

    void setWeightString(String weightString);
}
