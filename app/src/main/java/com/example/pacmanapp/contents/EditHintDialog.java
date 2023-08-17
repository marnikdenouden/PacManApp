package com.example.pacmanapp.contents;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.SelectableContent;

import org.jetbrains.annotations.NotNull;

public class EditHintDialog extends DialogFragment {
    private final AppCompatActivity activity;
    private final EditHint.HintEditor hintEditor;

    public EditHintDialog(@NotNull AppCompatActivity activity,
                          @NotNull EditHint editHint) {
        this.activity = activity;
        this.hintEditor = editHint.getHintEditor();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        @SuppressLint("InflateParams") View editHintView = LayoutInflater.from(activity)
                .inflate(R.layout.activity_edit_hint, null, false);

        EditText keyTextView = editHintView.findViewById(R.id.key_text);
        Util.configureEditText(keyTextView, hintEditor.getKey(), hintEditor::setKey);

        EditText hintTextView = editHintView.findViewById(R.id.hint_text);
        Util.configureEditText(hintTextView, hintEditor.getHintText(), hintEditor::setHintText);

        ImageView iconImageView = editHintView.findViewById(R.id.hint_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(editHintView.getResources(),
                hintEditor.getHintTarget().getIconId(), editHintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO should be a way to select a fruit as hint target.
            }
        });

        TextView labelTextView = editHintView.findViewById(R.id.hint_label);
        labelTextView.setText(hintEditor.getHintTarget().getLabel());

        builder.setView(editHintView).setPositiveButton("Save", (dialogInterface, i) -> {
                    hintEditor.save();
                    hintEditor.updateLastAddedView();
                    dismiss();
                }).setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    // TODO figure out how to make the view updated on created. Maybe do set View not the layout id but a created view?

}
