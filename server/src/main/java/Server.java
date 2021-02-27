import commands.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int PORT = 8189;
    private final boolean CENSORSHIP_MODE = true;

    public boolean isCENSORSHIP_MODE() {
        return CENSORSHIP_MODE;
    }

    private ServerSocket server;
    private Socket socket;
    private final List<ClientHandler> clients;
    private final AuthAndCensorService authAndCensorService;
    private final Map<String, String> filter;

    private final ExecutorService clientsService;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        authAndCensorService = new DBUsersAndCensor();
        clientsService = Executors.newCachedThreadPool();
        if (CENSORSHIP_MODE)
            filter = authAndCensorService.getFilter();
        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started");
            while (true) {
                socket = server.accept();
                System.out.println("Client connected: " + socket.getRemoteSocketAddress());
                clientsService.execute(new ClientHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientsService.shutdown();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void broadcastMsg(ClientHandler sender, String message) {
        StringBuilder sb = new StringBuilder(String.format("[%s]: %s",sender.getNickname(),message));
        for (ClientHandler client : clients) {
            client.sendMessage(sb.toString());
        }
    }

    public AuthAndCensorService getAuthAndCensorService() {
        return authAndCensorService;
    }

    public boolean isLoginNotAuthenticated(String login) {
        for (ClientHandler client : clients) {
            if (client.getLogin().equals(login))
                return false;
        }
        return true;
    }

    public void subscribe(ClientHandler newClient) {
        clients.add(newClient);
        broadcastClientList();
    }

    public void unSubscribe(ClientHandler rmClient) {
        clients.remove(rmClient);
        broadcastClientList();
    }

    private void broadcastClientList() {
        StringBuilder sb = new StringBuilder(Commands.CLIENT_LIST);
        for (ClientHandler client : clients)
            sb.append(" ").append(client.getNickname());
        String message = sb.toString();
        for (ClientHandler client : clients)
            client.sendMessage(message);
    }
    public void privateMessage(ClientHandler sender, String recipient, String message) {
        StringBuilder sbMcg = new StringBuilder(String.format("[%s] to [%s]: %s",sender.getNickname(),recipient, message));
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(recipient) || client.equals(sender))
                client.sendMessage(sbMcg.toString());
            else sender.sendMessage("Нет такого пользователя");
        }
    }

    public void broadcastNewClientList(ClientHandler changeNickClient) {
        clients.remove(changeNickClient);
        clients.add(changeNickClient);
        broadcastClientList();
    }
    public String censorship(String word) {
        for (String s : filter.keySet()) {
            if (word.equals(s))
                return filter.get(word);
        }
        return word;
    }
}
