package com.github.finncker.desktop.controller;

import java.math.BigDecimal;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;
import com.github.finncker.desktop.service.AccountService;
import com.github.finncker.desktop.service.UserService;
import com.github.finncker.desktop.util.FormatUtil;
import com.github.finncker.desktop.util.NavigationUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
  private AccountService accountService = new AccountService();

  @FXML
  private void initialize() {
    User user = null;

    try {
      user = userService.read();
      if (user != null) {
        if (userNameLabel != null) {
          userNameLabel.setText(user.getFullName() != null ? user.getFullName() : "Usuário");
        }
        if (userEmailLabel != null) {
          userEmailLabel.setText(user.getEmail() != null ? user.getEmail() : "");
        }
        updateBalance();
      }
    } catch (UserNotFoundException unfe) {
      log.warn("Usuário não encontrado, as informações do usuário não serão exibidas no Side Panel.");
      if (userNameLabel != null) {
        userNameLabel.setText("Usuário não encontrado");
      }
      if (userEmailLabel != null) {
        userEmailLabel.setText("");
      }
      if (userBalanceLabel != null) {
        userBalanceLabel.setText("R$ 0,00");
      }
    } catch (Exception e) {
      log.error("Erro ao inicializar SidePanel: {}", e.getMessage(), e);
    }
  }

  private void updateBalance() {
    try {
      if (userBalanceLabel == null) {
        log.warn("userBalanceLabel é null, não é possível atualizar o saldo");
        return;
      }
      BigDecimal totalBalance = BigDecimal.ZERO;
      for (Account account : accountService.getAll()) {
        BigDecimal accountBalance = account.getInitialBalance() != null ? account.getInitialBalance() : BigDecimal.ZERO;
        for (var transaction : account.getTransactions()) {
          if (transaction.getAmount() != null) {
            accountBalance = accountBalance.add(transaction.getAmount());
          }
        }
        totalBalance = totalBalance.add(accountBalance);
      }
      userBalanceLabel.setText(FormatUtil.formatCurrency(totalBalance));
    } catch (Exception e) {
      log.error("Erro ao calcular saldo total: {}", e.getMessage(), e);
      if (userBalanceLabel != null) {
        userBalanceLabel.setText("R$ 0,00");
      }
    }
  }

  @FXML
  private void handleButtonDashboard() {
    BorderPane rootPane = (BorderPane) userNameLabel.getScene().getRoot();
    NavigationUtil.navigateTo(rootPane, "/fxml/Dashboard.fxml");
  }

  @FXML
  private void handleButtonTransactions() {
    BorderPane rootPane = (BorderPane) userNameLabel.getScene().getRoot();
    NavigationUtil.navigateTo(rootPane, "/fxml/Transactions.fxml");
  }

  @FXML
  private void handleButtonAccounts() {
    BorderPane rootPane = (BorderPane) userNameLabel.getScene().getRoot();
    NavigationUtil.navigateTo(rootPane, "/fxml/AccountsView.fxml");
  }

  @FXML
  private void handleButtonCategories() {
    BorderPane rootPane = (BorderPane) userNameLabel.getScene().getRoot();
    NavigationUtil.navigateTo(rootPane, "/fxml/Categories.fxml");
  }
}
