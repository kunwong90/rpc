package com.learn.rpc.cluster;

import com.learn.rpc.util.MathUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    public RoundRobinLoadBalance(List<String> hosts) {
        super(hosts);
    }

    private AtomicInteger idx = new AtomicInteger(0);

    @Override
    public String doSelect() {
        List<String> hosts = getHosts();
        int index = getNextIndex();
        int count = hosts.size();
        return hosts.get(index % count);
    }

    private int getNextIndex() {
        return MathUtil.getNonNegative(idx.getAndIncrement());
    }
}
