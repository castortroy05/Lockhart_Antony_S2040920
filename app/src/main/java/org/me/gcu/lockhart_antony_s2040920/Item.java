package org.me.gcu.lockhart_antony_s2040920;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Item {
    public String title;
    public String link;
    public String description;
    public String location;
    public String pubDate;
    public Date startDate;
    public Date endDate;

    public Item(String title, String link, String description, String location, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.location = location;
        this.pubDate = pubDate;
        parseDescription();
    }

    private void parseDescription() {
        if (description != null && description.contains("Start Date:") && description.contains("End Date:")) {
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

    private Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm", Locale.UK);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getDaysToComplete() {
        if (startDate != null && endDate != null) {
            long diff = endDate.getTime() - startDate.getTime();
            return (int) (diff / (24 * 60 * 60 * 1000));
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(title, item.title) &&
                Objects.equals(description, item.description) &&
                Objects.equals(location, item.location) &&
                Objects.equals(pubDate, item.pubDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, location, pubDate);
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}