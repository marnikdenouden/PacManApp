package com.example.pacmanapp.contents;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Container {
    /**
     * Get the content list of the container.
     *
     * @param activity Activity to update the content list for
     * @param editable Truth assignment if the updated content list should be editable
     * @return contentList List of content currently stored in the container
     */
    List<Content> getContent(@NotNull AppCompatActivity activity, boolean editable);

    /**
     * Set the content list of the content container.
     *
     * @param contentList List of content to store
     */
    void setContent(@Nullable List<Content> contentList);
}
