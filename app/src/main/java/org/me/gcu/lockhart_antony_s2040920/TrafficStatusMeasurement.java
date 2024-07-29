package org.me.gcu.lockhart_antony_s2040920;

public class TrafficStatusMeasurement extends DatexItem {
    private String id;
    private String publicationTime;
    private String siteReference;
    private String measurementTime;
    private String trafficStatus;

    public TrafficStatusMeasurement(String id, String publicationTime, String siteReference, String measurementTime, String trafficStatus) {
        this.id = id;
        this.publicationTime = publicationTime;
        this.siteReference = siteReference;
        this.measurementTime = measurementTime;
        this.trafficStatus = trafficStatus;
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

    public String getSiteReference() {
        return siteReference;
    }

    public void setSiteReference(String siteReference) {
        this.siteReference = siteReference;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public String getTrafficStatus() {
        return trafficStatus;
    }

    public void setTrafficStatus(String trafficStatus) {
        this.trafficStatus = trafficStatus;
    }
}