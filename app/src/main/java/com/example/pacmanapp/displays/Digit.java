package com.example.pacmanapp.displays;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class Digit {
    private ImageView imageView;

    private Map<Number, Drawable> numberDrawableMap;

    Digit(Context context, AppCompatActivity activity, int digitId) {
        this(context, activity.findViewById(digitId));
    }
// TODO allow color of digit to be set, so clock and score can be distinct
    Digit(Context context, ImageView imageView) {
        this.imageView = imageView;
        createNumberDrawableMap(context);
    }

    /**
     * Create number drawable map for the digit.
     *
     * @param context Context to get drawable with
     */
    private void createNumberDrawableMap(Context context) {
        numberDrawableMap = new HashMap<>();

        for (Number number: Number.values()) {
            numberDrawableMap.put(number, number.getDrawable(context));
        }

    }

    /**
     * Set number value of the digit.
     *
     * @param number Number to set digit to
     */
    public void setValue(Number number) {
        imageView.setImageDrawable(numberDrawableMap.get(number));
    }

    /**
     * Set number value of the digit.
     *
     * @param value Value to set digit to
     */
    public void setValue(int value) {
        setValue(Number.getNumber(value));
    }

    /**
     * Set visibility of the digit.
     *
     * @param visible Boolean to set digit visible or not
     */
    public void setVisible(boolean visible) {
        if (visible) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }


}
