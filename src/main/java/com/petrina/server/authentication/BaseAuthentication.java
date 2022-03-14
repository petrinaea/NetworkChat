package com.petrina.server.authentication;

import com.petrina.server.models.User;

import java.util.List;

public class BaseAuthentication implements AuthenticationService {

  private  static final List<User> clients = List.of(
          new User("katrin", "1234","Katrin_cat"),
          new User("sasha", "5678","Sasha_dog"),
          new User("kaban", "9012","Kaban_pig")
  );

  @Override
  public String getUserNameByLoginAndPassword(String login, String password) {
    for (User client : clients) {
      if (client.getLogin().equals(login) && client.getPassword().equals(password)){
        return client.getUsername();
      }
    }
    return null;
  }

  @Override
  public void startAuth() {
    System.out.println("Начало аутентификации");

  }

  @Override
  public void endAuth() {
    System.out.println("Конец аутентификации");
  }
}
