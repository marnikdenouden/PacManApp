package com.example.pacmanapp.activities.inspect;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.markers.PowerPellet;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;

import java.io.Serializable;

public class InspectPowerPelletActivity extends AppCompatActivity {
    private static final String TAG = "InspectPowerPalletActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_powerpellet);
        Serializable selected =  getIntent().getSerializableExtra("selected");
        if (!(selected instanceof PowerPellet)) {
            Log.e(TAG, "Did not get selected that is an instance of power pallet.");
            finish();
            return;
        }
        PowerPellet powerPellet = (PowerPellet) selected;
        //setContent(powerPallet);

        NavigationBar.configure(this, false, PageType.INSPECT);
    }
}
