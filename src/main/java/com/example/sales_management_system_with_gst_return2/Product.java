package com.example.sales_management_system_with_gst_return2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Product {

    ConnectionClass connectionClass = new ConnectionClass();
    @FXML
    TextField productname_fieldbox;

    @FXML
    TextField productprice_fieldbox;

    @FXML
    TextField hsn_fieldbox;

    @FXML
    TextField barcode_fieldbox;

    @FXML
    TextField openingbalance_fieldbox;

    @FXML
    Button save_button;

    @FXML
    private ComboBox<String> productcategory_combobox;

    @FXML
    private ComboBox<String> productgst_combobox;

    @FXML
    private ComboBox<String> productcess_combobox;

    @FXML
    private ComboBox<String> productunit_combobox;

    @FXML
    Button modify_button;

    @FXML
    Button delete_button;

    @FXML
    private ComboBox<String> productsearch_combobox;

    @FXML
    String[] Unit = {
            "BAG-BAGS", "BAL-BALE", "BDL-BUNDLES", "BKL-BUCKLES",
            "BOU-BILLION OF UNITS", "BOX-BOX", "BTL-BOTTLES", "BUN-BUNCHES",
            "CAN-CANS", "CBM-CUBIC METERS", "CCM-CUBIC CENTIMETERS",
            "CMS-CENTIMETERS", "CTN-CARTONS", "DOZ-DOZENS", "DRM-DRUMS",
            "GGK-GREAT GROSS", "GMS-GRAMMES", "GRS-GROSS", "GYD-GROSS YARDS",
            "KGS-KILOGRAMS", "KLR-KILOLITRE", "KME-KILOMETRE", "LTR-LITRES",
            "MLT-MILLILITRE", "MTR-METERS", "MTS-METRIC TON", "NOS-NUMBERS",
            "PAC-PACKS", "PCS-PIECES", "PRS-PAIRS", "QTL-QUINTAL",
            "ROL-ROLLS", "SET-SETS", "SQF-SQUARE FEET", "SQM-SQUARE METERS",
            "SQY-SQUARE YARDS", "TBS-TABLETS", "TGM-TEN GROSS",
            "THD-THOUSANDS", "TON-TONNES", "TUB-TUBES", "UGS-US GALLONS",
            "UNT-UNITS", "YDS-YARDS", "OTH-OTHERS"
    };

    @FXML
    String[] gstrate = {"1", "5", "12", "18", "28"};

    @FXML
    String[] cessrate = {"0", "12"};

    @FXML
    private Label product_label;

    String categoryname;
    int catagoryid;

    String str;

    @FXML
    private Label searchproduct_label;


    @FXML
    public void initialize() throws SQLException {
        productunit_combobox.getItems().addAll(Unit);
        productgst_combobox.getItems().addAll(gstrate);
        productcess_combobox.getItems().addAll(cessrate);

        loadproductcategory();
        delete_button.setVisible(false);
        productsearch_combobox.setVisible(false);
        searchproduct_label.setVisible(false);

    }

    @FXML
    public void loadproductcategory() {
        try {
            Connection connection = connectionClass.CONN();
            String query = "Select catname from productcategory";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productcategory_combobox.getItems().add(rs.getString("catname"));

            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void getcategoryid() throws SQLException {
        categoryname = productcategory_combobox.getValue();

        try {
            Connection connection = connectionClass.CONN();
            String query = "Select catid from productcategory where catname='" + categoryname + "'";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                catagoryid = rs.getInt("catid");
            }
            System.out.println("ADDED" + catagoryid);
            System.out.println("ADDED" + categoryname);
            rs.close();
            stmt.close();
            connection.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    private void insertdata() throws SQLException {

        getcategoryid();
        str=productname_fieldbox.getText();


        try {
            Connection connection = connectionClass.CONN();
            String query = "insert into product(productname,gstrate,cess,rate,hsn,barcode,unit,catid,opbl)values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, String.valueOf(propercase(str)));
            BigDecimal value = new BigDecimal(productgst_combobox.getValue());
            stmt.setBigDecimal(2, value);
            BigDecimal value2 = new BigDecimal(productcess_combobox.getValue());
            stmt.setBigDecimal(3, value2);
            BigDecimal value3 = new BigDecimal(productprice_fieldbox.getText());
            stmt.setBigDecimal(4, value3);
            stmt.setString(5, hsn_fieldbox.getText());
            stmt.setString(6, barcode_fieldbox.getText());
            stmt.setString(7, productunit_combobox.getValue());
            stmt.setInt(8, catagoryid);
            BigDecimal value4 = new BigDecimal(openingbalance_fieldbox.getText());
            stmt.setBigDecimal(9, value4);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Update successful. Rows affected: " + affectedRows);
            } else {
                System.out.println("Update failed. No rows affected.");
            }

            stmt.close();
            connection.close();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        clearfields();

    }

    @FXML
    private void savebuttonclick() throws SQLException, IOException {
        categoryname = productcategory_combobox.getValue();
        if (productname_fieldbox.getText().isEmpty()) {
            showalert("Product Name Is Required");
            productname_fieldbox.requestFocus();
        } else if (categoryname == null || categoryname.isEmpty()) {
            showalert("Product Category is required");
            productcategory_combobox.requestFocus();
        } else if (productprice_fieldbox.getText().isEmpty() || !isnumeric(productprice_fieldbox.getText())) {
            showalert("Product Price is required");
            productprice_fieldbox.requestFocus();
        } else if ((productunit_combobox.getValue() == null || productunit_combobox.getValue().isEmpty())) {
            showalert("Product Unit is required");
            productunit_combobox.requestFocus();
        } else if ((productgst_combobox.getValue() == null || productgst_combobox.getValue().isEmpty())) {
            showalert("Product Gst% is required");
            productgst_combobox.requestFocus();

        } else {
            if (productcess_combobox.getValue() == null || productcess_combobox.getValue().isEmpty()) {

                productcess_combobox.setValue("0");
            } else if (openingbalance_fieldbox.getText().isEmpty() || isnumeric(openingbalance_fieldbox.getText())) {
                openingbalance_fieldbox.setText("0");

            }
            try (Connection connection = connectionClass.CONN()) {

                String checkquery = "SELECT COUNT(*) FROM product WHERE productname = ? and not productname ='" + productsearch_combobox.getValue() + "'";
                PreparedStatement checkStmt = connection.prepareStatement(checkquery);
                checkStmt.setString(1, productname_fieldbox.getText());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    showalert("Product Name already exists.");
                    productname_fieldbox.requestFocus();
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

            if (save_button.getText().equals("Save")) {
                alert.setContentText("Are You Sure You Want To Save Data");
            } else {
                alert.setContentText("Do You Want To Update This Data");
            }

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (save_button.getText().equals("Save")) {
                    insertdata();
                } else {
                    updateproduct();
                    clearfields();
                }
            }

        }


    }

    @FXML
    private void showalert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Required");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private boolean isnumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private void clearfields() {
        productname_fieldbox.clear();
        productprice_fieldbox.clear();
        productcess_combobox.setValue("");
        productgst_combobox.setValue("");
        productcategory_combobox.setValue("");
        productunit_combobox.setValue("");
        openingbalance_fieldbox.clear();
        hsn_fieldbox.clear();
        barcode_fieldbox.clear();
        productsearch_combobox.setValue("");
    }

    @FXML
    private void modifydata() throws SQLException {
        if (modify_button.getText().equals("Modify")) {

            save_button.setText("Update");
            searchproduct_label.setVisible(true);
            delete_button.setVisible(true);
            productsearch_combobox.setVisible(true);
            modify_button.setText("Back");
            datalist();
            clearfields();

        } else {
            save_button.setText("Save");
            searchproduct_label.setVisible(false);
            delete_button.setVisible(false);
            productsearch_combobox.setVisible(false);
            modify_button.setText("Modify");
        }
    }

    private void updateproduct() throws SQLException {
        getcategoryid();
        try {
            Connection connection = connectionClass.CONN();
            String query = "UPDATE product SET productname =? , gstrate=?, cess=?, rate=?, hsn=?, barcode=?, unit=?, catid=?, opbl=? WHERE productname = '" + productsearch_combobox.getValue() + "'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, productname_fieldbox.getText());
            BigDecimal value = new BigDecimal(productgst_combobox.getValue());
            stmt.setBigDecimal(2, value);
            BigDecimal value2 = new BigDecimal(productcess_combobox.getValue());
            stmt.setBigDecimal(3, value2);
            BigDecimal value3 = new BigDecimal(productprice_fieldbox.getText());
            stmt.setBigDecimal(4, value3);
            stmt.setString(5, hsn_fieldbox.getText());
            stmt.setString(6, barcode_fieldbox.getText());
            stmt.setString(7, productunit_combobox.getValue());
            stmt.setInt(8, catagoryid);
            BigDecimal value4 = new BigDecimal(openingbalance_fieldbox.getText());
            stmt.setBigDecimal(9, value4);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Update successful. Rows affected: " + affectedRows);
            } else {
                System.out.println("Update failed. No rows affected.");
            }
            save_button.setText("Save");
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        save_button.setText("Save");
        searchproduct_label.setVisible(false);
        productsearch_combobox.setVisible(false);
        delete_button.setVisible(false);
        modify_button.setText("Modify");
        clearfields();

    }

    private void datalist() throws SQLException {
        ObservableList<String> ProductName = FXCollections.observableArrayList();
        Connection connection = connectionClass.CONN();
        String query = "Select productname from product order by productname";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        productsearch_combobox.getItems().clear();

        while (rs.next()) {
            ProductName.add(rs.getString("productname"));
        }

        rs.close();
        stmt.close();
        connection.close();

        productsearch_combobox.getItems().addAll(ProductName);
        productsearch_combobox.setItems(ProductName);
        productsearch_combobox.setEditable(true);

        TextField editor = productsearch_combobox.getEditor();
        editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            String text = editor.getText();
            if (text.isEmpty()) {
                productsearch_combobox.hide();
                productsearch_combobox.setItems(ProductName);
            } else {
                ObservableList<String> filteredItems = FXCollections.observableArrayList();
                for (String item : ProductName) {
                    if (item.toLowerCase().contains(text.toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                productsearch_combobox.setItems(filteredItems);
                productsearch_combobox.show();
            }
        });

    }

    @FXML
    private void selecteditems() throws SQLException {
        String selecteditem = productsearch_combobox.getValue();
        productname_fieldbox.setText(selecteditem);
        Showproductdetail();
    }

    private void Showproductdetail() throws SQLException {
        Connection connection = connectionClass.CONN();
        String query = "select productname, product.catid, catname, rate, unit, gstrate, cess, hsn, barcode, opbl from product INNER JOIN productcategory ON product.catid = productcategory.catid where productname = '" + productsearch_combobox.getValue() + "'";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            productname_fieldbox.setText(rs.getString("productname"));
            catagoryid = (rs.getInt("catid"));

            productcategory_combobox.setPromptText(rs.getString("catname"));
            productcategory_combobox.setValue(rs.getString("catname"));

            productprice_fieldbox.setText(rs.getString("rate"));

            productunit_combobox.setPromptText(rs.getString("unit"));
            productunit_combobox.setValue(rs.getString("unit"));

            productgst_combobox.setPromptText(rs.getString("gstrate"));
            productgst_combobox.setValue(rs.getString("gstrate"));

            productcess_combobox.setPromptText(rs.getString("cess"));
            productcess_combobox.setValue(rs.getString("cess"));

            hsn_fieldbox.setText(rs.getString("hsn"));

            barcode_fieldbox.setText(rs.getString("barcode"));

            openingbalance_fieldbox.setText(rs.getString("opbl"));
        }
        rs.close();
        stmt.close();
        connection.close();

    }

    @FXML
    private void delete() throws SQLException {
        try (Connection connection = connectionClass.CONN()) {
            String productName = productsearch_combobox.getValue();

            if (productName == null || productName.isEmpty()) {
                showalert("Please select a Product name to delete.");
                return;
            }

            String query = "DELETE FROM product WHERE productName = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, productName);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    showalert("Product deleted successfully.");
                    clearfields();
                    save_button.setText("Save");
                    searchproduct_label.setVisible(false);
                    productsearch_combobox.setVisible(false);
                    delete_button.setVisible(false);
                    modify_button.setText("Modify");



                } else {
                    showalert("No Product found with the given name.");
                }
            } catch (SQLException e) {
                showalert("An error occurred while deleting the Product.");
                throw new RuntimeException(e);

            }
            clearfields();
        }
    }
    public StringBuilder propercase(String str){
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String properWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            result.append(properWord).append(" ");
        }
        return result;
    }
}




