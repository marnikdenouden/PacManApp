package com.example.pacmanapp.selection;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public interface Selectable extends Serializable {

    Class<? extends AppCompatActivity> getInspectPage();

    Class<? extends AppCompatActivity> getEditPage();

    public String getLabel();

    public int getIconId();

    public String getDescription();

}
