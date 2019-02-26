package com.learn.rpc.client;

import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import com.learn.rpc.register.ServiceDiscovery;
import com.learn.rpc.register.ServiceRegister;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcProxy {

    private final Logger logger = LoggerFactory.getLogger(RpcProxy.class);

    private ServiceDiscovery serviceDiscovery;

    private String serviceAddress;

    private RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(Class<T> interfaceClass) {
        return create(interfaceClass, StringUtils.EMPTY);
    }

    /**
     *
     * @param interfaceClass
     * @param implCode
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> interfaceClass, String implCode) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},(Object proxy, Method method, Object[] args) -> {
                    // 创建 RPC 请求对象并设置请求属性
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setInterfaceName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);
                    // 获取 RPC 服务地址
                    if (serviceDiscovery != null) {
                        String serviceName = interfaceClass.getName();
                        if (StringUtils.isNotBlank(implCode)) {
                            serviceName = serviceName + "#" + implCode;
                        }
                        serviceAddress = serviceDiscovery.discover(serviceName);
                        logger.info("discover service: {} => {}", serviceName, serviceAddress);
                    }
                    if (StringUtils.isEmpty(serviceAddress)) {
                        throw new RuntimeException("server address is empty");
                    }
                    // 从 RPC 服务地址中解析主机名与端口号
                    String[] array = StringUtils.split(serviceAddress, ":");
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    // 创建 RPC 客户端对象并发送 RPC 请求
                    RpcClientHandler rpcClientHandler = new RpcClientHandler(host, port);
                    long time = System.currentTimeMillis();
                    RpcResponse response = rpcClientHandler.sendRequest(request);
                    logger.info("cost time: {} ms", System.currentTimeMillis() - time);
                    if (response == null) {
                        throw new RuntimeException("response is null");
                    }
                    // 返回 RPC 响应结果
                    return response.getResult();
                }
        );
    }
}
