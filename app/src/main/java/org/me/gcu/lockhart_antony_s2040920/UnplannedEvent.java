package org.me.gcu.lockhart_antony_s2040920;

public class UnplannedEvent {
    private final String id;
    private final String publicationTime;
    private final String description;
    private final String location;

    public UnplannedEvent(String id, String publicationTime, String description, String location) {
        this.id = id;
        this.publicationTime = publicationTime;
        this.description = description;
        this.location = location;
    }

    // Getters (and potentially setters) would go here

    public String getId() {
        return id;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }
}