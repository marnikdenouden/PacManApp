package com.example.pacmanapp.activities.edit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.contents.Util;
import com.example.pacmanapp.selection.NextSelectionSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.Selector;
import com.example.pacmanapp.selection.selectables.BlankInspect;

import org.jetbrains.annotations.NotNull;

public class EditHintDialog extends DialogFragment {
    private final static String TAG = "EditHintDialog";
    private final AppCompatActivity activity;
    private final HintEdit.HintEditor hintEditor;

    public EditHintDialog(@NotNull AppCompatActivity activity,
                          @NotNull HintEdit hintEdit) {
        this.activity = activity;
        this.hintEditor = hintEdit.getHintEditor();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        @SuppressLint("InflateParams") View editHintView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_edit_hint, null, false);

        EditText keyTextView = editHintView.findViewById(R.id.key_text);
        Util.configureEditText(keyTextView, hintEditor.getKey(), hintEditor::setKey);

        EditText hintTextView = editHintView.findViewById(R.id.hint_text);
        Util.configureEditText(hintTextView, hintEditor.getHintText(), hintEditor::setHintText);

        ImageView iconImageView = editHintView.findViewById(R.id.hint_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(editHintView.getResources(),
                hintEditor.getHintTarget().getIconId(), editHintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);

        iconImageView.setOnClickListener(view -> {
            Selector selector = NextSelectionSelector.getNextSelectionSelector(
                    R.id.editHintNextSelectionSelector, new BlankInspect(getResources()));

            // Take the next selected selectable as the hint target of this hint.
            selector.addOnSelectionListener((Selectable hintTarget) -> {
                Log.i(TAG, "Selected hint target " + hintTarget.getLabel() + " for hint");
                hintEditor.setHintTarget(hintTarget);
                hintEditor.save();
            });

            Toast.makeText(getContext(), "Select any selectable to have the hint be for",
                    Toast.LENGTH_LONG).show();
            dismiss();
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
