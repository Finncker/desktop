package com.github.finncker.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.java.Log;

@Log
public class CategoryRegistrationController {

    @FXML
    private Button closeButton;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField categoryNameField;
    @FXML
    private ComboBox<String> iconComboBox;
    @FXML
    private ComboBox<String> colorComboBox;
    @FXML
    private TextField monthlyBudgetField;
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
        typeComboBox.getItems().addAll(
                "Despesa",
                "Receita");
        typeComboBox.setValue("Despesa");

        iconComboBox.getItems().addAll(
                "🛒 Compras",
                "🍔 Alimentação",
                "🚗 Transporte",
                "🏠 Moradia",
                "💊 Saúde",
                "📚 Educação",
                "🎮 Lazer",
                "💰 Salário",
                "📈 Investimentos",
                "💳 Contas",
                "✈️ Viagem",
                "👕 Vestuário",
                "🎁 Presentes",
                "🔧 Manutenção",
                "📱 Tecnologia",
                "🐕 Pets",
                "⚡ Utilidades",
                "📝 Outros");
        iconComboBox.setValue("🛒 Compras");

        // Cores
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
                "Turquesa");
        colorComboBox.setValue("Azul");
    }

    private void setupValueValidation() {
        monthlyBudgetField.setPromptText("0,00");
        monthlyBudgetField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty())
                return;

            String filtered = newVal.replace(".", ",");
            if (!filtered.matches("\\d*(,\\d{0,2})?") || filtered.chars().filter(ch -> ch == ',').count() > 1) {
                monthlyBudgetField.setText(oldVal);
                return;
            }
            if (!filtered.equals(newVal))
                monthlyBudgetField.setText(filtered);
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

        log.info(String.format("Salvando categoria: %s, %s, %s, %s, %s",
                typeComboBox.getValue(),
                categoryNameField.getText().trim(),
                iconComboBox.getValue(),
                colorComboBox.getValue(),
                monthlyBudgetField.getText().trim()));

        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Categoria salva com sucesso!");
        handleClose();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (typeComboBox.getValue() == null || typeComboBox.getValue().isEmpty())
            errors.append("• Selecione o tipo de categoria\n");
        if (categoryNameField.getText().trim().isEmpty())
            errors.append("• Preencha o nome da categoria\n");

        // Validação do orçamento mensal (opcional, mas se preenchido deve ser válido)
        if (!monthlyBudgetField.getText().trim().isEmpty()) {
            try {
                double value = Double.parseDouble(monthlyBudgetField.getText().trim().replace(",", "."));
                if (value < 0)
                    errors.append("• O orçamento mensal não pode ser negativo\n");
            } catch (NumberFormatException e) {
                errors.append("• Orçamento mensal inválido\n");
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
        typeComboBox.setValue("Despesa");
        categoryNameField.clear();
        iconComboBox.setValue("🛒 Compras");
        colorComboBox.setValue("Azul");
        monthlyBudgetField.clear();
    }
}
