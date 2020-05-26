package com.yeoman.minispring;

import com.yeoman.minispring.handler.DispatcherServlet;
import com.yeoman.minispring.utils.MinispringUtil;
import com.yeoman.minispring.utils.YamlUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/19
 * @desc
 */
public class MiniSpringStarter {

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException {

        MinispringUtil.init(MiniSpringStarter.class);
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                        //对http请求进行解码
                        .addLast("decode", new HttpRequestDecoder())

                        //对http请求进行编码
                        .addLast("encode", new HttpResponseEncoder())
                                //上传post请求
                        .addLast(new HttpServerExpectContinueHandler())
                        //对http进行聚合，设置最大聚合字节长度为10M
                        .addLast(new HttpObjectAggregator(10 * 1024 * 1024))

                        //开启http内容压缩
                        .addLast(new HttpContentCompressor())
                                .addLast(new DispatcherServlet());
                    }
                });

        int port = (int) YamlUtil.getOrDefault("server.port", 9999);
        try {
            System.out.println("starting....");
            System.out.println("started port " + port);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

}
