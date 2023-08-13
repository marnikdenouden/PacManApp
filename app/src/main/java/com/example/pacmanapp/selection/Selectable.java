package com.example.pacmanapp.selection;

import com.example.pacmanapp.contents.Content;

import java.io.Serializable;
import java.util.List;

public interface Selectable extends Serializable {
    String getLabel();

    int getIconId();

    String getDescription();

    List<Content> getContent();
}

