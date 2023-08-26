package com.example.pacmanapp.contents;

import android.graphics.Bitmap;

import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.storage.SavePlatform;

import org.jetbrains.annotations.NotNull;

public class HintBuilder {
    String hintText;
    String hintImageId;
    final Selectable hintTarget;

    /**
     * Hint builder that can be created with a selectable.
     *
     * @param hintTarget Selectable to create hint for.
     */
    public HintBuilder(@NotNull Selectable hintTarget) {
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
     * Add a hint image id to the hint builder.
     *
     * @param hintImageId String id that will be added to hint builder
     * @return HintBuilder that be continued
     */
    public HintBuilder setHintImage(@NotNull String hintImageId) {
        this.hintImageId = hintImageId;
        return this;
    }


    /**
     * Finish hint building and get the created hint.
     *
     * @return Hint that was created by the hint builder
     */
    public Content build() {
        if (hintImageId != null) {
            return new HintImage(this);
        }
        return new HintText(this);
    }
}