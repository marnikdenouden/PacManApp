package com.example.pacmanapp.displays;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

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

    public Clock(Context context, AppCompatActivity activity) {
        createDigits(context, activity);
    }

    void createDigits(Context context, AppCompatActivity activity) {
        hour = new Digit(context, activity.findViewById(hourId));
        minute_0 = new Digit(context, activity.findViewById(minuteId_0));
        minute_1 = new Digit(context, activity.findViewById(minuteId_1));
        second_0 = new Digit(context, activity.findViewById(secondId_0));
        second_1 = new Digit(context, activity.findViewById(secondId_1));
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
        hour.setValue(hours); // Set hour value

        // Set minute values
        minute_0.setValue(minutes / 10); // Set significant minute value
        minute_1.setValue(minutes % 10); // Set rounding minute value

        // Set second values
        second_0.setValue(remainingSeconds / 10); // Set significant second value
        second_1.setValue(remainingSeconds % 10); // Set rounding second value
    }
}
