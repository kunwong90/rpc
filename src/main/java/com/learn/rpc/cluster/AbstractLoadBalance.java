package com.learn.rpc.cluster;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {


    private List<String> hosts;

    public AbstractLoadBalance(List<String> hosts) {
        if (CollectionUtils.isEmpty(hosts)) {
            throw new RuntimeException("服务提供者不能为空!");
        }
        this.hosts = hosts;
    }


    @Override
    public String select() {
        if (hosts.size() == 1) {
            return hosts.get(0);
        }
        return doSelect();
    }

    protected List<String> getHosts() {
        return hosts;
    }

    public abstract String doSelect();
}
