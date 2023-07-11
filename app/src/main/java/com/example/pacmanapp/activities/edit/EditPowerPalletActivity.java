package com.example.pacmanapp.activities.edit;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.PowerPallet;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;

import java.io.Serializable;

public class EditPowerPalletActivity extends AppCompatActivity {
    private final static String TAG = "EditPowerPalletActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_powerpallet);
        Serializable selected =  getIntent().getSerializableExtra("selected");
        if (!(selected instanceof PowerPallet)) {
            Log.e(TAG, "Did not get selected that is an instance of power pallet.");
            finish();
            return;
        }
        PowerPallet powerPallet = (PowerPallet) selected;
        //setContent(powerPallet);

        NavigationBar.configure(this, true, PageType.INSPECT);
    }
}
