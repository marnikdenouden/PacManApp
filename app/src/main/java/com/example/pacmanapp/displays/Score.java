package com.example.pacmanapp.displays;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class Score {
    Digit[] digits;

    public Score(int maxDigits, int scoreLayoutId, Context context, AppCompatActivity activity) {
        LinearLayout scoreLayout = activity.findViewById(scoreLayoutId);
        if (scoreLayout == null) {
            throw new NullPointerException("scoreLayoutId did not provide a valid score layout.");
        }

        createDigits(maxDigits, scoreLayout, context);
    }

    public Score(int maxDigits, @NotNull LinearLayout scoreLayout, Context context) {
        createDigits(maxDigits, scoreLayout, context);
    }

    /**
     * Create the digits that display the score.
     *
     * @param maxDigits Max digits that are used to display the score
     * @param scoreLayout Layout that the score is contained in
     * @param context Context to set digits with
     */
    private void createDigits(int maxDigits, @NotNull LinearLayout scoreLayout, Context context) {
        digits = new Digit[maxDigits];

        for (int i = 0; i < maxDigits; i++) {
            ImageView imageView = new ImageView(context);
            int digitSize = (int) context.getResources().getDimension(R.dimen.digitSize);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(digitSize, digitSize);
            layoutParams.setMarginStart(4);
            imageView.setLayoutParams(layoutParams);
            scoreLayout.addView(imageView, 0);
            digits[i] = new Digit(context, imageView);
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
