package com.example.pacmanapp.activities.map;

import com.example.pacmanapp.activities.general.ConfirmDialog;

public class RemoveMarkerDialog extends ConfirmDialog<RemoveMarkerDialog.Remove> {
    /**
     * Construct a remove marker dialog,
     * which requires creating activity to implement the Remove interface.
     */
    public RemoveMarkerDialog() {
        super(RemoveMarkerDialog.Remove.class,
                "Do you confirm to remove the selected marker?", "Remove");
    }

    @Override
    public void onConfirm(Remove activity) {
        activity.removeMarker();
    }

    public interface Remove {
        void removeMarker();
    }
}
