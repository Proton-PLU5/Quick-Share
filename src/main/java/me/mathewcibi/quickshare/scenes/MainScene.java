package me.mathewcibi.quickshare.scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import me.mathewcibi.quickshare.utils.NetworkClient;
import me.mathewcibi.quickshare.utils.NetworkServer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class MainScene extends Scene {
    private String IP = "";

    public MainScene(Group root) {
        super(root);
        this.setFill(Color.TRANSPARENT);
        this.getStylesheets().add(this.getClass().getResource("/me/mathewcibi/quickshare/stylesheets/textarea.css").toExternalForm());

        // Create backgrounds
        Rectangle background = new Rectangle(962, 768, Color.web("2C2C2C"));
        Rectangle headerBackground = new Rectangle(962, 100, Color.web("212121"));

        // Title
        Label titleLabel = new Label("Quick Share");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setPrefSize(961-30, 100);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setLayoutX(24);
        titleLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 48));

        /// Connection Status
        Label connectionStatusTitleLabel = new Label("CONNECTION STATUS");
        connectionStatusTitleLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));
        connectionStatusTitleLabel.setTextFill(Color.web("6D5D5D"));
        connectionStatusTitleLabel.setLayoutX(126);
        connectionStatusTitleLabel.setLayoutY(114);

        Line connectionStatusLine = new Line(126, 146, 126+716, 146);
        connectionStatusLine.setStrokeWidth(3);
        connectionStatusLine.setStroke(Color.web("9D9D9D"));

        ImageView pc = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/pc_unselected.png").toExternalForm());
        pc.setFitHeight(64);
        pc.setFitWidth(64);
        pc.setLayoutX(151);
        pc.setLayoutY(167);

        ImageView otherpc = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/otherpc_unselected.png").toExternalForm());
        otherpc.setFitHeight(64);
        otherpc.setFitWidth(100);
        otherpc.setLayoutX(739);
        otherpc.setLayoutY(167);

        ImageView wifi = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/wifi_unselected.png").toExternalForm());
        wifi.setFitHeight(64);
        wifi.setFitWidth(64);
        wifi.setLayoutX(452);
        wifi.setLayoutY(167);

        /// Connection Type
        Label connectionTypeTitleLabel = new Label("SELECT CONNECTION TYPE");
        connectionTypeTitleLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));
        connectionTypeTitleLabel.setTextFill(Color.web("6D5D5D"));
        connectionTypeTitleLabel.setLayoutX(126);
        connectionTypeTitleLabel.setLayoutY(257);

        Line connectionTypeLine = new Line(126, 289, 126+716, 289);
        connectionTypeLine.setStrokeWidth(3);
        connectionTypeLine.setStroke(Color.web("9D9D9D"));

        // Receiver and Sender Buttons
        DropShadow dropShadow = new DropShadow();
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.WHITE.deriveColor(1,1,1,0.5));
        innerShadow.setRadius(10);
        innerShadow.setOffsetX(0);
        innerShadow.setOffsetY(0);

        dropShadow.setInput(innerShadow);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setSpread(2);
        dropShadow.setRadius(4);
        dropShadow.setOffsetX(-4);
        dropShadow.setOffsetY(4);
        dropShadow.setColor(Color.BLACK.deriveColor(1,1,1,0.25));

        Rectangle receiverButton = new Rectangle(291, 111, Color.web("323232"));
        receiverButton.setArcHeight(10);
        receiverButton.setArcWidth(10);
        receiverButton.setEffect(dropShadow);

        Label receiverLabel = new Label("Receiver");
        receiverLabel.setTextFill(Color.WHITE);
        receiverLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter.ttf"), 24));

        StackPane receiverStack = new StackPane(receiverButton, receiverLabel);
        receiverStack.setLayoutX(126);
        receiverStack.setLayoutY(304);

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

        Rectangle senderButton = new Rectangle(291, 111, Color.web("004BC3"));
        senderButton.setArcHeight(10);
        senderButton.setArcWidth(10);
        senderButton.setEffect(dropShadow);

        Label senderLabel = new Label("Sender");
        senderLabel.setTextFill(Color.WHITE);
        senderLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter.ttf"), 24));

        StackPane senderStack = new StackPane(senderButton, senderLabel);
        senderStack.setLayoutX(551);
        senderStack.setLayoutY(304);

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

        /// IP INPUT
        Label ipLabel = new Label("ENTER IP");
        ipLabel.setTextFill(Color.web("6D5D5D"));
        ipLabel.setLayoutX(226);
        ipLabel.setLayoutY(432);
        ipLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        TextField ipInput = new TextField();
        ipInput.setStyle("-fx-text-fill: #000000;");
        ipInput.setPromptText("IP Address");
        ipInput.setBackground(new Background(new BackgroundFill(Color.web("D9D9D9"), new CornerRadii(5), new Insets(5))));
        ipInput.setLayoutX(339);
        ipInput.setLayoutY(430);
        ipInput.setPrefWidth(282);
        ipInput.setPrefHeight(32);
        ipInput.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 20));
        ipInput.textProperty().addListener((observable, oldValue, newValue) -> {
            IP = newValue;
        });

        /// Communication Log
        Rectangle communicationLogBackground = new Rectangle(757, 223, Color.web("D9D9D9"));
        communicationLogBackground.setArcHeight(10);
        communicationLogBackground.setArcWidth(10);
        communicationLogBackground.setLayoutX(102);
        communicationLogBackground.setLayoutY(490);

        Label communicationLogTitleLabel = new Label("COMMUNICATION LOG");
        communicationLogTitleLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));
        communicationLogTitleLabel.setTextFill(Color.web("6D5D5D"));
        communicationLogTitleLabel.setLayoutX(126);
        communicationLogTitleLabel.setLayoutY(496);

        Line communicationLogLine = new Line(103, 528, 103+755, 528);
        communicationLogLine.setStrokeWidth(3);
        communicationLogLine.setStroke(Color.web("9D9D9D"));

        TextArea communicationLog = new TextArea();
        communicationLog.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 16));
        communicationLog.setLayoutX(126);
        communicationLog.setLayoutY(535);
        communicationLog.setPrefSize(713, 133);
        communicationLog.setWrapText(true);
        communicationLog.setMaxSize(713, 133);
        communicationLog.setEditable(false);

        // Device IP
        String ip = "ERROR GETTING IP";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Label deviceIPLabel = new Label("DEVICE IP: " + ip);
        deviceIPLabel.setTextFill(Color.web("6D5D5D"));
        deviceIPLabel.setLayoutX(10);
        deviceIPLabel.setLayoutY(732);
        deviceIPLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        /// Message Bar
        Rectangle messageBarBackground = new Rectangle(758, 32, Color.web("383A40"));
        messageBarBackground.setLayoutX(102);
        messageBarBackground.setLayoutY(681);
        messageBarBackground.setArcWidth(10);
        messageBarBackground.setArcHeight(10);
        messageBarBackground.setEffect(dropShadow);

        TextField messageBar = new TextField();
        messageBar.setPrefSize(685,32);
        messageBar.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 16));
        messageBar.setStyle("-fx-text-fill: #9EA4B5;");
        messageBar.setPromptText("Type a message...");
        messageBar.setLayoutX(135);
        messageBar.setLayoutY(681);
        messageBar.setBackground(Background.EMPTY);


        // Add all elements to the root
        root.getChildren().add(background);
        root.getChildren().add(headerBackground);
        root.getChildren().add(titleLabel);

        for (int y= 0; y < 2; y++) {
            int initialX = y == 0 ? 247 : 544;
            for (int x = 0; x < 6; x++) {
                Circle dotCircle = new Circle(6, Color.BLACK);
                dotCircle.setLayoutX(initialX + (x * (21+12)));
                dotCircle.setLayoutY(193);
                root.getChildren().add(dotCircle);
            }
        }


        root.getChildren().add(connectionStatusTitleLabel);
        root.getChildren().add(connectionStatusLine);
        root.getChildren().add(connectionTypeTitleLabel);
        root.getChildren().add(connectionTypeLine);
        root.getChildren().add(pc);
        root.getChildren().add(otherpc);
        root.getChildren().add(wifi);

        root.getChildren().add(receiverStack);
        root.getChildren().add(senderStack);

        root.getChildren().add(communicationLogBackground);
        root.getChildren().add(communicationLogTitleLabel);
        root.getChildren().add(communicationLogLine);
        root.getChildren().add(communicationLog);

        root.getChildren().add(ipLabel);
        root.getChildren().add(ipInput);
        root.getChildren().add(deviceIPLabel);

        root.getChildren().add(messageBarBackground);
        root.getChildren().add(messageBar);
    }
}
