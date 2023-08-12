package com.example.pacmanapp.location;

import org.jetbrains.annotations.NotNull;

public interface DynamicLocation {

    /**
     * Provides location observer with location notifications.
     *
     * @param locationObserver Location observer to add to listeners
     */
    void addObserver(@NotNull LocationObserver locationObserver);

    /**
     * Removes location observer.
     *
     * @param locationObserver Location observer to remove from listeners
     */
    void removeObserver(@NotNull LocationObserver locationObserver);
}
