package com.example.pacmanapp.activities.save;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.navigation.Navigate;

@SuppressLint({"AppCompatCustomView", "ViewConstructor"})
public class SaveButton extends Button {

    public SaveButton(String saveName, AppCompatActivity currentActivity) {
        super(currentActivity.getApplicationContext());
        setText(saveName);
        Intent intent = new Intent();
        intent.putExtra("SaveName", saveName);
        setOnClickListener(view -> Navigate.navigate(intent, currentActivity, SavePopUpActivity.class));
    }

}
