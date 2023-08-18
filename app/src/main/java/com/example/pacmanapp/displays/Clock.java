package com.example.pacmanapp.displays;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Clock {
    private final static int hourId = R.id.digit_hour;
    private final static int minuteId_0 = R.id.digit_minute_0;
    private final static int minuteId_1 = R.id.digit_minute_1;
    private final static int secondId_0 = R.id.digit_second_0;
    private final static int secondId_1 = R.id.digit_second_1;

    Digit hour;
    Digit minute_0;
    Digit minute_1;
    Digit second_0;
    Digit second_1;

    /**
     * Create clock with specified context, activity and color id.
     *
     * @param context Context to create clock with
     * @param activity Activity to create clock in
     * @param colorId int color id to use for clock
     */
    public Clock(@NotNull Context context, @NotNull AppCompatActivity activity, int colorId) {
        createDigits(context, activity, colorId);
    }

    /**
     * Create the digits of the clock
     *
     * @param context Context to create digits with
     * @param activity Activity create digits in
     * @param colorId int color id to use for digits
     */
    private void createDigits(@NotNull Context context, @NotNull AppCompatActivity activity, int colorId) {
        hour = new Digit(context, activity, hourId, colorId);
        minute_0 = new Digit(context, activity, minuteId_0, colorId);
        minute_1 = new Digit(context, activity, minuteId_1, colorId);
        second_0 = new Digit(context, activity, secondId_0, colorId);
        second_1 = new Digit(context, activity, secondId_1, colorId);
    }

    /**
     * Set time of clock to the time duration.
     *
     * @param time Time to set clock to, max displayed time is 10 hours
     */
    public void setTime(Duration time) {
        long seconds = time.getSeconds();

        // Compute hours, minutes and remaining seconds
        int hours = (int) seconds / 3600;
        int minutes = (int) (seconds % 3600) / 60;
        int remainingSeconds = (int) seconds % 60;

        // Set hour value
        if (hours == 0) {
            hour.setVisible(false);
        } else {
            hour.setVisible(true);
            hour.setValue(hours); // Set hour value
        }

        // Set minute values
        minute_0.setValue(minutes / 10); // Set significant minute value
        minute_1.setValue(minutes % 10); // Set rounding minute value

        // Set second values
        second_0.setValue(remainingSeconds / 10); // Set significant second value
        second_1.setValue(remainingSeconds % 10); // Set rounding second value
    }
}
