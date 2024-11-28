package com.example.sales_management_system_with_gst_return2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class PurchaseInvoice {
    ConnectionClass connectionClass =new ConnectionClass();
    @FXML
    TextField barcode_textfield;
    @FXML
    private ComboBox<String> productname_combobox;
    @FXML
    TextField qty_textfield;
    @FXML
    TextField rate_textfield;
    @FXML
    Button addproduct_button;
    @FXML
    TextField total_textfield;
    @FXML
    private TableView<productDetail> completedetail_tableview;
    @FXML
    private TableColumn<productDetail, Integer>prid_tableview;
    @FXML
    private TableColumn<productDetail, String>hsn_tableview;
    @FXML
    private TableColumn<productDetail, String>unit_tableview;
    @FXML
    private TableColumn<productDetail, String>productname_tableview;
    @FXML
    private TableColumn<productDetail, BigDecimal> gstpercent_tableview;
    @FXML
    private TableColumn<productDetail, BigDecimal> amount_tableview;
    @FXML
    private TableColumn<productDetail, BigDecimal> qty_tableview;
    @FXML
    private TableColumn<productDetail, BigDecimal> rate_tableview;

    @FXML
    private TableView<taxDetail> completegst_tableview;
    @FXML
    private TableColumn<taxDetail, BigDecimal>gstamount_tableview;
    @FXML
    private TableColumn<taxDetail, BigDecimal>gstpercent2_tableview;
    @FXML
    private TableColumn<taxDetail, BigDecimal>taxamount_tableview;
    @FXML
    private TableColumn<taxDetail, String>gstdetail_tableview;

    @FXML
    DatePicker date_datepicker;
    @FXML
    private ComboBox<String> customer_combobox;
    @FXML
    private ComboBox<String> placeofsupply_combobox;
    @FXML
    TextField mobileno_textfield;
    @FXML
    TextField gst_textfield;
    @FXML
    TextField rounding_textfield;
    @FXML
    TextField gtotal_textfield;
    @FXML
    TextField qtytotal_textfield;
    @FXML
    Button makeinvoice_button;
    @FXML
    TextField customergst_textfield;
    @FXML
    Button save_button;
    @FXML
    TextField address_textfield;

    String[] States={"01-Jammu & Kashmir", "02-Himachal Pradesh", "03-Punjab", "04-Chandigarh",
            "05-Uttarakhand", "06-Haryana", "07-Delhi", "08-Rajasthan", "09-Uttar Pradesh",
            "10-Bihar", "11-Sikkim", "12-Arunachal Pradesh", "13-Nagaland", "14-Manipur",
            "15-Mizoram", "16-Tripura", "17-Meghalaya", "18-Assam", "19-West Bengal",
            "20-Jharkhand", "21-Odisha", "22-Chhattisgarh", "23-Madhya Pradesh",
            "24-Gujarat", "25-Daman & Diu", "26-Dadra & Nagar Haveli & Daman & Diu",
            "27-Maharashtra", "29-Karnataka", "30-Goa", "31-Lakshadweep", "32-Kerala",
            "33-Tamil Nadu", "34-Puducherry", "35-Andaman & Nicobar Islands",
            "36-Telangana", "37-Andhra Pradesh", "38-Ladakh", "97-Other Territory"};

    private String productNames;
    private String productUnit;

    int productId = 0;
    BigDecimal gstRate;
    BigDecimal cess;
    BigDecimal rate;
    String hsn;
    String barcode;

    ObservableList<productDetail> Data = FXCollections.observableArrayList();
    ObservableList<taxDetail> TaxData = FXCollections.observableArrayList();

    String placeOfSupply;
    String customergst;
    String mobileno;
    String address;
    BigDecimal cgstTotal;
    BigDecimal sgstTotal;
    BigDecimal igstTotal;
    BigDecimal cessTotal;


    int custId;
    int traId;
    String state;
    int billno=0;

    public void initialize() throws SQLException
    {
        productList();
        customerList();
        placeofsupply_combobox.getItems().addAll(States);
        companystate();
        date_datepicker.setValue(LocalDate.now());

    }


    private void customerList() throws SQLException {
        ObservableList<String>customerNames=FXCollections.observableArrayList();
        Connection connection= connectionClass.CONN();
        String query="Select custname from customer order by custname";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs =stmt.executeQuery();
        customer_combobox.getItems().clear();

        while(rs.next())
        {
            customerNames.add(rs.getString("custname"));
        }
        rs.close();
        stmt.close();
        connection.close();
        customer_combobox.getItems().addAll(customerNames);
        customer_combobox.setItems(customerNames);
        customer_combobox.setEditable(true);
        TextField editor = customer_combobox.getEditor();
        editor.addEventHandler(KeyEvent.KEY_RELEASED,event ->{
            String list= editor.getText();
            if(list.isEmpty())
            {
                customer_combobox.setItems(customerNames);
            }
            else {
                ObservableList<String> filtereditems = FXCollections.observableArrayList();
                for (String Item : customerNames) {
                    if (Item.toLowerCase().contains(list.toLowerCase())) {
                        filtereditems.add(Item);
                    }
                }
                customer_combobox.setItems(filtereditems);
                customer_combobox.show();
            }
        });

    }
    @FXML
    private void customerDetail() throws SQLException {
        Connection connection = connectionClass.CONN();
        String query ="Select custstate, custgstin, custphoneno, custaddress from customer where custname='"+ customer_combobox.getValue()+"'";
        PreparedStatement stmt= connection.prepareStatement(query);
        ResultSet rs =stmt.executeQuery();

        while (rs.next())
        {
            placeOfSupply = (rs.getString("custstate"));
            customergst = (rs.getString("custgstin"));
            mobileno = (rs.getString("custphoneno"));
            address=(rs.getString("custaddress"));
        }
        customergst_textfield.setText(customergst);
        placeofsupply_combobox.setValue(placeOfSupply);
        mobileno_textfield.setText(mobileno);
        address_textfield.setText(address);


        rs.close();
        stmt.close();
        connection.close();

    }

    private int billno() throws SQLException {
        Connection connection= connectionClass.CONN();
        String query="SELECT COALESCE(MAX(billno), 0) AS billno FROM transactions order by classid";
        PreparedStatement stmt= connection.prepareStatement(query);
        ResultSet rs= stmt.executeQuery();

        while(rs.next()){
            billno=rs.getInt("billno");
        }
        rs.close();
        stmt.close();
        connection.close();
        billno=billno+1;

        return billno;

    }

    private void productList() throws SQLException
    {
        ObservableList<String>productNames= FXCollections.observableArrayList();
        Connection connection= connectionClass.CONN();
        String query="Select productname from product order by productname";
        PreparedStatement stmt= connection.prepareStatement(query);
        ResultSet rs= stmt.executeQuery();
        productname_combobox.getItems().clear();

        while(rs.next())
        {
            productNames.add(rs.getString("productname"));
        }
        rs.close();
        stmt.close();
        connection.close();
        productname_combobox.getItems().addAll(productNames);
        productname_combobox.setItems(productNames);
        productname_combobox.setEditable(true);
        TextField editor = productname_combobox.getEditor();
        editor.addEventHandler(KeyEvent.KEY_RELEASED,event -> {
            String text= editor.getText();
            if(text.isEmpty())
            {
                productname_combobox.setItems(productNames);

            }
            else {
                ObservableList<String> filtereditems = FXCollections.observableArrayList();
                for (String Item : productNames) {
                    if (Item.toLowerCase().contains(text.toLowerCase())) {
                        filtereditems.add(Item);
                    }
                }
                productname_combobox.setItems(filtereditems);
                productname_combobox.show();

            }
        });
    }
    @FXML
    public void productdetail() throws SQLException {
        Connection connection= connectionClass.CONN();
        String query="select productname, prid,  rate, unit, gstrate, cess, hsn, barcode from product where productname = '"+ productname_combobox.getValue()+"'";
        PreparedStatement stmt= connection.prepareStatement(query);
        ResultSet rs =stmt.executeQuery();

        while(rs.next()){
            productNames=(rs.getString("productname"));
            productId = (rs.getInt("prid"));

            rate = new BigDecimal(rs.getString("rate"));
            rate_textfield.setText(rs.getString("rate"));
            productUnit=(rs.getString("unit"));
            gstRate = new BigDecimal(rs.getString("gstrate"));
            cess = new BigDecimal(rs.getString("cess"));
            hsn= (rs.getString("hsn"));
            barcode = (rs.getString("barcode"));


        }

        rs.close();
        stmt.close();
        connection.close();
    }
    public void companystate() throws SQLException {
        Connection connection= connectionClass.CONN();
        String query="Select cmpstate from company";
        PreparedStatement stmt= connection.prepareStatement(query);
        ResultSet rs =stmt.executeQuery();
        while(rs.next()){
            state=(rs.getString("cmpstate"));
        }
        rs.close();
        stmt.close();
        connection.close();


    }

    @FXML
    private void OnBarcodeClick()
    {
        barcode_textfield.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER)
            {

                try{
                    Connection connection = connectionClass.CONN();
                    String query = "select productname, prid,  rate, unit, gstrate, cess, hsn, barcode from product where barcode = '"+barcode_textfield.getText()+"'";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next())
                    {
                        productNames = (rs.getString("productname"));
                        productId = (rs.getInt("prid"));
                        rate = new BigDecimal(rs.getString("rate"));
                        rate_textfield.setText(rs.getString("rate"));
                        productUnit = (rs.getString("unit"));
                        gstRate = new BigDecimal(rs.getString("gstrate"));
                        cess = new BigDecimal(rs.getString("cess"));
                        hsn = (rs.getString("hsn"));
                        barcode = (rs.getString("barcode"));

                    }
                    rs.close();
                    stmt.close();
                    connection.close();
                }
                catch (SQLException e) {
                    alert("No Product With This Barcode");
                }
                if (Objects.equals(productNames, "") || productNames == null){


                    alert("Product not found");
                    barcode_textfield.clear();
                    return;

                }
                add();
                barcode_textfield.clear();
            }
        });


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
    public void OnAddButtonClick() throws SQLException {

        if (Objects.equals(productNames, "") || productNames == null){
            System.out.println(productNames);
        }
        else {
            add();
        }
    }

    private void add(){
        if (qty_textfield.getText().isEmpty()) {
            qty_textfield.setText("1");
        }
        prid_tableview.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productname_tableview.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        hsn_tableview.setCellValueFactory(new PropertyValueFactory<>("hsn"));
        rate_tableview.setCellValueFactory(new PropertyValueFactory<>("rate"));
        qty_tableview.setCellValueFactory(new PropertyValueFactory<>("qty"));
        gstpercent_tableview.setCellValueFactory(new PropertyValueFactory<>("gst"));
        unit_tableview.setCellValueFactory(new PropertyValueFactory<>("unit"));
        amount_tableview.setCellValueFactory(new PropertyValueFactory<>("amount"));

        BigDecimal qty= new BigDecimal(qty_textfield.getText());
        BigDecimal rate = new BigDecimal(rate_textfield.getText());
        BigDecimal amount = qty.multiply(rate);
        Data.add(new productDetail(productId,hsn,productUnit,amount,gstRate,qty,rate,productNames));
        completedetail_tableview.setItems(Data);
        BigDecimal total=CalculateTotalAmount().setScale(2, RoundingMode.HALF_UP);
        total_textfield.setText(String.valueOf(total));


        productname_combobox.setValue("");
        qty_textfield.setText("");
        rate_textfield.setText("");
        productNames="";

        displayTotalAmountsByGst();
        BigDecimal taxtotal=CalculateGstAmount().setScale(2,RoundingMode.HALF_UP);
        gst_textfield.setText(String.valueOf(taxtotal));
        qtytotal_textfield.setText(String.valueOf(CalculateTotalQuantity().setScale(2,RoundingMode.HALF_UP)));
        BigDecimal sum=(total.add(taxtotal));
        BigDecimal GrandTotal= sum.setScale(0,RoundingMode.HALF_UP);
        BigDecimal roundoff=GrandTotal.subtract(sum);

        rounding_textfield.setText(String.valueOf(roundoff));
        gtotal_textfield.setText(String.valueOf(GrandTotal));
    }



    public BigDecimal CalculateTotalAmount()
    {

        BigDecimal total = BigDecimal.ZERO;
        for (productDetail product : Data) {
            total = total.add(product.getAmount());
        }
        return total;
    }


    public BigDecimal CalculateTotalQuantity() {

        BigDecimal total = BigDecimal.ZERO;
        for (productDetail product : Data) {
            total = total.add(product.getQty());
        }
        return total;
    }

    public BigDecimal CalculateGstAmount(){
        BigDecimal amount = BigDecimal.ZERO;
        for(taxDetail taxDetail: TaxData){
            amount=amount.add(taxDetail.getTaxAmount());
        }
        return amount;
    }

    private Map<BigDecimal,BigDecimal>CalculateTotalAmountByGST(){
        Map<BigDecimal,BigDecimal>totalByGst=new HashMap<>();
        for(productDetail product:Data){
            BigDecimal gst = product.getGst();
            BigDecimal amount = product.getAmount();
            totalByGst.put(gst, totalByGst.getOrDefault(gst, BigDecimal.ZERO).add(amount));
        }
        return totalByGst;
    }

    private void TotalAmountByGST(){
        Map<String,BigDecimal>totalByGstName=new HashMap<>();
        for(taxDetail taxDetail : TaxData){
            String gst = taxDetail.getGstName();
            BigDecimal amount = taxDetail.getTaxAmount();
            totalByGstName.put(gst, totalByGstName.getOrDefault(gst, BigDecimal.ZERO).add(amount));
            System.out.println(amount);
        }
        System.out.println(totalByGstName);
        System.out.println("test");
        cgstTotal= BigDecimal.valueOf(0);
        sgstTotal=BigDecimal.valueOf(0);
        igstTotal=BigDecimal.valueOf(0);
        cessTotal=BigDecimal.valueOf(0);
        totalByGstName.forEach((gst, amount) ->{
                    if(gst.equals("CGST"))
                    {
                        cgstTotal=amount;
                        System.out.println("CGST: " + amount);
                        System.out.println("CGST: " + cgstTotal);

                    }
                    if(gst.equals("SGST"))
                    {
                        sgstTotal=amount;
                        sgstTotal=sgstTotal.setScale(2, RoundingMode.HALF_UP);
                        System.out.println(sgstTotal);

                    }
                    if(gst.equals("IGST")){
                        igstTotal=amount.setScale(2, RoundingMode.HALF_UP);

                    }
                    if(gst.equals("CESS")){
                        cessTotal=amount.setScale(2, RoundingMode.HALF_UP);

                    }
                }
        );
    }

    @FXML
    private void displayTotalAmountsByGst() {
        completegst_tableview.getItems().clear();
        Map<BigDecimal,BigDecimal> totalByGst= CalculateTotalAmountByGST();
        totalByGst.forEach((gst, totalAmount) ->{
            System.out.println("GST: "+gst+"Total Amount: "+ totalAmount);
            totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);
            BigDecimal gstRate = gst.divide(BigDecimal.valueOf(2),2, RoundingMode.HALF_UP);
            BigDecimal taxAmount = (totalAmount.multiply(gstRate)).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
            BigDecimal IgstTaxAmount=(totalAmount.multiply(gst)).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP );
            gstdetail_tableview.setCellValueFactory(new PropertyValueFactory<>("GstName"));
            gstpercent2_tableview.setCellValueFactory(new PropertyValueFactory<>("GstRate"));
            gstamount_tableview.setCellValueFactory(new PropertyValueFactory<>("Amount"));
            gstdetail_tableview.setCellValueFactory(new PropertyValueFactory<>("GstName"));
            taxamount_tableview.setCellValueFactory(new PropertyValueFactory<>("TaxAmount"));

            if(state.equals(placeofsupply_combobox.getValue())) {
                TaxData.add(new taxDetail("CGST", totalAmount, gstRate, taxAmount));
                TaxData.add(new taxDetail("SGST", totalAmount, gstRate, taxAmount));
            }
            else {
                TaxData.add(new taxDetail("IGST", totalAmount,gst,IgstTaxAmount));
            }
            completegst_tableview.setItems(TaxData);
            gstamount_tableview.setStyle("-fx-alignment: CENTER-RIGHT;");
            gstpercent2_tableview.setStyle("-fx-alignment: CENTER-RIGHT;");
            gstpercent2_tableview.setStyle("-fx-alignment: CENTER;");
        });
    }


    private void CallDataFromCustomer() throws SQLException {
        Connection connection= connectionClass.CONN();
        String query="select custid from customer where custname='"+customer_combobox.getValue()+"'";
        PreparedStatement stmt= connection.prepareStatement(query);
        ResultSet rs =stmt.executeQuery();
        while(rs.next()){
            custId=rs.getInt("custid");
        }
        rs.close();
        stmt.close();
        connection.close();

    }



    @FXML
    private void OnSaveButtonClick() throws SQLException {
        saveInTransaction();
        SaveTransactionDetail();
        SaveTaxDetail();
        clearall();

    }

    private void saveInTransaction() throws SQLException {
        TotalAmountByGST();
        CallDataFromCustomer();
        try{


            Connection connection = connectionClass.CONN();
            String query="Insert into transactions(classid,custname,billingaddress,custgstin,phoneno,total,offamount,igst,cgst,sgst,cess,quantity,invtype,billdate,custid,billno) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1,2);
            stmt.setString(2,customer_combobox.getValue());
            stmt.setString(3,address_textfield.getText());
            stmt.setString(4,customergst_textfield.getText());
            stmt.setString(5,mobileno_textfield.getText());
            BigDecimal value=new BigDecimal(total_textfield.getText());
            stmt.setBigDecimal(6,value);
            BigDecimal value2 = new BigDecimal(rounding_textfield.getText());
            stmt.setBigDecimal(7,value2);
            stmt.setBigDecimal(8,igstTotal);
            stmt.setBigDecimal(9,cgstTotal);
            stmt.setBigDecimal(10,sgstTotal);
            stmt.setBigDecimal(11,cessTotal);
            BigDecimal value3=new BigDecimal(qtytotal_textfield.getText());
            stmt.setBigDecimal(12,value3);
            stmt.setString(13,"Regular");
            LocalDate localdate=date_datepicker.getValue();
            Date sqlDate = Date.valueOf(localdate);
            stmt.setDate(14,sqlDate);
            stmt.setInt(15,custId);
            stmt.setInt(16,billno());


            stmt.executeUpdate();

            ResultSet rs= stmt.getGeneratedKeys();
            if(rs.next()){
                traId= (int) rs.getLong(1);
                System.out.println(traId);

            }
            stmt.close();
            connection.close();

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    private void SaveTransactionDetail() throws SQLException {
        for(productDetail product : Data)
        {
            try {
                Connection connection = connectionClass.CONN();
                String query = "Insert Into transactiondetail(traid,prname,hsn,prid,unit,quantity,rate,gstrate,total) values(?,?,?,?,?,?,?,?,?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1,traId);
                stmt.setString(2,product.getProductName());
                stmt.setString(3,product.getHsn());
                stmt.setInt(4,product.getProductId());
                stmt.setString(5,product.getUnit());
                stmt.setBigDecimal(6,product.getQty());
                stmt.setBigDecimal(7,product.getRate());
                stmt.setBigDecimal(8,product.getGst());
                stmt.setBigDecimal(9,product.getAmount());


                stmt.executeUpdate();
                stmt.close();
                connection.close();

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void SaveTaxDetail(){
        for(taxDetail taxDetail : TaxData)
        {
            try{
                Connection connection= connectionClass.CONN();
                String query="Insert Into taxdetail(gstname,traid,gstrate,onamt,taxamt) values(?,?,?,?,?)";
                PreparedStatement stmt= connection.prepareStatement(query);
                stmt.setString(1,taxDetail.getGstName());
                stmt.setInt(2,traId);
                stmt.setBigDecimal(3,taxDetail.getGstRate());
                stmt.setBigDecimal(4,taxDetail.getAmount());
                stmt.setBigDecimal(5,taxDetail.getTaxAmount());


                stmt.executeUpdate();

                stmt.close();
                connection.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }

    private void clearall(){
        total_textfield.clear();
        gtotal_textfield.clear();
        qtytotal_textfield.clear();
        customer_combobox.setValue("");
        customergst_textfield.clear();
        mobileno_textfield.clear();
        address_textfield.clear();
        placeofsupply_combobox.setValue("");
        gst_textfield.clear();
        rounding_textfield.clear();
        completegst_tableview.getItems().clear();
        completedetail_tableview.getItems().clear();
        Data.clear();
        TaxData.clear();

    }
}





