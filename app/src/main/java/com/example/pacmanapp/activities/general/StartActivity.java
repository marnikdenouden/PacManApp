package com.example.pacmanapp.activities.general;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.map.AdminMapActivity;
import com.example.pacmanapp.activities.map.PlayMapActivity;
import com.example.pacmanapp.activities.save.SaveActivity;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.NavigationBarType;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(view -> {
            Navigate.navigate(StartActivity.this, PlayMapActivity.class);
            NavigationBar.setNavigationBarType(NavigationBarType.PLAY);
        });

        Button adminButton = findViewById(R.id.adminButton);
        adminButton.setOnClickListener(view -> {
            Navigate.navigate(StartActivity.this, AdminMapActivity.class);
            NavigationBar.setNavigationBarType(NavigationBarType.ADMIN);
        });

        Button savesButton = findViewById(R.id.savesButton);
        Navigate.configure(savesButton, this, SaveActivity.class);
    }
}
