package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class Information implements Content {
    private final String info;

    public Information(String info) {
        this.info = info;
    }

    @Override
    public View addView(@NotNull AppCompatActivity activity,
                        @NotNull ViewGroup viewGroup, boolean editable) {
        View informationView = activity.getLayoutInflater().inflate(R.layout.content_information,
                viewGroup, false);
        TextView textView = informationView.findViewById(R.id.information_text);
        textView.setText(info);
        viewGroup.addView(informationView);
        return informationView;
    }
}
