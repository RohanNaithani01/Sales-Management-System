package com.example.sales_management_system_with_gst_return2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private Label welcomeback_label;

    public void initialize() {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("login.fxml")));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("Login");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(welcomeback_label.getScene().getWindow());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void oncompanyButtonClick() throws IOException {
        //loading new window named company
        FXMLLoader fxmlLoader= new FXMLLoader(Main.class.getResource("company.fxml") );
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage= new Stage();
        stage.setTitle("Company Detail");
        stage.setScene(scene);
        // for window view model
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(welcomeback_label.getScene().getWindow());
        stage.show();

    }

    @FXML
    protected void onPurchaseInvoiceButtonClick() throws IOException {
        //loading new window named company
        FXMLLoader fxmlLoader= new FXMLLoader(Main.class.getResource("Purchase_Invoice.fxml") );
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage= new Stage();
        stage.setTitle("Purchase Invoice");
        stage.setScene(scene);
        // for window view model
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(welcomeback_label.getScene().getWindow());
        stage.show();

    }

    @FXML
    protected void onproductButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("product.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Product Category");
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(welcomeback_label.getScene().getWindow());
        stage.show();

    }
    @FXML
    protected void oncategoryButtonClick() throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Category.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Product Category");
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(welcomeback_label.getScene().getWindow());
        stage.show();
    }
    @FXML
    protected void onInvoiceButtonClick() throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Invoice.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Product Category");
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(welcomeback_label.getScene().getWindow());
        stage.show();
    }

    @FXML
    protected void oncustomerButtonClick() throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Customer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Customer");
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(welcomeback_label.getScene().getWindow());
        stage.show();
    }


}