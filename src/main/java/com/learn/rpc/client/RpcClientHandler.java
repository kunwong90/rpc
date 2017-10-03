package com.learn.rpc.client;

import com.learn.rpc.protocol.RpcDecoder;
import com.learn.rpc.protocol.RpcEncoder;
import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import com.sun.org.apache.regexp.internal.RE;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private String address;

    private int port;


    public RpcClientHandler(String address, int port) {
        this.address = address;
        this.port = port;
    }

    private final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    private volatile RpcResponse rpcResponse;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        //从服务端返回的响应
        this.rpcResponse = response;
    }

    public RpcResponse sendRequest(RpcRequest rpcRequest) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new RpcEncoder(RpcRequest.class));
                    pipeline.addLast(new RpcDecoder(RpcResponse.class));
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    pipeline.addLast(RpcClientHandler.this);
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
            Channel channel = channelFuture.channel();
            //调用服务端
            channel.writeAndFlush(rpcRequest).sync();
            channelFuture.channel().closeFuture().sync();
            return rpcResponse;
        } catch (Exception e) {
            rpcResponse.setThrowable(e);
            logger.error("RpcClientHandler Exception", e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        return rpcResponse;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
