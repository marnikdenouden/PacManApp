package com.example.pacmanapp.selection;

import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;

import java.io.Serializable;
import java.util.List;

public interface Selectable extends Serializable, ContentContainer {
    String getLabel();

    int getIconId();

    String getDescription();

}

