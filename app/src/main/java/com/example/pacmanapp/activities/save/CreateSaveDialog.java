package com.example.pacmanapp.activities.save;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;

public class CreateSaveDialog extends DialogFragment {
    private final static String TAG = "CreateSaveDialog";
    private final SaveActivity activity;

    CreateSaveDialog(SaveActivity saveActivity) {
        this.activity = saveActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setView(R.layout.dialog_create_save)
                .setPositiveButton("Confirm", (dialogInterface, i) -> createSave())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    /**
     * Create save from edit text save name.
     */
    private void createSave() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.e(TAG, "Could not get dialog when trying to create new save");
            return;
        }
        EditText editText = dialog.findViewById(R.id.createSaveName);
        if (editText == null) {
            Log.e(TAG, "Could not get edit text view from create save layout");
            return;
        }
        activity.createSave(editText.getText().toString());
        dismiss();
    }
}
