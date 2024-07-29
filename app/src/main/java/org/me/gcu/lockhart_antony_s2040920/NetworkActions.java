package org.me.gcu.lockhart_antony_s2040920;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NetworkActions extends AppCompatActivity {
    private DataLoader dataLoader;
    private UIManager uiManager;
    private SearchManager searchManager;
    private DataManager dataManager;
    private List<DatexItem> loadedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        initializeComponents();
        setupUI();
        loadAllData();
    }

    private void initializeComponents() {
        ListView listView = findViewById(R.id.listView1);
        TextView itemCount = findViewById(R.id.itemCount);

        dataManager = new DataManager(this);
        dataLoader = new DataLoader(dataManager);
        uiManager = new UIManager(this, listView, itemCount);
        searchManager = new SearchManager();
        loadedItems = new ArrayList<>();
    }

    private void setupUI() {
        uiManager.setupUI();

        Button searchButton = findViewById(R.id.searchButton);
        Button dateSearchButton = findViewById(R.id.dateSearchButton);
        EditText searchBox = findViewById(R.id.searchBox);
        EditText dateSearchText = findViewById(R.id.dateSearchText);
        Button refreshButton = findViewById(R.id.refreshButton);

        searchButton.setOnClickListener(v -> searchRoad(searchBox.getText().toString()));
        dateSearchButton.setOnClickListener(v -> searchDate(dateSearchText.getText().toString()));
        refreshButton.setOnClickListener(v -> refreshData());
    }

    private void loadAllData() {
        uiManager.checkConnection();
        if (!uiManager.isNetworkAvailable()) return;

        String authHeader = getIntent().getStringExtra("AUTH_HEADER");
        dataLoader.loadAllData(authHeader, new DataLoader.LoadDataCallback() {
            @Override
            public void onDataLoaded() {
                runOnUiThread(() -> {
                    updateUI();
                    uiManager.showToast("Data loaded successfully");
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> uiManager.showToast(message));
            }
        });
    }

    private void updateUI() {
        loadedItems.clear();
        loadedItems.addAll(dataManager.getAllRoadworks());
        loadedItems.addAll(dataManager.getAllUnplannedEvents());
        loadedItems.addAll(dataManager.getAllTrafficStatuses());
        loadedItems.addAll(dataManager.getAllTravelTimes());
        loadedItems.addAll(dataManager.getAllVMSUnits());
        uiManager.updateListView(loadedItems);
    }

    private void searchRoad(String searchText) {
        List<DatexItem> searchResults = searchManager.searchByText(loadedItems, searchText);
        uiManager.updateListView(searchResults);
        uiManager.hideKeyboard();
    }

    private void searchDate(String dateSearchText) {
        List<DatexItem> searchResults = searchManager.searchByDate(loadedItems, dateSearchText);
        uiManager.updateListView(searchResults);
    }

    private void refreshData() {
        dataManager.clearAllData();
        loadAllData();
    }

    public void loadMap(String location) {
        uiManager.loadMap(location);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataLoader.shutdown();
    }
}