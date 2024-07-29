package org.me.gcu.lockhart_antony_s2040920;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchManager {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public List<DatexItem> searchByText(List<DatexItem> items, String searchText) {
        List<DatexItem> results = new ArrayList<>();
        for (DatexItem item : items) {
            if (matchesSearchCriteria(item, searchText)) {
                results.add(item);
            }
        }
        return results;
    }

    public List<DatexItem> searchByDate(List<DatexItem> items, String dateSearchText) {
        Date dateShort;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.UK);
        String dateLong = null;
        try {
            dateShort = new SimpleDateFormat(DATE_FORMAT, Locale.UK).parse(dateSearchText);
            if (dateShort != null) {
                dateLong = sdf.format(dateShort);
            }
        } catch (ParseException e) {
            Log.e("SearchManager", "Date parsing error", e);
        }

        if (dateLong == null) {
            return new ArrayList<>();
        }

        return searchByText(items, dateLong);
    }

    private boolean matchesSearchCriteria(DatexItem item, String searchText) {
        String lowerSearchText = searchText.toLowerCase();

        if (item instanceof Roadwork roadwork) {
            return roadwork.getDescription().toLowerCase().contains(lowerSearchText) ||
                    roadwork.getLocation().toLowerCase().contains(lowerSearchText) ||
                    roadwork.getId().toLowerCase().contains(lowerSearchText);
        }else if (item instanceof UnplannedEvent event) {
            return event.getDescription().toLowerCase().contains(lowerSearchText);
        } else if (item instanceof TrafficStatusMeasurement status) {
            return status.getTrafficStatus().toLowerCase().contains(lowerSearchText);
        } else if (item instanceof TravelTimeMeasurement travelTime) {
            return travelTime.getSiteReference().toLowerCase().contains(lowerSearchText);
        } else if (item instanceof VMSUnit vmsUnit) {
            return vmsUnit.getMessages().stream()
                    .anyMatch(message -> message.getTextContent().toLowerCase().contains(lowerSearchText));
        }
        return false;
    }

    private LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.UK);
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}