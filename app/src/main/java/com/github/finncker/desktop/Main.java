package com.github.finncker.desktop;

import com.github.finncker.desktop.service.UserService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private UserService userService = new UserService();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root;

        if (userService.userExists()) {
            root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
        } else {
            root = FXMLLoader.load(getClass().getResource("/fxml/UserRegistrationView.fxml"));
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Finncker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
