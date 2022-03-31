package org.me.gcu.lockhart_antony_s2040920;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.me.gcu.lockhart_antony_s2040920.PullParser.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private final List<Item> itemList;
    private int layoutResource;
    public ItemAdapter(Context context, int resource, List<Item> itemList) {
        super(context, resource, itemList);
        this.layoutResource =resource;
        this.itemList = itemList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView itemName = convertView.findViewById(R.id.title);
        TextView itemDescription = convertView.findViewById(R.id.description);
        itemName.setText(item.title);
        itemDescription.setText(item.description);
        return convertView;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

}
