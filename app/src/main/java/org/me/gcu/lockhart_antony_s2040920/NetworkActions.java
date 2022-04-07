package org.me.gcu.lockhart_antony_s2040920;


import static org.me.gcu.lockhart_antony_s2040920.MainActivity.currentIncidents;
import static org.me.gcu.lockhart_antony_s2040920.MainActivity.currentroadworks;
import static org.me.gcu.lockhart_antony_s2040920.MainActivity.plannedroadworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NetworkActions extends Activity {
    public ArrayList<Item> loadedItems = new ArrayList<>();
    public ArrayList<Item> loadedAllItems = new ArrayList<>();
    HashMap<Item, List<Item>> itemsHashmap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Intent intent = getIntent();
        String urlSource = intent.getStringExtra(MainActivity.EXTRA_FEED);
        Button searchButton = findViewById(R.id.searchButton);
        EditText searchBox = findViewById(R.id.searchBox);
        searchButton.setOnClickListener(
                view -> {
                    Log.v("EditText", searchBox.getText().toString());
                    search(searchBox.getText().toString());
                });



        new Thread(new Task(urlSource)).start();

        


    }

    public void searchItems(View view) {
        EditText searchBox = findViewById(R.id.searchBox);
        String searchText = searchBox.getText().toString();
        search(searchText);
    }

    public void readAllFeeds(View view) {
        loadList("all");
    }

    private void loadList(String list) {
        if(loadedItems.isEmpty()) {
            new Thread(new Task(list)).start();
        }
        else{
            loadedItems.clear();
            new Thread(new Task(list)).start();
        }
        }

    public void readCurrent(View view) {
        loadList(currentroadworks);

    }

    public void readPlanned(View view) {
        loadList(plannedroadworks);

    }

    public void readIncidents(View view) {
        loadList(currentIncidents);

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

        @SuppressLint("SetTextI18n")
        private void run2() {
            setContentView(R.layout.activity_results);
            ArrayAdapter<Item> adapter;
            ListView lv = findViewById(R.id.listView1);
            adapter = new ItemAdapter(NetworkActions.this, R.layout.list_item, loadedItems
            );
            TextView itemCount = findViewById(R.id.itemCount);
            //pass arraylist with uuid to map method
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Map<Item, List<Item>> map = loadedItems.stream().collect(Collectors.groupingBy(Item::getUuid));
                Log.e("MyTag", "map: " + map);
                itemsHashmap = (HashMap<Item, List<Item>>) map;
                


            }
            

            
            itemCount.setText("Displaying " + loadedItems.size() + " items");
            lv.setAdapter(adapter);

        }
    }

   //convert array list loadedItems to HashMap using uuid as key
    //then use uuid as key to get list of items with that uuid
       private Map<String, List<Item>> getMap(ArrayList<Item> loadedItems) {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               return loadedItems.stream().collect(Collectors.groupingBy(Item::getUuid));
           }
           return getMap(loadedItems);
           
       }

       //onclick function for search button
       


   

    //get text from searchBox and search for matching items in arraylist
    @SuppressLint("SetTextI18n")
    private void search(String searchText) {
        ArrayAdapter<Item> adapter;
        ListView lv = findViewById(R.id.listView1);
        //search for matching items in arraylist
        List<Item> searchResults = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            searchResults = loadedItems.stream().filter(item -> item.title.toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
        }
        adapter = new ItemAdapter(NetworkActions.this, R.layout.list_item, searchResults);

        TextView itemCount = findViewById(R.id.itemCount);
        //pass arraylist with generated id to map method
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            Map<String, List<Item>> groupedItems = loadedItems.stream().collect(Collectors.groupingBy(Item::getUuid));
//        }
        itemCount.setText("Displaying " + Objects.requireNonNull(searchResults).size() + " items");
        lv.setAdapter(adapter);
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
