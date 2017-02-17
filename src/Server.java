import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by svetlana on 10.02.17.
 */
public class Server {
    public static void main(String[] args) {
        try {

            System.out.println("\nSERVER");

            int port = Integer.parseInt(args[0]);
            ServerSocket serverSocket = new ServerSocket(port);

            int i = 0;
            while (true) {
                i++;
                Socket socket = serverSocket.accept();  // Ожидание соединения с клиентом
                System.out.println("Client accepted");

                Thread thread = new Thread(new Session(socket, i));
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
