package com.learn.rpc.server;

import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;


public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private volatile Map<String, Object> beanMap;

    public RpcServerHandler(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        LOGGER.info("client request info : {}", ctx.channel().remoteAddress() + ", " + request);
        String interfaceName = request.getInterfaceName();
        String implCode = request.getImplCode();
        Object bean;
        if (StringUtils.isNotBlank(implCode)) {
            bean = beanMap.get(interfaceName + "#" + implCode);
        } else {
            bean = beanMap.get(interfaceName);
        }

        Class[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        String methodName = request.getMethodName();
        Method method = bean.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        Object result = method.invoke(bean, parameters);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        rpcResponse.setResult(result);
        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
