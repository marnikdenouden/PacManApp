package com.example.pacmanapp.activities.fruit;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.selection.selectables.Fruit;

import java.util.Objects;

public class CreateFruitDialog extends DialogFragment {
    private final static String TAG = "CreateSaveDialog";
    private final FruitActivity activity;
    private Fruit.FruitType selectedFruitType;

    CreateFruitDialog(FruitActivity fruitActivity) {
        this.activity = fruitActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setView(R.layout.dialog_create_fruit);
        builder.setTitle("Select fruit type to add");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> createFruit())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        Dialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> addFruitTypeOptions());
        return dialog;
    }

    /**
     * Add the fruit type options in the dialog.
     */
    private void addFruitTypeOptions() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.e(TAG, "Could not get dialog when trying to open dialog");
            return;
        }

        LinearLayout linearLayout = dialog.findViewById(R.id.fruitType_options);
        if (linearLayout == null) {
            Log.e(TAG, "Could not get fruit type option linear layout from create fruit dialog layout");
            return;
        }

        linearLayout.removeAllViews();
        Context context = getContext();
        if (context == null) {
            Log.e(TAG, "Could not get context while adding fruit type options");
            return;
        }

        for (Fruit.FruitType fruitType: Fruit.FruitType.values()) {
            View fruitTypeOptionView = LayoutInflater.from(context)
                    .inflate(R.layout.fruit_type_option, linearLayout, false);
            ImageView fruitTypeButton = fruitTypeOptionView.findViewById(R.id.fruitType_button);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                    fruitType.getDrawableId(), context.getTheme());
            fruitTypeButton.setImageDrawable(drawable);
            fruitTypeButton.setOnClickListener(view -> selectFruitType(fruitType));
            linearLayout.addView(fruitTypeOptionView);
        }
    }

    /**
     * Select a fruit type that the user can create a fruit for.
     *
     * @param fruitType Fruit type selected in dialog
     */
    private void selectFruitType(Fruit.FruitType fruitType) {
        selectedFruitType = fruitType;
        Objects.requireNonNull(getDialog()).setTitle("Add " + fruitType + "?");
    }

    /**
     * Create fruit from currently selected fruit type.
     */
    private void createFruit() {
        if (selectedFruitType == null) {
            Toast.makeText(getContext(), "No fruit type selected to create fruit with",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        activity.createFruit(selectedFruitType);
        dismiss();
    }

}
