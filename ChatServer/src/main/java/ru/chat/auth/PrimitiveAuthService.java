package ru.chat.auth;

import ru.chat.database.DBClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimitiveAuthService implements AuthService{

    private List<Client> clients;

    public PrimitiveAuthService() {

        try {
            refreshUsers();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void refreshUsers() throws ClassNotFoundException, SQLException {
        DBClass.connect();
        clients = DBClass.readUsersForAuth();
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
        try {
            refreshUsers();
            for (Client c : clients){
            if (c.getLogin().equals(login) && c.getPassword().equals(pass)) return c.getUsername();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


}
