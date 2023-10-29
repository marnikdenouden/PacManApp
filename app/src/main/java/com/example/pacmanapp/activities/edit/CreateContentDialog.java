package com.example.pacmanapp.activities.edit;

import androidx.annotation.NonNull;

import com.example.pacmanapp.activities.general.CreateDialog;
import com.example.pacmanapp.contents.ContentType;

public class CreateContentDialog extends CreateDialog<ContentType> {
    private final static String TAG = "CreateContentDialog";

    /**
     * Construct a create dialog,
     * which requires creating activity to implement constructor interface.
     *
     */
    public CreateContentDialog() {
        super("Content", ContentType.class, contentConstructor.class);
    }

    public interface contentConstructor extends CreateDialog.Constructor<ContentType> {
        @Override
        void create(@NonNull ContentType contentType);
    }
}
