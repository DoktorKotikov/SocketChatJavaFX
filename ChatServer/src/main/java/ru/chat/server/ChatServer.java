package ru.chat.server;

import ru.chat.auth.AuthService;
import ru.chat.auth.PrimitiveAuthService;
import ru.chat.messages.MessageDTO;
import ru.chat.messages.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 65500;
    private List<ClientHandler> onlineClientsList;
    private AuthService authService;

    public ChatServer() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server started");
            authService = new PrimitiveAuthService();
            authService.start();
            onlineClientsList = new LinkedList<>();
            while(true){
                System.out.println("Waiting connection");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isUserBusy(String username) {
        for (ClientHandler clientHandler : onlineClientsList){
            if(clientHandler.getcurrentUserName().equals(username)) return true;
        }
        return false;
    }

    public synchronized void broadcastMEssage(MessageDTO dto) {
        for (ClientHandler clientHandler : onlineClientsList){
            clientHandler.sendMessage(dto);
        }
    }

    public synchronized void privateMessage(MessageDTO dto){
        for (ClientHandler clientHandler : onlineClientsList) {
            if(dto.getTo().equals(clientHandler.getcurrentUserName())){
                clientHandler.sendMessage(dto);
                break;
            }
        }
    }
//    public synchronized void privateMessage(MessageDTO dto){
//        for (ClientHandler clientHandler : onlineClientsList) {
//            if(dto.getTo().equals(clientHandler.getcurrentUserName()) || dto.getFrom().equals(clientHandler.getcurrentUserName())){
//                clientHandler.sendMessage(dto);
//                break;
//            }
//        }
//    }

    public synchronized void broadcastOnlineClients(){
        MessageDTO dto = new MessageDTO();
        dto.setMessageType(MessageType.CLIENTS_LIST_MESSAGE);
        List<String> onlines = new LinkedList<>();
        for (ClientHandler clientHandler : onlineClientsList) {
            onlines.add(clientHandler.getcurrentUserName());
        }
        dto.setUserOnline(onlines);
        broadcastMEssage(dto);
    }

    public synchronized void subscribe(ClientHandler c) {
        onlineClientsList.add(c);
        broadcastOnlineClients();
    }

    public synchronized void unsubscribe(ClientHandler c) {
        onlineClientsList.remove(c);
        broadcastOnlineClients();
    }

    public AuthService getAuthService() {
        return authService;
    }
}
