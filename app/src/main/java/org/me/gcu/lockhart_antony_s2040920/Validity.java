package org.me.gcu.lockhart_antony_s2040920;

import java.util.Objects;

public class Validity {
    private String validityStatus;
    private String overallStartTime;
    private String overallEndTime;

    public Validity(String validityStatus, String overallStartTime, String overallEndTime) {
        this.validityStatus = validityStatus;
        this.overallStartTime = overallStartTime;
        this.overallEndTime = overallEndTime;
    }

    // Getters and Setters
    public String getValidityStatus() { return validityStatus; }
    public void setValidityStatus(String validityStatus) { this.validityStatus = validityStatus; }

    public String getOverallStartTime() { return overallStartTime; }
    public void setOverallStartTime(String overallStartTime) { this.overallStartTime = overallStartTime; }

    public String getOverallEndTime() { return overallEndTime; }
    public void setOverallEndTime(String overallEndTime) { this.overallEndTime = overallEndTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Validity validity = (Validity) o;
        return Objects.equals(validityStatus, validity.validityStatus) &&
                Objects.equals(overallStartTime, validity.overallStartTime) &&
                Objects.equals(overallEndTime, validity.overallEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validityStatus, overallStartTime, overallEndTime);
    }

    @Override
    public String toString() {
        return "Validity{" +
                "validityStatus='" + validityStatus + '\'' +
                ", overallStartTime='" + overallStartTime + '\'' +
                ", overallEndTime='" + overallEndTime + '\'' +
                '}';
    }
}