package com.github.finncker.desktop.controller;

import com.github.finncker.desktop.model.exceptions.UserAlreadyExists;
import com.github.finncker.desktop.service.UserService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRegistrationController {
  @FXML
  private TextField userNameTextField;
  @FXML
  private TextField userEmailTextField;
  @FXML
  private Button startButton;

  private UserService userService = new UserService();

  @FXML
  private void initialize() {
    startButton.disableProperty()
        .bind(userNameTextField.textProperty().isEmpty().or(userEmailTextField.textProperty().isEmpty()));
  }

  @FXML
  private void handleStartButton() {
    try {
      userService.create(userNameTextField.getText(), userEmailTextField.getText());
    } catch (UserAlreadyExists uae) {
      log.error("Falha ao cadastrar usuário, um usuário já existe.");
    }
  }
}
