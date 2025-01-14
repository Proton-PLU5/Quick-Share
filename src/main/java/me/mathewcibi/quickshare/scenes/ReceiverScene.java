package me.mathewcibi.quickshare.scenes;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

public class ReceiverScene extends Scene {
    private String IP = "";

    public ReceiverScene(Group root) {
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

        Label orLabel = new Label("OR");
        orLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));
        orLabel.setTextFill(Color.web("6D5D5D"));
        orLabel.setLayoutX(466);
        orLabel.setLayoutY(346);

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
            ((Stage) this.getWindow()).setScene(new SenderScene(new Group()));
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
        root.getChildren().add(orLabel);

        root.getChildren().add(receiverStack);
        root.getChildren().add(senderStack);

        root.getChildren().add(ipLabel);
        root.getChildren().add(ipInput);
        root.getChildren().add(deviceIPLabel);
    }
}
