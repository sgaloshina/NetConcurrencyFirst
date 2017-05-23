package concurrentutiles;

import java.util.LinkedList;

/**
 * Created by svetlana on 24.03.17.
 */
public class ThreadPool {
    private static final Object lock = new Object();
    private final int maxSize;
    private Channel freeWorkers;
    private LinkedList<WorkerThread> allWorkers = new LinkedList<WorkerThread>();

    public ThreadPool(int maxSize) {
        this.maxSize = maxSize;
        freeWorkers = new Channel(maxSize);
        WorkerThread workerThread = new WorkerThread(this);
        allWorkers.addLast(workerThread);
        freeWorkers.put(workerThread);
    }

    public void execute (Runnable task){
        if(freeWorkers.size() == 0){
            synchronized (lock) {
                if (allWorkers.size() < maxSize) {
                    WorkerThread workerThread = new WorkerThread(this);
                    allWorkers.addLast(workerThread);
                    freeWorkers.put(workerThread);
                }
            }
        }
        ((WorkerThread)freeWorkers.take()).execute(task);
    }

    public void onTaskComplited(WorkerThread workerThread){
        freeWorkers.put(workerThread);
    }

    public void stop() {
        for (WorkerThread worker : allWorkers)
            worker.stop();
    }

    public boolean isBusy(){
        // Все воркеры заняты и невозможно создать нового
        return (freeWorkers.size() == 0) && (allWorkers.size() == maxSize);
    }
}
