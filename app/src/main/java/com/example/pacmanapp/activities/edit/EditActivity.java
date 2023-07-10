package com.example.pacmanapp.activities.edit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.Selector;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Selector selector = SelectionCrier.getInstance().getSelector(R.id.editAllSelector);
        Class<? extends AppCompatActivity> editPage = selector.getSelected().getInspectPage();
        Intent intent = new Intent(this, editPage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Navigate.navigate(intent, this, editPage);
        finish();
    }
}
