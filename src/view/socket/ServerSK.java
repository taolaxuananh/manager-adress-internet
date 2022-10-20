package view.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class ServerSK {
    private static final int POST = 5555;

    private static ServerSK serverSK;

    private ServerSK() {
    }

    public static ServerSK getInstance() throws IOException {
        if (Objects.isNull(serverSK)) {
            serverSK = new ServerSK();
        }
        return serverSK;
    }

    public void read() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(POST);
        System.out.println("Binding to port " + POST + "please wait ...");
        System.out.println("Waiting for a client ...");
        Socket socket = serverSocket.accept();
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Object str = inputStream.readObject();
        if (str instanceof String) {
            System.out.println(str);
        } else {
            read();
        }

    }

}
