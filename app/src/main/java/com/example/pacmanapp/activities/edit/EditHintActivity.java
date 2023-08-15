package com.example.pacmanapp.activities.edit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.EditHint;
import com.example.pacmanapp.contents.Util;
import com.example.pacmanapp.navigation.Navigate;

import org.jetbrains.annotations.NotNull;

public class EditHintActivity extends AppCompatActivity {
    private static EditHint editHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hint);
        EditHint.HintEditor hintEditor = editHint.getHintEditor();
        EditText keyTextView = findViewById(R.id.key_text);
        Util.configureEditText(keyTextView, hintEditor.getKey(), hintEditor::setKey);

        EditText hintTextView = findViewById(R.id.hint_text);
        Util.configureEditText(hintTextView, hintEditor.getHintText(), hintEditor::setHintText);

        ImageView iconImageView = findViewById(R.id.hint_icon);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO should be a way to select a fruit as hint target.
            }
        });
    }

    public static void open(@NotNull AppCompatActivity currentActivity,
                            @NotNull EditHint editHint) {
        EditHintActivity.editHint = editHint;
        Navigate.navigate(currentActivity, EditHintActivity.class);
    }

    // TODO figure out how to make the view updated on created. Maybe do set View not the layout id but a created view?

}
