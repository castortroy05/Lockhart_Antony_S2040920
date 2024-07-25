package org.me.gcu.lockhart_antony_s2040920;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<DatexItem> {

    private final List<Item> itemList;

    public ItemAdapter(Context context, int resource, List<Item> itemList) {
        super(context, resource);
        this.itemList = itemList;
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
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

        itemName.setText(item.getId().toUpperCase());
        itemDescription.setText(item.getPublicationTime());

        if (item instanceof Roadwork roadwork) {
            setupRoadworkView(roadwork, itemName, itemDescription, txtStartDate, txtEndDate, delayDuration);
        } else if (item instanceof UnplannedEvent event) {
            setupUnplannedEventView(event, itemName, itemDescription, txtDelayInfo);
        } else if (item instanceof TrafficStatusMeasurement status) {
            setupTrafficStatusView(status, itemName, itemDescription);
        } else if (item instanceof TravelTimeMeasurement travelTime) {
            setupTravelTimeView(travelTime, itemName, itemDescription);
        } else if (item instanceof VMSUnit vmsUnit) {
            setupVMSView(vmsUnit, itemName, itemDescription);
        }

        setupLocationLink(item, itemLocation);

        return convertView;
    }

    private void setupRoadworkView(Roadwork roadwork, TextView itemName, TextView itemDescription,
                                   TextView txtStartDate, TextView txtEndDate, TextView delayDuration) {
        itemName.setText(roadwork instanceof CurrentRoadwork ? "Current Roadwork" : "Future Roadwork");
        itemDescription.setText(roadwork.getDescription());
        txtStartDate.setText(formatDate(roadwork.getStartDate()));
        txtEndDate.setText(formatDate(roadwork.getEndDate()));

        long daysToComplete = calculateDaysBetween(roadwork.getStartDate(), roadwork.getEndDate());
        delayDuration.setText(String.valueOf(daysToComplete));
        setDelayDurationColor(delayDuration, daysToComplete);
    }

    private void setupUnplannedEventView(UnplannedEvent event, TextView itemName, TextView itemDescription,
                                         TextView txtDelayInfo) {
        itemName.setText("Unplanned Event");
        itemDescription.setText(event.getDescription());
        txtDelayInfo.setText(event.getDescription());
    }

    private void setupTrafficStatusView(TrafficStatusMeasurement status, TextView itemName, TextView itemDescription) {
        itemName.setText("Traffic Status");
        itemDescription.setText(status.getTrafficStatus());
    }

    private void setupTravelTimeView(TravelTimeMeasurement travelTime, TextView itemName, TextView itemDescription) {
        itemName.setText("Travel Time");
        itemDescription.setText(String.format(Locale.getDefault(),
                "Travel Time: %.2f, Free Flow Time: %.2f",
                travelTime.getTravelTime(), travelTime.getFreeFlowTravelTime()));
    }

    private void setupVMSView(VMSUnit vmsUnit, TextView itemName, TextView itemDescription) {
        itemName.setText("VMS Message");
        if (!vmsUnit.getMessages().isEmpty()) {
            itemDescription.setText(vmsUnit.getMessages().get(0).getTextContent());
        }
    }

    private void setupLocationLink(DatexItem item, TextView itemLocation) {
        String location = "";
        if (item instanceof Roadwork) {
            location = ((Roadwork) item).getLocation();
        } else if (item instanceof UnplannedEvent) {
            location = ((UnplannedEvent) item).getLocation();
        }

        if (!location.isEmpty()) {
            String[] latlong = location.split(" ");
            if (latlong.length == 2) {
                String locationLink = "https://www.google.com/maps?z=12&t=k&q=loc:" + latlong[0] + "+" + latlong[1];
                itemLocation.setText(locationLink);
            }
        }
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.UK);
        return sdf.format(date);
    }

    private long calculateDaysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return 0;
        long difference = Math.abs(endDate.getTime() - startDate.getTime());
        return difference / (24 * 60 * 60 * 1000);
    }

    private void setDelayDurationColor(TextView delayDuration, long days) {
        int colorResId;
        if (days < 7) {
            colorResId = R.color.GreenYellow;
        } else if (days < 30) {
            colorResId = R.color.Orange;
        } else {
            colorResId = R.color.Red;
        }
        delayDuration.setTextColor(ContextCompat.getColor(getContext(), colorResId));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
}