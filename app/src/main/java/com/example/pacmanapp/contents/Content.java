package com.example.pacmanapp.contents;

import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface Content extends Serializable {
    /**
     * Add a hint view to the provided view group.
     *
     * @param viewGroup View group to insert hint view in
     * @param editable Truth assignment, if hint should be editable in view.
     */
    void addView(@NotNull ViewGroup viewGroup, boolean editable);

}