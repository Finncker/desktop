package com.github.finncker.desktop.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController {

    @FXML
    private Label labelSaldo;
    @FXML
    private Label labelReceitas;
    @FXML
    private Label labelDespesas;
    @FXML
    private Label labelEconomia;

    @FXML
    private BarChart<String, Number> graficoBarras;
    @FXML
    private PieChart graficoPizza;
    @FXML
    private ListView<String> listaTransacoes;

    private AccountService accountService = new AccountService();
    private TransactionService transactionService = new TransactionService();
    private CategoryService categoryService = new CategoryService();

    @FXML
    public void initialize() {
        try {
            carregarDadosReais();
        } catch (Exception e) {
            System.err.println("Erro ao inicializar Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarDadosReais() {
        // Calcular saldo total
        BigDecimal saldoTotal = BigDecimal.ZERO;
        BigDecimal receitas = BigDecimal.ZERO;
        BigDecimal despesas = BigDecimal.ZERO;

        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        // Calcular saldo de todas as contas
        for (Account account : accountService.getAll()) {
            BigDecimal accountBalance = account.getInitialBalance() != null ? account.getInitialBalance()
                    : BigDecimal.ZERO;
            for (Transaction transaction : account.getTransactions()) {
                if (transaction.getAmount() != null) {
                    accountBalance = accountBalance.add(transaction.getAmount());

                    // Calcular receitas e despesas do mês
                    if (transaction.getDate() != null &&
                            !transaction.getDate().isBefore(monthStart) &&
                            !transaction.getDate().isAfter(monthEnd)) {
                        if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                            receitas = receitas.add(transaction.getAmount());
                        } else {
                            despesas = despesas.add(transaction.getAmount().abs());
                        }
                    }
                }
            }
            saldoTotal = saldoTotal.add(accountBalance);
        }

        BigDecimal economia = receitas.subtract(despesas);

        // Atualizar labels
        labelSaldo.setText(FormatUtil.formatCurrency(saldoTotal));
        labelReceitas.setText(FormatUtil.formatCurrency(receitas));
        labelDespesas.setText(FormatUtil.formatCurrency(despesas));
        labelEconomia.setText(FormatUtil.formatCurrency(economia));

        // Gráfico de Barras - últimos 3 meses
        graficoBarras.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Movimentação");

        for (int i = 2; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            LocalDate mStart = month.atDay(1);
            LocalDate mEnd = month.atEndOfMonth();

            BigDecimal monthTotal = BigDecimal.ZERO;
            for (Transaction t : transactionService.getAll()) {
                if (t.getDate() != null && !t.getDate().isBefore(mStart) && !t.getDate().isAfter(mEnd)) {
                    if (t.getAmount() != null) {
                        monthTotal = monthTotal.add(t.getAmount().abs());
                    }
                }
            }

            String monthName = month.getMonth().toString().substring(0, 1) +
                    month.getMonth().toString().substring(1, 3).toLowerCase();
            series.getData().add(new XYChart.Data<>(monthName, monthTotal.doubleValue()));
        }

        graficoBarras.getData().add(series);

        // Gráfico de Pizza - por categoria
        graficoPizza.getData().clear();
        Map<UUID, BigDecimal> categoryTotals = new HashMap<>();
        Map<UUID, Category> categoryMap = new HashMap<>();

        for (Category cat : categoryService.getAll()) {
            categoryMap.put(cat.getUuid(), cat);
            categoryTotals.put(cat.getUuid(), BigDecimal.ZERO);
        }

        for (Transaction t : transactionService.getAll()) {
            if (t.getCategoryUUID() != null && t.getAmount() != null) {
                BigDecimal current = categoryTotals.getOrDefault(t.getCategoryUUID(), BigDecimal.ZERO);
                categoryTotals.put(t.getCategoryUUID(), current.add(t.getAmount().abs()));
            }
        }

        for (Map.Entry<UUID, BigDecimal> entry : categoryTotals.entrySet()) {
            Category cat = categoryMap.get(entry.getKey());
            if (cat != null && entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                graficoPizza.getData().add(new PieChart.Data(
                        cat.getName(),
                        entry.getValue().doubleValue()));
            }
        }

        // Lista de transações recentes
        listaTransacoes.getItems().clear();
        List<Transaction> recentTransactions = transactionService.getAll().stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .limit(5)
                .collect(Collectors.toList());

        for (Transaction t : recentTransactions) {
            Category cat = categoryMap.get(t.getCategoryUUID());
            String categoryName = cat != null ? cat.getName() : "Sem categoria";
            String type = (t.getAmount() != null && t.getAmount().compareTo(BigDecimal.ZERO) > 0) ? "Receita"
                    : "Despesa";
            String amount = FormatUtil.formatCurrency(t.getAmount() != null ? t.getAmount().abs() : BigDecimal.ZERO);
            String date = FormatUtil.formatDate(t.getDate());

            listaTransacoes.getItems().add(
                    String.format("%s - %s (%s) - %s",
                            t.getDescription() != null ? t.getDescription() : "Sem descrição",
                            amount, type, date));
        }
    }
}