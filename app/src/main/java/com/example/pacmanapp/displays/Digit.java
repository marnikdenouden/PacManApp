package com.example.pacmanapp.displays;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class Digit {
    private final ImageView imageView;

    private Map<Number, Drawable> numberDrawableMap;

    /**
     * Create digit for the specified context, image view and color id.
     *
     * @param context Context to create digits with
     * @param activity Activity to create digits in
     * @param digitId int Id of view to create digits in
     * @param colorId int color id to use for the digits
     */
    Digit(Context context, AppCompatActivity activity, int digitId, int colorId) {
        this(context, activity.findViewById(digitId), colorId);
    }

    /**
     * Create digit for the specified context, image view and color id.
     *
     * @param context Context to create digits with
     * @param imageView Imageview to create digits in
     * @param colorId int color id to use for the digits
     */
    Digit(Context context, ImageView imageView, int colorId) {
        this.imageView = imageView;
        createNumberDrawableMap(context, colorId);
    }

    /**
     * Create number drawable map for the digit.
     *
     * @param context Context to get drawable with
     * @param colorId Int color id to set for drawables
     */
    private void createNumberDrawableMap(Context context, int colorId) {
        numberDrawableMap = new HashMap<>();

        for (Number number: Number.values()) {
            Drawable drawable = number.getDrawable(context);
            int color = context.getResources().getColor(colorId, context.getTheme());
            drawable.setFilterBitmap(false);
            drawable.setTint(color);
            numberDrawableMap.put(number, drawable);
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
