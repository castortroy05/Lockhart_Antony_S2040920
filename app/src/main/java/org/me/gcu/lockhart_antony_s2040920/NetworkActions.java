package org.me.gcu.lockhart_antony_s2040920;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * NetworkActions activity handles the loading and display of traffic data.
 * It manages network requests, parses XML data, and presents the information to the user.
 */
public class NetworkActions extends AppCompatActivity {
    private ArrayList<Item> loadedItems = new ArrayList<>();
    private String authHeader;

    /**
     * Initializes the activity, sets up UI components, and starts data loading.
     *
     * @param savedInstanceState If non-null, this activity is being re-initialized after previously being shut down.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        checkConnection();

        Intent intent = getIntent();
        String urlSource = intent.getStringExtra(MainActivity.EXTRA_FEED);
        authHeader = intent.getStringExtra("AUTH_HEADER");

        setupUI();

        new Thread(new Task(urlSource)).start();
    }

    /**
     * Sets up the UI components including buttons and input fields.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setupUI() {
        Button searchButton = findViewById(R.id.searchButton);
        Button dateSearchButton = findViewById(R.id.dateSearchButton);
        EditText dateSearchText = findViewById(R.id.dateSearchText);
        EditText searchBox = findViewById(R.id.searchBox);

        dateSearchText.setInputType(InputType.TYPE_NULL);
        dateSearchText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showDatePickerDialog(dateSearchText);
                v.performClick();
            }
            return true;
        });

        searchButton.setOnClickListener(view -> searchRoad(searchBox.getText().toString()));

        searchBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchRoad(searchBox.getText().toString());
                return true;
            }
            return false;
        });

        dateSearchButton.setOnClickListener(view -> searchDate(dateSearchText.getText().toString()));
    }

    /**
     * Displays a date picker dialog and sets the selected date to the given EditText.
     *
     * @param dateSearchText The EditText to update with the selected date.
     */
    private void showDatePickerDialog(EditText dateSearchText) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(NetworkActions.this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        dateSearchText.setText(String.format(Locale.UK, "%d/%d/%d", dayOfMonth, monthOfYear + 1, year1)),
                year, month, day);
        picker.show();
    }

    /**
     * Searches for items based on the given date.
     *
     * @param dateSearchText The date string to search for.
     */
    @SuppressLint("SimpleDateFormat")
    private void searchDate(String dateSearchText) {
        Date dateShort;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.UK);
        String dateLong = null;
        try {
            dateShort = new SimpleDateFormat("dd/MM/yyyy", Locale.UK).parse(dateSearchText);
            if (dateShort != null) {
                dateLong = sdf.format(dateShort);
            }
        } catch (ParseException e) {
            Log.e("NetworkActions", "Date parsing error", e);
        }
        updateListView(searchResults(dateLong));
    }

    /**
     * Searches for items based on the given search text.
     *
     * @param searchText The text to search for in item titles and descriptions.
     * @return A list of items matching the search criteria.
     */
    private List<Item> searchResults(String searchText) {
        List<Item> searchResults = new ArrayList<>();
        for (Item item : loadedItems) {
            if (item.description != null && (item.title.equalsIgnoreCase(searchText) || item.description.contains(searchText))) {
                searchResults.add(item);
            }
        }
        return searchResults;
    }

    /**
     * Updates the ListView with the given list of items.
     *
     * @param items The list of items to display.
     */
    private void updateListView(List<Item> items) {
        ArrayAdapter<Item> adapter = new ItemAdapter(NetworkActions.this, R.layout.list_item, items);
        ListView lv = findViewById(R.id.listView1);
        TextView itemCount = findViewById(R.id.itemCount);
        itemCount.setText(MessageFormat.format("Displaying {0} items", items.size()));
        lv.setAdapter(adapter);
    }

    /**
     * Searches for road information based on the given search text.
     *
     * @param searchText The text to search for.
     */
    private void searchRoad(String searchText) {
        updateListView(searchResults(searchText));
        hideKeyboard();
    }

    /**
     * Hides the soft keyboard.
     */
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Checks for network connectivity and shows an alert if not available.
     */
    public void checkConnection() {
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Close", (dialogInterface, i) -> finish())
                    .show();
        } else {
            Toast.makeText(NetworkActions.this, "Loading...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens Google Maps with the location specified in the 'location' TextView.
     *
     * @param view The view that triggered this method.
     */
    public void loadMap(View view) {
        TextView location = findViewById(R.id.location);
        String url = location.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    /**
     * Checks if a network connection is available.
     *
     * @return true if a network connection is available, false otherwise.
     */
    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            android.net.NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (
                    capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    /**
     * Asynchronous task for loading XML data from the network.
     */
    private class Task implements Runnable {
        private final String feed;

        public Task(String datafeed) {
            feed = datafeed;
        }

        @Override
        public void run() {
            try {
                if ("all".equals(feed)) {
                    loadedItems = loadAllXmlFromNetwork();
                } else {
                    loadedItems = loadXmlFromNetwork(feed);
                }
            } catch (IOException | XmlPullParserException e) {
                Log.e("NetworkActions", "Error loading XML", e);
            }

            runOnUiThread(this::updateUI);
        }

        private void updateUI() {
            setContentView(R.layout.activity_results);
            updateListView(loadedItems);
        }
    }

    /**
     * Loads XML data from a single network source.
     *
     * @param urlString The URL to load data from.
     * @return A list of Item objects parsed from the XML.
     * @throws XmlPullParserException If there's an error parsing the XML.
     * @throws IOException If there's an error reading from the network.
     */
    private ArrayList<Item> loadXmlFromNetwork(String urlString)
            throws XmlPullParserException, IOException {
        List<Item> items;
        try (InputStream stream = downloadUrl(urlString)) {
            items = DatexParser.parse(stream);
        }
        return new ArrayList<>(items);
    }

    /**
     * Loads XML data from all network sources.
     *
     * @return A list of Item objects parsed from all XML sources.
     * @throws XmlPullParserException If there's an error parsing the XML.
     * @throws IOException If there's an error reading from the network.
     */
    private ArrayList<Item> loadAllXmlFromNetwork()
            throws XmlPullParserException, IOException {
        String[] urls = {
                MainActivity.DATEX_BASE_URL + MainActivity.CURRENT_INCIDENTS,
                MainActivity.DATEX_BASE_URL + MainActivity.CURRENT_ROADWORKS,
                MainActivity.DATEX_BASE_URL + MainActivity.PLANNED_ROADWORKS
        };
        ArrayList<Item> allItems = new ArrayList<>();
        for (String url : urls) {
            allItems.addAll(loadXmlFromNetwork(url));
        }
        return allItems;
    }

    /**
     * Downloads data from a URL.
     *
     * @param urlString The URL to download from.
     * @return An InputStream containing the downloaded data.
     * @throws IOException If there's an error downloading the data.
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setRequestProperty("Authorization", authHeader);
        conn.connect();
        return conn.getInputStream();
    }
}