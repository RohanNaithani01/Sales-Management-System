package com.example.sales_management_system_with_gst_return2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    ConnectionClass connectionClass = new ConnectionClass();
    @FXML
    Button cancel_button;
    @FXML
    Button login_button;
    @FXML
    TextField username_textfield;
    @FXML
    PasswordField password_passwordfield;

    String Plain_Username;
    String username="";
    String Plain_Password="";
    String password="";

    @FXML
    public void OnLoginButtonClick(ActionEvent event) throws SQLException {
        if(check()){
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void OnCancelButtonClick(ActionEvent event)
    {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        Main.closePrimaryStage();

    }

    private boolean check() throws SQLException
    {
        Plain_Username = username_textfield.getText();
        Plain_Password = password_passwordfield.getText();

        try {
            Connection connection = connectionClass.CONN();
            String query = "select password_hash,username from users where username='" + Plain_Username + "'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                password = rs.getString("password_hash");
                username = rs.getString("username");
            }
            if (Plain_Password.equals(password) && Plain_Username.equals(username)) {
                return true;
            }
            else {
                showalert("Invalid credentials.");
                return false;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }



    private void showalert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Required");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
