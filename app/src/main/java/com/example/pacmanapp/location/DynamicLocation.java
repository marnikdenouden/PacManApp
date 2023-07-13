package com.example.pacmanapp.location;

public interface DynamicLocation {

    /**
     * Provides location observer with location notifications.
     *
     * @param locationObserver Location observer to add to listeners
     */
    void addObserver(LocationObserver locationObserver);
}
