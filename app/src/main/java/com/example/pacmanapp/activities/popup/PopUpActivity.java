package com.example.pacmanapp.activities.popup;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PopUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    /**
     * Close the popup.
     *
     * @param view View that is send with the close call,
     *             so xml object can call it
     */
    public void close(View view) {
        finish();
    }
}
