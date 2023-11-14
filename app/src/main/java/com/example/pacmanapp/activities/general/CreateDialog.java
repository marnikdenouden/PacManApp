package com.example.pacmanapp.activities.general;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class CreateDialog<Type extends Enum<Type> & Option> extends DialogFragment {
    private final static String TAG = "CreateDialog";
    private final String name;
    private final Class<Type> enumClass;
    private final Class<? extends Constructor<Type>> constructorClass;
    private Constructor<Type> constructor;
    private Type selectedType;

    /**
     * Construct a create dialog,
     * which requires creating activity to implement constructor interface.
     *
     * @param name Name of the product to create with the dialog
     * @param enumClass Class of the enum type to create for product
     * @param constructorClass Class that activity extends for creating product of enum type
     */
    public CreateDialog(@NotNull String name, @NotNull Class<Type> enumClass,
                        @NotNull Class<? extends Constructor<Type>> constructorClass) {
        this.name = name;
        this.enumClass = enumClass;
        this.constructorClass = constructorClass;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.DialogTheme);
        builder.setView(R.layout.dialog_create);
        builder.setTitle("Select " + name + " type to add");
        builder.setPositiveButton("Create", (dialogInterface, i) -> create())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        Dialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> addTypeOptions());
        restoreInstanceState(savedInstanceState);
        updateDialog(dialog);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        FragmentActivity parentActivity = getActivity();
        if (constructorClass.isInstance(parentActivity)) {
            this.constructor = constructorClass.cast(parentActivity);
        } else {
            Log.w(TAG, "Dialog started with an activity that is not an correct constructor");
            dismiss();
        }
    }

    /**
     * Update the dialog with the current values.
     *
     * @param dialog Dialog to update view for
     */
    private void updateDialog(@NotNull Dialog dialog) {
        if (selectedType != null) {
            dialog.setTitle("Add " + selectedType + "?");
        }
    }

    /**
     * Add the type options in the dialog.
     */
    private void addTypeOptions() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.e(TAG, "Could not get dialog when trying to open dialog");
            return;
        }

        LinearLayout linearLayout = dialog.findViewById(R.id.type_options);
        if (linearLayout == null) {
            Log.e(TAG, "Could not get type option linear layout from create dialog layout");
            return;
        }

        linearLayout.removeAllViews();
        Context context = getContext();
        if (context == null) {
            Log.e(TAG, "Could not get context while adding type options");
            return;
        }

        for (Type type: Objects.requireNonNull(enumClass.getEnumConstants())) {
            View typeOptionView = LayoutInflater.from(context)
                    .inflate(R.layout.type_option, linearLayout, false);

            ImageView typeButton = typeOptionView.findViewById(R.id.type_button);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                    type.getDrawableId(), context.getTheme());
            typeButton.setImageDrawable(drawable);
            typeButton.setOnClickListener(view -> selectType(type));

            TextView typeName = typeOptionView.findViewById(R.id.type_name);
            typeName.setText(type.toString());

            linearLayout.addView(typeOptionView);
        }
    }

    /**
     * Select a type that the user can create a product for.
     *
     * @param type Type selected in dialog
     */
    private void selectType(@Nullable Type type) {
        selectedType = type;
        updateDialog(requireDialog());
        Log.d(TAG, "Updated dialog title with current type");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (selectedType != null) {
            outState.putString("type", selectedType.name());
        }

        Log.d(TAG, "Saved instance state");
    }

    /**
     * Restore instance state.
     *
     * @param savedInstanceState Save instance state to restore
     */
    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        if (savedInstanceState.containsKey("type")) {
            String typeName = savedInstanceState.getString("type");
            selectedType = Type.valueOf(enumClass, typeName);
        }

        Log.d(TAG, "Restored instance state");
    }

    /**
     * Create product from currently selected type.
     */
    private void create() {
        if (selectedType == null) {
            Toast.makeText(getContext(), "No type selected to create product with",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        constructor.create(selectedType);
        dismiss();
    }

    protected interface Constructor<Type> {
        void create(@NotNull Type type);
    }

}
