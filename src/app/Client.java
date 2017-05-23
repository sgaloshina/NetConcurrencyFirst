package app;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by svetlana on 10.02.17.
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("\nCLIENT");

        int port = 0;
        InetAddress host = null;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Usege integer for port");
        }
        try {
            host = InetAddress.getByName(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket socket = null;
        try {
            socket = new Socket(host, port);  // Клиент создает сокет и подключает его к порту на хосте
            System.out.println("The connection is established. Port " + socket.getLocalPort());

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("ISO-8859-1")));
            String message;
            message = inputStream.readUTF(); // ждем ответа от сервера (подтверждение подключния)
            System.out.println("app.Server: " + message);

            if (message.equalsIgnoreCase("you are connected.")) {
                System.out.println("Input message and press Enter to send");
                while (true) {
                    message = reader.readLine();    // читаем введенную строку
                    outputStream.writeUTF(message); // отсылаем сообщение серверу
                    outputStream.flush();
                    System.out.println(inputStream.readUTF());
                    if (message.equalsIgnoreCase("quit"))
                        break;
                }
            }
        } catch (NullPointerException e){
            System.out.println("Connection with server is interrupted.");
        } catch (ConnectException e) {
            System.out.println("Cannot connect to server.");
        } catch (IOException e) {
            System.out.println("Connection with server is interrupted.");
        }
        finally {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
