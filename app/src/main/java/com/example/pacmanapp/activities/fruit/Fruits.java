package com.example.pacmanapp.activities.fruit;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.contents.ButtonContent;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.SelectableButtonContent;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.selectables.Fruit;
import com.example.pacmanapp.selection.selectables.FruitStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Fruits extends ContentContainer {
    private final FruitStorage fruitStorage;

    /**
     * Create the content container for the fruit storage.
     *
     * @param fruitStorage Fruit storage to update content container for
     */
    public Fruits(@NotNull FruitStorage fruitStorage) {
        this.fruitStorage = fruitStorage;
    }

    @Override
    public List<Content> getContent(@NotNull AppCompatActivity activity, boolean editable) {
        List<Content> contentList = new ArrayList<>();
        for (Fruit fruit: fruitStorage.getFruitList()) {
            contentList.add(0, createFruitButton(fruit));
        }
        return contentList;
    }

    /**
     * Create a new fruit button for the specified fruit.
     *
     * @param fruit Fruit to create fruit button for
     * @return Button content created for the specified fruit
     */
    private ButtonContent createFruitButton(@NotNull Fruit fruit) {
        Runnable onFruitButtonClick = () -> {
            SelectionCrier.getInstance().select(fruit);
        };
        return new SelectableButtonContent(fruit.getLabel(), onFruitButtonClick);
    }
}
