package com.example.pacmanapp.activities.general;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.map.AdminMapActivity;
import com.example.pacmanapp.activities.map.PlayMapActivity;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.navigation.Navigate;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button playButton = findViewById(R.id.playButton);
        Navigate.configure(playButton, this, PlayMapActivity.class);

        Button adminButton = findViewById(R.id.adminButton);
        Navigate.configure(adminButton, this, AdminMapActivity.class);

        Button savesButton = findViewById(R.id.savesButton);
        Navigate.configure(savesButton, this, SaveActivity.class);
    }
}
