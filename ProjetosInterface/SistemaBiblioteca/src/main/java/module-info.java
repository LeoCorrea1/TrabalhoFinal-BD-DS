module com.example.SistemaBiblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.SistemaBiblioteca.controller to javafx.fxml;
    opens com.example.SistemaBiblioteca.app to javafx.fxml;
    opens com.example.SistemaBiblioteca.model to javafx.base, javafx.fxml;

    exports com.example.SistemaBiblioteca.app;
}
