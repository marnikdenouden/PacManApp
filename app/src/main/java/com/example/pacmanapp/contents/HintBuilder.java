package com.example.pacmanapp.contents;

import android.graphics.Bitmap;

import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

public class HintBuilder {
    String hintText;
    Bitmap bitmap;
    final Selectable hintTarget;

    /**
     * Hint builder that can be created with a selectable.
     *
     * @param hintTarget Selectable to create hint for.
     */
    public HintBuilder(Selectable hintTarget) {
        hintText = "";
        this.hintTarget = hintTarget;
    }

    /**
     * Add a hint string to the hint builder.
     *
     * @param hintText Hint that will be added to hint builder
     * @return HintBuilder that be continued
     */
    public HintBuilder setHintText(@NotNull String hintText) {
        this.hintText = hintText;
        return this;
    }

    /**
     * Finish hint building and get the created hint.
     *
     * @return Hint that was created by the hint builder
     */
    public Content build() {
        return new HintText(this);
    }
}