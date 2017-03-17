import java.util.LinkedList;

/**
 * Created by svetlana on 17.03.17.
 */
public class Channel {
    private int maxCount = 0;
    private final LinkedList<Runnable>  linkedList = new LinkedList<Runnable>();

    public Channel(int maxCount) { this.maxCount = maxCount; }
    void put(Runnable x) { linkedList.addLast(x); }
    Runnable take() { return linkedList.removeFirst(); }

}
