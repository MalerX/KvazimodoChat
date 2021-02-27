package kvazi_server;

import kvazi_commands.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connect {
    private static Socket socket;
    private static final int PORT = 8189;
    private static final String IP_ADDRESS = "localhost";

    private static DataInputStream in;
    private static DataOutputStream out;

    private String nickname;

    public Connect() {
        try {
            socket = new Socket(IP_ADDRESS,PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connect connect = new Connect();
        try {
            out.writeUTF(Commands.AUTH + " aaa aaa");
            System.out.println(in.readUTF());
            out.writeUTF(Commands.END);
            while (true)
                System.out.println(in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
