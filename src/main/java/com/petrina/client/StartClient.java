package com.petrina.client;

import com.petrina.client.controllers.ChatController;
import com.petrina.client.modules.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartClient extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("chat-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Chat");
    stage.setScene(scene);
    stage.setY(400);
    stage.setX(400);
    stage.setAlwaysOnTop(true);
    stage.show();

    Network network = new Network();
    ChatController chatController = fxmlLoader.getController();

    chatController.setNetwork(network);

    network.connect();
    network.waitMessage(chatController);
  }

  public static void main(String[] args) {
    launch();
  }
}