package com.lzs.chat.base.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/12 17:25
 * @since <版本号>
 */
public class ThreadUtil {
    public static final   int corePoolsize=2;
    public static final   int maximumPoolSize=10;
    public static final   int max_capacity=100;

    private final static ExecutorService pool = new ThreadPoolExecutor(corePoolsize, maximumPoolSize, 120L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(max_capacity), new NamedThreadFactory("lzs-thread-util"),
            new ThreadPoolExecutor.DiscardOldestPolicy());


    public static void  submit (Runnable task){
        pool.submit(task);
    }
}
