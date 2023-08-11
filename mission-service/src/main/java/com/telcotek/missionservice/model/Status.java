package com.telcotek.missionservice.model;

public enum Status {
    TODO("To do"),
    DOING("Doing"),
    DONE("Done");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
