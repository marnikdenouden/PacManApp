package com.example.pacmanapp.activities.save;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;

public class ClearSavesDialog extends DialogFragment {
    private final SaveActivity activity;

    ClearSavesDialog(SaveActivity saveActivity) {
        this.activity = saveActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to remove all saves?")
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    activity.clearSaves();
                    dismiss();
                }).setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }
}
