package com.example.pacmanapp.contents;

public class HintBuilder {

    private String label;


    public HintBuilder() {
        // Add any required values for hint in the hint builder parameter.
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Hint build() {
        return new Hint(this);
    }
}
