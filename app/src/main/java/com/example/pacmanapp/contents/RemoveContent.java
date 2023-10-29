package com.example.pacmanapp.contents;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;

import org.jetbrains.annotations.NotNull;

public class RemoveContent implements Content {
    private final String TAG = "RemoveContent";
    private final Content content;
    private boolean isRemoved = false;
    private View removeView;

    public RemoveContent(@NotNull Content content) {
        this.content = content;
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup, boolean editable) {
        removeView = LayoutInflater.from(activity)
                .inflate(R.layout.content_remove, viewGroup,false);
        ViewGroup contentLayout = removeView.findViewById(R.id.content_container);
        View removeButton = removeView.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(view -> removeContent());
        content.addView(activity, contentLayout, editable);
        viewGroup.addView(removeView);
        return removeView;
    }

    /**
     * Remove content in this class.
     */
    private void removeContent() {
        isRemoved = true;
        removeView.setVisibility(View.GONE);
        Log.d(TAG, "Set content to be removed.");
    }

    /**
     * Check if content is removed.
     *
     * @return Truth assignment, if content is removed
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * Get the content that is stored.
     *
     * @return Content content that is stored
     */
    public Content getContent() {
        return content;
    }
}
