package com.example.pacmanapp.activities.edit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.SelectableContent;

import java.io.Serializable;

public class EditPacDotActivity extends AppCompatActivity {
    private static final String TAG = "InspectPacDotActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pacdot);
        Serializable selected =  getIntent().getSerializableExtra("selected");
        if (!(selected instanceof PacDot)) {
            Log.e(TAG, "Did not get selected that is an instance of pac dot.");
            finish();
            return;
        }
        PacDot pacDot = (PacDot) selected;
        setContent(pacDot);

        NavigationBar.configure(this, true, PageType.INSPECT);
    }

    /**
     * Set content of the inspect pac dot activity page.
     *
     * @param pacDot Pac dot to use for setting the content
     */
    private void setContent(PacDot pacDot) {
        SelectableContent.setContent(this, pacDot);
        setHintText(pacDot);
    }

    /**
     * Set the hint text on the activity page.
     *
     * @param pacDot Pac dot to get hint from
     */
    private void setHintText(PacDot pacDot) {
        EditText hintEditTextView = findViewById(R.id.pacdot_hint);
        if (hintEditTextView == null) {
            Log.w(TAG, "Could not set hint text for pac dot.");
            return;
        }
        hintEditTextView.setText(pacDot.getHint());
        hintEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pacDot.setHint(editable.toString());
            }
        });
    }
}
