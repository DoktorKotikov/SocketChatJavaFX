package ru.chat.database;

import ru.chat.auth.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBClass {
    private static Connection connection;
    private static Statement statement;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:chatUsers.db");
        statement = connection.createStatement();
    }

    public static void createTable() throws SQLException {
        statement.executeUpdate("CREATE table if not exists chatUsers (id integer primary key autoincrement, username text, login text, password text) ;");
    }

    public static void insertTestsUsers() throws SQLException {
        statement.executeUpdate("insert into chatUsers (username, login, password) values ('user1', 'log1', 'pass1');");
        statement.executeUpdate("insert into chatUsers (username, login, password) values ('user2', 'log2', 'pass2');");
        statement.executeUpdate("insert into chatUsers (username, login, password) values ('user3', 'log3', 'pass3');");
    }

    public static List<Client> readUsersForAuth() {
        ArrayList<Client> clients = new ArrayList<>();
        try (ResultSet rs = statement.executeQuery("select username, login, password from chatUsers;")) {
            while (rs.next()){
                clients.add(new Client(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return clients;
    }

    public static void createInsertDbWithTestsUsers() {
        try {
            connect();
            createTable();
            insertTestsUsers();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
