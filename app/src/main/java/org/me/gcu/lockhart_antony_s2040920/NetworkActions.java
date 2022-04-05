package org.me.gcu.lockhart_antony_s2040920;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkActions extends Activity {
    public ArrayList<Item> loadedItems = new ArrayList<>();
    public ArrayList<Item> loadedAllItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Intent intent = getIntent();
        String urlSource = intent.getStringExtra(MainActivity.EXTRA_FEED);

        new Thread(new Task(urlSource)).start();

    }

    private class Task implements Runnable
    {
        private final String feed;
        public Task(String datafeed)
        {
            feed = datafeed;
        }
        @Override
        public void run()
        {
            try {
                if(feed.contains("all")){
                    loadedItems = loadAllXmlFromNetwork();
                }
                else{
                loadedItems = loadXmlFromNetwork(feed);}
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            Log.e("MyTag","in run");
            NetworkActions.this.runOnUiThread(this::run2);
        }

        private void run2() {
            setContentView(R.layout.activity_results);
            ArrayAdapter<Item> adapter;
            ListView lv = findViewById(R.id.listView1);
            adapter = new ItemAdapter(NetworkActions.this, R.layout.list_item, loadedItems
            );
            TextView itemCount = (TextView) findViewById(R.id.itemCount);
            itemCount.setText(new StringBuilder().append("Displaying ").append(String.valueOf(loadedItems.size() + " items")).toString());
            lv.setAdapter(adapter);

        }
    }

    private ArrayList<Item> loadXmlFromNetwork(String urlString)
            throws XmlPullParserException, IOException {

        List<Item> items;
        try (InputStream stream = downloadUrl(urlString)) {
            items = PullParser.parse(stream);
            loadedItems.addAll(items);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }

        // Store retrieve each item and format it as an html string
        return loadedItems;
    }

    private ArrayList<Item> loadAllXmlFromNetwork()
            throws XmlPullParserException, IOException {
        String[] urls = new ArrayList<>(Arrays.asList( "https://trafficscotland.org/rss/feeds/currentincidents.aspx", "https://trafficscotland.org/rss/feeds/roadworks.aspx","https://trafficscotland.org/rss/feeds/plannedroadworks.aspx")).toArray(new String[0]);
        List<Item> items;
        for (String url: urls
             ) {try (InputStream stream = downloadUrl(url)) {
            items = PullParser.parse(stream);
            loadedAllItems.addAll(items);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.

        }

        }

        // Store retrieve each item and format it as an html string
        return loadedAllItems;
    }


    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

}
