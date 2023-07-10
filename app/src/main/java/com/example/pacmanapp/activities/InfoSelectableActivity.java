package com.example.pacmanapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.selection.Selectable;

public class InfoSelectableActivity extends AppCompatActivity implements Selectable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_info);
    }

    @Override
    public Class<? extends AppCompatActivity> getInspectPage() {
        return InfoSelectableActivity.class;
    }

    @Override
    public Class<? extends AppCompatActivity> getEditPage() {
        return InfoSelectableActivity.class;
    }
}
