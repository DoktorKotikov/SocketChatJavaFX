package ru.chat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.ChatMessageService;
import network.MessageProcessor;
import ru.chat.messages.MessageDTO;
import ru.chat.messages.MessageType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable, MessageProcessor {
    private final String ALL = "SEND TO ALL";
    @FXML
    public TextArea chatArea;
    @FXML
    public ListView usersOnline;
    @FXML
    public Button btnSendMessage;
    @FXML
    public TextField textMessage;
    @FXML
    public HBox chatBox;
    @FXML
    public HBox inputBox;
    @FXML
    public MenuBar menuBar;
    @FXML
    public HBox authPanel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passField;

    private ChatMessageService messageService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new ChatMessageService("localhost", 65500, this);
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
        sendMessage();
    }

    public void btnSend(ActionEvent actionEvent) {
        sendMessage();
    }

//    private void sendMessage(){
//        String msg = textMessage.getText();
//        if (msg.length() > 0) {
//            MessageDTO dto = new MessageDTO();
//            if(msg.startsWith("/w")) {
//                dto.setMessageType(MessageType.PRIVATE_MESSAGE);
//                String array[] = msg.split(" ", 3);
//                dto.setTo(array[1]);
//                dto.setBody(array[2]);
//            } else {
//                dto.setMessageType(MessageType.PUBLIC_MESSAGE);
//                dto.setBody(msg);
//            }
//            messageService.sendMessage(dto.convertToJson());//тут отправляем текст
//            textMessage.clear();
//        }
//    }

    //метод отправки лс из урока
    private void sendMessage(){
        String msg = textMessage.getText();
        if (msg.length() == 0) return;
        MessageDTO dto = new MessageDTO();
        String selected = (String) usersOnline.getSelectionModel().getSelectedItem();
        if (selected.equals(ALL)) dto.setMessageType(MessageType.PUBLIC_MESSAGE);
        else {
            dto.setMessageType(MessageType.PRIVATE_MESSAGE);
            dto.setTo(selected);
            }
        dto.setBody(msg);
        messageService.sendMessage(dto.convertToJson());//тут отправляем текст
        textMessage.clear();
        }



    private void showMessage(MessageDTO message) {
        String msg = String.format("[%s] [%s] -> %s\n",message.getMessageType(), message.getFrom(), message.getBody());
        chatArea.appendText(msg);
        saveLogs(msg);
    }

    private void saveLogs(String msg) {
        File logs = new File("logs.txt");
        try (FileWriter fos = new FileWriter(logs, true)){
            fos.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processMessage(String msg) {
        MessageDTO dto = MessageDTO.convertFromJson(msg);
        switch (dto.getMessageType()){

//            case PRIVATE_MESSAGE:
//                showBroadcastMessage("Private mess from " + dto.getFrom() + " to " + dto.getTo() + " :" + dto.getBody()) ;
//                break;
            case PUBLIC_MESSAGE:
            case PRIVATE_MESSAGE:
                showMessage(dto) ;
                break;
            case CLIENTS_LIST_MESSAGE:
                refreshUserList(dto);
                break;
            case AUTH_CONFIRM:
                //App.getStage().setTitle(dto.getBody());
                authPanel.setVisible(false);
                chatBox.setVisible(true);
                inputBox.setVisible(true);
                menuBar.setVisible(true);
                break;
            case ERROR_MESSAGE:
                showError(dto);
                break;
        }
    }

    public void sendAuth(ActionEvent actionEvent) {
        String log = loginField.getText();
        String pass = passField.getText();
        if (log.equals("") || pass.equals("")) return;
        MessageDTO dto = new MessageDTO();
        dto.setLogin(log);
        dto.setPassword(pass);
        dto.setMessageType(MessageType.SEND_AUTH_MESSAGE);
        messageService.sendMessage(dto.convertToJson());
    }

    private void showError(Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText(e.getMessage());
        VBox dialog = new VBox();
        Label label = new Label("Trace:");
        TextArea textArea = new TextArea();

        StringBuilder builder = new StringBuilder();
        for (StackTraceElement el : e.getStackTrace()){
            builder.append(el).append(System.lineSeparator());
        }

        textArea.setText(builder.toString());
        dialog.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialog);
        alert.showAndWait();
    }

    private void showError(MessageDTO dto){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText(dto.getMessageType().toString());
        VBox dialog = new VBox();
        Label label = new Label("Trace:");
        TextArea textArea = new TextArea();
        textArea.setText(dto.getBody());
        dialog.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialog);
        alert.showAndWait();
    }

    private void refreshUserList(MessageDTO dto){
        dto.getUserOnline().add(0, ALL);
        usersOnline.setItems(FXCollections.observableArrayList(dto.getUserOnline()));
        usersOnline.getSelectionModel().selectFirst();
    }
}
