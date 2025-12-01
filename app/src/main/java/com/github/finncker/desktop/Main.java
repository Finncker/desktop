package com.github.finncker.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = Objects.requireNonNull(
                getClass().getResource("/fxml/Transactions.fxml"),
                "FXML file not found: /fxml/Transactions.fxml"
        );
        Parent root = FXMLLoader.load(fxmlUrl);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Finncker - Transações");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
