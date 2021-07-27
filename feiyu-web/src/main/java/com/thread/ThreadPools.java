package com.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPools {


    private static final int CORE_POOL_SIZE = 50;
    private static final int MAX_POOL_SIZE = 100;
    private static final int QUEUE_CAPACITY = 2000;
    private static final Long KEEP_ALIVE_TIME = 2L;
    public static ThreadPoolExecutor getThread() {
        //使用阿里巴巴推荐的创建线程池的方式
        // 通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        return executor;
    }
}