package io.github.nickid2018.mcde.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncUtil {

    private static ExecutorService executor;

    public static void start() {
        executor = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "MCDE-Async-Thread");
            thread.setDaemon(true);
            return thread;
        });
    }

    public static void execute(Runnable runnable) {
        if (executor != null)
            executor.execute(runnable);
    }

    public static <V> Future<V> submit(Callable<V> callable) {
        return executor != null ? executor.submit(callable) : null;
    }

    public static void terminate() {
        executor.shutdown();
        executor = null;
    }
}
