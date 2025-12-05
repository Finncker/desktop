package com.github.finncker.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UserRegistrationController {
  @FXML
  private TextField nameTextField;
  @FXML
  private TextField emailTextField;
  @FXML
  private Button startButton;

  @FXML
  private void initialize() {
    startButton.disableProperty()
        .bind(nameTextField.textProperty().isEmpty().or(emailTextField.textProperty().isEmpty()));
  }

  @FXML
  private void handleStartAction() {
    System.err.println("Button clicked");
  }
}