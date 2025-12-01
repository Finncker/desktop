package com.github.finncker.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController{

    @FXML private Label labelSaldo;
    @FXML private Label labelReceitas;
    @FXML private Label labelDespesas;
    @FXML private Label labelEconomia;
    
    @FXML private BarChart<String, Number> graficoBarras;
    @FXML private PieChart graficoPizza; 
    @FXML private ListView<String> listaTransacoes;

    @FXML
    public void initialize(){
        System.out.println("Iniciando Dashboard...");
        carregarDadosFicticios();
    }

    private void carregarDadosFicticios(){
        //Cards (Simulando valores do banco)
        labelSaldo.setText("R$ 15.200,50");
        labelReceitas.setText("R$ 22.000,00");
        labelDespesas.setText("R$ 6.799,50");
        labelEconomia.setText("R$ 3.000,00");

        //Gráfico de Barras
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Movimentação 2025");
        series.getData().add(new XYChart.Data<>("Jan", 12000));
        series.getData().add(new XYChart.Data<>("Fev", 15000));
        series.getData().add(new XYChart.Data<>("Mar", 9000));
        
        graficoBarras.getData().add(series);

        //Gráfico de Pizza
        graficoPizza.getData().addAll(
        new PieChart.Data("Alimentação", 40),
        new PieChart.Data("Transporte", 20),
        new PieChart.Data("Lazer", 10),
        new PieChart.Data("Outros", 30));

        //Lista
        listaTransacoes.getItems().addAll(
            "Supermercado - R$ 450,00 (Despesa)",
            "Salário Mensal - R$ 5.000,00 (Receita)",
            "Posto de Gasolina - R$ 200,00 (Despesa)",
            "Freelance Projeto Java - R$ 1.500,00 (Receita)"
        );
    }
}