package com.example.pacmanapp.contents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class Util {
    public static void configureEditText(EditText editText, String text,
                                         TextListener textListener) {
        editText.setText(text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textListener.afterTextChanged(editable.toString());
            }
        });
    }

    /**
     * Add a custom button to the content before adding it to the specified view group and activity.
     *
     * @param activity Activity to add the content with button in
     * @param viewGroup ViewGroup to add the content with button layout to
     * @param editable Truth assignment, if content should be editable
     * @param content Content to add button to
     * @param onClickListener OnClickListener for the button to use
     *
     * @return View view that was added to the specified view group
     */
    public static View addButton(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup,
                                 boolean editable, int iconId, @NotNull Content content,
                                 View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.content_add_button, viewGroup,
                false);
        content.addView(activity, view.findViewById(R.id.content_container), editable);
        ImageView imageButton = view.findViewById(R.id.added_button);
        imageButton.setOnClickListener(onClickListener);
        ImageView imageIcon = view.findViewById(R.id.added_button_icon);
        Drawable iconDrawable = AppCompatResources.getDrawable(activity, iconId);
        imageIcon.setImageDrawable(iconDrawable);
        viewGroup.addView(view);
        return view;
    }

    public interface TextListener {
        void afterTextChanged(String text);
    }

}
