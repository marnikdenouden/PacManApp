package com.example.pacmanapp.selection;

import com.example.pacmanapp.contents.Container;

import java.io.Serializable;

public interface Selectable extends Serializable, Container {
    String getLabel();

    int getIconId();

    String getDescription();

}

