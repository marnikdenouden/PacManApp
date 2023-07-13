package com.example.pacmanapp.activities.save;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;

public class SaveDialog extends DialogFragment {
    private static final String TAG = "SaveDialog";
    private final SaveActivity activity;
    private final String saveName;

    SaveDialog(String saveName, SaveActivity saveActivity) {
        this.activity = saveActivity;
        this.saveName = saveName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(R.layout.dialog_save)
                .setPositiveButton("Load", (dialogInterface, i) -> {
                    activity.loadSave(saveName);
                    dismiss();
                }).setNegativeButton("Remove", (dialogInterface, i) -> {
                    activity.removeSave(saveName);
                    dismiss();
                }).setNeutralButton("Cancel", (dialogInterface, i) -> dismiss());

        Dialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> setSaveName());
        return dialog;
    }

    public void setSaveName() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.e(TAG, "Could not get dialog when trying to open dialog");
            return;
        }
        TextView textView = dialog.findViewById(R.id.saveNameTitle);
        if (textView == null) {
            Log.e(TAG, "Could not get saveNameTitle text view from dialog save layout");
            return;
        }
        textView.setText(saveName);
    }
}
