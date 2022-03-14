package com.petrina.server.handler;

import com.petrina.server.MyServer;
import com.petrina.server.authentication.AuthenticationService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

  private static final String AUTH_CMD_PREFIX = "/auth";
  private static final String AUTHOK_CMD_PREFIX = "/authok";
  private static final String AUTHERR_CMD_PREFIX = "/autherr";
  private static final String CLIENT_MSG_CMD_PREFIX = "/cMsg";
  private static final String SERVER_MSG_CMD_PREFIX = "/sMsg";
  private static final String PRIVATE_MSG_CMD_PREFIX = "/pMsg";
  private static final String STOP_SERVER_CMD_PREFIX = "/stop";
  private static final String END_CLIENT_CMD_PREFIX = "/end";

  private MyServer myServer;
  private Socket clientSocket;
  private DataOutputStream out;
  private DataInputStream in;
  private String username;

  public ClientHandler(MyServer myServer, Socket socket) {

    this.myServer = myServer;
    clientSocket = socket;
  }

  public void handle() throws IOException {
    out = new DataOutputStream(clientSocket.getOutputStream());
    in = new DataInputStream(clientSocket.getInputStream());

    new Thread(() -> {

      try {
        authentication();
        readMessage();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }).start();
  }

  public String getUsername() {
    return username;
  }

  private void authentication() throws IOException {
    while (true){
      String message = in.readUTF();
      if (message.startsWith(AUTH_CMD_PREFIX)){
        boolean isSuccessAuth = processAuthentication(message);
        if (isSuccessAuth){
          break;
        }

      } else {
        out.writeUTF(AUTHERR_CMD_PREFIX + " ошибка аутентификации");
        System.out.println("неудачная попытка аутентификации");
      }

    }

  }

  private boolean processAuthentication(String message) throws IOException {
    String[] parts = message.split("\\s+");
    if (parts.length != 3){
      out.writeUTF(AUTHERR_CMD_PREFIX + " ошибка аутентификации");
    }
    String login = parts[1];
    String password = parts[2];

    AuthenticationService auth = myServer.getAuthenticationService();

    username = auth.getUserNameByLoginAndPassword(login,password);

    if (username != null){
      if (myServer.isUserNameBusy(username)){
        out.writeUTF(AUTHERR_CMD_PREFIX + " Логин уже используется");
        return false;

      }

      out.writeUTF(AUTHOK_CMD_PREFIX + " " + username);
      myServer.subscribe(this);
      System.out.println("Пользователь " + username + " подключился к чату");
      return true;

    } else {
      out.writeUTF(AUTHERR_CMD_PREFIX + " Логин или пароль не соответствуют действительности");
      return false;
    }

  }

  private void readMessage() throws IOException {
    while (true){
      String message = in.readUTF();
      System.out.println("message | " + username + ": " + message);
      if (message.startsWith(STOP_SERVER_CMD_PREFIX)){
        System.exit(0);
      } else if (message.startsWith(END_CLIENT_CMD_PREFIX)){
        return;
      } else if (message.startsWith(PRIVATE_MSG_CMD_PREFIX)){
        myServer.shortCastMessage(message,this); // home task
      } else {
        myServer.broadCastMessage(message,this);
      }
    }
  }

  public void sendMessage(String sender, String message) throws IOException {
    out.writeUTF(String.format("%s %s %s", CLIENT_MSG_CMD_PREFIX,sender,message));
  }

// home task
  public void sendPrivateMessage(String sender, String message) throws IOException {
    out.writeUTF(String.format("%s %s",sender,message));
  }
}
