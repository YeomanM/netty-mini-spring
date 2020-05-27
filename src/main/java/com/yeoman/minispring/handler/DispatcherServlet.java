package com.yeoman.minispring.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.AsciiString;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
            String contentType = request.headers().get("Content-type").trim();
            System.out.println(contentType);
            ByteBuf str = request.content();
            String reqUrl = request.uri();
            HttpMethod method = request.method();
//            h(request);
            System.out.println(reqUrl);
            System.out.println(method.name());
            System.out.println("=======================end=========================");
        }

//        super.channelRead(ctx, msg);
        responsePush(ctx, "[\"收到了\"]".getBytes(StandardCharsets.UTF_8));
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

    private HttpHeaders headers;
    private HttpRequest request;
    private FullHttpRequest fullRequest;

    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONTENT_TYPE = AsciiString.of("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.of("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.of("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.of("keep-alive");

    private static final AsciiString ACCESS_CONTROL_ALLOW_ORIGIN = AsciiString.of("Access-Control-Allow-Origin");
    private static final AsciiString ACCESS_CONTROL_ALLOW_HEADERS = AsciiString.of("Access-Control-Allow-Headers");
    private static final AsciiString ACCESS_CONTROL_ALLOW_METHODS = AsciiString.of("Access-Control-Allow-Methods");
    private static final AsciiString ACCESS_CONTROL_ALLOW_CREDENTIALS = AsciiString.of("Access-Control-Allow-Credentials");


    private void responsePush(ChannelHandlerContext ctx, byte[] content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));
        // 允许跨域访问
        response.headers().set(CONTENT_TYPE, "application/json;charset=UTF-8");
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "*");// 允许headers自定义
        response.headers().set(ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT,DELETE");
        response.headers().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.headers().set(CONNECTION, KEEP_ALIVE);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
