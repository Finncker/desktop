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

@Log
public class TransactionsController {

    @FXML
    private Button exportButton;
    @FXML
    private Button newTransactionButton;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private DatePicker periodPicker;
    @FXML
    private VBox transactionsContainer;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        setupFilters();
        setupSearch();
        loadTransactions();
    }

    private void setupFilters() {
        // Populate filter combo box
        filterComboBox.getItems().addAll(
                "Todas",
                "Receitas",
                "Despesas",
                "Alimentação",
                "Transporte",
                "Moradia",
                "Saúde",
                "Educação",
                "Lazer",
                "Outros");
        filterComboBox.setValue("Todas");

        // Add listener for filter changes
        filterComboBox.setOnAction(e -> filterTransactions());
        periodPicker.setOnAction(e -> filterTransactions());
    }

    private void setupSearch() {
        // Add listener for search field
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterTransactions();
        });
    }

    private void loadTransactions() {
        // This method will load transactions from the database
        // For now, the example transactions are already in the FXML
        log.info("Loading transactions...");

        // TODO: Implement actual transaction loading from repository
        // Example:
        // transactionsContainer.getChildren().clear();
        // List<Transaction> transactions = transactionRepository.findAll();
        // transactions.forEach(this::addTransactionItem);
    }

    private void filterTransactions() {
        String searchText = searchField.getText().toLowerCase();
        String selectedFilter = filterComboBox.getValue();
        LocalDate selectedDate = periodPicker.getValue();

        log.info(String.format("Filtering transactions: search='%s', filter='%s', date='%s'",
                searchText, selectedFilter, selectedDate));

        // TODO: Implement actual filtering logic
        // This would filter the transactions based on search text, category filter, and
        // date
    }

    @FXML
    private void handleNewTransaction() {
        log.info("Opening new transaction dialog...");

        // TODO: Open TransactionRegistration.fxml in a modal dialog
        // Example:
        // try {
        // FXMLLoader loader = new
        // FXMLLoader(getClass().getResource("/fxml/TransactionRegistration.fxml"));
        // Parent root = loader.load();
        // Stage stage = new Stage();
        // stage.initModality(Modality.APPLICATION_MODAL);
        // stage.setTitle("Nova Transação");
        // stage.setScene(new Scene(root));
        // stage.showAndWait();
        // loadTransactions(); // Reload after adding
        // } catch (IOException e) {
        // log.severe("Error opening transaction dialog: " + e.getMessage());
        // }
    }

    @FXML
    private void handleExport() {
        log.info("Exporting transactions...");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportar");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de exportação em desenvolvimento.");
        alert.showAndWait();
    }

    private void addTransactionItem(String title, String category, String date,
            String amount, boolean isIncome, String icon) {

        HBox transactionItem = new HBox(15);
        transactionItem.getStyleClass().add("transaction-item");
        transactionItem.setPadding(new Insets(15, 20, 15, 20));
        transactionItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox iconContainer = new VBox();
        iconContainer.getStyleClass().addAll("icon-container", isIncome ? "income-icon" : "expense-icon");
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

        Text amountText = new Text((isIncome ? "+" : "") + amount);
        amountText.getStyleClass().addAll("transaction-amount",
                isIncome ? "income-amount" : "expense-amount");
        Text trendText = new Text(isIncome ? "📈" : "📉");
        trendText.getStyleClass().addAll("transaction-trend",
                isIncome ? "income-trend" : "expense-trend");

        amountContainer.getChildren().addAll(amountText, trendText);

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
}
