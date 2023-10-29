package com.example.pacmanapp.activities.edit;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.contents.Content;
import com.example.pacmanapp.contents.ContentContainer;
import com.example.pacmanapp.contents.ContentType;
import com.example.pacmanapp.contents.RemoveContent;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.SelectablePlatform;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.selectables.InfoEdit;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.Selector;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity
        implements CreateContentDialog.contentConstructor, SelectablePlatform {
    private final String TAG = "EditActivity";
    private Selector selector;
    private Selectable selected;
    private List<Content> savedContentList;
    private List<RemoveContent> removeContentList;
    private boolean isRemovingContent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        selector = AcceptAllSelector.getSelector(R.id.editAllSelector,
                new InfoEdit(getResources()));
        NavigationBar.configure(this, PageType.EDIT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        selected = selector.getSelected();
        SelectableContent.setData(this, getViewGroup(), selected, true);
        ImageView iconView = findViewById(R.id.selectable_icon);
        iconView.setOnClickListener(view ->
                InspectActivity.open(EditActivity.this, selected));
        configureEditButtons();
    }

    /**
     * Open edit activity with specified selectable.
     *
     * @param currentActivity Current activity to open edit activity from
     * @param selectable Selectable to edit in the edit activity
     */
    public static void open(@NotNull AppCompatActivity currentActivity,
                            @NotNull Selectable selectable) {
        AcceptAllSelector.getSelector(R.id.editAllSelector,
                new InfoEdit(currentActivity.getResources()));
        SelectionCrier.getInstance().select(selectable);
        Navigate.navigate(currentActivity, EditActivity.class);
    }

    public void configureEditButtons() {
        Button leftButton = findViewById(R.id.button_left);
        Button rightButton = findViewById(R.id.button_right);

        if (!isRemovingContent) {
            leftButton.setText("Add");
            leftButton.setOnClickListener(view -> addContent());
            rightButton.setText("Remove");
            rightButton.setOnClickListener(view -> startContentRemoving());
        } else {
            leftButton.setText("Cancel");
            leftButton.setOnClickListener(view -> cancelRemoving());
            rightButton.setText("Confirm");
            rightButton.setOnClickListener(view -> confirmRemoving());
        }
        Log.d(TAG, "Configured edit buttons");
    }

    public void addContent() {
        new CreateContentDialog().show(getSupportFragmentManager(), "CreateContent");
        Log.d(TAG, "Opened dialog to add content");
    }

    public void startContentRemoving() {
        Log.d(TAG, "Started content removing");
        isRemovingContent = true;
        savedContentList = selected.getContent(this, true);

        // Replace the content displayed with the remove content elements.
        removeContentList = new ArrayList<>();
        List<Content> contentList = new ArrayList<>();
        for (Content content: savedContentList) {
            RemoveContent removeContent = new RemoveContent(content);
            removeContentList.add(removeContent);
            contentList.add(removeContent);
        }
        setContentList(contentList);

        configureEditButtons();
    }

    public void cancelRemoving() {
        Log.d(TAG, "Canceled removing of content");
        setContentList(savedContentList);
        stopContentRemoving();
    }

    public void confirmRemoving() {
        Log.d(TAG, "Confirmed removing of content");
        List<Content> newContentList = new ArrayList<>();
        for (RemoveContent removeContent: removeContentList) {
            if (!removeContent.isRemoved()) {
                newContentList.add(removeContent.getContent());
            }
        }
        setContentList(newContentList);
        selected.setContent(newContentList);
        stopContentRemoving();
    }

    public void stopContentRemoving() {
        Log.d(TAG, "Ended content removing");
        isRemovingContent = false;
        configureEditButtons();
    }

    public void setContentList(@NotNull List<Content> contentList) {
        ContentContainer contentContainer = new ContentContainer(contentList);
        SelectableContent.setContent(this, getViewGroup(), contentContainer, true);
        Log.d(TAG, "Updated content list");
    }

    /**
     * Get the view group for this activity.
     */
    public ViewGroup getViewGroup() {
        return findViewById(R.id.edit_activity);
    }

    @Override
    public void create(@NonNull ContentType contentType) {
        contentType.addContent(selected, this);
        SelectableContent.setContent(this, getViewGroup(), selected, true);
    }

    @Override
    public Selectable getSelected() {
        return selected;
    }
}
