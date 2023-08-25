package com.example.pacmanapp.contents;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanapp.R;
import com.example.pacmanapp.activities.edit.EditHintDialog;
import com.example.pacmanapp.selection.Selectable;

import org.jetbrains.annotations.NotNull;

public class HintEdit implements Content {
    private Content content;
    private final Selectable hintProvider;
    private String hintText = "";
    private String key = "";
    private Selectable hintTarget;

    // Variables used to update the last added view of this edit hint.
    private transient View view;
    private transient ViewGroup viewGroup;
    private transient AppCompatActivity activity;
    private transient boolean editable;

    /**
     * Create edit hint that edit its hint content.
     *
     * @param hintProvider Selectable that provides the hint and any clues to the optional key
     */
    public HintEdit(@NotNull Selectable hintProvider) {
        this.hintProvider = hintProvider;
        // Assume the hint provider is the same as the target so hint provider is never null.
        this.hintTarget = hintProvider;
        new HintEditor().save();
    }

    @Override
    public View addView(@NonNull AppCompatActivity activity, @NonNull ViewGroup viewGroup, boolean editable) {
        this.viewGroup = viewGroup;
        this.activity = activity;
        this.editable = editable;

        if (editable) {
            View.OnClickListener onClickListener = view -> new EditHintDialog(activity, this)
                    .show(activity.getSupportFragmentManager(), "EditHint");
            view = Util.addButton(activity, viewGroup, true, R.drawable.icon_edit, content,
            onClickListener);
        } else {
            view = content.addView(activity, viewGroup, false);
        }
        return view;
    }

    /**
     * Get a new hint editor based on the current edit hint values.
     *
     * @return HintEditor that is created based on current edit hint values.
     */
    public HintEditor getHintEditor() {
        return new HintEditor();
    }

    public class HintEditor {
        private String hintText = "";
        private String key = "";
        private Selectable hintTarget;

        /**
         * Create a new hint editor based on the current values of the edit hint.
         */
        private HintEditor() {
            hintText = HintEdit.this.hintText;
            key = HintEdit.this.key;
            hintTarget = HintEdit.this.hintTarget;
        }

        /**
         * Set the hint text.
         *
         * @param hint Text for the hint to display
         */
        public void setHintText(String hint) {
            this.hintText = hint;
        }

        /**
         * Get hint text that is displayed in the edit hint.
         *
         * @return hintText String that is displayed in the edit hint
         */
        public String getHintText() {
            return hintText;
        }

        /**
         * Set key for the hint, remove lock by passing empty string.
         *
         * @param key Key to set for the hint
         */
        public void setKey(@NotNull String key) {
            this.key = key;
        }

        /**
         * Key that is optionally set to lock the hint.
         *
         * @return key string that locks the hint if not an empty string
         */
        public String getKey() {
            return key;
        }

        /**
         * Get hint provider selectable that displays the edit hint.
         *
         * @return Hint provider selectable
         */
        public Selectable getHintProvider() {
            return hintProvider;
        }

        /**
         * Set the hint target selectable that the hint gives location clues for.
         *
         * @param hintTarget Selectable that hint is given for
         */
        public void setHintTarget(@NotNull Selectable hintTarget) {
            this.hintTarget = hintTarget;
        }

        /**
         * Get the hint target selectable that the hint is written for.
         *
         * @return hintTarget selectable that the hint is written for
         */
        public Selectable getHintTarget() {
            return hintTarget;
        }

        /**
         * Finish editing the hint and save it in the edit hint content.
         */
        public void save() {
            HintEdit.this.hintText = hintText;
            HintEdit.this.key = key;
            HintEdit.this.hintTarget = hintTarget;

            HintBuilder hintBuilder = new HintBuilder(hintTarget);
            if (!hintText.equals("")) {
                hintBuilder.setHintText(hintText);
            }
            Content hint = hintBuilder.build();
            content = hint;

            if (!key.equals("")) {
                content = new LockedContent(hint, HintEdit.this.hintProvider, key);
            }
        }

        // TODO it requires a lot to update the content from within, maybe this can be simplified and reworked overall?
        //  Maybe add a content storage that you can get from selectables. Instead of just returning a lost of content.
        public void updateLastAddedView() {
            int index = viewGroup.indexOfChild(view);
            viewGroup.removeView(view);
            View updatedView = addView(activity, viewGroup, editable);
            // Remove and add the view at its previous index.
            viewGroup.removeView(updatedView);
            viewGroup.addView(updatedView, index);
        }

    }

}
