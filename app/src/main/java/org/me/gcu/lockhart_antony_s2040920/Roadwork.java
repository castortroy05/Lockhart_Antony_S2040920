package org.me.gcu.lockhart_antony_s2040920;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public abstract class Roadwork extends DatexItem {
    protected String description;
    protected String location;
    protected LocalDate startDate;
    protected LocalDate endDate;

    protected Roadwork(String id, String publicationTime, String description, String location, String startDate, String endDate) {
        super(id, publicationTime);
        this.description = description;
        this.location = location;
        this.startDate = parseDate(startDate);
        this.endDate = parseDate(endDate);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = parseDate(startDate);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = parseDate(endDate);
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss", Locale.UK);
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            Log.e("Roadwork", "Error parsing date: " + dateString, e);
            return null;
        }
    }
}