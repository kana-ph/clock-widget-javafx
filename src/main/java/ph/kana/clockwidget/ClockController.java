package ph.kana.clockwidget;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClockController implements Initializable {
    @FXML
    private Label localTimeLabel;

    @FXML
    private Label localDateLabel;

    @FXML
    private GridPane binariesGridPane;

    private final int BIT_COUNT = 6;
    private final Circle[][] LIGHTS = new Circle[3][BIT_COUNT];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLightsCache();
        clockAnimation()
            .start();
    }

    private void initializeLightsCache() {
        for (Node node : binariesGridPane.getChildren()) {
            var light = (Circle) node;
            var row = GridPane.getRowIndex(light);
            var col = GridPane.getColumnIndex(light);
            LIGHTS[row][col] = light;
        }
    }

    private AnimationTimer clockAnimation() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                var currentTime = LocalDateTime.now();
                updateLocalDateTime(currentTime);
                updateBinaryClock(currentTime);
            }
        };
    }

    private void updateLocalDateTime(LocalDateTime dateTime) {
        var time = String.format("%02d:%02d:%02d", dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
        localTimeLabel.setText(time);

        var month = dateTime.getMonth()
            .getDisplayName(TextStyle.FULL, Locale.getDefault());
        var date = String.format("%s %d", month, dateTime.getDayOfMonth());
        localDateLabel.setText(date);
    }

    private void updateBinaryClock(LocalDateTime time) {
        setBinaryClock(0, toBits(time.getHour()));
        setBinaryClock(1, toBits(time.getMinute()));
        setBinaryClock(2, toBits(time.getSecond()));
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
