package com.example.pacmanapp.activities.general;

import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.selection.selectables.FruitStorage;

import java.util.List;

public class Fruits implements ContentContainer {

    private final FruitStorage fruitStorage;

    public Fruits(FruitStorage fruitStorage) {
        this.fruitStorage = fruitStorage;
    }

    @Override
    public List<Content> getContent() {
        return null; // TODO return a list of fruit buttons
    }
}
