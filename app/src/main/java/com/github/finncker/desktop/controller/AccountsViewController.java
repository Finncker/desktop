package com.github.finncker.desktop.controller;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.service.AccountService;
import com.github.finncker.desktop.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class AccountsViewController {

    @FXML
    private Label patrimonioTotalLabel;
    @FXML
    private Label totalAtivosLabel;
    @FXML
    private Label totalDividasLabel;
    @FXML
    private TilePane accountsTilePane;
    @FXML
    private Button novaContaButton;

    private AccountService accountService = new AccountService();

    @FXML
    private void initialize() {
        try {
            if (novaContaButton != null) {
                novaContaButton.setOnAction(e -> handleNewAccount());
            }
            loadAccounts();
            calculateTotals();
        } catch (Exception e) {
            log.error("Erro ao inicializar AccountsViewController: {}", e.getMessage(), e);
        }
    }

    private void loadAccounts() {
        if (accountsTilePane == null) {
            log.warn("accountsTilePane é null, não é possível carregar contas");
            return;
        }
        accountsTilePane.getChildren().clear();
        List<Account> accounts = accountService.getAll();

        for (Account account : accounts) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccountCard.fxml"));
                VBox card = loader.load();

                // Encontrar os labels no card usando busca recursiva
                Label accountNameLabel = findLabelById(card, "accountNameLabel");
                Label institutionLabel = findLabelById(card, "institutionLabel");
                Label balanceLabel = findLabelById(card, "balanceLabel");
                Label lastTransactionLabel = findLabelById(card, "lastTransactionLabel");

                if (accountNameLabel != null) {
                    accountNameLabel.setText(account.getName() != null ? account.getName() : "Conta sem nome");
                }
                if (institutionLabel != null) {
                    institutionLabel
                            .setText(account.getInstitution() != null ? account.getInstitution() : "Sem instituição");
                }

                // Calcular saldo atual
                BigDecimal balance = account.getInitialBalance() != null ? account.getInitialBalance()
                        : BigDecimal.ZERO;
                for (Transaction transaction : account.getTransactions()) {
                    if (transaction.getAmount() != null) {
                        balance = balance.add(transaction.getAmount());
                    }
                }

                if (balanceLabel != null) {
                    balanceLabel.setText(FormatUtil.formatCurrency(balance));
                }

                // Última transação
                if (lastTransactionLabel != null && !account.getTransactions().isEmpty()) {
                    Transaction lastTransaction = account.getTransactions().stream()
                            .max(Comparator.comparing(Transaction::getDate))
                            .orElse(null);
                    if (lastTransaction != null && lastTransaction.getDate() != null) {
                        long daysAgo = ChronoUnit.DAYS.between(lastTransaction.getDate(), LocalDate.now());
                        if (daysAgo == 0) {
                            lastTransactionLabel.setText("Hoje");
                        } else if (daysAgo == 1) {
                            lastTransactionLabel.setText("Há um dia");
                        } else {
                            lastTransactionLabel.setText("Há " + daysAgo + " dias");
                        }
                    } else {
                        lastTransactionLabel.setText("Nenhuma transação");
                    }
                } else {
                    if (lastTransactionLabel != null) {
                        lastTransactionLabel.setText("Nenhuma transação");
                    }
                }

                accountsTilePane.getChildren().add(card);
            } catch (IOException e) {
                log.error("Erro ao carregar card de conta", e);
            }
        }
    }

    private void calculateTotals() {
        try {
            BigDecimal patrimonioTotal = BigDecimal.ZERO;
            BigDecimal totalAtivos = BigDecimal.ZERO;
            BigDecimal totalDividas = BigDecimal.ZERO;

            for (Account account : accountService.getAll()) {
                BigDecimal balance = account.getInitialBalance() != null ? account.getInitialBalance()
                        : BigDecimal.ZERO;
                for (Transaction transaction : account.getTransactions()) {
                    if (transaction.getAmount() != null) {
                        balance = balance.add(transaction.getAmount());
                    }
                }

                patrimonioTotal = patrimonioTotal.add(balance);
                if (balance.compareTo(BigDecimal.ZERO) >= 0) {
                    totalAtivos = totalAtivos.add(balance);
                } else {
                    totalDividas = totalDividas.add(balance.abs());
                }
            }

            if (patrimonioTotalLabel != null) {
                patrimonioTotalLabel.setText(FormatUtil.formatCurrency(patrimonioTotal));
            } else {
                log.warn("patrimonioTotalLabel é null");
            }
            if (totalAtivosLabel != null) {
                totalAtivosLabel.setText(FormatUtil.formatCurrency(totalAtivos));
            } else {
                log.warn("totalAtivosLabel é null");
            }
            if (totalDividasLabel != null) {
                totalDividasLabel.setText(FormatUtil.formatCurrency(totalDividas));
            } else {
                log.warn("totalDividasLabel é null");
            }
        } catch (Exception e) {
            log.error("Erro ao calcular totais: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleNewAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccountRegistration.fxml"));
            Parent root = loader.load();

            AccountRegistrationController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Nova Conta");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Recarregar após fechar o modal
            loadAccounts();
            calculateTotals();
        } catch (IOException e) {
            log.error("Erro ao abrir modal de registro de conta", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao abrir formulário de nova conta.");
            alert.showAndWait();
        }
    }

    public void refresh() {
        loadAccounts();
        calculateTotals();
    }

    private Label findLabelById(javafx.scene.Parent parent, String id) {
        if (parent instanceof Label && id.equals(((Label) parent).getId())) {
            return (Label) parent;
        }
        if (parent instanceof javafx.scene.layout.Pane) {
            for (javafx.scene.Node node : ((javafx.scene.layout.Pane) parent).getChildren()) {
                if (node instanceof Label && id.equals(((Label) node).getId())) {
                    return (Label) node;
                }
                if (node instanceof javafx.scene.Parent) {
                    Label found = findLabelById((javafx.scene.Parent) node, id);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }
}
