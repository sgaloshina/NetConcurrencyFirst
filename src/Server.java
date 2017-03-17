import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by svetlana on 10.02.17.
 */
public class Server {

    private static final Object lock = new Object();    // монитор
    private static int maxSessionCount;     // максимальное количество одновременных сессий. Передается в аргументах main.
    private static volatile int sessionCount = 0;   // текущее количество сессий
    static Channel channel = new Channel(maxSessionCount);

    // декремент счетчика сессий. Вызывается при закрытии сессии.
    public static void closeSession(){
        synchronized (lock) {
            sessionCount--;
            lock.notify();       // оповещаем ждущий поток
            System.out.println("Session closed");
        }
    }

    public static void main(String[] args) {
        maxSessionCount = Integer.parseInt(args[1]);
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            System.out.println("\nSERVER");
            int port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 0;  // счетчик создаваемых потоков, чтобы давать им раные имена
        while (true) {
            try {
                socket = serverSocket.accept();  // Ожидание соединения с клиентом
                System.out.println("Client accepted");

                // Если есть возможность подключить еще одного клиента, подключаем, инкрементим счетчик
                // Если нет возможности, вызываем wait()
                synchronized (lock) {
                    while (sessionCount == maxSessionCount) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    channel.put(new Session(socket));
                    Thread thread = new Thread(channel.take());
                    sessionCount++;
                    thread.setName("thread " + (++i));
                    thread.start();
                }
            }
            catch (IOException e) {
                e.printStackTrace();;
            }
        }
    }
}
