package com.example.pacmanapp.activities.edit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.AcceptAllSelector;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<? extends AppCompatActivity> editPage =
                AcceptAllSelector.getSelector().getSelected().getEditPage();
        Intent intent = new Intent(this, editPage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Navigate.navigate(intent, this, editPage);
        finish();
    }
}
