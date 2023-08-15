package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class Information implements Content {
    private final String info;

    public Information(String info) {
        this.info = info;
    }
// TODO make a layout for this class, so the text view can be more easily customized and adjusted.
    @Override
    public View addView(@NotNull AppCompatActivity activity,
                        @NotNull ViewGroup viewGroup, boolean editable) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setText(info);
        viewGroup.addView(textView);
        return textView;
    }
}
