package ru.chat;

import ru.chat.messages.MessageDTO;
import ru.chat.messages.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//обработчик
public class ClientHandler {
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private ChatServer chatServer;
    private String user;
    //создаем клиента
    public ClientHandler(Socket socket, ChatServer chatServer){
        try{
            this.chatServer = chatServer;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(() ->{
                    authenticate();
                    readMessages();
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //отправляем сообщение
    public void sendMessage(MessageDTO dto) {
        try {
            outputStream.writeUTF(dto.convertToJson());
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    //читаем сообщение
    private void readMessages() {
        try {
            while (true) {
                String msg = inputStream.readUTF();
                MessageDTO dto = MessageDTO.convertFromJson(msg);
                dto.setFrom(user);
                switch (dto.getMessageType()) {
                    case PUBLIC_MESSAGE:
                        chatServer.broadcastMEssage(dto);
                        break;
                    case PRIVATE_MESSAGE:
                        chatServer.privateMessage(dto);
                        break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void authenticate() {
        try {
            while (true) {
                String authMessage = inputStream.readUTF();
                MessageDTO dto = MessageDTO.convertFromJson(authMessage);
                String username = chatServer.getAuthService().getUsernameByLoginPass(dto.getLogin(), dto.getPassword());
                MessageDTO send = new MessageDTO();
                if (username == null) {
                    send.setMessageType(MessageType.ERROR_MESSAGE);
                    send.setBody("Wrong login or pass!");
                    sendMessage(send);
                } else {
                    send.setMessageType(MessageType.AUTH_CONFIRM);
                    send.setBody(username);
                    user = username;
                    chatServer.subscribe(this);
                    sendMessage(send);
                    break;
                }

            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public void closeHandler(){
        try {
            chatServer.unsubscribe(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
