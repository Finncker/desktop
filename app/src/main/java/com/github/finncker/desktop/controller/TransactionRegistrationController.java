package com.github.finncker.desktop.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.service.AccountService;
import com.github.finncker.desktop.service.CategoryService;
import com.github.finncker.desktop.service.TransactionService;
import com.github.finncker.desktop.util.FormatUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

@Log
public class TransactionRegistrationController {

    @FXML
    private Button closeButton;
    @FXML
    private ToggleButton incomeToggle;
    @FXML
    private ToggleButton expenseToggle;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField valueField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private ComboBox<String> accountComboBox;
    @FXML
    private Button saveButton;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private AccountService accountService = new AccountService();
    private CategoryService categoryService = new CategoryService();
    private TransactionService transactionService = new TransactionService();

    private Map<String, UUID> accountMap = new HashMap<>();
    private Map<String, UUID> categoryMap = new HashMap<>();
    private TransactionsController parentController;

    @FXML
    public void initialize() {
        configureDatePicker();
        setupToggleGroup();
        populateComboBoxes();
        setupValueValidation();
        Platform.runLater(() -> datePicker.setValue(LocalDate.now()));
    }

    private void setupToggleGroup() {
        ToggleGroup typeGroup = new ToggleGroup();
        incomeToggle.setToggleGroup(typeGroup);
        expenseToggle.setToggleGroup(typeGroup);
        expenseToggle.setSelected(true);
    }

    private void configureDatePicker() {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? DATE_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, DATE_FORMATTER);
                    } catch (Exception e) {
                        log.warning("Erro ao converter data: " + string);
                    }
                }
                return null;
            }
        });
        datePicker.setPromptText("dd/MM/yyyy");
    }

    private void populateComboBoxes() {
        accountMap.clear();
        categoryMap.clear();

        // Carregar contas reais
        accountComboBox.getItems().clear();
        for (Account account : accountService.getAll()) {
            String displayName = account.getName() != null ? account.getName() : "Conta sem nome";
            accountComboBox.getItems().add(displayName);
            accountMap.put(displayName, account.getUuid());
        }

        // Carregar categorias reais
        categoryComboBox.getItems().clear();
        for (Category category : categoryService.getAll()) {
            String displayName = category.getName() != null ? category.getName() : "Categoria sem nome";
            categoryComboBox.getItems().add(displayName);
            categoryMap.put(displayName, category.getUuid());
        }
    }

    private void setupValueValidation() {
        valueField.setPromptText("0,00");
        valueField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty())
                return;

            String filtered = newVal.replace(".", ",");
            if (!filtered.matches("\\d*(,\\d{0,2})?") || filtered.chars().filter(ch -> ch == ',').count() > 1) {
                valueField.setText(oldVal);
                return;
            }
            if (!filtered.equals(newVal))
                valueField.setText(filtered);
        });
    }

    @FXML
    private void handleClose() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        if (!validateFields())
            return;

        try {
            UUID accountUUID = accountMap.get(accountComboBox.getValue());
            UUID categoryUUID = categoryMap.get(categoryComboBox.getValue());

            if (accountUUID == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Conta selecionada inválida.");
                return;
            }

            if (categoryUUID == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Categoria selecionada inválida.");
                return;
            }

            BigDecimal amount = FormatUtil.parseCurrency(valueField.getText().trim());
            if (expenseToggle.isSelected()) {
                amount = amount.negate();
            }

            Transaction transaction = Transaction.builder()
                    .description(descriptionField.getText().trim())
                    .amount(amount)
                    .date(datePicker.getValue())
                    .categoryUUID(categoryUUID)
                    .build();

            transactionService.create(accountUUID, transaction);

            log.info(String.format("Transação salva: %s, %s, %s",
                    descriptionField.getText().trim(), FormatUtil.formatCurrency(amount.abs()),
                    datePicker.getValue()));

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Transação salva com sucesso!");

            if (parentController != null) {
                parentController.refreshTransactions();
            }

            handleClose();
        } catch (Exception e) {
            log.severe("Erro ao salvar transação: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar transação: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (!incomeToggle.isSelected() && !expenseToggle.isSelected())
            errors.append("• Selecione o tipo de transação\n");
        if (descriptionField.getText().trim().isEmpty())
            errors.append("• Preencha a descrição\n");
        if (valueField.getText().trim().isEmpty())
            errors.append("• Preencha o valor\n");
        else {
            try {
                double value = Double.parseDouble(valueField.getText().trim().replace(",", "."));
                if (value <= 0)
                    errors.append("• O valor deve ser maior que zero\n");
            } catch (NumberFormatException e) {
                errors.append("• Valor inválido\n");
            }
        }
        if (datePicker.getValue() == null)
            errors.append("• Selecione a data\n");
        if (categoryComboBox.getValue() == null || categoryComboBox.getValue().isEmpty())
            errors.append("• Selecione a categoria\n");
        if (accountComboBox.getValue() == null || accountComboBox.getValue().isEmpty())
            errors.append("• Selecione a conta\n");

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Campos Obrigatórios",
                    "Por favor, preencha todos os campos obrigatórios:\n" + errors);
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(type == Alert.AlertType.WARNING ? null : null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void clearForm() {
        expenseToggle.setSelected(true);
        descriptionField.clear();
        valueField.clear();
        datePicker.setValue(LocalDate.now());
        categoryComboBox.getSelectionModel().clearSelection();
        accountComboBox.getSelectionModel().clearSelection();
    }

    public void setParentController(TransactionsController parentController) {
        this.parentController = parentController;
    }
}
