package com.example.pacmanapp.displays;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.GameSave;

import org.jetbrains.annotations.NotNull;

public class Score {
    private final static String TAG = "Score";
    private final static int scoreValueId = R.id.scoreValue;
    private final PlayValues playValues;
    private Digit[] digits;
    private final Clock clock;

    /**
     * Create score with various specified values.
     */
    public Score(@NotNull GameSave gameSave) {
        playValues = PlayValues.getFromSave(gameSave);
        clock = new Clock(gameSave);
    }

    /**
     * Add a value to the score.
     *
     * @param value int to add to the current score value
     */
    public void addValue(int value) {
        long currentValue = playValues.getValue(scoreValueId, 0);
        setValue((int) currentValue + value);
    }

    /**
     * Set value of the score to display.
     *
     * @param value Value to set score to
     */
    public void setValue(int value) {
        if (!clock.getTimeLeft().isZero()) {
            playValues.setValue(scoreValueId, value);
        }
    }

    /**
     * Update the score display with the specified values.
     *
     * @param maxDigits Max digits to display in the score, will be cut of if larger
     * @param activity Activity to update the score display in
     * @param scoreLayoutId Id of the linear layout to add score in
     * @param colorId color id to use for the digits
     */
    public void updateDisplay(int maxDigits, int scoreLayoutId,
                              @NotNull AppCompatActivity activity, int colorId) {
        LinearLayout scoreLayout = activity.requireViewById(scoreLayoutId);
        if (scoreLayout == null) {
            throw new NullPointerException("scoreLayoutId did not provide a valid score layout.");
        }
        updateDisplay(maxDigits, scoreLayout, activity, colorId);
    }

    /**
     * Update the score display with the specified values.
     *
     * @param maxDigits Max digits to display in the score, will be cut of if larger
     * @param scoreLayout Linear layout to add digits to
     * @param context Context to create digits views with
     * @param colorId color id to use for the digits
     */
    public void updateDisplay(int maxDigits, @NotNull LinearLayout scoreLayout,
                              @NotNull Context context, int colorId) {
        // Only create the display digits once for a score.
        if (digits == null) {
            createDisplayDigits(maxDigits, scoreLayout, context, colorId);
        }
        long scoreValue = playValues.getValue(scoreValueId, 0);
        setDisplayValue((int) scoreValue);
    }

    /**
     * Create the digits that display the score.
     *
     * @param maxDigits Max digits that are used to display the score
     * @param scoreLayout Layout that the score is contained in
     * @param context Context to set digits with
     */
    private void createDisplayDigits(int maxDigits, @NotNull LinearLayout scoreLayout,
                                     @NotNull Context context, int color) {
        digits = new Digit[maxDigits];

        for (int i = 0; i < maxDigits; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMarginStart(1);
            imageView.setLayoutParams(layoutParams);
            scoreLayout.addView(imageView, 0);
            digits[i] = new Digit(context, imageView, color);
        }
    }

    /**
     * Set the display value of the digits to the specified value.
     *
     * @pre display digits are created for this score.
     * @param value Value to display with the created digits.
     */
    private void setDisplayValue(int value) {
        if (digits == null) {
            Log.w(TAG, "Can not set display value without created digits.");
            return;
        }

        for (Digit digit : digits) {
            if (value == 0) {
                digit.setVisible(false);
                continue; // Continue to set more significant digits invisible.
            }
            digit.setVisible(true);
            digit.setValue(value);
            value = value / 10;
        }
    }

}
