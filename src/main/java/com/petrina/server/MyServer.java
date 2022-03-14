package com.petrina.server;

import com.petrina.server.authentication.AuthenticationService;
import com.petrina.server.authentication.BaseAuthentication;
import com.petrina.server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

  private final ServerSocket serverSocket;
  private final AuthenticationService authenticationService;
  private final List<ClientHandler> clients;

  public MyServer(int port) throws IOException {

    serverSocket = new ServerSocket(port);
    authenticationService = new BaseAuthentication();
    clients  = new ArrayList<>();

  }

  public void start() {
    System.out.println("Сервер запущен");

    try {
      while (true){
        waitAndProcessNewClientConnection();

      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void waitAndProcessNewClientConnection() throws IOException {
    System.out.println("Ожидание клиента");
    Socket socket = serverSocket.accept();
    System.out.println("Клиент подключился");

    processClientConnection(socket);
  }

  public AuthenticationService getAuthenticationService() {
    return authenticationService;
  }

  private void processClientConnection(Socket socket) throws IOException {
    ClientHandler handler = new ClientHandler(this, socket);
    handler.handle();
  }

  public synchronized void subscribe(ClientHandler clientHandler) {
    clients.add(clientHandler);
  }

  public synchronized void unSubscribe(ClientHandler clientHandler) {
    clients.remove(clientHandler);
  }

  public synchronized boolean isUserNameBusy(String username) {
    for (ClientHandler client : clients) {
      if (client.getUsername().equals(username)){
        return true;
      }
    } return false;
  }

  public synchronized void broadCastMessage(String message, ClientHandler sender) throws IOException {
    for (ClientHandler client : clients) {
      if (client == sender){
        continue;
      }
      client.sendMessage(sender.getUsername(),message);
    }
  }

  public synchronized void shortCastMessage(String message, ClientHandler sender) throws IOException {
    String[] parts = message.split("\\s+");
    String nickName = parts[1];

    for (ClientHandler client : clients) {
      if (client.getUsername().equals(nickName)){
        client.sendPrivateMessage(sender.getUsername(),message);
      }
    }
  }
}
