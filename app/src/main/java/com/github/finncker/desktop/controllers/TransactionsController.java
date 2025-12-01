package com.github.finncker.desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import lombok.extern.java.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log
public class TransactionsController {

    @FXML
    private Button exportButton;
    @FXML
    private Button newTransactionButton;
    @FXML
    private Button filterButton;
    @FXML
    private Button periodButton;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private DatePicker periodPicker;
    @FXML
    private VBox transactionsContainer;

    @FXML
    public void initialize() {
        loadTransactions();
    }

    @FXML
    public void handleNewTransaction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nova Transação");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de nova transação em desenvolvimento.");
        alert.showAndWait();
    }

    @FXML
    public void handleFilterClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Filtro");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de filtro em desenvolvimento.");
        alert.showAndWait();
    }

    @FXML
    public void handlePeriodClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Período");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de período em desenvolvimento.");
        alert.showAndWait();
    }

    @FXML
    public void handleExport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportar");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de exportação em desenvolvimento.");
        alert.showAndWait();
    }

    private void loadTransactions() {
        if (transactionsContainer != null) {
            transactionsContainer.getChildren().clear();
        }
        // TODO: Implement actual transaction loading
    }

    private void addTransactionItem(String title, String category, String date,
            String amount, boolean isIncome, String icon) {

        HBox transactionItem = new HBox(15);
        transactionItem.getStyleClass().add("transaction-item");
        transactionItem.setPadding(new Insets(15, 20, 15, 20));
        transactionItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox iconContainer = new VBox();
        iconContainer.getStyleClass().add("icon-container");
        Text iconText = new Text(icon);
        iconText.getStyleClass().add("transaction-icon");
        iconContainer.getChildren().add(iconText);

        VBox detailsContainer = new VBox(4);
        HBox.setHgrow(detailsContainer, javafx.scene.layout.Priority.ALWAYS);

        Text titleText = new Text(title);
        titleText.getStyleClass().add("transaction-title");

        HBox categoryDateBox = new HBox(8);
        Label categoryLabel = new Label(category);
        categoryLabel.getStyleClass().addAll("transaction-category",
                isIncome ? "income-category" : "expense-category");
        Text dateText = new Text(date);
        dateText.getStyleClass().add("transaction-date");
        categoryDateBox.getChildren().addAll(categoryLabel, dateText);

        detailsContainer.getChildren().addAll(titleText, categoryDateBox);

        VBox amountContainer = new VBox();
        amountContainer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        Text amountText = new Text((isIncome ? "+" : "-") + amount);
        amountText.getStyleClass().addAll("transaction-amount",
                isIncome ? "income-amount" : "expense-amount");

        amountContainer.getChildren().add(amountText);

        transactionItem.getChildren().addAll(iconContainer, detailsContainer, amountContainer);

        transactionItem.setOnMouseClicked(e -> handleTransactionClick(title));

        transactionsContainer.getChildren().add(transactionItem);
    }

    private void handleTransactionClick(String transactionTitle) {
        log.info("Transaction clicked: " + transactionTitle);
    }

    public void refreshTransactions() {
        loadTransactions();
    }

    private static class Transaction {
        String title;
        String category;
        LocalDate date;
        String amount;
        boolean isIncome;
        String icon;

        public Transaction(String title, String category, LocalDate date, String amount, boolean isIncome,
                String icon) {
            this.title = title;
            this.category = category;
            this.date = date;
            this.amount = amount;
            this.isIncome = isIncome;
            this.icon = icon;
        }
    }
}
