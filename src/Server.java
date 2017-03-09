import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by svetlana on 10.02.17.
 */
public class Server {

    private static int maxSessionCount;
    private static volatile int sessionCount = 0;

    public static void closeSession(){
        sessionCount--;
        System.out.println("Session closed");
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

                // Если есть возможность подключить еще одного клиента
                if (sessionCount < maxSessionCount) {
                    Thread thread = new Thread(new Session(socket));
                    sessionCount++;
                    thread.setName("thread " + (++i));
                    thread.start();
                }
                else {
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF("Sorry, Server is overloaded. Please try later");
                    outputStream.flush();
                    socket.close();
                    System.out.println("Connection closed");
                }
            }
            catch (IOException e) {
                e.printStackTrace();;
            }
        }
    }
}
