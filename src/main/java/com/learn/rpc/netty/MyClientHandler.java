package com.learn.rpc.netty;

import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by wangkun on 2017/5/20.
 */
public class MyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output : " + response);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("com.learn.rpc.test.HelloService");
        rpcRequest.setMethodName("hello");
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        ctx.writeAndFlush(rpcRequest);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("com.learn.rpc.test.HelloService");
        rpcRequest.setMethodName("hello");
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        ctx.writeAndFlush(rpcRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
