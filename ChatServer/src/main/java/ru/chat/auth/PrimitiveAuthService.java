package ru.chat.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimitiveAuthService implements AuthService{

    private List<Client> clients;

    public PrimitiveAuthService() {
        clients = new ArrayList<>(Arrays.asList(
                new Client("user1", "log1","pass1"),
                new Client("user2", "log2","pass2"),
                new Client("user3", "log3","pass3"),
                new Client("user4", "log4","pass4"),
                new Client("user5", "log5","pass5"),
                new Client("user6", "log6","pass6")

        ));
    }

    @Override
    public void start() {
        System.out.println("Auth started");
    }

    @Override
    public void stop() {
        System.out.println("Auth stopped");
    }

    @Override
    public String getUsernameByLoginPass(String login, String pass) {
        for (Client c : clients){
            if (c.getLogin().equals(login) && c.getPassword().equals(pass)) return c.getUsername();
        }
        return null;
    }
}
