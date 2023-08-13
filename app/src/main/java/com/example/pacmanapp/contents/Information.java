package com.example.pacmanapp.contents;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Information implements Content {
    private final String info;

    public Information(String info) {
        this.info = info;
    }
// TODO make a layout for this class, so the text view can be more easily customized and adjusted.
    @Override
    public void addView(@NonNull ViewGroup viewGroup, boolean editable) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setText(info);
        viewGroup.addView(textView);
    }
}
