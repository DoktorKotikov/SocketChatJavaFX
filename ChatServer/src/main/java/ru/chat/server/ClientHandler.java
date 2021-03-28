package ru.chat.server;

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
    private String currentUserName;

    private static final int WAIT_TIME = 120000;
    //создаем клиента
    public ClientHandler(Socket socket, ChatServer chatServer){
        try{
            this.chatServer = chatServer;
            this.socket = socket;
            this.socket.setSoTimeout(WAIT_TIME);
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(() ->{
                try {
                    authenticate();
                    readMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    private void readMessages() throws IOException {
        try {
            while (!Thread.currentThread().isInterrupted() || socket.isConnected()) {
                String msg = inputStream.readUTF();
                MessageDTO dto = MessageDTO.convertFromJson(msg);
                dto.setFrom(currentUserName);
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
            Thread.currentThread().interrupt();
        } finally {
            closeHandler();
        }
    }



    private void authenticate() {
            try {
                while (true) {
                    String authMessage = inputStream.readUTF();
                    MessageDTO dto = MessageDTO.convertFromJson(authMessage);
                    String username = chatServer.getAuthService().getUsernameByLoginPass(dto.getLogin(), dto.getPassword());
                    MessageDTO response = new MessageDTO();
                    if (username == null) {
                        response.setMessageType(MessageType.ERROR_MESSAGE);
                        response.setBody("Wrong login or pass!");
                    } else if (chatServer.isUserBusy(username)){
                        response.setMessageType(MessageType.ERROR_MESSAGE);
                        response.setBody("Yu are clone");
                        System.out.println("Clone");
                    }
                    else {
                        response.setMessageType(MessageType.AUTH_CONFIRM);
                        response.setBody(username);
                        currentUserName = username;
                        chatServer.subscribe(this);
                        sendMessage(response);
                        break;
                    }
                    sendMessage(response);

                }
            } catch (IOException e){
                e.printStackTrace();
                closeHandler();
            }

        }




    public String getcurrentUserName() {
        return currentUserName;
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
