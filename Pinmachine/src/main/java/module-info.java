module com.dellama.bank.pinterminal {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires okhttp3;
    requires okio;
    
    requires java.net.http;

    exports com.dellama.bank.pinterminal.launcher;
    opens com.dellama.bank.pinterminal.launcher to javafx.fxml;
    exports com.dellama.bank.pinterminal.controller;
    opens com.dellama.bank.pinterminal.controller to javafx.fxml;
    opens com.dellama.bank.pinterminal.view to javafx.graphics, javafx.fxml;
    exports com.dellama.bank.pinterminal.dto;
    opens com.dellama.bank.pinterminal.dto to javafx.fxml;
}