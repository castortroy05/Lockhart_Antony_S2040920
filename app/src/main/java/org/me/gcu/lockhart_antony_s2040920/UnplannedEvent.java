package org.me.gcu.lockhart_antony_s2040920;

public class UnplannedEvent extends DatexItem {
    private String description;
    private String location;

    // Parameterless constructor
    public UnplannedEvent() {
        super();
    }

    public UnplannedEvent(String id, String publicationTime, String description, String location) {
        super(id, publicationTime);
        this.description = description;
        this.location = location;
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}