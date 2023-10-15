package com.example.pacmanapp.activities.general;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.storage.GameSave;

import org.jetbrains.annotations.NotNull;

public class PreviewSaveDialog  extends DialogFragment {
    private final static String TAG = "PreviewSaveDialog";
    private final StartActivity activity;
    private final GameSave gameSave;

    PreviewSaveDialog(@NotNull StartActivity startActivity, @NotNull GameSave gameSave) {
        this.activity = startActivity;
        this.gameSave = gameSave;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setTitle("Choose action for game save \" " + gameSave.getSaveName() + "\"?");
        builder.setPositiveButton("Play", (dialogInterface, i) -> activity.play(gameSave))
                .setNeutralButton("Change", (dialogInterface, i) -> activity.changeSave())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }

}
