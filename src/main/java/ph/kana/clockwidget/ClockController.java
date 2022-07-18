package ph.kana.clockwidget;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.BitSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClockController implements Initializable {
    @FXML
    private Label localTimeLabel;

    @FXML
    private Label localDateLabel;

    @FXML
    private Pane rootPane;

    @FXML
    private GridPane binariesGridPane;

    private boolean appRunning = true;
    private final int BIT_COUNT = 6;
    private final Circle[][] LIGHTS = new Circle[3][BIT_COUNT];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCloseEvent();
        initializeLightsCache();
        clockThread().start();
    }

    private void initializeCloseEvent() {
        Platform.runLater(() ->
            rootPane.getScene()
                .getWindow()
                .setOnCloseRequest(windowEvent -> {
                    appRunning = false;
                    Platform.exit();
                })
        );
    }

    private void initializeLightsCache() {
        for (Node node : binariesGridPane.getChildren()) {
            var light = (Circle) node;
            var row = GridPane.getRowIndex(light);
            var col = GridPane.getColumnIndex(light);
            LIGHTS[row][col] = light;
        }
    }

    private Thread clockThread() {
        return new Thread(() -> {
            try {
                while (appRunning) {
                    var currentTime = LocalDateTime.now();
                    Platform.runLater(updateLocalDateTime(currentTime));
                    Platform.runLater(updateBinaryClock(currentTime));
                    Thread.sleep(1000L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }, "clock-thread");
    }

    private Runnable updateLocalDateTime(LocalDateTime dateTime) {
        return () -> {
            var time = String.format("%02d:%02d:%02d", dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
            localTimeLabel.setText(time);

            var month = dateTime.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.getDefault());
            var date = String.format("%s %d", month, dateTime.getDayOfMonth());
            localDateLabel.setText(date);
        };
    }

    private Runnable updateBinaryClock(LocalDateTime time) {
        return () -> {
            setBinaryClock(0, toBits(time.getHour()));
            setBinaryClock(1, toBits(time.getMinute()));
            setBinaryClock(2, toBits(time.getSecond()));
        };
    }

    private boolean[] toBits(int n) {
        byte b = (byte) n;
        boolean[] result = new boolean[BIT_COUNT];
        for (int i = 0; i < BIT_COUNT; i++) {
            result[i] = (b & (1 << i)) != 0;
        }
        return result;
    }

    private void setBinaryClock(int row, boolean[] lights) {
        for (int i = 0; i < BIT_COUNT; i++) {
            var circle = LIGHTS[row][i];
            var lightCssClasses = circle.getStyleClass();
            var lightClass = lights[i]?
                "light-on" : "light-off";
            lightCssClasses.removeIf(sc -> sc.startsWith("light-"));
            lightCssClasses.add(lightClass);
        }
    }
}
