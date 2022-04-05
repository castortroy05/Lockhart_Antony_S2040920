package org.me.gcu.lockhart_antony_s2040920;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

//import org.me.gcu.lockhart_antony_s2040920.Item;

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
        Log.e("FULLINFO", item.description.toString());

        if(item.description.contains("<br />")){
        Log.e("Inside If",item.description.toString());
            String[] info = item.description.split("<br />");

            Log.e("FULLINFO", item.description.toString());

            String endDateStr = "";
            String startDateStr;
            String delayInformation = "";

                    startDateStr = info[0];

            if(info.length > 1)
                endDateStr = info[1];

            if(info.length>2){

                String[] di = info[2].split(": ");


                if(di.length>1)
                    delayInformation = di[1];
            }

            startDateStr = startDateStr.substring(12);
            startDateStr.trim();
            //startDateStr = startDateStr.
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
            delayInfoLabel.setVisibility(View.VISIBLE);
            txtDelayInfo.setText(info[2]);
            txtStartDate.setText(startDate1.toString());
            txtEndDate.setText(endDate1.toString());
            itemName.setText(item.title);
            if((info[2].contains("TYPE")) || (info[2].contains("Works"))){
                itemName.setBackgroundResource(R.color.RoyalBlue);
            }
            else {
                itemName.setBackgroundResource(R.color.CornflowerBlue);
            }
            itemDescription.setText(item.description);
            itemDescription.setVisibility(View.GONE);
            descriptionLabel.setVisibility(View.GONE);
            itemLink.setText(item.link);
            itemLink.setVisibility(View.GONE);
            String[] latlong = item.location.split(" ");
            StringBuilder locationLink = new StringBuilder();
            locationLink.append("https://www.google.com/maps?z=12&t=k&q=").append("loc:").append(latlong[0]).append("+").append(latlong[1]);
            itemLocation.setText(locationLink);
            return convertView;
//            this.endDate = endDate;
        }
else {
            txtDelayInfo.setVisibility(View.GONE);
            delayInfoLabel.setVisibility(View.GONE);
            txtStartDate.setVisibility(View.GONE);
            startDateLabel.setVisibility(View.GONE);
            txtEndDate.setVisibility(View.GONE);
            endDateLabel.setVisibility(View.GONE);
            itemName.setText(item.title);
            itemName.setBackgroundResource(R.color.SteelBlue);
            itemDescription.setText(item.description);
            itemLink.setText(item.link);
            itemLink.setVisibility(View.GONE);
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
