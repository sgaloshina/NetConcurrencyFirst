package concurrentutiles;

/**
 * Created by svetlana on 24.03.17.
 */
public class WorkerThread implements Runnable {
    private final ThreadPool threadPool;
    private final Thread thread;
    Runnable currentTask = null;
    private final static Object lock = new Object();
    private volatile boolean isActive;

    public WorkerThread(ThreadPool threadPool) {
        this.threadPool = threadPool;
        this.thread = new Thread(this);
        this.isActive = true;
        this.thread.start();
    }

    public void execute(Runnable task) {
        synchronized (lock) {
            if (currentTask == null) {
                this.currentTask = task;
                lock.notify();
            } else
                throw new IllegalStateException("Error");
        }
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (true) {
                while (currentTask == null)
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        if (!isActive) return;
                    }
                try {
                    if (!isActive) return;
                    currentTask.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    currentTask = null;
                    threadPool.onTaskComplited(this);
                }
            }
        }
    }
    // ждем в цикле пока таска не нул. таск эта наша сессия, вызываем у нее ран.
    // сообщаем threadPool что освободились, когда сессия закончилась.
    // отслеживать экстренные завершения таски

    public void stop() {
        isActive = false;
        thread.interrupt();
        currentTask = null;
    }
}
