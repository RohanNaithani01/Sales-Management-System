package com.example.sales_management_system_with_gst_return2;



import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class Company {
    ConnectionClass connectionClass = new ConnectionClass();

    @FXML
    Button select_image;

    @FXML
    TextField companyname_fieldbox;

    @FXML
    TextField address_fieldbox;

    @FXML
    TextField phoneno_fieldbox;

    @FXML
    TextField gstin_fieldbox;

    @FXML
    ComboBox<String> country_combobox;

    @FXML
    ComboBox<String> state_combobox;

    @FXML
    Button updatecompany_button;

    @FXML
    ImageView logo_imagebox= new ImageView();

    File selectedFile=null;
    byte[] imageBytes=null;

    String[] States={"01-Jammu & Kashmir", "02-Himachal Pradesh", "03-Punjab", "04-Chandigarh",
            "05-Uttarakhand", "06-Haryana", "07-Delhi", "08-Rajasthan", "09-Uttar Pradesh",
            "10-Bihar", "11-Sikkim", "12-Arunachal Pradesh", "13-Nagaland", "14-Manipur",
            "15-Mizoram", "16-Tripura", "17-Meghalaya", "18-Assam", "19-West Bengal",
            "20-Jharkhand", "21-Odisha", "22-Chhattisgarh", "23-Madhya Pradesh",
            "24-Gujarat", "25-Daman & Diu", "26-Dadra & Nagar Haveli & Daman & Diu",
            "27-Maharashtra", "29-Karnataka", "30-Goa", "31-Lakshadweep", "32-Kerala",
            "33-Tamil Nadu", "34-Puducherry", "35-Andaman & Nicobar Islands",
            "36-Telangana", "37-Andhra Pradesh", "38-Ladakh", "97-Other Territory"};

    String[] country={"India"};



    public void initialize() {
            state_combobox.getItems().addAll(States);
            country_combobox.getItems().addAll(country);
        try {
            Connection connection = connectionClass.CONN();
            String query = "Select * from company";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    companyname_fieldbox.setText(rs.getString("cmpname"));
                    address_fieldbox.setText(rs.getString("cmpaddress"));
                    state_combobox.setValue(rs.getString("cmpstate"));
                    country_combobox.setValue(rs.getString("cmpcountry"));
                    phoneno_fieldbox.setText(rs.getString("cmpphoneno"));
                    gstin_fieldbox.setText(rs.getString("cmpgstin"));
                    imageBytes = rs.getBytes("cmplogo");
                    if (imageBytes != null) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                        Image image = new Image(bis);
                        logo_imagebox.setImage(image);
                    }
                }
            rs.close();
            stmt.close();

            query = "Select * from company";
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                updatecompany_button.setText("Save");
            }
            rs.close();
            stmt.close();
            connection.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @FXML
    private void savechanges() throws SQLException
    {
        if(companyname_fieldbox.getText().isEmpty())
        {
            alert("Please Enter Company Name");
            companyname_fieldbox.requestFocus();
            return;
        }

        else if(address_fieldbox.getText().isEmpty())
        {
            alert("Please Enter Company Address");
            address_fieldbox.requestFocus();
            return;
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);

        if(updatecompany_button.getText().equals("Save")){
            alert.setContentText("Are You Sure You Want To Save Data");
        }
        else {
            alert.setContentText("Do You Want To Update This Data");
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (updatecompany_button.getText().equals("Save")) {
                save();
                updatecompany_button.setText("Update");
            }
            else{
                updatedata();

            }
        }

    }

    private void save() {
        try{
            Connection connection = connectionClass.CONN();
            String query = "Insert into company(cmpname,cmpaddress,cmpcountry,cmpstate,cmpphoneno,cmpgstin,cmplogo) values(?,?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, companyname_fieldbox.getText());
            stmt.setString(2, address_fieldbox.getText());
            stmt.setString(3, country_combobox.getValue());
            stmt.setString(4, state_combobox.getValue());
            stmt.setString(5, phoneno_fieldbox.getText());
            stmt.setString(6, gstin_fieldbox.getText());

            if (selectedFile != null) {
                try {
                    byte[] imageBytes = convertImageToByteArray(selectedFile);
                    stmt.setBytes(7, imageBytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (imageBytes != null) {
                    stmt.setBytes(7, imageBytes);
                } else {
                    stmt.setNull(7, java.sql.Types.BLOB);
                }

            }
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("save successful. Rows affected: " + affectedRows);
            } else {
                System.out.println("save failed. No rows affected.");
            }

            stmt.close();
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void updatedata()
    {

        try {

            Connection connection = connectionClass.CONN();
            String query = "UPDATE company SET cmpname=?, cmpaddress=?,cmpcountry=?,cmpstate=?,cmpphoneno=?,cmpgstin=?,cmplogo=? ";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1,companyname_fieldbox.getText());
            stmt.setString(2,address_fieldbox.getText());
            stmt.setString(3,country_combobox.getValue());
            stmt.setString(4,state_combobox.getValue());
            stmt.setString(5,phoneno_fieldbox.getText());
            stmt.setString(6,gstin_fieldbox.getText());

            if (selectedFile != null)
            {
                try {
                    byte[] imageBytes = convertImageToByteArray(selectedFile);
                    stmt.setBytes(7, imageBytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                if(imageBytes!=null)
                {
                    stmt.setBytes(7, imageBytes);
                }
                else
                {
                    stmt.setNull(7, java.sql.Types.BLOB);
                }

            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Update successful. Rows affected: " + affectedRows);
            } else {
                System.out.println("Update failed. No rows affected.");
            }

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void selectimage() {
        Stage stage = (Stage) select_image.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            logo_imagebox.setImage(image);
        }
    }
    public byte[] convertImageToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    private void alert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Required Input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}

