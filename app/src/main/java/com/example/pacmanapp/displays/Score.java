package com.example.pacmanapp.displays;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class Score {
    Digit[] digits;

    /**
     * Create score with various specified values.
     *
     * @param maxDigits int max amount of digits for score to use
     * @param scoreLayoutId int id of the layout to place digits in
     * @param context Context to create score with
     * @param activity Activity to create score in
     * @param colorId int color id to use for score
     */
    public Score(int maxDigits, int scoreLayoutId, @NotNull Context context,
                 @NotNull AppCompatActivity activity, int colorId) {
        LinearLayout scoreLayout = activity.findViewById(scoreLayoutId);
        if (scoreLayout == null) {
            throw new NullPointerException("scoreLayoutId did not provide a valid score layout.");
        }

        createDigits(maxDigits, scoreLayout, context, colorId);
    }

    /**
     * Create score with various specified values.
     *
     * @param maxDigits int max amount of digits for score to use
     * @param scoreLayout Linear layout to place digits in
     * @param context Context to create score with
     * @param colorId int color id to use for score
     */
    public Score(int maxDigits, @NotNull LinearLayout scoreLayout,
                 @NotNull Context context, int colorId) {
        createDigits(maxDigits, scoreLayout, context, colorId);
    }

    /**
     * Create the digits that display the score.
     *
     * @param maxDigits Max digits that are used to display the score
     * @param scoreLayout Layout that the score is contained in
     * @param context Context to set digits with
     */
    private void createDigits(int maxDigits, @NotNull LinearLayout scoreLayout,
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
     * Set value of the score to display.
     *
     * @param value Value to set score to
     */
    public void setValue(int value) {
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
