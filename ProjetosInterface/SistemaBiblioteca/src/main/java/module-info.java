module com.example.SistemaBiblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.SistemaBiblioteca.controller to javafx.fxml;
    exports com.example.SistemaBiblioteca;


}