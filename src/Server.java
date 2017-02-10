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
            Socket socket = serverSocket.accept();  // Ожидание соединения с клиентом
            System.out.println("Client accepted");

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            String message = null;
            while(true) {
                message = inputStream.readUTF();
                System.out.println("Client sent message: " + message);
                if (message.equalsIgnoreCase("quit")) {
                    serverSocket.close();
                    System.out.println("Connection closed");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
