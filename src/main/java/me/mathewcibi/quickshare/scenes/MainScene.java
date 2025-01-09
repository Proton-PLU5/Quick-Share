package me.mathewcibi.quickshare.scenes;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainScene extends Scene {
    public MainScene(Group root) {
        super(root);
        this.setFill(Color.TRANSPARENT);

        // Create background
        Rectangle background = new Rectangle(320, 240, Color.web("2C2C2C"));

        root.getChildren().add(background);
    }
}
