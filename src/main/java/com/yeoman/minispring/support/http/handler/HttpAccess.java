package com.yeoman.minispring.support.http.handler;

import com.yeoman.minispring.handler.DispatcherServlet;
import com.yeoman.minispring.support.AbstractReqParamHelper;
import com.yeoman.minispring.support.ScanParamHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpRequest;

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
        String[] names = {"aaa", "bbb", "ccc"};
        System.out.println("I am going to get the params!");
        Object[] values = AbstractReqParamHelper
                .getInstance(request)
                .getReqParamValueByArrayParamName(request, names);
        System.out.println("I got the params!");
        System.out.println("The params is : ");
        for (int i = 0; i < names.length; i++) {
            System.out.println(String.format("(%s, %s)", names[i], values[i].toString()));
        }
        System.out.println("I wait 5s!");
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DispatcherServlet.responsePush(channel, "[\"收到了\"]".getBytes(StandardCharsets.UTF_8));
        System.out.println("I responsed!");
    }
}
