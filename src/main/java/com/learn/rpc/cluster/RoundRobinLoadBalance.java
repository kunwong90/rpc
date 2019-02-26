package com.learn.rpc.cluster;

import com.learn.rpc.util.MathUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private AtomicInteger idx = new AtomicInteger(0);

    @Override
    public String doSelect(List<String> hosts) {
        int index = getNextIndex();
        int count = hosts.size();
        return hosts.get(index % count);
    }

    private int getNextIndex() {
        return MathUtil.getNonNegative(idx.getAndIncrement());
    }
}
