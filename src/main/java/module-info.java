module me.mathewcibi.quickshare {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.mathewcibi.quickshare to javafx.fxml;
    exports me.mathewcibi.quickshare;
}