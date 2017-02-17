import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by svetlana on 17.02.17.
 */
public class Session implements Runnable{

    private Socket socket;
    private int threadNumber;

    public Session(Socket socket, int threadNum){
        this.socket = socket;
        this.threadNumber = threadNum;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            String message;
            while (true) {
                message = inputStream.readUTF();    // Сервер ожидает ответ от Клиента
                System.out.println("thread " + threadNumber +": Client sent message: " + message);  // Выводим сообщение клиента
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
