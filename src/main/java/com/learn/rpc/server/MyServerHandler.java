package com.learn.rpc.server;

import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class MyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        System.out.println("客户端请供求信息:" + ctx.channel().remoteAddress() + ", " + request);
        String className = request.getClassName();
        Class clazz = Class.forName(className);
        System.out.println(clazz);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        rpcResponse.setResult("hehe");
        ctx.channel().writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
