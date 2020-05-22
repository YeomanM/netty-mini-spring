package com.yeoman.minispring.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.nio.charset.Charset;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/22
 * @desc
 */
public class DispatcherServlet extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            ByteBuf str = request.content();
            String reqUrl = request.uri();
            HttpMethod method = request.method();
            System.out.println(str.toString(Charset.defaultCharset()));
            System.out.println(reqUrl);
            System.out.println(method.name());
            System.out.println("=======================end=========================");
        }

        super.channelRead(ctx, msg);
    }

//    private void response(ChannelHandlerContext ctx, ) {
//
//    }
}
