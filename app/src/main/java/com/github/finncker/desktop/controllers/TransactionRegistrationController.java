package com.github.finncker.desktop.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log
public class TransactionRegistrationController {

    @FXML private Button closeButton;
    @FXML private ToggleButton incomeToggle;
    @FXML private ToggleButton expenseToggle;
    @FXML private TextField descriptionField;
    @FXML private TextField valueField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> accountComboBox;
    @FXML private Button saveButton;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        categoryComboBox.getItems().addAll("Alimentação", "Transporte", "Moradia", "Saúde", 
                "Educação", "Lazer", "Salário", "Investimentos", "Outros");
        accountComboBox.getItems().addAll("Conta Corrente", "Poupança", "Carteira", "Cartão de Crédito");
    }

    private void setupValueValidation() {
        valueField.setPromptText("0,00");
        valueField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) return;
            
            String filtered = newVal.replace(".", ",");
            if (!filtered.matches("\\d*(,\\d{0,2})?") || filtered.chars().filter(ch -> ch == ',').count() > 1) {
                valueField.setText(oldVal);
                return;
            }
            if (!filtered.equals(newVal)) valueField.setText(filtered);
        });
    }

    @FXML
    private void handleClose() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        if (!validateFields()) return;

        String type = incomeToggle.isSelected() ? "Receita" : "Despesa";
        log.info(String.format("Salvando: %s, %s, %s, %s, %s, %s", 
                type, descriptionField.getText().trim(), valueField.getText().trim(),
                datePicker.getValue(), categoryComboBox.getValue(), accountComboBox.getValue()));

        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Transação salva com sucesso!");
        handleClose();
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
                if (value <= 0) errors.append("• O valor deve ser maior que zero\n");
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
}
