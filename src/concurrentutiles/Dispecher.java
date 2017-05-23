package concurrentutiles;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

/**
 * Created by svetlana on 17.03.17.
 */
public class Dispecher implements Runnable {
    private ThreadPool threadPool;
    private Channel channel;
    private volatile boolean isActive;
    Thread thread;
    private int i = 0;

    public Dispecher(Channel channel, ThreadPool threadPool){
        this.channel = channel;
        this.threadPool = threadPool;
    }

    public void start() {
        thread = new Thread(this);  //this --> channel.take() ?
        isActive = true;
        thread.setName("thread " + (++i));
        thread.start();
    }

    @Override
    public void run() {
        while(isActive){
            try {
                threadPool.execute(channel.take());
            } catch (InternalException e) {
                if (!isActive) return;
            }
        }
    }

    public void stop() {
        if (isActive){
            threadPool.stop();
            thread.interrupt();
            isActive = false;
        }
    }

    public boolean isActive(){
        return this.isActive;
    }
}
// берет сессии из канала, отправляет их в threadpool
// вызывает threadPool.execute

