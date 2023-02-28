package com.dellama.bank.pinterminal.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.geometry.Pos.TOP_CENTER;

public class SceneManager {

    private Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Loads a scene
    public FXMLLoader getScene(String fxml) {
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
            Parent root = loader.load();
            scene = new Scene(root);
            primaryStage.setScene(scene);
            return loader;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    public void showScene() {
        getScene("/fxml/pin-terminal-view.fxml");
    }

    public void createJournal(String string) {
        Text text = new Text(string);
        ScrollPane sp = new ScrollPane();
        sp.setContent(text);
        Stage secondStage = new Stage();
        Scene scene = new Scene(sp, 300, 600);
        BorderPane.setAlignment(text, TOP_CENTER);
        BorderPane borderPane = new BorderPane(text);
        secondStage.setScene(scene);
        secondStage.setTitle("Journal");
        secondStage.show();

    }
}

