package org.me.gcu.lockhart_antony_s2040920;
//Lockhart_Antony_S2040920

import static org.me.gcu.lockhart_antony_s2040920.MainActivity.currentIncidents;
import static org.me.gcu.lockhart_antony_s2040920.MainActivity.currentroadworks;
import static org.me.gcu.lockhart_antony_s2040920.MainActivity.plannedroadworks;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NetworkActions extends AppCompatActivity {
    public ArrayList<Item> loadedItems = new ArrayList<>();
    public ArrayList<Item> loadedAllItems = new ArrayList<>();
    HashMap<Item, List<Item>> itemsHashmap = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Intent intent = getIntent();
        String urlSource = intent.getStringExtra(MainActivity.EXTRA_FEED);
        Button searchButton = findViewById(R.id.searchButton);
        Button dateSearchButton = findViewById(R.id.dateSearchButton);
        EditText dateSearchText = this.findViewById(R.id.dateSearchText);
        dateSearchText.setInputType(InputType.TYPE_NULL);
        dateSearchText.setOnTouchListener((v, event) -> {
            DatePickerDialog picker;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NetworkActions.this,
                        (DatePicker view, int year1, int monthOfYear, int dayOfMonth) -> dateSearchText.setText(new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year1).toString()), year, month, day);
                picker.show();
            }
            return false;
        });



        EditText searchBox = this.findViewById(R.id.searchBox);
        searchButton.setOnClickListener(
                view -> {
                    Log.v("EditText", searchBox.getText().toString());
                    searchRoad(searchBox.getText().toString());
                });

        searchBox.setOnEditorActionListener((v, actionId, event) -> {
            boolean result = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                EditText searchBox1 = findViewById(R.id.searchBox);
                searchBox1.setInputType(InputType.TYPE_CLASS_TEXT);
                String searchText = searchBox1.getText().toString();
                searchRoad(searchText);
                result = true;
            }
            return result;
        });



        dateSearchButton.setOnClickListener(
                view -> {
                    Log.v("EditText", searchBox.getText().toString());
                    searchDate(dateSearchText.getText().toString());
                });

        new Thread(new Task(urlSource)).start();

        


    }

    @SuppressLint("SimpleDateFormat")
    private void searchDate(String dateSearchText) {

        Date dateShort;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String dateLong = null;
        try {
            dateShort = new SimpleDateFormat("dd/MM/yyyy").parse(dateSearchText);
            assert dateShort != null;
            dateLong = sdf.format(dateShort);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String searchText = dateLong;
                ArrayAdapter<Item> adapter;
        ListView lv = findViewById(R.id.listView1);
        //search for matching items in arraylist
        List<Item> searchResults = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            searchResults = loadedItems.stream().filter(item -> {
               Log.e ("Search Term", searchText);
                assert searchText != null;
                return item.description.toLowerCase().contains(searchText.toLowerCase());
            }).collect(Collectors.toList());
        }
        adapter = new ItemAdapter(NetworkActions.this, R.layout.list_item, searchResults);

        TextView itemCount = findViewById(R.id.itemCount);
        //pass arraylist with generated id to map method
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            Map<String, List<Item>> groupedItems = loadedItems.stream().collect(Collectors.groupingBy(Item::getUuid));
//        }
        itemCount.setText(MessageFormat.format("Displaying {0} items", Objects.requireNonNull(searchResults).size()));
        lv.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void searchItems(View view) {
        EditText searchBox = findViewById(R.id.searchBox);
        String searchText = searchBox.getText().toString();
        searchRoad(searchText);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    public void searchDate(View view) {
        EditText dateSearchText = findViewById(R.id.dateSearchText);
        String searchText = dateSearchText.getText().toString();
        searchDate(searchText);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dateSearchText.getWindowToken(), 0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getDate(View view) {
        EditText dateSearchText = findViewById(R.id.dateSearchText);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker;
        picker = new DatePickerDialog(NetworkActions.this,
                (DatePicker v, int year1, int monthOfYear, int dayOfMonth) -> dateSearchText.setText(new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year1).toString()), year, month, day);
        picker.show();
    }

    public void loadMap(View view) {
        TextView location = findViewById(R.id.location);
        String url = location.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
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


   

    //get text from searchBox and search for matching items in arraylist
    private void searchRoad(String searchText) {
        ArrayAdapter<Item> adapter;
        ListView lv = findViewById(R.id.listView1);
        //search for matching items in arraylist

        List<Item> searchResults = new ArrayList<>();
        for (Item item : loadedItems) {
            if(item.title == null){
                Log.e("Empty Title", item.description.toUpperCase());}
            else{
            Log.e("Searching", item.title);}
            if (item.title !=null && (item.title.equalsIgnoreCase(searchText) || item.title.contains(searchText))) {
                searchResults.add(item);
            }
        }

        Log.e("SearchResults List", String.valueOf(searchResults));
        adapter = new ItemAdapter(NetworkActions.this, R.layout.list_item, searchResults);

        TextView itemCount = findViewById(R.id.itemCount);

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
            if(stream != null) stream.close();
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
            if(stream != null) stream.close();
        }



        }
        Map<Item, List<Item>> map = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            map = loadedItems.stream().collect(Collectors.groupingBy(Item::getUuid));
        }
        Log.e("MyTag", "map: " + map);
        itemsHashmap = (HashMap<Item, List<Item>>) map;

        
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
