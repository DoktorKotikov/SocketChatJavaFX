package ru.chat.auth;

public interface AuthService {
    void start();
    void stop();
    String getUsernameByLoginPass(String login, String pass);
}
