package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class ButtonContent implements Content {
    private final String label;
    private final Runnable onButtonClick;

    /**
     * Create a button content.
     *
     * @param label String used on button as text
     * @param onButtonClick Runnable that is run on button click
     */
    public ButtonContent(@NotNull String label, Runnable onButtonClick) {
        this.label = label;

        if (onButtonClick == null) {
            this.onButtonClick = () -> {};
        } else {
            this.onButtonClick = onButtonClick;
        }
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup,
                        boolean editable) {
        Button button = new Button(activity); // TODO create a layout view for the button.
        button.setOnClickListener(view -> {onButtonClick.run();});
        button.setText(label);
        viewGroup.addView(button);
        return button;
    }

}
