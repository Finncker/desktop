package com.github.finncker.desktop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.service.CategoryService;
import com.github.finncker.desktop.service.TransactionService;
import com.github.finncker.desktop.util.FormatUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CategoriesController {

    @FXML
    private FlowPane gridCategorias;
    @FXML
    private Button novaCategoriaButton;

    private CategoryService categoryService = new CategoryService();
    private TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        loadCategories();
        if (novaCategoriaButton != null) {
            novaCategoriaButton.setOnAction(e -> handleNewCategory());
        }
    }

    private void loadCategories() {
        if (gridCategorias == null) {
            return;
        }

        gridCategorias.getChildren().clear();

        List<Category> categories = categoryService.getAll();
        Map<UUID, BigDecimal> categorySpending = calculateCategorySpending();
        Map<UUID, Integer> transactionCounts = calculateTransactionCounts();
        YearMonth currentMonth = YearMonth.from(LocalDate.now());
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();
        YearMonth lastMonth = currentMonth.minusMonths(1);
        LocalDate lastMonthStart = lastMonth.atDay(1);
        LocalDate lastMonthEnd = lastMonth.atEndOfMonth();

        Map<UUID, BigDecimal> lastMonthSpending = calculateCategorySpendingForPeriod(lastMonthStart, lastMonthEnd);

        for (Category category : categories) {
            createCategoryCard(category,
                    categorySpending.getOrDefault(category.getUuid(), BigDecimal.ZERO),
                    lastMonthSpending.getOrDefault(category.getUuid(), BigDecimal.ZERO),
                    transactionCounts.getOrDefault(category.getUuid(), 0));
        }
    }

    private Map<UUID, Integer> calculateTransactionCounts() {
        Map<UUID, Integer> counts = new HashMap<>();
        YearMonth currentMonth = YearMonth.from(LocalDate.now());
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        for (Transaction transaction : transactionService.getAll()) {
            if (transaction.getCategoryUUID() != null &&
                    transaction.getDate() != null &&
                    !transaction.getDate().isBefore(monthStart) &&
                    !transaction.getDate().isAfter(monthEnd)) {
                counts.put(transaction.getCategoryUUID(),
                        counts.getOrDefault(transaction.getCategoryUUID(), 0) + 1);
            }
        }
        return counts;
    }

    private void createCategoryCard(Category category, BigDecimal currentSpending, BigDecimal lastMonthSpending,
            int transactionCount) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15;");
        card.setPrefWidth(300);
        card.setPrefHeight(200);

        // Efeito de sombra
        javafx.scene.effect.DropShadow dropShadow = new javafx.scene.effect.DropShadow();
        dropShadow.setColor(javafx.scene.paint.Color.rgb(60, 60, 60, 0.21));
        card.setEffect(dropShadow);

        // Header com ícone e nome
        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(12);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Botão ícone (simulado)
        javafx.scene.control.Button iconButton = new javafx.scene.control.Button();
        iconButton.setStyle(
                "-fx-background-color: #0097e6; -fx-background-radius: 8; -fx-min-height: 40; -fx-min-width: 40;");
        iconButton.setDisable(true);

        javafx.scene.layout.VBox nameBox = new javafx.scene.layout.VBox(2);
        Label nameLabel = new Label(category.getName() != null ? category.getName() : "Sem nome");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        Label transactionCountLabel = new Label(transactionCount + " transações");
        transactionCountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");

        nameBox.getChildren().addAll(nameLabel, transactionCountLabel);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Label menuLabel = new Label("...");
        menuLabel.setStyle("-fx-font-size: 18px;");

        headerBox.getChildren().addAll(iconButton, nameBox, spacer, menuLabel);

        // Grid com gasto e orçamento
        javafx.scene.layout.GridPane gridPane = new javafx.scene.layout.GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label gastoLabel = new Label("Gasto");
        gastoLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        Label gastoValueLabel = new Label(FormatUtil.formatCurrency(currentSpending));
        gastoValueLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px; -fx-font-weight: bold;");
        javafx.scene.layout.GridPane.setHalignment(gastoValueLabel, javafx.geometry.HPos.RIGHT);

        Label orcamentoLabel = new Label("Orçamento");
        orcamentoLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        Label orcamentoValueLabel = new Label();
        if (category.getBudget() != null && category.getBudget().compareTo(BigDecimal.ZERO) > 0) {
            orcamentoValueLabel.setText(FormatUtil.formatCurrency(category.getBudget()));
        } else {
            orcamentoValueLabel.setText("Sem orçamento");
        }
        orcamentoValueLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px; -fx-font-weight: bold;");
        javafx.scene.layout.GridPane.setHalignment(orcamentoValueLabel, javafx.geometry.HPos.RIGHT);

        gridPane.add(gastoLabel, 0, 0);
        gridPane.add(gastoValueLabel, 1, 0);
        gridPane.add(orcamentoLabel, 0, 1);
        gridPane.add(orcamentoValueLabel, 1, 1);

        // ProgressBar
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefHeight(15);
        progressBar.setMaxHeight(15);
        progressBar.setStyle("-fx-accent: #2c3e50; -fx-control-inner-background: #ecf0f1;");

        if (category.getBudget() != null && category.getBudget().compareTo(BigDecimal.ZERO) > 0) {
            double progress = currentSpending.divide(category.getBudget(), 4, java.math.RoundingMode.HALF_UP)
                    .doubleValue();
            progressBar.setProgress(Math.min(progress, 1.0));
        } else {
            progressBar.setProgress(0);
        }

        // Footer com percentual usado e restante
        javafx.scene.layout.HBox footerBox = new javafx.scene.layout.HBox();
        footerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label percentLabel = new Label();
        Label restanteLabel = new Label();

        if (category.getBudget() != null && category.getBudget().compareTo(BigDecimal.ZERO) > 0) {
            double progress = currentSpending.divide(category.getBudget(), 4, java.math.RoundingMode.HALF_UP)
                    .doubleValue();
            int percent = (int) (progress * 100);
            percentLabel.setText(percent + "% usado");
            percentLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px;");

            BigDecimal restante = category.getBudget().subtract(currentSpending);
            restanteLabel.setText(FormatUtil.formatCurrency(restante) + " restante");
            restanteLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px;");
        } else {
            percentLabel.setText("Sem orçamento");
            percentLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px;");
            restanteLabel.setText("");
        }

        javafx.scene.layout.Region footerSpacer = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(footerSpacer, javafx.scene.layout.Priority.ALWAYS);

        footerBox.getChildren().addAll(percentLabel, footerSpacer, restanteLabel);

        // Comparação com mês anterior
        Label comparisonLabel = new Label();
        if (lastMonthSpending.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal difference = currentSpending.subtract(lastMonthSpending);
            BigDecimal percentage = difference.divide(lastMonthSpending, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            String sign = percentage.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
            comparisonLabel.setText(String.format("~%s%.1f%% vs mês anterior", sign, percentage.doubleValue()));
            comparisonLabel.setStyle("-fx-text-fill: "
                    + (percentage.compareTo(BigDecimal.ZERO) >= 0 ? "#e74c3c" : "#27ae60") + "; -fx-font-size: 12px;");
        } else {
            comparisonLabel.setText("Sem dados do mês anterior");
            comparisonLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        }

        card.getChildren().addAll(headerBox, gridPane, progressBar, footerBox, comparisonLabel);
        gridCategorias.getChildren().add(card);
    }

    private Map<UUID, BigDecimal> calculateCategorySpending() {
        YearMonth currentMonth = YearMonth.from(LocalDate.now());
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();
        return calculateCategorySpendingForPeriod(monthStart, monthEnd);
    }

    private Map<UUID, BigDecimal> calculateCategorySpendingForPeriod(LocalDate start, LocalDate end) {
        Map<UUID, BigDecimal> spending = new HashMap<>();

        for (Transaction transaction : transactionService.getAll()) {
            if (transaction.getCategoryUUID() != null &&
                    transaction.getDate() != null &&
                    !transaction.getDate().isBefore(start) &&
                    !transaction.getDate().isAfter(end) &&
                    transaction.getAmount() != null) {

                BigDecimal current = spending.getOrDefault(transaction.getCategoryUUID(), BigDecimal.ZERO);
                spending.put(transaction.getCategoryUUID(), current.add(transaction.getAmount().abs()));
            }
        }

        return spending;
    }

    @FXML
    private void handleNewCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CategoryRegistration.fxml"));
            Parent root = loader.load();

            CategoryRegistrationController controller = loader.getController();
            if (controller != null) {
                controller.setParentController(this);
            }

            Stage stage = new Stage();
            stage.setTitle("Nova Categoria");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCategories();
        } catch (IOException e) {
            log.error("Erro ao abrir modal de categoria", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao abrir formulário de nova categoria.");
            alert.showAndWait();
        }
    }

    public void refresh() {
        loadCategories();
    }
}