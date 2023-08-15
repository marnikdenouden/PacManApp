package com.example.pacmanapp.activities.edit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.inspect.InspectActivity;
import com.example.pacmanapp.activities.save.CreateSaveDialog;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.selectables.InfoEdit;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.Selector;

import org.jetbrains.annotations.NotNull;

public class EditActivity extends AppCompatActivity {
    private Selector selector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit); // TODO add "Add hint" button to layout and activity.
        selector = AcceptAllSelector.getAcceptAllSelector(R.id.editAllSelector,
                new InfoEdit(getResources()));
        NavigationBar.configure(this, PageType.EDIT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Selectable selected = selector.getSelected();
        SelectableContent.setData(this, selected, true);
        ImageView iconView = findViewById(R.id.selectable_icon);
        iconView.setOnClickListener(view ->
                InspectActivity.open(EditActivity.this, selected));
    }

    public static void open(@NotNull AppCompatActivity currentActivity,
                            @NotNull Selectable selectable) {
        AcceptAllSelector.getAcceptAllSelector(R.id.editAllSelector,
                new InfoEdit(currentActivity.getResources()));
        SelectionCrier.getInstance().select(selectable);
        Navigate.navigate(currentActivity, EditActivity.class);
    }

}
