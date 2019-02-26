package com.learn.rpc.cluster;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    public String doSelect(List<String> hosts) {
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * 10);
        return hosts.get(idx);
    }
}
