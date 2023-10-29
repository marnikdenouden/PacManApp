package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContentContainer implements Container, Content, Serializable {
    private static final long serialVersionUID = 1L;
    private final static String TAG = "ContentContainer";

    private List<Content> contentList = new ArrayList<>();

    @Override
    public List<Content> getContent(@NotNull AppCompatActivity activity, boolean editable) {
        return contentList;
    }

    /**
     * Default constructor with empty content list,
     *  useful when overriding getContent method.
     */
    public ContentContainer() {

    }

    /**
     * Create a content container with a specified content list.
     *
     * @param contentList List of content to fill content container
     */
    public ContentContainer(@Nullable List<Content> contentList) {
        if (contentList != null) {
            setContent(contentList);
        }
    }

    /**
     * Set the content list of the content container.
     *
     * @param contentList List of content to store
     */
    @Override
    public void setContent(@Nullable List<Content> contentList) {
        if (contentList != null) {
            this.contentList = contentList;
        } else {
            this.contentList = new ArrayList<>();
        }
    }

    /**
     * Add content to the content list.
     *
     * @param content Content element to add to the content list
     */
    protected void addContent(Content content) {
        contentList.add(content);
    }

    /**
     * Remove content from the content list.
     *
     * @param content Content element to remove from content list
     */
    protected void removeContent(Content content) {
        contentList.remove(content);
    }

    @Override
    public View addView(@NotNull AppCompatActivity activity,
                 @NotNull ViewGroup viewGroup, boolean editable) {
        LinearLayout contentContainerView = new LinearLayout(activity);
        contentContainerView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentContainerView.setOrientation(LinearLayout.VERTICAL);

        for (Content content: getContent(activity, editable)) {
            content.addView(activity, contentContainerView, editable);
        }

        viewGroup.addView(contentContainerView);

        return contentContainerView;
    }

}
