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

    private final static ExecutorService pool = new ThreadPoolExecutor(1, 5, 0, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(20), new NamedThreadFactory("lzs-thread-util"),
            new ThreadPoolExecutor.DiscardOldestPolicy());


    public static void  submit (Runnable task){
        pool.submit(task);
    }
}
