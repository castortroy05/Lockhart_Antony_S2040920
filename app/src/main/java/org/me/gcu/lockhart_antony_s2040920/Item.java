package org.me.gcu.lockhart_antony_s2040920;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a generic item of traffic information with various attributes.
 */
public class Item {
    private static final String DATE_FORMAT = "EEEE, dd MMMM yyyy - HH:mm";

    private final String id;
    private final String title;
    private final String description;
    private final String location;
    private final String publicationTime;
    private Date startDate;
    private Date endDate;

    /**
     * Constructs an Item with the given attributes.
     *
     * @param id              The unique identifier of the item
     * @param title           The title or main content of the item
     * @param description     The description of the item (can be null)
     * @param location        The location associated with the item
     * @param publicationTime The publication time of the item
     */
    public Item(String id, String title, String description, String location, String publicationTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.publicationTime = publicationTime;
        parseDescription();
    }

    /**
     * Parses the description to extract start and end dates if available.
     */
    private void parseDescription() {
        if (description != null) {
            String[] parts = description.split("<br />");
            for (String part : parts) {
                if (part.startsWith("Start Date:")) {
                    startDate = parseDate(part.substring("Start Date:".length()).trim());
                } else if (part.startsWith("End Date:")) {
                    endDate = parseDate(part.substring("End Date:".length()).trim());
                }
            }
        }
    }

    /**
     * Parses a date string into a Date object.
     *
     * @param dateStr The date string to parse
     * @return The parsed Date object, or null if parsing fails
     */
    private Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.UK);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calculates the number of days between the start and end dates.
     *
     * @return The number of days, or 0 if either date is null
     */
    public int getDaysToComplete() {
        if (startDate != null && endDate != null) {
            long diff = endDate.getTime() - startDate.getTime();
            return (int) (diff / (24 * 60 * 60 * 1000));
        }
        return 0;
    }

    // Getter methods
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getPublicationTime() { return publicationTime; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }

    // Setter methods for dates (in case they need to be set manually)
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(title, item.title) &&
                Objects.equals(description, item.description) &&
                Objects.equals(location, item.location) &&
                Objects.equals(publicationTime, item.publicationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, location, publicationTime);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", publicationTime='" + publicationTime + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}