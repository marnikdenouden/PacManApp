package com.example.pacmanapp.activities.general;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.ButtonContent;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.selectables.FruitStorage;
import com.example.pacmanapp.storage.SavePlatform;

import java.util.Map;

public class FruitActivity extends AppCompatActivity {
    private Map<String, ButtonContent> fruitButtonMap;
    // TODO goal of this activity is to display the fruits in storage.
    //  Since fruits are selectable we can use the selectable preview layout.


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        FruitStorage fruitStorage = FruitStorage.getFromSave(SavePlatform.getSave());
        Fruits fruits = new Fruits(fruitStorage);

        ViewGroup viewGroup = findViewById(R.id.content_scroll_view);
        SelectableContent.setContent(this, viewGroup, fruits, true);
    }

}
