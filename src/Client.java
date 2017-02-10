import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by svetlana on 10.02.17.
 */
public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("\nCLIENT");

            int port = Integer.parseInt(args[1]);
            Socket socket = new Socket(args[0], 2017);  // Клиент создает сокет и подключает его к порту на хосте
            System.out.println("The connection is established.");

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = null;
            System.out.println("Type message and press Enter\n");
            while(true){
                message = reader.readLine();
                outputStream.writeUTF(message);
                outputStream.flush();
                if (message.equalsIgnoreCase("quit"))
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
