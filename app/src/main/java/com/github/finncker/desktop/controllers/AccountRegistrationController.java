package com.github.finncker.desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.java.Log;

@Log
public class AccountRegistrationController {

    @FXML
    private Button closeButton;
    @FXML
    private ComboBox<String> accountTypeComboBox;
    @FXML
    private TextField accountNameField;
    @FXML
    private TextField financialInstitutionField;
    @FXML
    private TextField initialBalanceField;
    @FXML
    private TextField accountNumberField;
    @FXML
    private ComboBox<String> colorComboBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        populateComboBoxes();
        setupValueValidation();
    }

    private void populateComboBoxes() {
        // Tipos de conta
        accountTypeComboBox.getItems().addAll(
                "Conta Corrente",
                "Conta Poupança",
                "Cartão de Crédito",
                "Carteira Digital",
                "Investimento",
                "Dinheiro");
        accountTypeComboBox.setValue("Conta Corrente");

        // Cores de identificação
        colorComboBox.getItems().addAll(
                "Azul",
                "Verde",
                "Vermelho",
                "Amarelo",
                "Roxo",
                "Laranja",
                "Rosa",
                "Cinza",
                "Marrom",
                "Preto");
        colorComboBox.setValue("Azul");
    }

    private void setupValueValidation() {
        initialBalanceField.setPromptText("0,00");
        initialBalanceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty())
                return;

            // Permite valores negativos para saldo inicial
            String filtered = newVal.replace(".", ",");

            // Verifica se começa com sinal negativo
            boolean isNegative = filtered.startsWith("-");
            String numberPart = isNegative ? filtered.substring(1) : filtered;

            if (!numberPart.matches("\\d*(,\\d{0,2})?") || numberPart.chars().filter(ch -> ch == ',').count() > 1) {
                initialBalanceField.setText(oldVal);
                return;
            }

            String result = isNegative ? "-" + numberPart : numberPart;
            if (!result.equals(newVal))
                initialBalanceField.setText(result);
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

        log.info(String.format("Salvando conta: %s, %s, %s, %s, %s, %s",
                accountTypeComboBox.getValue(),
                accountNameField.getText().trim(),
                financialInstitutionField.getText().trim(),
                initialBalanceField.getText().trim(),
                accountNumberField.getText().trim(),
                colorComboBox.getValue()));

        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Conta salva com sucesso!");
        handleClose();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (accountTypeComboBox.getValue() == null || accountTypeComboBox.getValue().isEmpty())
            errors.append("• Selecione o tipo de conta\n");
        if (accountNameField.getText().trim().isEmpty())
            errors.append("• Preencha o nome da conta\n");
        if (financialInstitutionField.getText().trim().isEmpty())
            errors.append("• Preencha a instituição financeira\n");

        // Validação do saldo inicial (opcional, mas se preenchido deve ser válido)
        if (!initialBalanceField.getText().trim().isEmpty()) {
            try {
                String balanceText = initialBalanceField.getText().trim().replace(",", ".");
                Double.parseDouble(balanceText);
            } catch (NumberFormatException e) {
                errors.append("• Saldo inicial inválido\n");
            }
        }

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
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void clearForm() {
        accountTypeComboBox.setValue("Conta Corrente");
        accountNameField.clear();
        financialInstitutionField.clear();
        initialBalanceField.clear();
        accountNumberField.clear();
        colorComboBox.setValue("Azul");
    }
}
