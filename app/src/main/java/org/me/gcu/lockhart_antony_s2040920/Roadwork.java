package org.me.gcu.lockhart_antony_s2040920;

import java.util.Date;

public abstract class Roadwork {
    protected String id;
    protected String publicationTime;
    protected String description;
    protected String location;
    protected String startDate;
    protected String endDate;

    protected Roadwork(String id, String publicationTime, String description, String location, String startDate, String endDate) {
        this.id = id;
        this.publicationTime = publicationTime;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters (and potentially setters) would go here

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}