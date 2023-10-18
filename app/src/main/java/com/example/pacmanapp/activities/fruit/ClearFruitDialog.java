package com.example.pacmanapp.activities.fruit;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;

public class ClearFruitDialog extends DialogFragment {
    private final FruitActivity activity;

    ClearFruitDialog(FruitActivity saveActivity) {
        this.activity = saveActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setMessage("Are you sure you want to remove all fruits?")
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    activity.clearSaves();
                    dismiss();
                }).setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }
}
