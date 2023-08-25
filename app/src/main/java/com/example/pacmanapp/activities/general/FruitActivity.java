package com.example.pacmanapp.activities.general;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.save.ClearSavesDialog;
import com.example.pacmanapp.activities.save.CreateSaveDialog;
import com.example.pacmanapp.contents.ButtonContent;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.selectables.Fruit;
import com.example.pacmanapp.selection.selectables.FruitStorage;
import com.example.pacmanapp.storage.SaveManager;
import com.example.pacmanapp.storage.SavePlatform;

import java.util.Map;

public class FruitActivity extends AppCompatActivity {
    private FruitStorage fruitStorage;
    private Fruits fruits;
// TODO fix that dialogs crash app when creating from saved instance state and maybe start using saved instance state?
//  https://stackoverflow.com/questions/25658280/how-to-saveinstancestate-on-dialog-class

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fruit);
        NavigationBar.configure(this, PageType.FRUITS);

        fruitStorage = FruitStorage.getFromSave(SavePlatform.getSave());
        fruits = new Fruits(fruitStorage);
        updateFruitList();

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> new CreateFruitDialog(this)
                .show(getSupportFragmentManager(), "CreateFruit"));

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view -> new ClearFruitDialog(this)
                .show(getSupportFragmentManager(), "ClearFruit"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SaveManager.getInstance(this).saveCurrentSave();
    }

    /**
     * Update fruit list.
     */
    public void updateFruitList() {
        ViewGroup viewGroup = findViewById(R.id.content_scroll_view);
        SelectableContent.setContent(this, viewGroup, fruits, true);
    }

    /**
     * Create a fruit with the specified fruit type.
     *
     * @param fruitType Fruit type to create fruit with
     */
    void createFruit(Fruit.FruitType fruitType) {
        Fruit fruit = new Fruit(fruitType);
        fruitStorage.addFruit(new Fruit(fruitType));
        SelectionCrier.getInstance().select(fruit);
        updateFruitList();
    }

    /**
     * Clear all saves stored in the save manager.
     */
    void clearSaves() {
        fruitStorage.clearFruits();
        updateFruitList();
    }

}
