module ph.kana.clockwidget {
    requires javafx.controls;
    requires javafx.fxml;

    opens ph.kana.clockwidget to javafx.fxml;
    exports ph.kana.clockwidget;
}