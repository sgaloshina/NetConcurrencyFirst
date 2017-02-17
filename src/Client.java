import java.io.*;
import java.net.Socket;

/**
 * Created by svetlana on 10.02.17.
 */
public class Client {
    public static void main(String[] args) {
        try {

            System.out.println("\nCLIENT");

            int port = Integer.parseInt(args[1]);
            Socket socket = new Socket(args[0], port);  // Клиент создает сокет и подключает его к порту на хосте
            System.out.println("The connection is established.");

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String message;
            System.out.println("Type message and press Enter\n");
            while(true){
                message = reader.readLine();    // читаем введенную строку
                outputStream.writeUTF(message); // отсылаем сообщение серверу
                outputStream.flush();
                //message = inputStream.readUTF(); // ждем ответа от сервера
                //System.out.println("Server sent me: " + message);

                if (message.toLowerCase().endsWith("quit\""))
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
