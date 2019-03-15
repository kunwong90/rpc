package com.learn.rpc.remoting.zookeeper.curator;


import com.learn.rpc.BaseTest;
import com.learn.rpc.constant.ZkConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class CuratorZookeeperClientTest extends BaseTest {


    @Resource
    private CuratorZookeeperClient curatorZookeeperClient;


    @Test
    public void getChildren() {
        List<String> services = curatorZookeeperClient.getChildren(ZkConstant.RPC_ZK_ROOT);
        logger.info("getChildren = {}", services);
        if (CollectionUtils.isNotEmpty(services)) {
            for (String service : services) {
                List<String> serviceProviderAndConsumer = curatorZookeeperClient.getChildren(ZkConstant.RPC_ZK_ROOT + ZkConstant.ZK_SEPARATOR + service);
                logger.info("serviceProviderAndConsumer = {}", serviceProviderAndConsumer);
                if (CollectionUtils.isNotEmpty(serviceProviderAndConsumer)) {
                    for (String str : serviceProviderAndConsumer) {
                        List<String> list = curatorZookeeperClient.getChildren(ZkConstant.RPC_ZK_ROOT + ZkConstant.ZK_SEPARATOR + service + ZkConstant.ZK_SEPARATOR + str);
                        System.out.println(list);
                    }
                }
            }
        }
    }

}