package com.example.pacmanapp.selection;

import com.example.pacmanapp.contents.Container;
import com.example.pacmanapp.contents.Content;

import java.io.Serializable;

public interface Selectable extends Serializable, Container, Content {
    String getLabel();

    int getIconId();

    String getDescription();
}

