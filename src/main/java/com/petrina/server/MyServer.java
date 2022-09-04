package com.petrina.server;

import com.petrina.server.authentication.AuthenticationService;
import com.petrina.server.authentication.DBAuthenticationService;
import com.petrina.server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
  private final ServerSocket serverSocket;
  private final AuthenticationService authenticationService;
  private final List<ClientHandler> clients;

  public MyServer(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    authenticationService = new DBAuthenticationService();
    clients = new ArrayList<>();
  }

  public final static ExecutorService executorService = Executors.newFixedThreadPool(4);

  public void start() {
    System.out.println("СЕРВЕР ЗАПУЩЕН!");
    System.out.println("----------------");

    try {
      while (true) {
        waitAndProcessNewClientConnection();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void waitAndProcessNewClientConnection() throws IOException {
    System.out.println("Ожидание клиента...");
    Socket socket = serverSocket.accept();
    System.out.println("Клиент подключился!");

    processClientConnection(socket);
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
    System.out.println(clients);
  }

  public synchronized boolean isUsernameBusy(String username) {
    for (ClientHandler client : clients) {
      if (client.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  public AuthenticationService getAuthenticationService() {
    return authenticationService;
  }

  public synchronized void broadcastMessage(String message, ClientHandler sender, boolean isServerMessage) throws IOException {
    for (ClientHandler client : clients) {
      if (client == sender) {
        continue;
      }
      client.sendMessage(isServerMessage ? null : sender.getUsername(), message);
    }
  }

  public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
    broadcastMessage(message, sender, false);
  }

  public synchronized void sendPrivateMessage(ClientHandler sender, String recipient, String privateMessage) throws IOException {
    for (ClientHandler client : clients) {
      if (client.getUsername().equals(recipient)) {
        client.sendMessage(sender.getUsername(), privateMessage);
      }
    }
  }
}
