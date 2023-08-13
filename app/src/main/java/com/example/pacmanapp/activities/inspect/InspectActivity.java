package com.example.pacmanapp.activities.inspect;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.selection.selectables.InfoInspect;
import com.example.pacmanapp.navigation.NavigationBar;
import com.example.pacmanapp.navigation.PageType;
import com.example.pacmanapp.selection.AcceptAllSelector;
import com.example.pacmanapp.selection.Selectable;
import com.example.pacmanapp.selection.SelectableContent;
import com.example.pacmanapp.selection.Selector;

public class InspectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        Selector selector = AcceptAllSelector.getAcceptAllSelector(R.id.inspectAllSelector,
                new InfoInspect(getResources()));
        Selectable selected = selector.getSelected();
        SelectableContent.setData(this, selected, false);
        NavigationBar.configure(this, PageType.INSPECT);
    }
}
