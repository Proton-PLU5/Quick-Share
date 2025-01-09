package me.mathewcibi.quickshare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.mathewcibi.quickshare.scenes.MainScene;
import me.mathewcibi.quickshare.utils.NetworkClient;

import java.io.IOException;

public class Quickshare extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainScene mainScene = new MainScene(new Group());
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Quick Share");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void appLaunch() {
        launch();
    }
}