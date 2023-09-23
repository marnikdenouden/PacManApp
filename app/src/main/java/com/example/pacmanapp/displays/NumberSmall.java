package com.example.pacmanapp.displays;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;

import java.util.ArrayList;

// Info, use pinky color for received score points and inky for points from eating ghosts.

public enum NumberSmall {
    ZERO(R.drawable.number_0_small),
    ONE(R.drawable.number_1_small),
    TWO(R.drawable.number_2_small),
    THREE(R.drawable.number_3_small),
    FOUR(R.drawable.number_4_small),
    FIVE(R.drawable.number_5_small),
    SIX(R.drawable.number_6_small),
    SEVEN(R.drawable.number_7_small),
    EIGHT(R.drawable.number_8_small),
    NINE(R.drawable.number_9_small);

    static private String TAG = "Number";

    int drawableId;
    NumberSmall(int drawableId) {
        this.drawableId = drawableId;
    }

    /**
     * Gets a view with a small number from a specified value.
     *
     * @param viewGroup View group to add small number to
     * @param value Value of the small number
     * @param color Color for the small number
     */
    public static void AddSmallNumberView(ViewGroup viewGroup, int value, int color) {
        Context context = viewGroup.getContext();

        ArrayList<View> views = new ArrayList<>();
        while (value > 0) {
            views.add(NumberSmall.getNumber(value).getSmallNumberView(context, color));
            value = value / 10;
        }

        LinearLayout smallNumber = new LinearLayout(context);
        smallNumber.setId(R.id.small_number);
        for (int i = views.size() - 1; i >= 0; i--) {
            smallNumber.addView(views.get(i));
        }

        viewGroup.addView(smallNumber);
    }

    private View getSmallNumberView(Context context, int color) {
        Drawable drawable = getDrawable(context);
        drawable.setTint(color);
        ImageView smallNumber = new ImageView(context);
        smallNumber.setImageDrawable(drawable);
        smallNumber.setId(R.id.small_digit);
        return smallNumber;
    }

    /**
     * Gets the drawable for the number.
     *
     * @param context Context in which to find drawable
     * @return Drawable for the number
     */
    private Drawable getDrawable(Context context) {
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
    private static NumberSmall getNumber(int value) {
        switch(value % 10) {
            case 0:
                return NumberSmall.ZERO;
            case 1:
                return NumberSmall.ONE;
            case 2:
                return NumberSmall.TWO;
            case 3:
                return NumberSmall.THREE;
            case 4:
                return NumberSmall.FOUR;
            case 5:
                return NumberSmall.FIVE;
            case 6:
                return NumberSmall.SIX;
            case 7:
                return NumberSmall.SEVEN;
            case 8:
                return NumberSmall.EIGHT;
            case 9:
                return NumberSmall.NINE;
            default:
                Log.e(TAG, "value could not be parsed to number, returned zero");
                return NumberSmall.ZERO;
        }
    }

}
