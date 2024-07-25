package org.me.gcu.lockhart_antony_s2040920;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity is the entry point of the application.
 * It provides buttons for users to access different types of traffic information.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Constant used as a key when passing data to other activities
    public static final String EXTRA_FEED = "org.me.gcu.lockhart_antony_s2040920.FEED";

    // Base URL for the DATEX II traffic data API
    public static final String DATEX_BASE_URL = "https://datex2.trafficscotland.org/rest/2.3/publications/";

    // Endpoints for different types of traffic information
    public static final String PLANNED_ROADWORKS = "FutureRoadworks/Content.xml";
    public static final String CURRENT_ROADWORKS = "CurrentRoadworks/Content.xml";
    public static final String CURRENT_INCIDENTS = "UnplannedEvents/Content.xml";

    // API credentials for authentication
    private static final String CLIENT_ID = "3c3b2e71-9588-4974-b6cd-20fbb54a202e";
    private static final String CLIENT_KEY = "P62cRj3YWx-3X8qJlSkyZdVr.RI0kgepLinmFYCuev0qe9u.NnTAf.3G2Bt4KZyx";

    /**
     * Called when the activity is first created.
     * Sets up the user interface and initializes button click listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate called");

        Button incidentButton = findViewById(R.id.incidentButton);
        Button plannedButton = findViewById(R.id.plannedButton);
        Button currentButton = findViewById(R.id.currentButton);
        Button allButton = findViewById(R.id.allButton);

        incidentButton.setOnClickListener(this);
        plannedButton.setOnClickListener(this);
        currentButton.setOnClickListener(this);
        allButton.setOnClickListener(this);
    }
    @SuppressLint("ObsoleteSdkInt")
    private String getAuthHeader() {
        String auth = BuildConfig.CLIENT_ID + ":" + BuildConfig.CLIENT_KEY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        } else {
            return "Basic " + android.util.Base64.encodeToString(auth.getBytes(), android.util.Base64.NO_WRAP);
        }
    }
    /**
     * Handles click events for all buttons in the activity.
     * Checks for network availability, determines which button was clicked,
     * and starts the appropriate activity with the correct data.
     *
     * @param v The view (button) that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (!isNetworkAvailable()) {
            showNetworkAlert();
            return;
        }

        String urlSource;
        int id = v.getId();
        if (id == R.id.incidentButton) {
            urlSource = Constants.DATEX_BASE_URL + Constants.CURRENT_INCIDENTS;
        } else if (id == R.id.plannedButton) {
            urlSource = Constants.DATEX_BASE_URL + Constants.PLANNED_ROADWORKS;
        } else if (id == R.id.currentButton) {
            urlSource = Constants.DATEX_BASE_URL + Constants.CURRENT_ROADWORKS;
        } else if (id == R.id.allButton) {
            urlSource = "all";
        } else {
            Log.e("MainActivity", "Unknown button clicked");
            return;
        }

        Intent intent = new Intent(this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, urlSource);
        intent.putExtra("AUTH_HEADER", getAuthHeader());
        startActivity(intent);

        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays an alert dialog when no network connection is available.
     * The dialog informs the user to check their internet connection and
     * provides an option to close the app.
     */
    private void showNetworkAlert() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Internet Connection Alert")
                .setMessage("Please Check Your Internet Connection")
                .setPositiveButton("Close", (dialog, which) -> finish())
                .show();
    }

    /**
     * Checks if the device has an active network connection.
     * This method is backwards compatible and uses different APIs based on the Android version.
     *
     * @return true if a network connection is available, false otherwise.
     */
    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings({"deprecation"})    // Suppress deprecation warnings for NetworkInfo

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For Android 6.0 (API 23) and above
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
            // For Android versions below 6.0
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }


}