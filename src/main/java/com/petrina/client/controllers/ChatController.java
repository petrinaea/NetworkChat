package com.petrina.client.controllers;

import com.petrina.client.modules.Network;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ChatController {

  @FXML
  private ListView<String> usersList;

  @FXML
  private TextArea chatHistory;

  @FXML
  private TextField inputField;

  private Network network;

  public void setNetwork(Network network){
    this.network = network;

  }

  @FXML
  void initialize() {
    usersList.setItems(FXCollections.observableArrayList("A", "B", "C", "D"));
  }


  @FXML
  public void sendMessage() {
    String message = inputField.getText();
    inputField.clear();
    if (message.trim().isEmpty()){
      return;
    }
    network.sendMessage(message);

//    appendMessage(message);

  }

  public void appendMessage(String message) {
    chatHistory.appendText(message);
    chatHistory.appendText(System.lineSeparator());
  }


}