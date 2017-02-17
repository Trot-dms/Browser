package sample.helpers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by trot on 17.02.17.
 */
public class TimeHelper {
    public static String getActualTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime parsedTime = LocalTime.parse(time.format(formatter), formatter);
        return parsedTime.toString();
    }

    public static void changeTimeLabelInterval(int seconds, Label timeLabel) {
        Timeline oneSecond = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        event -> timeLabel.setText(getActualTime())));
        oneSecond.setCycleCount(Timeline.INDEFINITE);
        oneSecond.play();
    }

}
