package me.mathewcibi.quickshare;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.mathewcibi.quickshare.scenes.MainScene;
import me.mathewcibi.quickshare.utils.CryptographyUtils;
import me.mathewcibi.quickshare.utils.NetworkServer;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;

public class Quickshare extends Application {
    public static final Queue<byte[]> DATA_TO_SEND = new LinkedList<>();
    public static int ITEMS_TO_SEND = 0;
    public static NetworkServer serverThread = null;
    public static final CryptographyUtils CRYPTOGRAPHY_UTILS;

    static {
        try {
            CRYPTOGRAPHY_UTILS = new CryptographyUtils();
            CRYPTOGRAPHY_UTILS.generateSymmetricKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        MainScene mainScene = new MainScene(new Group());
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Quick Share");
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.getIcons().add(new Image(this.getClass().getResource("/me/mathewcibi/quickshare/quickshare_logo.png").toExternalForm()));

        Platform.runLater(() -> {
            mainScene.getWindow().setOnCloseRequest(event -> {                serverThread.running = false;
                serverThread.interrupt();
                Platform.exit();
                System.exit(0);
            });
        });
        
        serverThread = new NetworkServer(mainScene.getTransferLogArea());
        serverThread.start();
    }

    public static void appLaunch() {
        launch();
    }
}