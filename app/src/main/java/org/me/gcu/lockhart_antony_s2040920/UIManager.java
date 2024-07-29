package org.me.gcu.lockhart_antony_s2040920;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UIManager {
    private final Activity activity;
    private final ListView listView;
    private final TextView itemCountView;

    public UIManager(Activity activity, ListView listView, TextView itemCountView) {
        this.activity = activity;
        this.listView = listView;
        this.itemCountView = itemCountView;
    }

    public void updateListView(List<DatexItem> items) {
        ItemAdapter adapter = new ItemAdapter(activity, R.layout.list_item, items);
        listView.setAdapter(adapter);
        itemCountView.setText(MessageFormat.format("Displaying {0} items", items.size()));
    }

    public void showDatePickerDialog(EditText dateSearchText) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(activity,
                (view, year1, monthOfYear, dayOfMonth) ->
                        dateSearchText.setText(String.format(Locale.UK, "%d/%d/%d", dayOfMonth, monthOfYear + 1, year1)),
                year, month, day);
        picker.show();
    }

    public void hideKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void checkConnection() {
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Close", (dialogInterface, i) -> activity.finish())
                    .show();
        } else {
            Toast.makeText(activity, "Loading...", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadMap(String location) {
        String[] latlong = location.split(" ");
        if (latlong.length == 2) {
            String url = "https://www.google.com/maps?z=12&t=k&q=loc:" + latlong[0] + "+" + latlong[1];
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage("com.google.android.apps.maps");
            activity.startActivity(intent);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    public void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public void setupUI() {
        Button searchButton = activity.findViewById(R.id.searchButton);
        Button dateSearchButton = activity.findViewById(R.id.dateSearchButton);
        EditText dateSearchText = activity.findViewById(R.id.dateSearchText);
        EditText searchBox = activity.findViewById(R.id.searchBox);
        Button refreshButton = activity.findViewById(R.id.refreshButton);

        dateSearchText.setOnClickListener(v -> showDatePickerDialog(dateSearchText));

        searchBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchButton.performClick();
                return true;
            }
            return false;
        });
    }
}