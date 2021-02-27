import commands.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Server server;
    private final Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //секция аутетификации.
            while (true) {
                String authInputMessage = in.readUTF();
                if (authInputMessage.startsWith("/")) {
                    if (authInputMessage.startsWith(Commands.END)) {
                        System.out.println("Client want to disconnected");
                        out.writeUTF(Commands.END);
                        throw new RuntimeException("Client want to disconnected");
                    }
                    if (authInputMessage.startsWith(Commands.AUTH)) {
                        String[] token = authInputMessage.split("\\s");
                        String newNick = server.getAuthAndCensorService()
                                .getNicknameByLoginAndPassword(token[1], token[2]);
                        login = token[1];
                        if (newNick != null) {
                            if (server.isLoginNotAuthenticated(login)) {
                                nickname = newNick;
                                sendMessage(Commands.AUTH_OK + " " + newNick);
                                server.subscribe(this);
                                break;
                            } else
                                sendMessage("С этим логином уже вошли");
                        } else
                            sendMessage("Не верный логин/пароль");
                    }
                    if (authInputMessage.startsWith(Commands.REG)) {
                        String[] token = authInputMessage.split("\\s");
                        if (token.length < 4)
                            continue;
                        boolean regResult = server.getAuthAndCensorService()
                                .registration(token[1], token[2], token[3]);
                        if (regResult)
                            sendMessage(Commands.REG_OK);
                        else
                            sendMessage(Commands.REG_NO);
                    }
                }
            }
            //секция работы.
            while (true) {
                String inputMessage = in.readUTF();
                if (inputMessage.startsWith("/")) {
                    if (inputMessage.equals(Commands.END)) {
                        out.writeUTF(Commands.END);
                        break;
                    }
                    if (inputMessage.startsWith(Commands.PRIVATE_MSG)) {
                        String[] splitMessage = inputMessage.split("\\s", 3);
                        if (splitMessage.length < 3)
                            continue;
                        server.privateMessage(this, splitMessage[1], splitMessage[2]);
                    }
                    if (inputMessage.startsWith(Commands.CHANGE_NICK)) {
                        String[] splitMessage = inputMessage.split("\\s", 2);
                        if (splitMessage.length < 2 || splitMessage[1].length() > 10)
                            continue;
                        if (server.getAuthAndCensorService().changeNickname(login, splitMessage[1])) {
                            server.broadcastMsg(String.format("%s известен теперь как %s", nickname, splitMessage[1]));
                            nickname = splitMessage[1];
                            server.broadcastNewClientList(this);
                        } else {
                            sendMessage("Данный никнейм занят");
                            continue;
                        }
                    }
                }
                if (!server.isCENSORSHIP_MODE())
                    server.broadcastMsg(this, inputMessage);
                else {
                    StringBuilder sb = new StringBuilder();
                    String[] strToCensus = inputMessage.split("\\s");
                    for (int i = 0; i < strToCensus.length; i++) {
                        strToCensus[i] = server.censorship(strToCensus[i]);
                    }
                    for (String strToCen : strToCensus) {
                        sb.append(strToCen).append(" ");
                    }
                    server.broadcastMsg(this, sb.toString().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Client disconnected.");
            server.unSubscribe(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }

    public void sendMessage(String outMessage) {
        try {
            out.writeUTF(outMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
