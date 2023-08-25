package com.example.pacmanapp.activities.general;

import com.example.pacmanapp.activities.save.SaveDialog;
import com.example.pacmanapp.contents.ButtonContent;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.SelectableButtonContent;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.selectables.Fruit;
import com.example.pacmanapp.selection.selectables.FruitStorage;

import java.util.ArrayList;
import java.util.List;

public class Fruits implements ContentContainer {
    private final FruitStorage fruitStorage;

    public Fruits(FruitStorage fruitStorage) {
        this.fruitStorage = fruitStorage;
    }

    @Override
    public List<Content> getContent() {
        List<Content> contentList = new ArrayList<>();
        for (Fruit fruit: fruitStorage.getFruitCollection()) {
            contentList.add(createFruitButton(fruit));
        }
        return contentList;
    }

    /**
     * Create a new fruit button for the specified fruit.
     *
     * @param fruit Fruit to create fruit button for
     * @return Button content created for the specified fruit
     */
    private ButtonContent createFruitButton(Fruit fruit) {
        Runnable onFruitButtonClick = () -> {
            SelectionCrier.getInstance().select(fruit);
        };
        return new SelectableButtonContent(fruit.getLabel(), onFruitButtonClick);
    }
}
