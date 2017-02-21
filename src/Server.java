import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by svetlana on 10.02.17.
 */
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        List<Thread> threadList = new ArrayList();
        final int MAX_CLIENT = 3;

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

                // Убираем умершие потоки, чтобы освободить возможность подключения для нового клиента
                for (int j = 0; j < threadList.size(); j++){
                    if (!threadList.get(j).isAlive())
                        threadList.remove(j);
                }

                // Если есть возможность подключить еще одного клиента
                if (threadList.size() < MAX_CLIENT) {
                    Thread thread = new Thread(new Session(socket));
                    threadList.add(thread);
                    thread.setName("thread " + (++i));
                    thread.start();
                }
                else {
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF("Sorry, Server is overloaded.");
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
