package com.learn.rpc.register;

import com.learn.rpc.cluster.LoadBalance;
import com.learn.rpc.constant.ZkConstant;
import com.learn.rpc.remoting.zookeeper.curator.CuratorZookeeperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

public class ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private LoadBalance loadBalance;

    @Resource
    private CuratorZookeeperClient curatorZookeeperClient;

    public ServiceDiscovery(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    public String discover(String name) {
        // 获取 service providers 节点
        String servicePath = ZkConstant.RPC_ZK_ROOT + ZkConstant.ZK_SEPARATOR + name + ZkConstant.RPC_ZK_TYPE_PROVIDERS;
        LOGGER.info("service providers path = {}", servicePath);
        if (!curatorZookeeperClient.checkExists(servicePath)) {
            throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
        }
        List<String> addressList = curatorZookeeperClient.getChildren(servicePath);
        LOGGER.info("service providers address list = {}", addressList);
        if (CollectionUtils.isEmpty(addressList)) {
            throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
        }
        //return loadBalance.select(addressList);
        return null;
    }
}
