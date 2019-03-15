package com.learn.rpc.register;

import com.learn.rpc.constant.ZkConstant;
import com.learn.rpc.remoting.zookeeper.curator.CuratorZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ServiceRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegister.class);

    @Resource
    private CuratorZookeeperClient curatorZookeeperClient;

    public void register(String serviceName, String serviceAddress) {
        // 创建 registry 节点（持久）
        String registryPath = ZkConstant.RPC_ZK_ROOT;
        if (!curatorZookeeperClient.checkExists(registryPath)) {
            curatorZookeeperClient.createPersistent(registryPath);
            LOGGER.info("create registry node: {}", registryPath);
        }
        // 创建 service 节点（持久）
        String servicePath = registryPath + ZkConstant.ZK_SEPARATOR + serviceName;
        if (!curatorZookeeperClient.checkExists(servicePath)) {
            curatorZookeeperClient.createPersistent(servicePath);
            LOGGER.info("create service node: {}", servicePath);
        }

        // 创建providers路径
        String providersPath = servicePath + ZkConstant.RPC_ZK_TYPE_PROVIDERS;
        if (!curatorZookeeperClient.checkExists(providersPath)) {
            curatorZookeeperClient.createPersistent(providersPath);
            LOGGER.info("create providers node : {}", providersPath);
        }
        String ipAddressPath = providersPath + ZkConstant.ZK_SEPARATOR + serviceAddress;
        if (!curatorZookeeperClient.checkExists(ipAddressPath)) {
            curatorZookeeperClient.createEphemeral(ipAddressPath);
        }
    }
}
