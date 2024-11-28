package com.example.sales_management_system_with_gst_return2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Customer {

    ConnectionClass connectionClass = new ConnectionClass();
    @FXML
    TextField customername_textfield;

    @FXML
    TextField customerphoneno_textfield;

    @FXML
    TextField customergstin_textfield;

    @FXML
    TextField customeropbl_textfield;

    @FXML
    TextField customeraddress_textfield;

    @FXML
    ComboBox<String> customerstate_combobox;

    String[] states = {
            "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
            "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
            "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
            "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
            "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
            "Uttar Pradesh", "Uttarakhand", "West Bengal"
    };

    @FXML
    Button save_button;

    @FXML
    Button modify_button;

    @FXML
    Button delete_button;

    @FXML
    ComboBox <String> searchcustomer_combobox;

    @FXML
    Label searchcustomer_label;

    String opbl;

    public void initialize() {
        customerstate_combobox.getItems().addAll(states);
        delete_button.setVisible(false);
        searchcustomer_combobox.setVisible(false);
        searchcustomer_label.setVisible(false);
        opbl= customeropbl_textfield.getText();
    }



    @FXML
    private void savedata() throws SQLException {

        try {
            Connection connection = connectionClass.CONN();
            String query = "insert into customer(custname,custphoneno,custaddress,custstate,custgstin,opbl)values(?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, customername_textfield.getText());
            stmt.setString(2, customerphoneno_textfield.getText());
            stmt.setString(3, customeraddress_textfield.getText());
            stmt.setString(4, customerstate_combobox.getValue());
            stmt.setString(5, customergstin_textfield.getText());

            BigDecimal value = new BigDecimal(opbl);
            stmt.setBigDecimal(6, value);


            int affectedrows = stmt.executeUpdate();
            if (affectedrows > 0) {
                System.out.print("Rows Affected" + affectedrows);
            } else {
                System.out.print("No Rows Affected");
            }
            stmt.close();
            connection.close();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void savechanges() throws SQLException {

        if(customername_textfield.getText().isEmpty())
        {
            alert("Customer Name Is Empty");
            customername_textfield.requestFocus();
            return;
        } else if (customerphoneno_textfield.getText().isEmpty()) {
            alert("Customer Phone No Is Empty");
            customerphoneno_textfield.requestFocus();
            return;
        }else if (customeraddress_textfield.getText().isEmpty()) {
            alert("Customer Address Is Empty");
            customeraddress_textfield.requestFocus();
            return;

        }else if (customerstate_combobox.getValue().isEmpty()) {
            alert("Customer State Is Empty");
            customerstate_combobox.requestFocus();
            return;
        } else if (customergstin_textfield.getText().isEmpty()) {
            alert("Customer Gst No Is Empty");
            customergstin_textfield.requestFocus();
            return;
        }
        else if (!(isnumeric(opbl)) || opbl.equals(""))
        {

            opbl="0";
            return;
        }
        try(Connection connection = connectionClass.CONN())
        {

            String checkquery="SELECT COUNT(*) FROM customer WHERE custname = ? and not custname ='" + searchcustomer_combobox.getValue() + "'";
            PreparedStatement checkStmt = connection.prepareStatement(checkquery);
            checkStmt.setString(1, customername_textfield.getText());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                alert("Customer Name already exists.");
                customername_textfield.requestFocus();
                return;
            }
            rs.close();
            checkStmt.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);

        if(save_button.getText().equals("Save")){
            alert.setContentText("Are You Sure You Want To Save Data");
        }
        else {
            alert.setContentText("Do You Want To Update This Data");
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (save_button.getText().equals("Save")) {
                savedata();
                cleardata();
            }
            else{
                updatedata();
                cleardata();
            }
        }
    }



    private void updatedata() {

        try{
            Connection connection = connectionClass.CONN();
            String query="update customer Set custname=?,custphoneno=?,custaddress=?,custstate=?,custgstin=?,opbl=? WHERE custname = '" + searchcustomer_combobox.getValue() + "'";
            PreparedStatement stmt= connection.prepareStatement(query);
            stmt.setString(1, customername_textfield.getText());
            stmt.setString(2, customerphoneno_textfield.getText());
            stmt.setString(3, customeraddress_textfield.getText());

            stmt.setString(4, customerstate_combobox.getValue());
            stmt.setString(5, customergstin_textfield.getText());
            BigDecimal value = new BigDecimal(opbl);
            stmt.setBigDecimal(6, value);


            int affectedrows = stmt.executeUpdate();
            if (affectedrows > 0) {
                System.out.print("Rows Affected" + affectedrows);
            } else {
                System.out.print("No Rows Affected");
            }
            stmt.close();
            connection.close();

            connection.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
    private void datalist() throws SQLException {
        ObservableList<String> customername = FXCollections.observableArrayList();
        Connection connection = connectionClass.CONN();
        String query = "Select custname from customer order by custname";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        searchcustomer_combobox.getItems().clear();

        while (rs.next()) {
            customername.add(rs.getString("custname"));
        }

        rs.close();
        stmt.close();
        connection.close();

        searchcustomer_combobox.getItems().addAll(customername);
        searchcustomer_combobox.setItems(customername);
        searchcustomer_combobox.setEditable(true);

        TextField editor = searchcustomer_combobox.getEditor();
        editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            String text = editor.getText();
            if (text.isEmpty()) {
                searchcustomer_combobox.hide();
                searchcustomer_combobox.setItems(customername);
            } else {
                ObservableList<String> filteredItems = FXCollections.observableArrayList();
                for (String item : customername) {
                    if (item.toLowerCase().contains(text.toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                searchcustomer_combobox.setItems(filteredItems);
                searchcustomer_combobox.show();
            }
        });
    }

    @FXML
    private void selectedcustomer() throws SQLException {
        String selectedcustomer=searchcustomer_combobox.getValue();
        customername_textfield.setText(selectedcustomer);
        customerdetail();
    }

    private void customerdetail() throws SQLException {
        Connection connection = connectionClass.CONN();
        String query= "select custname,custphoneno,custaddress,custstate,custgstin,opbl from customer where custname='"+searchcustomer_combobox.getValue()+"'";
        PreparedStatement stmt= connection.prepareStatement(query);
        ResultSet rs= stmt.executeQuery();

        while(rs.next())
        {
            customername_textfield.setText(rs.getString("custname"));

            customerphoneno_textfield.setText(rs.getString("custphoneno"));

            customeraddress_textfield.setText(rs.getString("custaddress"));

            customerstate_combobox.setPromptText(rs.getString("custstate"));
            customerstate_combobox.setValue(rs.getString("custstate"));

            customergstin_textfield.setText(rs.getString("custgstin"));

            customeropbl_textfield.setText(rs.getString("opbl"));

        }
        rs.close();
        stmt.close();
        connection.close();
    }

    private boolean isnumeric(String value2)
    {
        if (value2 == null || value2.isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(value2);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void alert(String message)
    {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Required Input");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
    }
    @FXML
    private void modifyclick() throws SQLException {
        if(modify_button.getText().equals("Modify")) {
            searchcustomer_label.setVisible(true);
            searchcustomer_combobox.setVisible(true);
            delete_button.setVisible(true);
            save_button.setText("Update");
            modify_button.setText("Back");
            cleardata();
            datalist();
        }
        else {
            searchcustomer_label.setVisible(false);
            searchcustomer_combobox.setVisible(false);
            delete_button.setVisible(false);
            modify_button.setText("Modify");
            save_button.setText("Save");
            cleardata();

        }
    }


    private void cleardata() {
        customername_textfield.clear();
        customeropbl_textfield.clear();
        customerphoneno_textfield.clear();
        customergstin_textfield.clear();
        customeraddress_textfield.clear();
        customerstate_combobox.setValue(null);
        searchcustomer_combobox.setValue("");
    }
@FXML
    private void delete() throws SQLException {
        String customername=searchcustomer_combobox.getValue();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are You Sure You Want To delete Data");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try{
                    Connection connection= connectionClass.CONN();
                    String query="Delete from customer where custname=?";
                    PreparedStatement stmt= connection.prepareStatement(query);
                    stmt.setString(1,customername);
                    stmt.executeUpdate();
                    searchcustomer_label.setVisible(false);
                    searchcustomer_combobox.setVisible(false);
                    delete_button.setVisible(false);
                    modify_button.setText("Modify");
                    save_button.setText("Save");
                    stmt.close();
                    connection.close();
                    cleardata();

            } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            }
    }
}
