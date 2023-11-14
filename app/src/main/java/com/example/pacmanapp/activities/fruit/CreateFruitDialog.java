package com.example.pacmanapp.activities.fruit;

import androidx.annotation.NonNull;

import com.example.pacmanapp.activities.general.CreateDialog;
import com.example.pacmanapp.selection.selectables.Fruit;

public class CreateFruitDialog extends CreateDialog<Fruit.FruitType> {

    /**
     * Construct a create dialog,
     * which requires creating activity to implement constructor interface.
     */
    public CreateFruitDialog() {
        super("Fruit", Fruit.FruitType.class, fruitConstructor.class);
    }

    public interface fruitConstructor extends CreateDialog.Constructor<Fruit.FruitType> {
        @Override
        void create(@NonNull Fruit.FruitType fruitType);
    }
}
