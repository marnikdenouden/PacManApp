package com.example.pacmanapp.activities.save;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.SaveManager;

public class CreateSaveDialog extends DialogFragment {
    private final static String TAG = "CreateSaveDialog";
    private final SaveActivity activity;
// TODO add java doc comment
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

        String saveName = editText.getText().toString();
        if (SaveManager.getInstance(activity).hasSave(saveName)) {
            Toast.makeText(activity, "Save with name " + saveName + " already exists",
                    Toast.LENGTH_LONG).show();
            return;
        }

        activity.createSave(saveName);
        dismiss();
    }
}
