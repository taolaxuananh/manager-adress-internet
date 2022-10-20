package view.socket;

import model.Computer;
import model.OrderFood;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ClientSK {
    private static final int SERVER_PORT = 5555;

    private static final String SERVER_IP = "127.0.0.";

    private static ClientSK clientSK;

    private ClientSK() {}

    public static ClientSK getInstance() {
        if (Objects.isNull(clientSK)) {
            clientSK = new ClientSK();
        }
        return clientSK;
    }

    public void write(Integer clientIp, String message) throws IOException, InterruptedException {
        Socket socket = new Socket(SERVER_IP + clientIp, SERVER_PORT);
        System.out.println("Connected: " + socket);
        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        os.writeObject(message);
        os.flush();
        Thread.sleep(200);
    }
}
