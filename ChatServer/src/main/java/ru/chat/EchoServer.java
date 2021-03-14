package ru.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private static Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(65500)) {
            System.out.println("Serv start");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //создаем поток и пишем в консоль сервера сообщения, и передаем их клиенту
            new Thread(() -> {
                while (true) {
                    String message = SCANNER.nextLine();//читаем сообщение с консоли
                    try {
                        out.writeUTF("Message from server " + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            //тут читаем сообщения клиента и пуляем их на морду
            while (true){
                String mess = in.readUTF();
                System.out.println("Recieved " + mess);
                out.writeUTF("Echo " + mess);
                }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
