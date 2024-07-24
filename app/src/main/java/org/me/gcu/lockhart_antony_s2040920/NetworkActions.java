package org.me.gcu.lockhart_antony_s2040920;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NetworkActions activity handles the loading and display of traffic data.
 * It manages network requests, parses XML data, and presents the information to the user.
 */
public class NetworkActions extends AppCompatActivity {
    private final ArrayList<Item> loadedItems = new ArrayList<>();
    private String authHeader;
    private DataManager dataManager;
    private ExecutorService executorService;

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

        dataManager = new DataManager(this);
        executorService = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        authHeader = intent.getStringExtra("AUTH_HEADER");

        setupUI();

        loadAllData();
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
        Button refreshButton = findViewById(R.id.refreshButton);

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

        refreshButton.setOnClickListener(v -> refreshData());
    }

    private void loadAllData() {
        executorService.execute(new LoadDataTask());
    }

    private void refreshData() {
        dataManager.clearAllData();
        loadAllData();
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
            if (item.getDescription() != null && (item.getTitle().equalsIgnoreCase(searchText) || item.getDescription().contains(searchText))) {
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
    @SuppressLint("MissingPermission")
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private class LoadDataTask implements Runnable {
        @Override
        public void run() {
            boolean success = true;
            try {
                String[] urls = {
                        MainActivity.DATEX_BASE_URL + MainActivity.CURRENT_ROADWORKS,
                        MainActivity.DATEX_BASE_URL + MainActivity.PLANNED_ROADWORKS,
                        MainActivity.DATEX_BASE_URL + MainActivity.CURRENT_INCIDENTS,
                        MainActivity.DATEX_BASE_URL + "TrafficStatusData/content.xml",
                        MainActivity.DATEX_BASE_URL + "TravelTimeData/content.xml",
                        MainActivity.DATEX_BASE_URL + "VMS/content.xml"
                };

                for (String url : urls) {
                    InputStream stream = downloadUrl(url);
                    processData(url, stream);
                    stream.close();
                }
            } catch (IOException | XmlPullParserException e) {
                Log.e("NetworkActions", "Error loading data", e);
                success = false;
            }

            final boolean finalSuccess = success;
            runOnUiThread(() -> {
                if (finalSuccess) {
                    updateUI();
                    Toast.makeText(NetworkActions.this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NetworkActions.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void updateUI() {
            // Retrieve data from the database
            List<Roadwork> allRoadworks = dataManager.getAllRoadworks();
            List<UnplannedEvent> allEvents = dataManager.getAllUnplannedEvents();
            List<TrafficStatusMeasurement> allTrafficStatuses = dataManager.getAllTrafficStatuses();
            List<TravelTimeMeasurement> allTravelTimes = dataManager.getAllTravelTimes();
            List<VMSUnit> allVMSUnits = dataManager.getAllVMSUnits();

            // Convert objects to Item objects
            loadedItems.clear();
            for (Roadwork roadwork : allRoadworks) {
                Item item = new Item(roadwork.getId(), roadwork.getDescription(), null, roadwork.getLocation(), roadwork.getPublicationTime());
                item.setStartDate(roadwork.getStartDate());
                item.setEndDate(roadwork.getEndDate());
                loadedItems.add(item);
            }

            for (UnplannedEvent event : allEvents) {
                Item item = new Item(event.getId(), event.getDescription(), null, event.getLocation(), event.getPublicationTime());
                loadedItems.add(item);
            }

            for (TrafficStatusMeasurement status : allTrafficStatuses) {
                Item item = new Item(status.getId(), status.getTrafficStatus(), null, status.getSiteReference(), status.getPublicationTime());
                loadedItems.add(item);
            }

            for (TravelTimeMeasurement travelTime : allTravelTimes) {
                Item item = new Item(travelTime.getId(), "Travel Time: " + travelTime.getTravelTime(), null, travelTime.getSiteReference(), travelTime.getPublicationTime());
                loadedItems.add(item);
            }

            for (VMSUnit vmsUnit : allVMSUnits) {
                for (VMSMessage message : vmsUnit.getMessages()) {
                    Item item = new Item(vmsUnit.getId(), message.getTextContent(), null, vmsUnit.getVmsUnitReference(), message.getTimeLastSet());
                    loadedItems.add(item);
                }
            }

            // Update the ListView
            updateListView(loadedItems);
        }

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

        private void processData(String url, InputStream stream) throws XmlPullParserException, IOException {
            // Use a switch statement based on the URL content for better readability and potential performance improvement.
            // Extract the URL constants to an enum for better organization and type safety.
            switch (getEndpointType(url)) {
                case CURRENT_ROADWORKS:
                    List<CurrentRoadwork> currentRoadworks = DatexParser.parseCurrentRoadworks(stream);
                    for (CurrentRoadwork roadwork : currentRoadworks) {
                        dataManager.insertRoadwork(roadwork);
                    }
                    break;
                case PLANNED_ROADWORKS:
                    List<FutureRoadwork> futureRoadworks = DatexParser.parseFutureRoadworks(stream);
                    for (FutureRoadwork roadwork : futureRoadworks) {
                        dataManager.insertRoadwork(roadwork);
                    }
                    break;
                case CURRENT_INCIDENTS:
                    List<UnplannedEvent> events = DatexParser.parseUnplannedEvents(stream);
                    for (UnplannedEvent event : events) {
                        dataManager.insertUnplannedEvent(event);
                    }
                    break;
                case TRAFFIC_STATUS:
                    List<TrafficStatusMeasurement> statuses = DatexParser.parseTrafficStatus(stream);
                    for (TrafficStatusMeasurement status : statuses) {
                        dataManager.insertTrafficStatus(status);
                    }
                    break;
                case TRAVEL_TIME:
                    List<TravelTimeMeasurement> travelTimes = DatexParser.parseTravelTime(stream);
                    for (TravelTimeMeasurement travelTime : travelTimes) {
                        dataManager.insertTravelTime(travelTime);
                    }
                    break;
                case VMS:
                    List<VMSUnit> vmsUnits = DatexParser.parseVMS(stream);
                    for (VMSUnit vmsUnit : vmsUnits) {
                        dataManager.insertVMS(vmsUnit);
                    }
                    break;
                default:
                    // Handle unknown URL types, maybe log a warning or throw an exception.
                    break;
            }
        }

        // Define an enum for the different endpoint types
        private enum EndpointType {
            CURRENT_ROADWORKS,
            PLANNED_ROADWORKS,
            CURRENT_INCIDENTS,
            TRAFFIC_STATUS,
            TRAVEL_TIME,
            VMS
        }

        // Helper method to determine the endpoint type from the URL
        private EndpointType getEndpointType(String url) {
                if (url == null) {
                    return null;
                }
                if (url.contains(MainActivity.CURRENT_ROADWORKS)) {
                return EndpointType.CURRENT_ROADWORKS;
            } else if (url.contains(MainActivity.PLANNED_ROADWORKS)) {
                return EndpointType.PLANNED_ROADWORKS;
            } else if (url.contains(MainActivity.CURRENT_INCIDENTS)) {
                return EndpointType.CURRENT_INCIDENTS;
            } else if (url.contains("TrafficStatusData")) {
                return EndpointType.TRAFFIC_STATUS;
            } else if (url.contains("TravelTimeData")) {
                return EndpointType.TRAVEL_TIME;
            } else if (url.contains("VMS")) {
                return EndpointType.VMS;
            } else {
                return null; // Or throw an exception for unknown types
            }
        }

        }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}