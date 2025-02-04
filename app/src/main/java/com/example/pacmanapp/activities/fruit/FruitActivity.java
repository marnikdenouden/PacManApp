package com.example.pacmanapp.activities.fruit;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.general.CreateDialog;
import com.example.pacmanapp.activities.settings.AdminSettingsActivity;
import com.example.pacmanapp.activities.start.StartActivity;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.selectables.Fruit;
import com.example.pacmanapp.selection.selectables.FruitStorage;
import com.example.pacmanapp.storage.SavePlatform;

public class FruitActivity extends AppCompatActivity implements CreateFruitDialog.fruitConstructor {
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

        setCreateButton();
        setClearButton();
        setQuitButton();

    }

    /**
     * Set the create button to open create fruit dialog with.
     */
    public void setCreateButton() {
        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> new CreateFruitDialog()
                .show(getSupportFragmentManager(), "CreateFruit"));
    }

    /**
     * Set the clear button to remove all fruits with.
     */
    public void setClearButton() {
        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view -> new ClearFruitDialog(this)
                .show(getSupportFragmentManager(), "ClearFruit"));
    }

    /**
     * Set the quit button to return to the start activity.
     */
    public void setQuitButton() {
        Button quitButton = findViewById(R.id.quitButton);
        quitButton.setOnClickListener(view -> quit());
    }

    @Override
    protected void onStop() {
        super.onStop();
        SavePlatform.save();
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
    public void create(@NonNull Fruit.FruitType fruitType) {
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

    /**
     * Call to quit the current game save.
     */
    void quit() {
        SavePlatform.quit();
        Navigate.navigate(FruitActivity.this, StartActivity.class);
        finish();
    }

}
