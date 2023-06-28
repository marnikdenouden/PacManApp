package com.example.pacmanapp.location;

public interface LocationPasser {
    /**
     * Pass location updater to child or add child location observers to location updater.
     *
     * @param locationUpdater Location updater to pass or add observers to.
     */
    public abstract void passLocationUpdater(LocationUpdater locationUpdater);
}
