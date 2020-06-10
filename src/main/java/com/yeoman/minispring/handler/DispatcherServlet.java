package com.yeoman.minispring.handler;

import com.yeoman.minispring.support.http.handler.HttpAccess;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.AsciiString;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
            if (((FullHttpRequest) msg).uri().contains(DispatcherServlet.FAVICON_ICO)) {
                System.out.println(11111);
                String path = HttpAccess.class.getClassLoader().getResource("favicon.ico").getFile();
                if (path == null) {
                    DispatcherServlet.responsePush(ctx.channel(), "".getBytes(StandardCharsets.UTF_8));
                    return;
                }
                try {
                    path = path.substring(1);
                    System.out.println(path);
                    DispatcherServlet.responseFilePush(ctx, new File(path), (FullHttpRequest) msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FullHttpRequest request = (FullHttpRequest) msg;
            RequestHandler.submitTask(new HttpAccess(request, ctx.channel()));
            System.out.println("I cannot wait response!");
        }
    }

    public static final String FAVICON_ICO = "/favicon.ico";
    public static final AsciiString CONTENT_TYPE = AsciiString.of("Content-Type");
    public static final AsciiString CONTENT_LENGTH = AsciiString.of("Content-Length");
    public static final AsciiString CONNECTION = AsciiString.of("Connection");
    public static final AsciiString KEEP_ALIVE = AsciiString.of("keep-alive");

    private static final AsciiString ACCESS_CONTROL_ALLOW_ORIGIN = AsciiString.of("Access-Control-Allow-Origin");
    private static final AsciiString ACCESS_CONTROL_ALLOW_HEADERS = AsciiString.of("Access-Control-Allow-Headers");
    private static final AsciiString ACCESS_CONTROL_ALLOW_METHODS = AsciiString.of("Access-Control-Allow-Methods");
    private static final AsciiString ACCESS_CONTROL_ALLOW_CREDENTIALS = AsciiString.of("Access-Control-Allow-Credentials");


    public static void responsePush(Channel ctx, byte[] content) {
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

    private static void send100Continue(Channel ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }


    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    public static void responseFilePush(Channel ctx, File file, FullHttpRequest request) throws IOException {

        if (HttpHeaders.is100ContinueExpected(request)) {
            send100Continue(ctx);                               //3
        }

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/ico");
        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        RandomAccessFile f = new RandomAccessFile(file, "r");
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, f.length());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.KEEP_ALIVE);
        }

        ctx.write(response);
        ctx.write(new ChunkedNioFile(f.getChannel()));
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        f.close();
    }

    public static void responseFilePush(ChannelHandlerContext ctx, File file, FullHttpRequest request) throws IOException {

        if (HttpHeaders.is100ContinueExpected(request)) {
            send100Continue(ctx);                               //3
        }

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/ico");
        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        RandomAccessFile f = new RandomAccessFile(file, "r");
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, f.length());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.KEEP_ALIVE);
        }

        ctx.write(response);
        ctx.write(new ChunkedNioFile(f.getChannel()));
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        f.close();
    }
}
