package com.example.pacmanapp.activities.save;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;

public class SaveDialog extends DialogFragment {
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
        return builder.create();
    }
}
