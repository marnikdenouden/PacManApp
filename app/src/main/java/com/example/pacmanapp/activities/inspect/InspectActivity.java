package com.example.pacmanapp.activities.inspect;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.navigation.Navigate;
import com.example.pacmanapp.selection.SelectionCrier;
import com.example.pacmanapp.selection.selectables.InfoEdit;
import com.example.pacmanapp.selection.selectables.InfoInspect;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.Selector;

import org.jetbrains.annotations.NotNull;

public class InspectActivity extends AppCompatActivity {

    private Selector selector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        selector = AcceptAllSelector.getAcceptAllSelector(R.id.inspectAllSelector,
                new InfoInspect(getResources()));
        NavigationBar.configure(this, PageType.INSPECT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Selectable selected = selector.getSelected();
        ViewGroup viewGroup = findViewById(R.id.inspect_activity);
        SelectableContent.setData(this, viewGroup, selected, false);
        ImageView iconView = findViewById(R.id.selectable_icon);
        iconView.setOnClickListener(view -> finish());
    }
// TODO add java doc comment
    public static void open(@NotNull AppCompatActivity currentActivity,
                            @NotNull Selectable selectable) {
        AcceptAllSelector.getAcceptAllSelector(R.id.inspectAllSelector,
                new InfoEdit(currentActivity.getResources()));
        SelectionCrier.getInstance().select(selectable);
        Navigate.navigate(currentActivity, InspectActivity.class);
    }
}
