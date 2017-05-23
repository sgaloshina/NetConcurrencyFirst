package concurrentutiles;

import java.util.LinkedList;

/**
 * Created by svetlana on 17.03.17.
 */
public class Channel {
    private static final Object lock = new Object();    // монитор
    private int maxCount = 0;
    private final LinkedList<Runnable>  linkedList = new LinkedList<Runnable>();

    public Channel(int maxCount) {
        this.maxCount = maxCount;
    }

    public void put(Runnable x) {
        synchronized (lock) {
            while(linkedList.size() == maxCount) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            linkedList.addLast(x);
            lock.notifyAll();
        }
    }

    Runnable take() {
        synchronized (lock) {
            while(linkedList.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notifyAll();
            return linkedList.removeFirst();
        }
    }

    public int size(){
        synchronized (lock){
            return linkedList.size();
        }
    }

}
