package com.yeoman.minispring.handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RequestHandler {

    private static ThreadPoolExecutor executor;
    private static Byte lock = 1;

    public static void init() {
        if (executor == null) {
            synchronized (lock) {
                if (executor == null) {
                    BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(20);
                    executor = new ThreadPoolExecutor(
                            10, 15, 600, TimeUnit.SECONDS,
                            queue, new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
    }

    public static void submitTask(Runnable runnable) {
        init();
        executor.execute(runnable);
    }



}
