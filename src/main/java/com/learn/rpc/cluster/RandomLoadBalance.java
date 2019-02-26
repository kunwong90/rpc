package com.learn.rpc.cluster;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance extends AbstractLoadBalance {

    public RandomLoadBalance(List<String> hosts) {
        super(hosts);
    }

    @Override
    public String doSelect() {
        List<String> hosts = getHosts();
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * 10);
        return hosts.get(idx);
    }
}
