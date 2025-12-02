package com.github.finncker.desktop.controller;

import com.github.finncker.desktop.model.exceptions.UserAlreadyExists;
import com.github.finncker.desktop.service.UserService;
import com.github.finncker.desktop.util.NavigationUtil;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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

      // Navegar para Dashboard após criar usuário
      Stage stage = (Stage) startButton.getScene().getWindow();
      Parent root = NavigationUtil.loadFXML("/fxml/Dashboard.fxml");
      if (root != null) {
        stage.setScene(new Scene(root));
        stage.setTitle("Finncker");
      } else {
        log.error("Erro ao carregar Dashboard.fxml");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText("Erro ao carregar a tela principal.");
        alert.showAndWait();
      }
    } catch (UserAlreadyExists uae) {
      log.error("Falha ao cadastrar usuário, um usuário já existe.");
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Erro");
      alert.setHeaderText(null);
      alert.setContentText("Um usuário já existe. Por favor, feche e abra o aplicativo novamente.");
      alert.showAndWait();
    } catch (Exception e) {
      log.error("Erro ao criar usuário", e);
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Erro");
      alert.setHeaderText(null);
      alert.setContentText("Erro ao criar usuário: " + e.getMessage());
      alert.showAndWait();
    }
  }
}
