package org.me.gcu.lockhart_antony_s2040920;

import java.util.Objects;

public class SituationRecord {
    private String id;
    private String creationTime;
    private String versionTime;
    private String type;
    private String probabilityOfOccurrence;
    private Validity validity;
    private Impact impact;
    private String comment;
    private GroupOfLocations groupOfLocations;

    public SituationRecord(String id, String creationTime, String versionTime, String type,
                           String probabilityOfOccurrence, Validity validity, Impact impact,
                           String comment, GroupOfLocations groupOfLocations) {
        this.id = id;
        this.creationTime = creationTime;
        this.versionTime = versionTime;
        this.type = type;
        this.probabilityOfOccurrence = probabilityOfOccurrence;
        this.validity = validity;
        this.impact = impact;
        this.comment = comment;
        this.groupOfLocations = groupOfLocations;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCreationTime() { return creationTime; }
    public void setCreationTime(String creationTime) { this.creationTime = creationTime; }

    public String getVersionTime() { return versionTime; }
    public void setVersionTime(String versionTime) { this.versionTime = versionTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getProbabilityOfOccurrence() { return probabilityOfOccurrence; }
    public void setProbabilityOfOccurrence(String probabilityOfOccurrence) { this.probabilityOfOccurrence = probabilityOfOccurrence; }

    public Validity getValidity() { return validity; }
    public void setValidity(Validity validity) { this.validity = validity; }

    public Impact getImpact() { return impact; }
    public void setImpact(Impact impact) { this.impact = impact; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public GroupOfLocations getGroupOfLocations() { return groupOfLocations; }
    public void setGroupOfLocations(GroupOfLocations groupOfLocations) { this.groupOfLocations = groupOfLocations; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SituationRecord that = (SituationRecord) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creationTime, that.creationTime) &&
                Objects.equals(versionTime, that.versionTime) &&
                Objects.equals(type, that.type) &&
                Objects.equals(probabilityOfOccurrence, that.probabilityOfOccurrence) &&
                Objects.equals(validity, that.validity) &&
                Objects.equals(impact, that.impact) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(groupOfLocations, that.groupOfLocations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationTime, versionTime, type, probabilityOfOccurrence, validity, impact, comment, groupOfLocations);
    }

    @Override
    public String toString() {
        return "SituationRecord{" +
                "id='" + id + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", versionTime='" + versionTime + '\'' +
                ", type='" + type + '\'' +
                ", probabilityOfOccurrence='" + probabilityOfOccurrence + '\'' +
                ", validity=" + validity +
                ", impact=" + impact +
                ", comment='" + comment + '\'' +
                ", groupOfLocations=" + groupOfLocations +
                '}';
    }
}