package com.example.sales_management_system_with_gst_return2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;
    @Override

    public void start(Stage stage) throws IOException {
        Main.primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard2.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setMaximized(true);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void closePrimaryStage() {
        primaryStage.close();
    }
}