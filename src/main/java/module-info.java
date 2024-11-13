module org.example.controller.burgershopmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jdk.jdi;
    requires mysql.connector.j;


    opens org.example.controller.burgershopmanagement to javafx.fxml;
    exports org.example.controller.burgershopmanagement;
    exports db;
    opens db to javafx.fxml;
}