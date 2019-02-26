package com.learn.rpc.register;

import com.learn.rpc.cluster.LoadBalance;
import com.learn.rpc.constant.ZkConstant;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private String zkAddress;

    private LoadBalance loadBalance;

    public ServiceDiscovery(String zkAddress, LoadBalance loadBalance) {
        this.zkAddress = zkAddress;
        this.loadBalance = loadBalance;
    }

    public String discover(String name) {
        // 创建 ZooKeeper 客户端
        ZkClient zkClient = new ZkClient(zkAddress, ZkConstant.ZK_SESSION_TIMEOUT, ZkConstant.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("connect zookeeper");
        try {
            // 获取 service providers 节点
            String servicePath = ZkConstant.RPC_ZK_ROOT + ZkConstant.ZK_SEPARATOR + name + ZkConstant.RPC_ZK_TYPE_PROVIDERS;
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtils.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
            return loadBalance.select(addressList);
        } finally {
            zkClient.close();
        }
    }
}
