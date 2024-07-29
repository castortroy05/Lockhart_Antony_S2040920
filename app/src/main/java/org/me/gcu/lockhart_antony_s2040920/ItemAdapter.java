package org.me.gcu.lockhart_antony_s2040920;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<DatexItem> {

    private final List<DatexItem> itemList;

    public ItemAdapter(Context context, int resource, List<DatexItem> itemList) {
        super(context, resource, itemList);
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        DatexItem item = getItem(position);
        if (item == null) return convertView;

        TextView itemName = convertView.findViewById(R.id.title);
        TextView itemDescription = convertView.findViewById(R.id.description);
        TextView itemLocation = convertView.findViewById(R.id.location);
        TextView txtDelayInfo = convertView.findViewById(R.id.txtDelayInfo);
        TextView txtStartDate = convertView.findViewById(R.id.txtStartDate);
        TextView txtEndDate = convertView.findViewById(R.id.txtEndDate);
        TextView delayDuration = convertView.findViewById(R.id.delayDuration);

        itemName.setText(item.getId());
        itemDescription.setText(item.getPublicationTime());

        if (item instanceof Roadwork roadwork) {
            setupRoadworkView(roadwork, itemName, itemDescription, txtStartDate, txtEndDate, delayDuration);
        } else if (item instanceof UnplannedEvent unplannedEvent) {
            setupUnplannedEventView(unplannedEvent, itemName, itemDescription, txtDelayInfo);
        } else if (item instanceof TrafficStatusMeasurement trafficStatusMeasurement) {
            setupTrafficStatusView(trafficStatusMeasurement, itemName, itemDescription);
        } else if (item instanceof TravelTimeMeasurement travelTimeMeasurement) {
            setupTravelTimeView(travelTimeMeasurement, itemName, itemDescription);
        } else if (item instanceof VMSUnit vmsUnit) {
            setupVMSView(vmsUnit, itemName, itemDescription);
        }

        setupLocationLink(item, itemLocation);

        return convertView;
    }

    private void setupRoadworkView(Roadwork roadwork, TextView itemName, TextView itemDescription,
                                   TextView txtStartDate, TextView txtEndDate, TextView delayDuration) {
        itemName.setText(roadwork instanceof CurrentRoadwork ? R.string.current_roadwork : R.string.future_roadwork);
        itemDescription.setText(roadwork.getDescription());
        txtStartDate.setText(formatDate(roadwork.getStartDate()));
        txtEndDate.setText(formatDate(roadwork.getEndDate()));

        long daysToComplete = calculateDaysBetween(roadwork.getStartDate(), roadwork.getEndDate());
        delayDuration.setText(String.valueOf(daysToComplete));
        setDelayDurationColor(delayDuration, daysToComplete);
    }

    private void setupUnplannedEventView(UnplannedEvent event, TextView itemName, TextView itemDescription,
                                         TextView txtDelayInfo) {
        itemName.setText(R.string.unplanned_event);
        itemDescription.setText(event.getDescription());
        txtDelayInfo.setText(event.getDescription());
    }

    private void setupTrafficStatusView(TrafficStatusMeasurement status, TextView itemName, TextView itemDescription) {
        itemName.setText(R.string.traffic_status);
        itemDescription.setText(status.getTrafficStatus());
    }

    private void setupTravelTimeView(TravelTimeMeasurement travelTime, TextView itemName, TextView itemDescription) {
        itemName.setText(R.string.travel_time);
        itemDescription.setText(String.format(Locale.getDefault(),
                getContext().getString(R.string.travel_time_format),
                travelTime.getTravelTime(), travelTime.getFreeFlowTravelTime()));
    }

    private void setupVMSView(VMSUnit vmsUnit, TextView itemName, TextView itemDescription) {
        itemName.setText(R.string.vms_message);
        if (!vmsUnit.getMessages().isEmpty()) {
            itemDescription.setText(vmsUnit.getMessages().get(0).getTextContent());
        }
    }

    private void setupLocationLink(DatexItem item, TextView itemLocation) {
        String location = "";
        if (item instanceof Roadwork roadwork) {
            location = roadwork.getLocation();
        } else if (item instanceof UnplannedEvent unplannedEvent) {
            location = unplannedEvent.getLocation();
        }

        if (!location.isEmpty()) {
            String[] latlong = location.split(" ");
            if (latlong.length == 2) {
                String locationLink = "https://www.google.com/maps?z=12&t=k&q=loc:" + latlong[0] + "+" + latlong[1];
                itemLocation.setText(locationLink);
            }
        }
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.UK);
        return date.format(formatter);
    }

    private long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    private void setDelayDurationColor(TextView delayDuration, long days) {
        int colorResId;
        if (days < 7) {
            colorResId = R.color.green_yellow;
        } else if (days < 30) {
            colorResId = R.color.orange;
        } else {
            colorResId = R.color.red;
        }
        delayDuration.setTextColor(ContextCompat.getColor(getContext(), colorResId));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
}