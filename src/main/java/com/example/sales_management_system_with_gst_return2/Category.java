package com.example.sales_management_system_with_gst_return2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


    public class Category {

        ConnectionClass connectionClass = new ConnectionClass();
        @FXML
        TextField categoryname_fieldbox;
        @FXML
        Button categoryadd_button;
        @FXML
        Button categorydelete_button;
        @FXML
        Button categorymodify_button;
        @FXML
        public ComboBox<String> categorysearch_combobox;
        @FXML
        Label searchcategory_label;
        int catid;
        @FXML
        Button forcedelete_button;


        public void initialize() {
            searchcategory_label.setVisible(false);
            categorysearch_combobox.setVisible(false);
            categorydelete_button.setVisible(false);
            forcedelete_button.setVisible(false);


        }

        @FXML
        public void savechanges() throws SQLException {
            if (categoryname_fieldbox.getText().isEmpty()) {
                alert("Input Category Name");
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);

            if (categoryadd_button.getText().equals("ADD")) {
                alert.setContentText("Are You Sure You Want To Save Data");
            } else {
                alert.setContentText("Do You Want To Update This Data");
            }
            if (categoryadd_button.getText().equals("ADD")) {
                save();
            } else {
                Update();
            }

        }

        public void save() {

            try {
                Connection connection = connectionClass.CONN();
                String query = "Insert into productcategory (catname) values(?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, categoryname_fieldbox.getText());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Updated Successful. Rows affected" + affectedRows);
                } else {
                    System.out.println("Update Fail No rows affected");
                }
                stmt.close();
                connection.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            clear();
        }

        @FXML
        private void modify() throws SQLException {
            if (categorymodify_button.getText().equals("Modify")) {
                categoryadd_button.setText("Update");
                categorysearch_combobox.setVisible(true);
                categorydelete_button.setVisible(true);
                categorymodify_button.setText("Back");
                searchcategory_label.setVisible(true);
                forcedelete_button.setVisible(true);
                dataList();
            } else {
                categoryadd_button.setText("Add");
                categorysearch_combobox.setVisible(false);
                categorydelete_button.setVisible(false);
                categorymodify_button.setText("Modify");
                searchcategory_label.setVisible(false);

            }
        }

        private void Update() throws SQLException {
            try {
                Connection connection = connectionClass.CONN();
                String query = "update productcategory set catname=? where catname='" + categorysearch_combobox.getValue() + "'";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, categoryname_fieldbox.getText());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Updated Successful. Rows affected" + affectedRows);
                } else {
                    System.out.println("Update Fail No rows affected");
                }
                stmt.close();
                connection.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            categoryadd_button.setText("Add");
            categorysearch_combobox.setVisible(false);
            categorydelete_button.setVisible(false);
            categorymodify_button.setText("Modify");
            searchcategory_label.setVisible(false);
            forcedelete_button.setVisible(false);

            clear();
        }

        @FXML
        private void selectedCategory() throws SQLException {
            String selectedCategory = categorysearch_combobox.getValue();
            categorysearch_combobox.setValue(selectedCategory);
            categoryDetail();
        }

        private void categoryDetail() throws SQLException {
            Connection connection = connectionClass.CONN();
            String query = "select catid,catname from productcategory where catname= '" + categorysearch_combobox.getValue() + "'";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                categoryname_fieldbox.setText(rs.getString("catname"));
                catid = (rs.getInt("catid"));


            }
            rs.close();
            stmt.close();
            connection.close();
        }

        private void dataList() throws SQLException {
            ObservableList<String> categoryName = FXCollections.observableArrayList();
            Connection connection = connectionClass.CONN();
            String query = "Select catname from productcategory order by catname";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categoryName.add(rs.getString("catname"));
            }

            rs.close();
            stmt.close();
            connection.close();

            categorysearch_combobox.getItems().addAll(categoryName);
            categorysearch_combobox.setItems(categoryName);
            categorysearch_combobox.setEditable(true);

            TextField editor = categorysearch_combobox.getEditor();
            editor.addEventHandler(KeyEvent.KEY_RELEASED, event ->
            {
                String text = editor.getText();
                if (text.isEmpty()) {
                    categorysearch_combobox.hide();
                    categorysearch_combobox.setItems(categoryName);
                } else {
                    ObservableList<String> filteredItems = FXCollections.observableArrayList();
                    for (String item : categoryName) {
                        if (item.toLowerCase().contains(text.toLowerCase())) {
                            filteredItems.add(item);
                        }
                    }
                    categorysearch_combobox.setItems(filteredItems);
                    categorysearch_combobox.show();
                }
            });
        }


        private void alert(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Required Input");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
        private void confirmalert(String message) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        private void clear() {
            categorysearch_combobox.setValue("");
            categoryname_fieldbox.clear();
        }

        @FXML
        private void delete() throws SQLException {
            try (Connection connection = connectionClass.CONN()) {
                String catname = categoryname_fieldbox.getText();

                if (catname == null || catname.isEmpty()) {
                    alert("Please select a catagory name to delete.");
                    return;
                }

                String query = "DELETE FROM productcategory WHERE catname = ?";

                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, catname);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        alert("category deleted successfully.");
                        categoryadd_button.setText("Add");
                        categorysearch_combobox.setVisible(false);
                        categorydelete_button.setVisible(false);
                        categorymodify_button.setText("Modify");
                        searchcategory_label.setVisible(false);
                        forcedelete_button.setVisible(false);
                        clear();

                    } else {
                        alert("No category found with the given name.");
                    }
                } catch (SQLException e) {
                    alert("An error occurred while deleting the category.");
                    throw new RuntimeException(e);

                }
            }
        }


        @FXML
        private void forcedelete() {
                confirmalert("All Products Within This Category May Be Deleted");
                try {

                    String deleteChildQuery = "delete from product  WHERE catid = ?";
                    String deleteParentQuery = "delete from productcategory where catid = ?";

                    Connection connection = connectionClass.CONN();
                    PreparedStatement deleteChildStmt = connection.prepareStatement(deleteChildQuery);
                    PreparedStatement deleteParentStmt = connection.prepareStatement(deleteParentQuery);
                    {

                        int parentId = catid;

                        // Step 1: Delete from child table
                        deleteChildStmt.setInt(1, parentId);
                        deleteChildStmt.executeUpdate();

                        // Step 2: Delete from parent table
                        deleteParentStmt.setInt(1, parentId);
                        int rowsAffected = deleteParentStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Parent row deleted successfully.");
                        } else {
                            System.out.println("No rows found to delete in parent table.");
                        }
                        categoryadd_button.setText("Add");
                        categorysearch_combobox.setVisible(false);
                        categorydelete_button.setVisible(false);
                        categorymodify_button.setText("Modify");
                        searchcategory_label.setVisible(false);
                        forcedelete_button.setVisible(false);
                        clear();

                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            }





