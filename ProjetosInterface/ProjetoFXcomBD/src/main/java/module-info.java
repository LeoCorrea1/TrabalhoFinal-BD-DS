module com.example.projetofxcombd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.projetofxcombd.controller to javafx.fxml;
    exports com.example.projetofxcombd;


}