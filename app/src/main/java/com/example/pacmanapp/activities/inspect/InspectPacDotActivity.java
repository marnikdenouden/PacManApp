package com.example.pacmanapp.activities.inspect;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.PacDot;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.SelectableContent;

import java.io.Serializable;

public class InspectPacDotActivity extends AppCompatActivity {
    private static final String TAG = "InspectPacDotActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_pacdot);
        Serializable selected =  getIntent().getSerializableExtra("selected");
        if (!(selected instanceof PacDot)) {
            Log.e(TAG, "Did not get selected that is an instance of pac dot.");
            finish();
            return;
        }
        PacDot pacDot = (PacDot) selected;
        setContent(pacDot);

        NavigationBar.configure(this, PageType.INSPECT);
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
        TextView hintTextView = findViewById(R.id.pacdot_hint);
        if (hintTextView == null) {
            Log.w(TAG, "Could not set hint text for pac dot.");
            return;
        }
        hintTextView.setText(pacDot.getHint());
    }
}
