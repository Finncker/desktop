package com.github.finncker.desktop.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.service.AccountService;
import com.github.finncker.desktop.service.CategoryService;
import com.github.finncker.desktop.service.TransactionService;
import com.github.finncker.desktop.util.FormatUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.java.Log;

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
    private VBox transactionsContainer;

    private TransactionService transactionService = new TransactionService();
    private AccountService accountService = new AccountService();
    private CategoryService categoryService = new CategoryService();

    private String currentFilter = "Todas";
    private LocalDate periodStart = null;
    private LocalDate periodEnd = null;

    @FXML
    public void initialize() {
        if (exportButton != null) {
            exportButton.setOnAction(e -> handleExport());
        }
        if (newTransactionButton != null) {
            newTransactionButton.setOnAction(e -> handleNewTransaction());
        }
        if (filterButton != null) {
            filterButton.setOnAction(e -> handleFilterClick());
        }
        if (periodButton != null) {
            periodButton.setOnAction(e -> handlePeriodClick());
        }
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> loadTransactions());
        }
        loadTransactions();
    }

    @FXML
    private void handleNewTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TransactionRegistration.fxml"));
            Parent root = loader.load();

            TransactionRegistrationController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Nova Transação");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadTransactions();
        } catch (Exception e) {
            log.severe("Erro ao abrir modal de transação: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao abrir formulário de nova transação.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleFilterClick() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Todas", "Todas", "Receitas", "Despesas");
        dialog.setTitle("Filtrar Transações");
        dialog.setHeaderText("Selecione o tipo de transação");
        dialog.setContentText("Tipo:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            currentFilter = result.get();
            loadTransactions();
        }
    }

    @FXML
    private void handlePeriodClick() {
        // Dialog simples para selecionar período (pode ser melhorado)
        TextInputDialog dialog = new TextInputDialog("Últimos 30 dias");
        dialog.setTitle("Período");
        dialog.setHeaderText("Selecione o período");
        dialog.setContentText("Período (ex: 'Últimos 30 dias', 'Este mês'):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String period = result.get();
            if (period.contains("30 dias") || period.contains("30")) {
                periodEnd = LocalDate.now();
                periodStart = periodEnd.minusDays(30);
            } else if (period.contains("mês") || period.contains("mes")) {
                periodEnd = LocalDate.now();
                periodStart = periodEnd.withDayOfMonth(1);
            } else {
                periodStart = null;
                periodEnd = null;
            }
            loadTransactions();
        }
    }

    private void handleExport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportar");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidade de exportação em desenvolvimento.");
        alert.showAndWait();
    }

    private void loadTransactions() {
        if (transactionsContainer == null) {
            return;
        }

        transactionsContainer.getChildren().clear();

        List<Transaction> allTransactions = transactionService.getAll();
        Map<UUID, Category> categoryMap = new HashMap<>();
        for (Category cat : categoryService.getAll()) {
            categoryMap.put(cat.getUuid(), cat);
        }

        Map<UUID, Account> accountMap = new HashMap<>();
        for (Account acc : accountService.getAll()) {
            accountMap.put(acc.getUuid(), acc);
        }

        // Filtrar transações
        List<Transaction> filteredTransactions = allTransactions.stream()
                .filter(t -> {
                    // Filtro por tipo
                    if (!"Todas".equals(currentFilter)) {
                        Category cat = categoryMap.get(t.getCategoryUUID());
                        if (cat != null) {
                            if ("Receitas".equals(currentFilter)
                                    && cat.getType() != com.github.finncker.desktop.model.enums.CategoryType.INCOME) {
                                return false;
                            }
                            if ("Despesas".equals(currentFilter)
                                    && cat.getType() != com.github.finncker.desktop.model.enums.CategoryType.EXPENSE) {
                                return false;
                            }
                        }
                    }

                    // Filtro por período
                    if (periodStart != null && t.getDate().isBefore(periodStart)) {
                        return false;
                    }
                    if (periodEnd != null && t.getDate().isAfter(periodEnd)) {
                        return false;
                    }

                    // Filtro por busca
                    if (searchField != null && !searchField.getText().trim().isEmpty()) {
                        String search = searchField.getText().trim().toLowerCase();
                        Category cat = categoryMap.get(t.getCategoryUUID());
                        String categoryName = cat != null ? cat.getName() : "";
                        if (!t.getDescription().toLowerCase().contains(search) &&
                                !categoryName.toLowerCase().contains(search)) {
                            return false;
                        }
                    }

                    return true;
                })
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());

        // Adicionar transações à lista
        for (Transaction transaction : filteredTransactions) {
            Category category = categoryMap.get(transaction.getCategoryUUID());
            String categoryName = category != null ? category.getName() : "Sem categoria";
            boolean isIncome = category != null
                    && category.getType() == com.github.finncker.desktop.model.enums.CategoryType.INCOME;

            BigDecimal amount = transaction.getAmount() != null ? transaction.getAmount() : BigDecimal.ZERO;
            boolean isPositive = amount.compareTo(BigDecimal.ZERO) >= 0;

            String icon = isIncome ? "💰" : "💸";
            String formattedAmount = FormatUtil.formatCurrency(amount.abs());
            String date = FormatUtil.formatDate(transaction.getDate());

            addTransactionItem(
                    transaction.getDescription() != null ? transaction.getDescription() : "Sem descrição",
                    categoryName,
                    date,
                    formattedAmount,
                    isPositive,
                    icon);
        }
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
}
