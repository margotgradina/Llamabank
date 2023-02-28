package com.dellama.bank.pinterminal.launcher;

import com.dellama.bank.pinterminal.model.ModelServiceImpl;
import com.dellama.bank.pinterminal.model.PinMachine;
import com.dellama.bank.pinterminal.model.service.ModelService;
import com.dellama.bank.pinterminal.view.SceneManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static ModelService modelService = new ModelServiceImpl();
    private static PinMachine pinMachine = null;
    private static SceneManager sceneManager = null;
    private static Stage primaryStage = null;

    public static void main(String[] args) {
        launch();
    }

    public static ModelService getModelService() {
        return modelService;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.primaryStage = primaryStage;
        primaryStage.setTitle("PIN MACHINE");
        getSceneManager().showScene();
        primaryStage.show();
    }

    public static SceneManager getSceneManager() {
        if (sceneManager == null) {
            sceneManager = new SceneManager(primaryStage);
        }
        return sceneManager;
    }

    public static PinMachine getPinMachine(){
        if (pinMachine == null){
           pinMachine = new PinMachine();
            //TODO HAALT PINMACHINE GEGEVENS OP UIT PERSISTENCY
        }
        return  pinMachine;
    }

}