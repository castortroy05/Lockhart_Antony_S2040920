package org.me.gcu.lockhart_antony_s2040920;

import java.util.Objects;

public abstract class DatexItem {
    private String id;
    private String publicationTime;

    public DatexItem(String id, String publicationTime) {
        this.id = id;
        this.publicationTime = publicationTime;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatexItem datexItem = (DatexItem) o;
        return Objects.equals(id, datexItem.id) &&
                Objects.equals(publicationTime, datexItem.publicationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicationTime);
    }

    @Override
    public String toString() {
        return "DatexItem{" +
                "id='" + id + '\'' +
                ", publicationTime='" + publicationTime + '\'' +
                '}';
    }
}