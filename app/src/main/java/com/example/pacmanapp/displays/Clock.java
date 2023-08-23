package com.example.pacmanapp.displays;

import android.os.SystemClock;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.GameSave;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Clock {
    private final static String TAG = "Clock";
    private final static int startTimeId = R.id.startTime;
    private final static int timeDurationId = R.id.timeDuration;
    private final PlayValues playValues;

    /**
     * Utilize chronometer clock within specified context, activity and color id.
     *
     * @param gameSave GameSave to store start time in
     */
    public Clock(@NotNull GameSave gameSave) {
        playValues = PlayValues.getFromSave(gameSave);
    }

    /**
     * Set time of clock to the specified time duration.
     *
     * @param time Time to set clock to, max displayed time is 10 hours
     */
    public void setTime(Duration time) {
        resetClock();
        long timeDuration = time.getSeconds() * 1000;
        playValues.setValue(timeDurationId, timeDuration);
    }

    /**
     * Reset the start time of the clock to now.
     */
    public void resetClock() {
        long currentTime = SystemClock.elapsedRealtime();
        playValues.setValue(startTimeId, currentTime);
    }

    /**
     * Update the display of the clock in the specified activity.
     *
     * @param activity Activity to update clock in
     * @param colorId int color id to use for clock display
     */
    public void updateDisplay(@NotNull AppCompatActivity activity, int colorId) {
        Chronometer chronometer = activity.findViewById(R.id.clock);
        int color = ResourcesCompat.getColor(activity.getResources(), colorId, activity.getTheme());
        chronometer.setTextColor(color);
        chronometer.setCountDown(true);

        long startTime = playValues.getValue(startTimeId, 0);
        long timeDuration = playValues.getValue(timeDurationId, 0);
        long currentTime = SystemClock.elapsedRealtime();

        long elapsedTime = currentTime - startTime;
        long displayTime = Math.max(0, timeDuration - elapsedTime);

        chronometer.setBase(SystemClock.elapsedRealtime() + displayTime);
        chronometer.start();
    }

}
