package com.example.pacmanapp.displays;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public enum Number {
    ZERO(R.drawable.number_0),
    ONE(R.drawable.number_1),
    TWO(R.drawable.number_2),
    THREE(R.drawable.number_3),
    FOUR(R.drawable.number_4),
    FIVE(R.drawable.number_5),
    SIX(R.drawable.number_6),
    SEVEN(R.drawable.number_7),
    EIGHT(R.drawable.number_8),
    NINE(R.drawable.number_9);

    static private final String TAG = "Number";

    final int drawableId;
    Number(int drawableId) {
        this.drawableId = drawableId;
    }

    /**
     * Gets the drawable for the number.
     *
     * @param context Context in which to find drawable
     * @return Drawable for the number
     */
    @NotNull
    Drawable getDrawable(Context context) {
        Drawable drawable = AppCompatResources.getDrawable(context, drawableId);
        assert drawable != null;
        drawable.setFilterBitmap(false);
        return drawable;
    }

    /**
     * Get the number for a value.
     *
     * @param value Value to get number for
     * @return Number of the value modulo 10 is given
     */
    static Number getNumber(int value) {
        switch(Math.abs(value) % 10) {
            case 0:
                return Number.ZERO;
            case 1:
                return Number.ONE;
            case 2:
                return Number.TWO;
            case 3:
                return Number.THREE;
            case 4:
                return Number.FOUR;
            case 5:
                return Number.FIVE;
            case 6:
                return Number.SIX;
            case 7:
                return Number.SEVEN;
            case 8:
                return Number.EIGHT;
            case 9:
                return Number.NINE;
            default:
                Log.e(TAG, "value could not be parsed to number, returned zero");
                return Number.ZERO;
        }
    }

}
