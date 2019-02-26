package com.learn.rpc.cluster;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String select(List<String> hosts) {
        if (CollectionUtils.isEmpty(hosts)) {
            throw new RuntimeException("服务提供者不能为空!");
        }
        if (hosts.size() == 1) {
            return hosts.get(0);
        }
        return doSelect(hosts);
    }

    public abstract String doSelect(List<String> hosts);
}
