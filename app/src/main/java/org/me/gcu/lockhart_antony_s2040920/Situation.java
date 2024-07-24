package org.me.gcu.lockhart_antony_s2040920;

import java.util.Objects;

public class Situation extends DatexItem {
    private SituationRecord situationRecord;

    public Situation(String id, String publicationTime, SituationRecord situationRecord) {
        super(id, publicationTime);
        this.situationRecord = situationRecord;
    }

    public SituationRecord getSituationRecord() {
        return situationRecord;
    }

    public void setSituationRecord(SituationRecord situationRecord) {
        this.situationRecord = situationRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Situation situation = (Situation) o;
        return Objects.equals(situationRecord, situation.situationRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), situationRecord);
    }

    @Override
    public String toString() {
        return "Situation{" +
                "id='" + getId() + '\'' +
                ", publicationTime='" + getPublicationTime() + '\'' +
                ", situationRecord=" + situationRecord +
                '}';
    }
}