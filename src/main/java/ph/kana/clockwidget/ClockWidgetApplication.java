package ph.kana.clockwidget;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ClockWidgetApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClockWidgetApplication.class.getResource("clock-widget.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Clock by kana.ph");
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);

        stage.initStyle(StageStyle.UTILITY);
        stage.show();
        stage.setResizable(false);
        stage.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}
