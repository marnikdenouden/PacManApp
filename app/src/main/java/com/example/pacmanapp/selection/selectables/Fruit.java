package com.example.pacmanapp.selection.selectables;

import androidx.annotation.NonNull;

import com.example.pacmanapp.R;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.HintEdit;
import com.example.pacmanapp.contents.Information;
import com.example.pacmanapp.contents.LockedContent;
import com.example.pacmanapp.selection.Selectable;

import java.util.ArrayList;
import java.util.List;

public class Fruit implements Selectable {
    private final FruitType fruitType;
    private final List<Content> contentList;

    public Fruit(FruitType fruitType) {
        this.fruitType = fruitType;
        contentList = new ArrayList<>();
        HintEdit hintEdit = new HintEdit(this);
        HintEdit.HintEditor hintEditor = hintEdit.getHintEditor();
        hintEditor.setHintText("Well done you get " + fruitType.getPoints() + " points");
        hintEditor.save();
        contentList.add(hintEdit);
    }

    @Override
    public String getLabel() {
        return fruitType.label;
    }

    @Override
    public int getIconId() {
        return fruitType.drawableId;
    }

    @Override
    public String getDescription() {
        return "Finding this fruit will reward you with " + fruitType.points + " points.";
    }

    @Override
    public List<Content> getContent() {
        return contentList;
    }

    /**
     * Get the fruit type of the fruit.
     *
     * @return Fruit type of the fruit
     */
    public FruitType getFruitType() {
        return fruitType;
    }

    public enum FruitType {
        CHERRY("Cherry", R.drawable.fruit_1_cherry, 200),
        STRAWBERRY("Strawberry", R.drawable.fruit_2_strawberry, 300),
        ORANGE("Orange", R.drawable.fruit_3_orange, 400),
        APPLE("Apple", R.drawable.fruit_4_apple, 700),
        MELON("Melon", R.drawable.fruit_5_melon, 500),
        GALAXIAN("Galaxian", R.drawable.fruit_6_galaxian, 2000),
        BELL("Bell", R.drawable.fruit_7_bell, 3000),
        KEY("Key", R.drawable.fruit_8_key, 5000);

        private final String label;
        private final int drawableId;
        private final int points;

        FruitType(String label, int drawableId, int value) {
            this.label = label;
            this.drawableId = drawableId;
            this.points = value;
        }

        @NonNull
        @Override
        public String toString() {
            return label;
        }

        /**
         * Get the drawable id of this fruit type.
         *
         * @return Drawable id of fruit type
         */
        public int getDrawableId() {
            return drawableId;
        }

        /**
         * Get the default point value of the fruit type.
         *
         * @return default point value int of fruit type
         */
        public int getPoints() {
            return points;
        }
    }
}
