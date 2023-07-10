package com.example.pacmanapp.activities.inspect;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.Selector;

public class InspectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Selector selector = SelectionCrier.getInstance().getSelector(R.id.inspectAllSelector);
        Class<? extends AppCompatActivity> inspectPage = selector.getSelected().getInspectPage();
        Intent intent = new Intent(this, inspectPage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Navigate.navigate(intent, this, inspectPage);
        finish();
    }
}
