package com.petrina.server.authentication;

public interface AuthenticationService {
  String getUserNameByLoginAndPassword(String login, String password);
  void startAuth();
  void endAuth();
}
