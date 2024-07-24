package org.me.gcu.lockhart_antony_s2040920;

import java.util.Objects;

public class GroupOfLocations {
    private String locationDescription;
    private double latitude;
    private double longitude;

    public GroupOfLocations(String locationDescription, double latitude, double longitude) {
        this.locationDescription = locationDescription;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getLocationDescription() { return locationDescription; }
    public void setLocationDescription(String locationDescription) { this.locationDescription = locationDescription; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupOfLocations that = (GroupOfLocations) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                Objects.equals(locationDescription, that.locationDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationDescription, latitude, longitude);
    }

    @Override
    public String toString() {
        return "GroupOfLocations{" +
                "locationDescription='" + locationDescription + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}