package ru.chat;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    public TextArea chatArea;
    @FXML
    public ListView usersOnline;
    @FXML
    public Button btnSendMessage;
    @FXML
    public TextField textMessage;

    private MessageService messageService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new MessageService(chatArea);
        usersOnline.setItems(FXCollections.observableArrayList("Vasya", "Petya", "Kolya"));
    }

    public void login(ActionEvent actionEvent) {
    }

    public void logout(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(1);
    }

    public void about(ActionEvent actionEvent) {
    }

    public void support(ActionEvent actionEvent) {
    }

    public void pressEnter(ActionEvent actionEvent) {
        appendTextFromTF();
    }

    public void btnSend(ActionEvent actionEvent) {
        appendTextFromTF();
    }

    private void appendTextFromTF(){
        String msg = textMessage.getText();
        if (msg.length() > 0) {
            messageService.sendMessage(msg);//тут отправляем текст
            textMessage.clear();
        }
    }


}
