package com.example.pacmanapp.contents;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class Util {
    public static void configureEditText(EditText editText, String text,
                                         TextListener textListener) {
        editText.setText(text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textListener.afterTextChanged(editable.toString());
            }
        });
    }

    /**
     * Add a custom button to the content before adding it to the specified view group and activity.
     *
     * @param activity Activity to add the content with button in
     * @param viewGroup ViewGroup to add the content with button layout to
     * @param editable Truth assignment, if content should be editable
     * @param label String to put on the button
     * @param content Content to add button to
     * @param onClickListener OnClickListener for the button to use
     */
    public static void addButton(@NotNull AppCompatActivity activity, @NotNull ViewGroup viewGroup,
                                 boolean editable, String label, @NotNull Content content,
                                 View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.add_button , viewGroup,
                false);
        content.addView(activity, view.findViewById(R.id.content_container), editable);
        Button button = view.findViewById(R.id.add_button);
        button.setText(label);
        button.setOnClickListener(onClickListener);
        viewGroup.addView(view);
    }

    public interface TextListener {
        void afterTextChanged(String text);
    }
}
