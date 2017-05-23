package netUtiles;

import netUtiles.MessageHandlerFactory;
import netUtiles.Session;
import concurrentutiles.Channel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by svetlana on 31.03.17.
 */
public class Host implements Runnable {
    private ServerSocket serverSocket;
    private Channel channel;
    private final Object lock = new Object();
    private final MessageHandlerFactory messageHandlerFactory;
    private Thread thread;

    public Host(int port, Channel channel, MessageHandlerFactory mHF) throws IOException {
        this.messageHandlerFactory = mHF;
        this.serverSocket = new ServerSocket(port);
        this.channel = channel;
    }

    public void start() {
        thread = new Thread(this); // this --> channel.take()?
        thread.start();
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            while (true) {
                socket = serverSocket.accept();  // Ожидание соединения с клиентом
                System.out.println("app.Client accepted");
                channel.put(new Session(socket, this, messageHandlerFactory.createMessageHandler()));
            }
        }
        catch (IOException e) {
            e.printStackTrace();;
        }
    }
}
// принимает соединения, создает сессии, кладет в канал