package com.github.finncker.desktop.controller;

import javafx.scene.control.Label;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;
import com.github.finncker.desktop.service.UserService;

import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SidePanelController {
  @FXML
  private Label userNameLabel;
  @FXML
  private Label userEmailLabel;
  @FXML
  private Label userBalanceLabel;

  private UserService userService = new UserService();

  @FXML
  private void initialize() {
    User user = null;

    try {
      user = userService.read();
    } catch (UserNotFoundException unfe) {
      log.error("Usuário não encontrado, as informações do usuário não serão exibidas no Side Panel.");
    }

    userNameLabel.setText(user.getFullName());
    userEmailLabel.setText(user.getEmail());
  }

  @FXML
  private void handleButtonDashboard() {

  }

  @FXML
  private void handleButtonTransactions() {

  }

  @FXML
  private void handleButtonAccounts() {

  }

  @FXML
  private void handleButtonCategories() {

  }
}
