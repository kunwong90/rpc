package com.learn.rpc.proxy;

import com.learn.rpc.client.RpcClientHandler;
import com.learn.rpc.common.URLParamType;
import com.learn.rpc.init.Request;
import com.learn.rpc.init.RpcContext;
import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import com.learn.rpc.register.ServiceDiscovery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;



public abstract class AbstractRefererHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRefererHandler.class);

    protected ServiceDiscovery serviceDiscovery;

    protected Class<T> clz;

    protected String interfaceName;

    protected String implCode;

    Object invokeRequest(RpcRequest request) throws Throwable {
        String serviceAddress = null;
        if (serviceDiscovery != null) {
            String serviceName = interfaceName;
            if (StringUtils.isNotBlank(implCode)) {
                serviceName = serviceName + "#" + implCode;
            }
            serviceAddress = serviceDiscovery.discover(serviceName);
            LOGGER.info("discover service: {} => {}", serviceName, serviceAddress);
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
        LOGGER.info("cost time: {} ms", System.currentTimeMillis() - time);
        if (response == null) {
            throw new RuntimeException("response is null");
        }
        // 返回 RPC 响应结果
        return response.getResult();
    }

    Object invokeRequest(Request request, Class returnType) throws Throwable {
        RpcContext curContext = RpcContext.getContext();
        // set rpc context attachments to request
        Map<String, String> attachments = curContext.getRpcAttachments();
        if (!attachments.isEmpty()) {
            for (Map.Entry<String, String> entry : attachments.entrySet()) {
                request.setAttachment(entry.getKey(), entry.getValue());
            }
        }
        // add to attachment if client request id is set
        if (StringUtils.isNotBlank(curContext.getClientRequestId())) {
            request.setAttachment(URLParamType.requestIdFromClient.getName(), curContext.getClientRequestId());
        }
        return null;
    }

    private Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType != null && returnType.isPrimitive()) {
            return PrimitiveDefault.getDefaultReturnValue(returnType);
        }
        return null;
    }

    private static class PrimitiveDefault {
        private static boolean defaultBoolean;
        private static char defaultChar;
        private static byte defaultByte;
        private static short defaultShort;
        private static int defaultInt;
        private static long defaultLong;
        private static float defaultFloat;
        private static double defaultDouble;

        private static Map<Class<?>, Object> primitiveValues = new HashMap<Class<?>, Object>();

        static {
            primitiveValues.put(boolean.class, defaultBoolean);
            primitiveValues.put(char.class, defaultChar);
            primitiveValues.put(byte.class, defaultByte);
            primitiveValues.put(short.class, defaultShort);
            primitiveValues.put(int.class, defaultInt);
            primitiveValues.put(long.class, defaultLong);
            primitiveValues.put(float.class, defaultFloat);
            primitiveValues.put(double.class, defaultDouble);
        }

        public static Object getDefaultReturnValue(Class<?> returnType) {
            return primitiveValues.get(returnType);
        }

    }
}
