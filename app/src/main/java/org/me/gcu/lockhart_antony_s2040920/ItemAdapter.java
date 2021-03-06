package org.me.gcu.lockhart_antony_s2040920;
//Lockhart_Antony_S2040920

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private final List<Item> itemList;


    public ItemAdapter(Context context, int resource, List<Item> itemList) {
        super(context, resource, itemList);
        this.itemList = itemList;

    }






    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView itemName = convertView.findViewById(R.id.title);
        TextView itemDescription = convertView.findViewById(R.id.description);
        TextView itemLink = convertView.findViewById(R.id.link);
        TextView itemLocation =convertView.findViewById(R.id.location);
        TextView txtDelayInfo = convertView.findViewById(R.id.txtDelayInfo);
        TextView txtStartDate = convertView.findViewById(R.id.txtStartDate);
        TextView txtEndDate = convertView.findViewById(R.id.txtEndDate);
        TextView delayInfoLabel = convertView.findViewById(R.id.delayInfoLabel);
        TextView startDateLabel = convertView.findViewById(R.id.startDateLabel);
        TextView endDateLabel = convertView.findViewById(R.id.endDateLabel);
        TextView descriptionLabel = convertView.findViewById(R.id.descriptionLabel);
        TextView delayDuration = convertView.findViewById(R.id.delayDuration);
        TextView delayDurationLabel = convertView.findViewById(R.id.delayDurationLabel);


        txtDelayInfo.setVisibility(View.VISIBLE);
        delayInfoLabel.setVisibility(View.VISIBLE);
        txtStartDate.setVisibility(View.VISIBLE);
        startDateLabel.setVisibility(View.VISIBLE);
        txtEndDate.setVisibility(View.VISIBLE);
        endDateLabel.setVisibility(View.VISIBLE);
        itemDescription.setVisibility(View.VISIBLE);
        descriptionLabel.setVisibility(View.VISIBLE);
        delayDuration.setVisibility(View.VISIBLE);
        delayDurationLabel.setVisibility(View.VISIBLE);



        if(item.description.contains("<br />")){
            String[] info = item.description.split("<br />");
            String startDateStr = info[0];

            String endDateStr = "";
            if(info.length > 1)
                endDateStr = info[1];

            startDateStr = startDateStr.substring(12);
            endDateStr = endDateStr.substring(10);
            Date startDate = null;
            Date endDate = null;

            try {
                startDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(startDateStr);
                endDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(endDateStr);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            Date startDate1 = startDate;
            Date endDate1 = endDate;
            long difference = 0;
            if (endDate1 != null) {
                difference = Math.abs(startDate1.getTime() - endDate1.getTime());
            }
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);
            if(differenceDates > 7 )
            {
                delayDuration.setTextColor(getContext().getResources().getColor(R.color.Orange));
                delayDurationLabel.setTextColor(getContext().getResources().getColor(R.color.Orange));
            }
            if(differenceDates > 30)
            {
                delayDuration.setTextColor(getContext().getResources().getColor(R.color.Red));
                delayDurationLabel.setTextColor(getContext().getResources().getColor(R.color.Red));
            }
           if(differenceDates < 7)
            {
                delayDuration.setTextColor(getContext().getResources().getColor(R.color.GreenYellow));
                delayDurationLabel.setTextColor(getContext().getResources().getColor(R.color.GreenYellow));
            }

            delayDuration.setText(dayDifference);

            delayInfoLabel.setVisibility(View.VISIBLE);
//            Log.e("Crash", info[1]);
            if(info[2] != null){
            txtDelayInfo.setText(info[2]);
            if(info[2].contains("Delay Information")){delayInfoLabel.setVisibility(View.GONE);}}
            if (startDate1 != null) {
                txtStartDate.setText(startDate1.toString());
            }
            if (endDate1 != null) {
                txtEndDate.setText(endDate1.toString());
            }
            if(item.title == null){item.title = "No title in file";
            itemName.setText(item.title.toUpperCase());}
            else{itemName.setText(item.title.toUpperCase());}

            if((info[2].contains("TYPE")) || (info[2].contains("Works"))){
                itemName.setBackgroundResource(R.color.RoyalBlue);
            }
            else {
                itemName.setBackgroundResource(R.color.CornflowerBlue);
            }
            itemDescription.setText(item.description);
            itemDescription.setVisibility(View.GONE);
            descriptionLabel.setVisibility(View.GONE);
//            delayInfoLabel.setVisibility(View.GONE);
            itemLink.setText(item.link);
            itemLink.setVisibility(View.GONE);
            String[] latlong = item.location.split(" ");
            StringBuilder locationLink = new StringBuilder();
            locationLink.append("https://www.google.com/maps?z=12&t=k&q=").append("loc:").append(latlong[0]).append("+").append(latlong[1]);
            itemLocation.setText(locationLink);
            itemLocation.setVisibility(View.GONE);

            return convertView;
        }
else {
            txtDelayInfo.setVisibility(View.GONE);
            delayInfoLabel.setVisibility(View.GONE);
            txtStartDate.setVisibility(View.GONE);
            startDateLabel.setVisibility(View.GONE);
            txtEndDate.setVisibility(View.GONE);
            endDateLabel.setVisibility(View.GONE);
            if(item.title == null){item.title = "No title in file";

                itemName.setText(item.title.toUpperCase());}
            else{            itemName.setText(item.title.toUpperCase());}
            itemName.setBackgroundResource(R.color.DodgerBlue);
            itemDescription.setText(item.description);
            delayDurationLabel.setVisibility(View.GONE);
            delayDuration.setVisibility(View.GONE);
            itemLink.setText(item.link);
            itemLink.setVisibility(View.GONE);
            itemLocation.setVisibility(View.GONE);
            String[] latlong = item.location.split(" ");
            StringBuilder locationLink = new StringBuilder();
            locationLink.append("https://www.google.com/maps?z=12&t=k&q=").append("loc:").append(latlong[0]).append("+").append(latlong[1]);
            itemLocation.setText(locationLink);
        }


        return convertView;
    }

    @Override
    public int getCount() {
        return itemList.size();

    }

}
