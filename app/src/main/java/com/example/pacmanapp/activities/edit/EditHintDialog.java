package com.example.pacmanapp.activities.edit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.general.Util;
import com.example.pacmanapp.selection.NextSelectionSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.Selector;
import com.example.pacmanapp.selection.selectables.BlankInspect;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EditHintDialog extends DialogFragment {
    private final static String TAG = "EditHintDialog";
    private final AppCompatActivity activity;
    private final HintEdit.HintEditor hintEditor;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public EditHintDialog(@NotNull AppCompatActivity activity,
                          @NotNull HintEdit hintEdit) {
        this.activity = activity;
        this.hintEditor = hintEdit.getHintEditor();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        @SuppressLint("InflateParams") View editHintView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_edit_hint, null, false);

        EditText keyTextView = editHintView.findViewById(R.id.key_text);
        Util.configureEditText(keyTextView, hintEditor.getKey(), hintEditor::setKey);

        EditText hintTextView = editHintView.findViewById(R.id.hint_text);
        Util.configureEditText(hintTextView, hintEditor.getHintText(), hintEditor::setHintText);

        Button addImageButton = editHintView.findViewById(R.id.add_image_button);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        Button removeImageButton = editHintView.findViewById(R.id.remove_image_button);
        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintEditor.removeHintImage();
            }
        });

        ImageView iconImageView = editHintView.findViewById(R.id.hint_icon);
        Drawable iconImage = ResourcesCompat.getDrawable(editHintView.getResources(),
                hintEditor.getHintTarget().getIconId(), editHintView.getContext().getTheme());
        iconImageView.setImageDrawable(iconImage);

        iconImageView.setOnClickListener(view -> {
            Selector selector = NextSelectionSelector.getSelector(
                    R.id.editHintNextSelectionSelector, new BlankInspect(getResources()));

            // Take the next selected selectable as the hint target of this hint.
            selector.addOnSelectionListener((Selectable hintTarget) -> {
                Log.i(TAG, "Selected hint target " + hintTarget.getLabel() + " for hint");
                hintEditor.setHintTarget(hintTarget);
                hintEditor.save();
            });

            Toast.makeText(getContext(), "Select any selectable to have the hint be for",
                    Toast.LENGTH_LONG).show();
            dismiss();
        });

        TextView labelTextView = editHintView.findViewById(R.id.hint_label);
        labelTextView.setText(hintEditor.getHintTarget().getLabel());

        builder.setView(editHintView).setPositiveButton("Save", (dialogInterface, i) -> {
                    hintEditor.save();
                    hintEditor.updateLastAddedView();
                    dismiss();
                }).setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Registers a photo picker activity launcher in single-select mode.
        pickMedia = registerForActivityResult(new PickVisualMedia(), imageUri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (imageUri != null) {
                    Log.d(TAG, "Selected URI: " + imageUri);
                } else {
                    Log.d(TAG, "No media selected");
                    return;
                }
                try {
                    Bitmap bitmap = MediaStore.Images.Media
                            .getBitmap(context.getContentResolver(), imageUri);
                    String imageId = imageUri.getLastPathSegment();
                    hintEditor.setHintImage(imageId, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    // TODO figure out how to make the view updated on created. Maybe do set View not the layout id but a created view?

}
