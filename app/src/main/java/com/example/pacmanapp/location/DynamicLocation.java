package com.example.pacmanapp.location;

public interface DynamicLocation {

    /**
     * Provides location observer with location notifications.
     *
     * @param locationObserver Location observer to add to listeners
     */
    public void addObserver(LocationObserver locationObserver);
}
