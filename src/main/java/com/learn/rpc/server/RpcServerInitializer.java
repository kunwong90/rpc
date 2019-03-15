package com.learn.rpc.server;

import com.learn.rpc.annotation.RpcProvide;
import com.learn.rpc.protocol.RpcDecoder;
import com.learn.rpc.protocol.RpcEncoder;
import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import com.learn.rpc.register.ServiceRegister;
import com.learn.rpc.util.IpAddressUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcServerInitializer implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerInitializer.class);

    @Resource
    private ServiceRegister serviceRegister;

    private int port;

    public void setServiceRegister(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }

    /**
     * key:存储beanName
     * value:bean对象
     */
    private volatile Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RpcProvide.class);
        if (MapUtils.isNotEmpty(beanMap)) {
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                Object bean = entry.getValue();
                LOGGER.info("beanName = {}, object = {}", entry.getKey(), bean);
                Class clazz = bean.getClass().getAnnotation(RpcProvide.class).value();
                LOGGER.info("clazz = {}, interfaceName = {}", clazz, clazz.getName());

                String implCode = bean.getClass().getAnnotation(RpcProvide.class).implCode();
                LOGGER.info("implCode = {}", implCode);

                Class<?>[] clazzs = bean.getClass().getInterfaces();
                //该实现类的注解和接口是否为同一个接口
                boolean sameClazz = false;
                if (ArrayUtils.isNotEmpty(clazzs)) {
                    for (Class cls : clazzs) {
                        if (cls.equals(clazz)) {
                            sameClazz = true;
                            break;
                        }
                    }
                } else {
                    throw new RuntimeException("没有找到该接口");
                }
                if (!sameClazz) {
                    throw new RuntimeException("注解中的接口和实现的接口不是同一个");
                }
                if (StringUtils.isBlank(implCode)) {
                    handlerMap.put(clazz.getName(), bean);
                } else {
                    handlerMap.put(clazz.getName() + "#" + implCode, bean);
                }

            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new RpcEncoder(RpcResponse.class));
                            pipeline.addLast(new RpcDecoder(RpcRequest.class));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast(new RpcServerHandler(handlerMap));
                        }
                    });

            // 获取 RPC 服务器的 IP 地址与端口号
            String localHostIp = IpAddressUtil.getLocalHostIpAddress();
            String serviceAddress = localHostIp + ":" + port;
            // 启动 RPC 服务器
            ChannelFuture channelFuture = serverBootstrap.bind(localHostIp, port).sync();
            // 注册 RPC 服务地址
            if (serviceRegister != null) {
                for (String interfaceName : handlerMap.keySet()) {
                    serviceRegister.register(interfaceName, serviceAddress);
                    LOGGER.info("register service: interfaceName = {} => serviceAddress = {}", interfaceName, serviceAddress);
                }
            }
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("RpcServerInitializer Exception",e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
    public void setPort(int port) {
        this.port = port;
    }
}
