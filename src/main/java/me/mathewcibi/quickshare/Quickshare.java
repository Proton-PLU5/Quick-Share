package me.mathewcibi.quickshare;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.mathewcibi.quickshare.scenes.SenderScene;
import me.mathewcibi.quickshare.utils.NetworkServer;

import java.util.LinkedList;
import java.util.Queue;

public class Quickshare extends Application {
    public static final Queue<byte[]> DATA_TO_SEND = new LinkedList<>();
    public static int ITEMS_TO_SEND = 3;
    public static final Thread serverThread = new Thread(() -> {
        try {
            System.out.println("Server Started");
            new NetworkServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    @Override
    public void start(Stage primaryStage) {
        SenderScene mainScene = new SenderScene(new Group());
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Quick Share");
        primaryStage.setResizable(false);
        primaryStage.show();
        serverThread.start();
    }

    public static void appLaunch() {
        launch();
    }
}