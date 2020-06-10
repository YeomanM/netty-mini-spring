package com.yeoman.minispring.support.http.handler;

import com.yeoman.minispring.handler.DispatcherServlet;
import com.yeoman.minispring.support.AbstractReqParamHelper;
import com.yeoman.minispring.support.ScanParamHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/5/28
 * @desc
 */
public class HttpAccess implements Runnable {

    private FullHttpRequest request;
    private Channel channel;

    public HttpAccess() {
    }

    public HttpAccess(FullHttpRequest request, Channel channel) {
        this.request = request;
        this.channel = channel;
    }

    @Override
    public void run() {

        if (request.uri().contains(DispatcherServlet.FAVICON_ICO)) {
            String path = HttpAccess.class.getClassLoader().getResource("favicon.ico").getFile();
            if (path == null) {
                DispatcherServlet.responsePush(channel, "".getBytes(StandardCharsets.UTF_8));
                return;
            }
            try {
                path = path.substring(1);
                DispatcherServlet.responseFilePush(channel, new File(path), request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ;
        }

        String[] names = {"aaa", "bbb", "ccc"};
        System.out.println("I am going to get the params!");
        System.out.println("I wait 25s!");
        Object[] values = AbstractReqParamHelper
                .getInstance(request)
                .getReqParamValueByArrayParamName(request, names);
        System.out.println("I got the params!");
        System.out.println("The params is : ");
        for (Object value : values) {
            System.out.println(value);
        }
        System.out.println("I wait 5s!");
        DispatcherServlet.responsePush(channel, "[\"收到了\"]".getBytes(StandardCharsets.UTF_8));
        System.out.println("I responsed!");
    }
}
