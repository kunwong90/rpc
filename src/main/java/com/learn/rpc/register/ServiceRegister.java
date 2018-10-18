package com.learn.rpc.register;

import com.learn.rpc.constant.ZkConstant;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServiceRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegister.class);
    private final ZkClient zkClient;

    public ServiceRegister(String zkAddress) {
        // 创建 ZooKeeper 客户端
        zkClient = new ZkClient(zkAddress, ZkConstant.ZK_SESSION_TIMEOUT, ZkConstant.ZK_CONNECTION_TIMEOUT);
    }

    public void register(String serviceName, String serviceAddress) {
        // 创建 registry 节点（持久）
        String registryPath = ZkConstant.RPC_ZK_ROOT;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            LOGGER.info("create registry node: {}", registryPath);
        }
        // 创建 service 节点（持久）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            LOGGER.info("create service node: {}", servicePath);
        }

        // 创建providers路径
        String providersPath = servicePath + ZkConstant.RPC_ZK_TYPE_PROVIDERS;
        if (!zkClient.exists(providersPath)) {
            zkClient.createPersistent(providersPath);
            LOGGER.info("create providers node : {}", providersPath);
        }
        // 获取当前机器的ip地址
        try {
            String localIp = InetAddress.getLocalHost().getHostAddress();
            String ipAddressPath = providersPath + ZkConstant.ZK_SEPARATOR + serviceAddress;
            if (!zkClient.exists(ipAddressPath)) {
                zkClient.createEphemeral(ipAddressPath);
            }
        } catch (UnknownHostException e) {
            LOGGER.error("get local host ip error.", e);
        } catch (Exception e) {
            LOGGER.error("create node exception.", e);
        }
        // 创建 address 节点（临时）
        /*String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        LOGGER.info("create address node: {}", addressNode);*/
    }
}
