import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by svetlana on 17.02.17.
 */
public class Session implements Runnable{

    private Socket socket;

    public Session(Socket socket){
        this.socket = socket;
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
                System.out.println(Thread.currentThread().getName() + " Client: " + message);  // Выводим сообщение клиента
                //outputStream.writeUTF("I got a message \"" + message + "\"");
                //outputStream.flush();
                if (message.equalsIgnoreCase("quit")) {
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
