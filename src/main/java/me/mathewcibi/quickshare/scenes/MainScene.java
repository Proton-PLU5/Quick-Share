package me.mathewcibi.quickshare.scenes;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.mathewcibi.quickshare.Quickshare;
import me.mathewcibi.quickshare.utils.NetworkClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MainScene extends Scene {
    private NetworkClient clientThread;
    private String IP = "";
    private TextArea transferLogTextArea;

    private List<Circle> userToWifiDots = new ArrayList<>();
    private List<Circle> wifiToReceiverDots = new ArrayList<>();

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

        ImageView pc = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/pc_selected.png").toExternalForm());
        pc.setFitHeight(64);
        pc.setFitWidth(64);
        pc.setLayoutX(151);
        pc.setLayoutY(167);

        ImageView otherpc = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/otherpc_unselected.png").toExternalForm());
        otherpc.setFitHeight(64);
        otherpc.setFitWidth(100);
        otherpc.setLayoutX(739);
        otherpc.setLayoutY(167);

        ImageView wifi = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/wifi_selected.png").toExternalForm());
        wifi.setFitHeight(64);
        wifi.setFitWidth(64);
        wifi.setLayoutX(452);
        wifi.setLayoutY(167);

        /// Connection Type
        Label connectionTypeTitleLabel = new Label("CONNECT TO OTHER DEVICE");
        connectionTypeTitleLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));
        connectionTypeTitleLabel.setTextFill(Color.web("6D5D5D"));
        connectionTypeTitleLabel.setLayoutX(126);
        connectionTypeTitleLabel.setLayoutY(257);

        Line connectionTypeLine = new Line(126, 289, 126+716, 289);
        connectionTypeLine.setStrokeWidth(3);
        connectionTypeLine.setStroke(Color.web("9D9D9D"));

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

        /// IP INPUT
        Label ipLabel = new Label("ENTER IP");
        ipLabel.setTextFill(Color.web("6D5D5D"));
        ipLabel.setLayoutX(226);
        ipLabel.setLayoutY(308);
        ipLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        TextField ipInput = new TextField();
        ipInput.setStyle("-fx-text-fill: #000000;");
        ipInput.setPromptText("IP Address");
        ipInput.setBackground(new Background(new BackgroundFill(Color.web("D9D9D9"), new CornerRadii(5), new Insets(5))));
        ipInput.setLayoutX(339);
        ipInput.setLayoutY(306);
        ipInput.setPrefWidth(282);
        ipInput.setPrefHeight(32);
        ipInput.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 20));
        ipInput.textProperty().addListener((observable, oldValue, newValue) -> {
            IP = newValue;
        });

        Rectangle connectionButton = new Rectangle(131, 32, Color.web("323131"));
        connectionButton.setArcHeight(10);
        connectionButton.setArcWidth(10);
        connectionButton.setEffect(dropShadow);

        Label connectionLabel = new Label("CONNECT");
        connectionLabel.setTextFill(Color.WHITE);
        connectionLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 16));

        StackPane connectionStack = new StackPane(connectionButton, connectionLabel);
        connectionStack.setLayoutX(630);
        connectionStack.setLayoutY(308);
        connectionStack.setOnMouseClicked(event -> {
            System.out.println("Connect Button Clicked");
            try {
                if (clientThread != null) {
                    if (clientThread.isAlive()) {
                        try {
                            clientThread.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        clientThread.interrupt();

                        for (Circle dot : wifiToReceiverDots) {
                            dot.setFill(Color.BLACK);
                        }

                        otherpc.setImage(new Image(this.getClass().getResource("/me/mathewcibi/quickshare/images/otherpc_unselected.png").toExternalForm()));

                        connectionButton.setFill(Color.web("323131"));
                        connectionLabel.setText("CONNECT");
                    } else {
                        clientThread = new NetworkClient(IP);
                        for (Circle dot : wifiToReceiverDots) {
                            dot.setFill(Color.web("0FB600"));
                        }
                        clientThread.start();
                        otherpc.setImage(new Image(this.getClass().getResource("/me/mathewcibi/quickshare/images/otherpc_selected.png").toExternalForm()));

                        connectionButton.setFill(Color.web("004BC3"));
                        connectionLabel.setText("CONNECTED");
                    }
                } else {
                    clientThread = new NetworkClient(IP);
                    for (Circle dot : wifiToReceiverDots) {
                        dot.setFill(Color.web("0FB600"));
                    }
                    clientThread.start();
                    otherpc.setImage(new Image(this.getClass().getResource("/me/mathewcibi/quickshare/images/otherpc_selected.png").toExternalForm()));

                    connectionButton.setFill(Color.web("004BC3"));
                    connectionLabel.setText("CONNECTED");
                }

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }


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

        double sendOptionsOffset = -20;

        // Send Options
        Label whatToSendLabel = new Label("WHAT DO YOU WANT TO SEND?");
        whatToSendLabel.setTextFill(Color.web("6D5D5D"));
        whatToSendLabel.setLayoutX(126);
        whatToSendLabel.setLayoutY(356 - sendOptionsOffset); // 361 - 5 - sendOptionsOffset
        whatToSendLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        Line whatToSendLine = new Line(126, 388 - sendOptionsOffset, 126 + 716, 388 - sendOptionsOffset); // 393 - 5 - sendOptionsOffset
        whatToSendLine.setStrokeWidth(3);
        whatToSendLine.setStroke(Color.web("9D9D9D"));

        /// File Button
        Rectangle fileButtonBackground = new Rectangle(100, 100, Color.web("323131"));
        fileButtonBackground.setArcHeight(10);
        fileButtonBackground.setArcWidth(10);
        fileButtonBackground.setEffect(dropShadow);

        ImageView fileButtonIcon = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/file.png").toExternalForm());
        fileButtonIcon.setFitHeight(57);
        fileButtonIcon.setFitWidth(57);
        fileButtonIcon.setTranslateY(-7);

        Label fileButtonLabel = new Label("FILE");
        fileButtonLabel.setTextFill(Color.WHITE);
        fileButtonLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 16));
        fileButtonLabel.setTranslateY(33);

        StackPane fileButtonPane = new StackPane(fileButtonBackground, fileButtonIcon, fileButtonLabel);
        fileButtonPane.setLayoutX(126);
        fileButtonPane.setLayoutY(403 - sendOptionsOffset); // 408 - 5 - sendOptionsOffset

        fileButtonPane.setOnMouseClicked(event -> {
            System.out.println("File Button Clicked");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File to Send");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            File file = fileChooser.showOpenDialog(this.getWindow());
            if (file != null) {
                openFile(file, false);
            }
        });

        /// Folder Button
        Rectangle folderButtonBackground = new Rectangle(100, 100, Color.web("323131"));
        folderButtonBackground.setArcHeight(10);
        folderButtonBackground.setArcWidth(10);
        folderButtonBackground.setEffect(dropShadow);

        ImageView folderButtonIcon = new ImageView(this.getClass().getResource("/me/mathewcibi/quickshare/images/folder.png").toExternalForm());
        folderButtonIcon.setFitHeight(57);
        folderButtonIcon.setFitWidth(57);
        folderButtonIcon.setTranslateY(-7);

        Label folderButtonLabel = new Label("FOLDER");
        folderButtonLabel.setTextFill(Color.WHITE);
        folderButtonLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 16));
        folderButtonLabel.setTranslateY(33);

        StackPane folderButtonPane = new StackPane(folderButtonBackground, folderButtonIcon, folderButtonLabel);
        folderButtonPane.setLayoutX(247);
        folderButtonPane.setLayoutY(403 - sendOptionsOffset); // 408 - 5 - sendOptionsOffset

        folderButtonPane.setOnMouseClicked(event -> {
            System.out.println("File Button Clicked");

            openDirectory();
        });

        /// Transfer Log
        Label transferLogLabel = new Label("TRANSFER LOG");
        transferLogLabel.setTextFill(Color.web("D9D9D9"));
        transferLogLabel.setLayoutX(391);
        transferLogLabel.setLayoutY(403 - sendOptionsOffset); // 408 - 5 - sendOptionsOffset
        transferLogLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        Line transferLogLine = new Line(368, 435 - sendOptionsOffset, 368 + 474, 435 - sendOptionsOffset); // 440 - 5 - sendOptionsOffset
        transferLogLine.setStrokeWidth(3);
        transferLogLine.setStroke(Color.web("9D9D9D"));

        Rectangle transferLogBackground = new Rectangle(474, 180, Color.web("212121"));
        transferLogBackground.setLayoutX(368);
        transferLogBackground.setLayoutY(402 - sendOptionsOffset); // 407 - 5 - sendOptionsOffset
        transferLogBackground.setArcHeight(10);
        transferLogBackground.setArcWidth(10);

        transferLogTextArea = new TextArea();
        transferLogTextArea.getStyleClass().add("textarea");
        transferLogTextArea.setLayoutX(391);
        transferLogTextArea.setLayoutY(441 - sendOptionsOffset); // 446 - 5 - sendOptionsOffset
        transferLogTextArea.setPrefWidth(431);
        transferLogTextArea.setPrefHeight(141);
        transferLogTextArea.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 12));
        transferLogTextArea.setStyle("-fx-text-fill: #FFFFFF;");
        transferLogTextArea.setEditable(false);

        // Credit Label
        Label creditLabel = new Label("CREATED BY MATHEW CIBI");
        creditLabel.setTextFill(Color.web("6D5D5D"));
        creditLabel.setLayoutX(688-50);
        creditLabel.setLayoutY(732);
        creditLabel.setFont(Font.loadFont(this.getClass().getResourceAsStream("/me/mathewcibi/quickshare/fonts/Inter-SemiBold.ttf"), 24));

        // Add stopping mechanic
        Platform.runLater(() -> {
            this.getWindow().setOnCloseRequest(event -> {
                if (clientThread != null) {
                    try {
                        clientThread.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    clientThread.interrupt();
                }
            });
        });


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
                if (y == 0) {
                    dotCircle.setFill(Color.web("0FB600"));
                }

                boolean b = y == 0 ? userToWifiDots.add(dotCircle) : wifiToReceiverDots.add(dotCircle);
            }
        }

        root.getChildren().add(connectionStatusTitleLabel);
        root.getChildren().add(connectionStatusLine);
        root.getChildren().add(connectionTypeTitleLabel);
        root.getChildren().add(connectionTypeLine);
        root.getChildren().add(pc);
        root.getChildren().add(otherpc);
        root.getChildren().add(wifi);

        root.getChildren().add(ipLabel);
        root.getChildren().add(ipInput);
        root.getChildren().add(connectionStack);
        root.getChildren().add(deviceIPLabel);

        root.getChildren().add(whatToSendLabel);
        root.getChildren().add(whatToSendLine);
        root.getChildren().add(fileButtonPane);
        root.getChildren().add(folderButtonPane);

        root.getChildren().add(transferLogBackground);
        root.getChildren().add(transferLogLabel);
        root.getChildren().add(transferLogLine);
        root.getChildren().add(transferLogTextArea);

        root.getChildren().add(creditLabel);
    }

    private void openDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory to send");
        File selectedDirectory = directoryChooser.showDialog(this.getWindow());
        transferLogTextArea.setText(transferLogTextArea.getText() + "Compressing directory: " + selectedDirectory.getName() + "\n");
        // Compress directory to zip
        Path zipPath = null;
        try {
            zipPath = Files.createFile(Paths.get(selectedDirectory.getAbsolutePath() + ".zip"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Path pp = Paths.get(selectedDirectory.getAbsolutePath());
            Files.walk(selectedDirectory.toPath())
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                            ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                            try {
                                zs.putNextEntry(zipEntry);
                                Files.copy(path, zs);
                                zs.closeEntry();
                            } catch (IOException e) {
                                System.err.println(e);
                            }
                    });
            transferLogTextArea.setText(transferLogTextArea.getText() + "Compressed directory: " + selectedDirectory.getName() + "\n");

            // Send Zip
            transferLogTextArea.setText(transferLogTextArea.getText() + "Sending directory: " + selectedDirectory.getName() + "\n");
            openFile(zipPath.toFile(), true);
            transferLogTextArea.setText(transferLogTextArea.getText() + "Sent directory: " + selectedDirectory.getName() + "\n");

            // Delete Zip
            Files.delete(zipPath);
        } catch (IOException e) {
            transferLogTextArea.setText(transferLogTextArea.getText() + "Error compressing directory: " + selectedDirectory.getName() + "\n");
        }
    }

    private void openFile(File file, boolean isZip) {
        byte[] fileData = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            fileInputStream.read(fileData);

            byte[] isZipBytes = isZip ? "true".getBytes() : "false".getBytes();

            Quickshare.ITEMS_TO_SEND = 1;
            synchronized (Quickshare.DATA_TO_SEND) {
                Quickshare.DATA_TO_SEND.add(isZipBytes);
                Quickshare.DATA_TO_SEND.add(file.getName().getBytes());
                Quickshare.DATA_TO_SEND.add(fileData);
                Quickshare.DATA_TO_SEND.notifyAll();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public TextArea getTransferLogArea() {
        return transferLogTextArea;
    }

    public void disconnectedFromReceiver() {
        for (Circle dot : wifiToReceiverDots) {
            dot.setFill(Color.BLACK);
        }
    }
}
