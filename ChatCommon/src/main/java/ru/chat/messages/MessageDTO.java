package ru.chat.messages;

import com.google.gson.Gson;

public class MessageDTO {
    private String body;
    private MessageType messageType;
    private String login;
    private String password;
    private String to;
    private String from;

    public static MessageDTO convertFromJson(String json){
        return new Gson().fromJson(json, MessageDTO.class);
    }

    public String convertToJson(){
        return new Gson().toJson(this);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}