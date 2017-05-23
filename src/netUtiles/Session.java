package netUtiles;

import app.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Handler;

/**
 * Created by svetlana on 17.02.17.
 */
public class Session implements Runnable{
    private final Host host;
    private final Socket socket;
    private final MessageHandler messageHandler;
    private final int port;

    public Session(Socket socket, Host host, MessageHandler messageHandler){
        this.socket = socket;
        this.port = socket.getPort();
        this.host = host;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            String message;
            outputStream.writeUTF("You are connected.");
            outputStream.flush();

            while (true) {
                message = inputStream.readUTF();    // Сервер ожидает сообщение от Клиента
                System.out.println(Thread.currentThread().getName() + " app.Client: " + message);  // Выводим сообщение клиента
                outputStream.writeUTF(messageHandler.handle(message));
                if (message.equalsIgnoreCase("quit")) {
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection interrupted");
        }
        finally {
            Server.closeSession();  // при завершении сессии декрементим счетчик
        }
    }
}
