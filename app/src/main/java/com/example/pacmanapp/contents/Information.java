package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.general.Util;

import org.jetbrains.annotations.NotNull;

public class Information implements Content {
    private String info;

    public Information() {
        info = "";
    }

    public Information(String info) {
        this.info = info;
    }

    @Override
    public View addView(@NotNull AppCompatActivity activity,
                        @NotNull ViewGroup viewGroup, boolean editable) {
        if (editable) {
            return addEditView(activity, viewGroup);
        } else {
            return addInfoView(activity, viewGroup);
        }
    }

    public View addInfoView(@NotNull AppCompatActivity activity,
                            @NotNull ViewGroup viewGroup) {
        View informationView = activity.getLayoutInflater().inflate(R.layout.content_information,
                viewGroup, false);
        TextView textView = informationView.findViewById(R.id.information_text);
        textView.setText(info);
        viewGroup.addView(informationView);
        return informationView;
    }

    public View addEditView(@NotNull AppCompatActivity activity,
                            @NotNull ViewGroup viewGroup) {
        View editInformationView = activity.getLayoutInflater()
                .inflate(R.layout.content_information_edit, viewGroup, false);
        EditText editTextView = editInformationView.findViewById(R.id.edit_text);
        Util.configureEditText(editTextView, info, text -> info = text);
        viewGroup.addView(editInformationView);
        return editInformationView;
    }

}
