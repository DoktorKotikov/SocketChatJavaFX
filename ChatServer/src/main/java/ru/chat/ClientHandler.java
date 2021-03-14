package ru.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//обработчик
public class ClientHandler {
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    //создаем клиента
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //читаем сообщение
    private void readMessages() throws IOException {
        while(true) {
            String clientMessage = inputStream.readUTF();
            System.out.println("Got message " + clientMessage);

            if (clientMessage.startsWith("/")){
                if (clientMessage.startsWith("/exit")) break;

            }
        }
    }
}
