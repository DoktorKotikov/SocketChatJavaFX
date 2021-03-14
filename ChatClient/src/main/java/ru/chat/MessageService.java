package ru.chat;

import java.awt.*;
import java.io.IOException;
import javafx.scene.control.TextArea;

//Для работы с сообщениями
public class MessageService {
    private static final String HOST = "localhost";
    private static final int PORT = 65500;
    private NetworkHelper networkHelper;
    private TextArea chatArea;

    public MessageService(TextArea chatArea){
        this.chatArea = chatArea;
        connectToServer();
    }

    private void connectToServer(){
        try {
            this.networkHelper = new NetworkHelper(HOST, PORT, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //отправляем сообщние
    public void sendMessage(String msg){
        networkHelper.writeMessage(msg);
    }

    //получаем сообщение и печатаем в чате
    public void receiveMessage(String msg){
        chatArea.appendText(msg + System.lineSeparator());
    }
}
