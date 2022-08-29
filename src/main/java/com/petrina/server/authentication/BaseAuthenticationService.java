package com.petrina.server.authentication;

import com.petrina.server.models.User;

import java.util.List;

public class BaseAuthenticationService implements AuthenticationService{

    private static final List<User> clients = List.of(
            new User("user1", "1111", "user1"),
            new User("user2", "2222", "user2"),
            new User("user3", "3333", "user3"),
            new User("user4", "4444", "user4")
    );

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        for (User client : clients) {
            if (client.getLogin().equals(login) && client.getPassword().equals(password)) {
                return client.getUsername();
            }
        }
        return null;
    }

    @Override
    public void startAuthentication() {
        System.out.println("Старт аутентификации");

    }

    @Override
    public void endAuthentication() {
        System.out.println("Конец аутентификации");

    }
}
