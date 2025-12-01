package com.github.finncker.desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CategoriesController{

    @FXML private Button btnIconAlimentacao;
    @FXML private Button btnIconMoradia;
    @FXML private Button btnIconTransporte;
    @FXML private Button btnIconLazer;
    @FXML private Button btnIconSaude;
    @FXML private Button btnIconEducacao;

    @FXML
    public void initialize(){
        System.out.println("Iniciando tela de Categorias...");

    
        pintarIcone(btnIconAlimentacao, "#0097e6"); 
        pintarIcone(btnIconMoradia, "#2ecc71");     
        pintarIcone(btnIconTransporte, "#e67e22");  
        pintarIcone(btnIconLazer, "#9b59b6");       
        pintarIcone(btnIconSaude, "#e74c3c");      
        pintarIcone(btnIconEducacao, "#34495e");    
    }

    private void pintarIcone(Button botao, String corHex){
        if (botao != null) {
            botao.setStyle("-fx-background-color: " + corHex + "; -fx-background-radius: 8; -fx-min-width: 40; -fx-min-height: 40;");
        }
    }
}