package com.example.pacmanapp.contents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.general.Option;

import org.jetbrains.annotations.NotNull;

public enum ContentType implements Option {
    INFORMATION(R.drawable.icon_info,
            (container, activity) -> container.addContent(new Information())),
    HINT(R.drawable.icon_hint, HintEdit::create);

    private final int drawableId;
    private final Constructor constructor;

    ContentType(int drawableId, Constructor constructor) {
        this.drawableId = drawableId;
        this.constructor = constructor;
    }

    @Override
    public int getDrawableId() {
        return drawableId;
    }

    @NonNull
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    /**
     * Initialize the creation of content for this content type.
     *
     * @param container Container to add content to
     * @param activity Current activity to use for creating content
     */
    public void addContent(@NotNull Container container, @NotNull AppCompatActivity activity) {
        constructor.addContent(container, activity);
    }

    private interface Constructor {
        /**
         * Initialize the creation of content for this content type.
         *
         * @param container Container to add content to
         * @param activity Current activity to use for creating content
         */
        void addContent(@NotNull Container container, @NotNull AppCompatActivity activity);
    }
}
