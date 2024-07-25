package org.me.gcu.lockhart_antony_s2040920;

import androidx.annotation.NonNull;

import java.util.Objects;

public class TravelTimeMeasurement extends DatexItem {
    private String siteReference;
    private String measurementTime;
    private double travelTime;
    private double freeFlowTravelTime;

    public TravelTimeMeasurement(String id, String publicationTime, String siteReference,
                                 String measurementTime, double travelTime, double freeFlowTravelTime) {
        super(id, publicationTime);
        this.siteReference = siteReference;
        this.measurementTime = measurementTime;
        this.travelTime = travelTime;
        this.freeFlowTravelTime = freeFlowTravelTime;
    }

    // Getters and Setters
    public String getSiteReference() { return siteReference; }
    public void setSiteReference(String siteReference) { this.siteReference = siteReference; }

    public String getMeasurementTime() { return measurementTime; }
    public void setMeasurementTime(String measurementTime) { this.measurementTime = measurementTime; }

    public double getTravelTime() { return travelTime; }
    public void setTravelTime(double travelTime) { this.travelTime = travelTime; }

    public double getFreeFlowTravelTime() { return freeFlowTravelTime; }
    public void setFreeFlowTravelTime(double freeFlowTravelTime) { this.freeFlowTravelTime = freeFlowTravelTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TravelTimeMeasurement that = (TravelTimeMeasurement) o;
        return Double.compare(that.travelTime, travelTime) == 0 &&
                Double.compare(that.freeFlowTravelTime, freeFlowTravelTime) == 0 &&
                Objects.equals(siteReference, that.siteReference) &&
                Objects.equals(measurementTime, that.measurementTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), siteReference, measurementTime, travelTime, freeFlowTravelTime);
    }

    @NonNull
    @Override
    public String toString() {
        return "TravelTimeMeasurement{" +
                "id='" + getId() + '\'' +
                ", publicationTime='" + getPublicationTime() + '\'' +
                ", siteReference='" + siteReference + '\'' +
                ", measurementTime='" + measurementTime + '\'' +
                ", travelTime=" + travelTime +
                ", freeFlowTravelTime=" + freeFlowTravelTime +
                '}';
    }
}