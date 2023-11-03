package com.example.pacmanapp.activities.general;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public abstract class ConfirmDialog<Interface> extends DialogFragment {
    private final static String TAG = "CreateDialog";
    private final String confirmLabel;
    private final String confirmTitle;
    private final Class<Interface> activityInterface;
    private Interface activity;

    /**
     * Construct a confirm dialog,
     * which requires creating activity to implement the specified interface.
     *
     * @param confirmLabel Label of the confirm button to set for dialog
     */
    public ConfirmDialog(@NotNull Class<Interface> activityInterface, @NotNull String confirmTitle,
                         @NotNull String confirmLabel) {
        this.activityInterface = activityInterface;
        this.confirmTitle = confirmTitle;
        this.confirmLabel = confirmLabel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.DialogTheme);
        builder.setView(R.layout.dialog_create);
        builder.setTitle(confirmTitle);
        builder.setPositiveButton(confirmLabel, (dialogInterface, i) -> onConfirm(activity))
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        FragmentActivity parentActivity = getActivity();
        if (activityInterface.isInstance(parentActivity)) {
            activity = activityInterface.cast(parentActivity);
        } else {
            Log.w(TAG, "Dialog started with an activity that does not implement the specified interface.");
            dismiss();
        }
    }

    public abstract void onConfirm(Interface activity);

}
