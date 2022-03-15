package org.me.gcu.lockhart_antony_s2040920;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class viewAdapter extends RecyclerView.Adapter<viewAdapter.FeedModelViewHolder> {

        private List<feedItem> mFeedItems;

        public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
            private View viewXML;

            public FeedModelViewHolder(View v) {
                super(v);
                viewXML = v;
            }
        }

    public viewAdapter(List < feedItem > feedItems) {
        mFeedItems = feedItems;
    }

        @Override
        public FeedModelViewHolder onCreateViewHolder (ViewGroup parent,int type){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

        @Override
        public void onBindViewHolder (FeedModelViewHolder holder,int position){
        final feedItem feed_item = mFeedItems.get(position);
        ((TextView) holder.viewXML.findViewById(R.id.titleText)).setText(feed_item.road);
        ((TextView) holder.viewXML.findViewById(R.id.descriptionText)).setText(feed_item.description);
        ((TextView) holder.viewXML.findViewById(R.id.linkText)).setText(feed_item.id);
    }

        @Override
        public int getItemCount () {
        return mFeedItems.size();
    }


    }
