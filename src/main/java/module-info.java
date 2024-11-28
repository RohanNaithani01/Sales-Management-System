module com.example.sales_management_system_with_gst_return2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.sales_management_system_with_gst_return2 to javafx.fxml;
    exports com.example.sales_management_system_with_gst_return2;
}