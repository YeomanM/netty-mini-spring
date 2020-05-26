package com.yeoman.minispring.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;

import java.io.IOException;
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
            h(request);
            System.out.println(reqUrl);
            System.out.println(method.name());
            System.out.println("=======================end=========================");
        }

        super.channelRead(ctx, msg);
    }

    public void h(FullHttpRequest request) throws IOException {

//        HttpPostMultipartRequestDecoder decoder = new HttpPostMultipartRequestDecoder(request);
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
        while (decoder.hasNext()) {
            InterfaceHttpData data = decoder.next();
            if (data instanceof Attribute) {
                Attribute attribute = (Attribute) data;

                System.out.println("收到mutlipart属性：" + attribute);
            } else if (data instanceof FileUpload){
                FileUpload upload = (FileUpload) data;
                System.out.println("收到multipart文件：" + upload);
            }
        }
        decoder.destroy();
    }

//    private void response(ChannelHandlerContext ctx, ) {
//
//    }
}
