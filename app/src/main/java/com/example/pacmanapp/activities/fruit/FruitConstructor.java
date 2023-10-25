package com.example.pacmanapp.activities.fruit;

import com.example.pacmanapp.selection.selectables.Fruit;

public interface FruitConstructor {
    /**
     * Create a fruit with the specified fruit type.
     *
     * @param fruitType Fruit type to create fruit with
     */
    void createFruit(Fruit.FruitType fruitType);
}
