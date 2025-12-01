module com.example.SistemaAcervo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.SistemaAcervo.app to javafx.graphics;
    opens com.example.SistemaAcervo.controller to javafx.fxml;
    opens com.example.SistemaAcervo.model to javafx.base;

    exports com.example.SistemaAcervo.app;
}