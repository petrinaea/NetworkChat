package com.petrina.client.controllers;

import com.petrina.client.modules.Network;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.text.DateFormat;
import java.util.Date;

public class ChatController {

  @FXML
  private ListView<String> usersList;

  @FXML
  private Label usernameTitle;

  @FXML
  private TextArea chatHistory;

  @FXML
  private TextField inputField;

  @FXML
  private Button sendButton;
  private String selectedRecipient;

  @FXML
  public void initialize() {
    usersList.setItems(FXCollections.observableArrayList("Diana", "Artem", "Alex", "Katrin"));
    sendButton.setOnAction(event -> sendMessage());
    inputField.setOnAction(event -> sendMessage());

    usersList.setCellFactory(lv -> {
      MultipleSelectionModel<String> selectionModel = usersList.getSelectionModel();
      ListCell<String> cell = new ListCell<>();
      cell.textProperty().bind(cell.itemProperty());
      cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
        usersList.requestFocus();
        if (!cell.isEmpty()) {
          int index = cell.getIndex();
          if (selectionModel.getSelectedIndices().contains(index)) {
            selectionModel.clearSelection(index);
            selectedRecipient = null;
          } else {
            selectionModel.select(index);
            selectedRecipient = cell.getItem();
          }
          event.consume();
        }
      });
      return cell;
    });
  }

  private Network network;

  public void setNetwork(Network network){
    this.network = network;

  }

  @FXML
  public void sendMessage() {
    String message = inputField.getText().trim();
    inputField.clear();


    if (message.trim().isEmpty()) {
      return;
    }

    if (selectedRecipient != null) {
      network.sendPrivateMessage(selectedRecipient, message);
    } else {
      network.sendMessage(message);
    }

    appendMessage("Я: " + message);

  }

  public void appendMessage(String message) {
    String timeStamp = DateFormat.getInstance().format(new Date());

    chatHistory.appendText(timeStamp);
    chatHistory.appendText(System.lineSeparator());
    chatHistory.appendText(message);
    chatHistory.appendText(System.lineSeparator());
    chatHistory.appendText(System.lineSeparator());
  }

  public void appendServerMessage(String serverMessage) {
    chatHistory.appendText(serverMessage);
    chatHistory.appendText(System.lineSeparator());
    chatHistory.appendText(System.lineSeparator());
  }

  public void setUsernameTitle(String username) {
    this.usernameTitle.setText(username);
  }


}