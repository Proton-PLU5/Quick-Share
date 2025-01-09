package me.mathewcibi.quickshare.scenes;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import me.mathewcibi.quickshare.utils.NetworkClient;
import me.mathewcibi.quickshare.utils.NetworkServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainScene extends Scene {
    private String IP = "";

    public MainScene(Group root) {
        super(root);
        this.setFill(Color.TRANSPARENT);

        // Create backgrounds
        Rectangle background = new Rectangle(1024, 768, Color.web("2C2C2C"));
        Rectangle headerBackground = new Rectangle(1024, 100, Color.web("1E1E1E"));

        // Title
        Label titleLabel = new Label("Quick Share");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(10);
        titleLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 64));

        // Receiver and Sender Buttons
        Rectangle receiverButton = new Rectangle(200, 50, Color.web("1E1E1E"));

        Label receiverLabel = new Label("Receiver");
        receiverLabel.setTextFill(Color.WHITE);
        receiverLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        StackPane receiverStack = new StackPane(receiverButton, receiverLabel);
        receiverStack.setLayoutX(10);
        receiverStack.setLayoutY(110);

        receiverStack.setOnMouseClicked(event -> {
            System.out.println("Receiver Button Clicked");
            Runnable server = () -> {
                try {
                    System.out.println("Server Started");
                    new NetworkServer();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread serverThread = new Thread(server);
            serverThread.start();
        });

        Rectangle senderButton = new Rectangle(200, 50, Color.web("1E1E1E"));

        Label senderLabel = new Label("Sender");
        senderLabel.setTextFill(Color.WHITE);
        senderLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        StackPane senderStack = new StackPane(senderButton, senderLabel);
        senderStack.setLayoutX(220);
        senderStack.setLayoutY(110);

        senderStack.setOnMouseClicked(event -> {
            System.out.println("Sender Button Clicked");
            Runnable client = () -> {
                try {
                    System.out.println("Client Started with IP: " + IP);
                    new NetworkClient(IP);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread clientThread = new Thread(client);
            clientThread.start();
        });

        // IP Input
        Label ipLabel = new Label("IP:");
        ipLabel.setTextFill(Color.WHITE);
        ipLabel.setLayoutX(10);
        ipLabel.setLayoutY(170);
        ipLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        TextArea ipInput = new TextArea();
        ipInput.setLayoutX(50);
        ipInput.setLayoutY(170);
        ipInput.setPrefWidth(200);
        ipInput.setMaxHeight(20);
        ipInput.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 18));
        ipInput.textProperty().addListener((observable, oldValue, newValue) -> {
            IP = newValue;
        });

        // Device IP
        String ip = "ERROR GETTING IP";
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            ip = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Label deviceIPLabel = new Label("Device IP: " + ip);
        deviceIPLabel.setTextFill(Color.WHITE);
        deviceIPLabel.setLayoutX(10);
        deviceIPLabel.setLayoutY(732);
        deviceIPLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));


        // Add all elements to the root
        root.getChildren().add(background);
        root.getChildren().add(headerBackground);
        root.getChildren().add(titleLabel);
        root.getChildren().add(receiverStack);
        root.getChildren().add(senderStack);
        root.getChildren().add(ipLabel);
        root.getChildren().add(ipInput);
        root.getChildren().add(deviceIPLabel);
    }
}
