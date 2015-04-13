package net.ym.zzy.favorite.data.executor;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zengzheying on 15/3/28.
 */
public class JobExecutor extends ThreadPoolExecutor{

    HashMap<String, Future<?>> futures = new HashMap<String, Future<?>>();

    private static class LazyHolder{
        private static final JobExecutor INSTANCE = new JobExecutor(new LinkedBlockingQueue<Runnable>());
    }

    public static JobExecutor getInstance(){
        return LazyHolder.INSTANCE;
    }

    private static final int INITIAL_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;

    private static final int KEEP_ALIVE_TIME = 10;

    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private JobExecutor(BlockingQueue<Runnable> workQueue){
        super(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue);
    }

    public void execute(Runnable runnable){
        if (runnable == null){
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        super.execute(runnable);
    }

    public Future<?> submit(Runnable runnable, String key){
        if (runnable == null){
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        Future<?> future = super.submit(runnable);
        futures.put(key, future);
        return future;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        Future<?> future = (Future<?>)r;
        synchronized (futures) {
            Set<String> keySet = futures.keySet();
            for (String key : keySet){
                if (future == futures.get(key)){
                    futures.remove(key);
                }
            }
        }
    }

    public void cancel(String key){
        synchronized (futures) {
            if (futures.containsKey(key)){
                futures.get(key).cancel(true);
            }
        }
    }
}
