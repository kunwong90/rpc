package com.learn.rpc.server;

import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private volatile Map<String, Object> beanMap;

    public RpcServerHandler(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        System.out.println("客户端请供求信息:" + ctx.channel().remoteAddress() + ", " + request);
        String interfaceName = request.getInterfaceName();
        Class interfaceClass = Class.forName(interfaceName);
        Object bean = beanMap.get(interfaceName);
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
