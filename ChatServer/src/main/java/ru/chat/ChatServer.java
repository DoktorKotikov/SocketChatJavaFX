package ru.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static final int PORT = 65112;

    public ChatServer() {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            while(true){
                System.out.println("Waiting connection");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
