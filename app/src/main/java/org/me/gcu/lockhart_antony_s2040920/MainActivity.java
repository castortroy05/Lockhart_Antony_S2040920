package org.me.gcu.lockhart_antony_s2040920;
//Lockhart_Antony_S2040920


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    public final static String EXTRA_FEED ="org.me.gcu.lockhart_antony_s2040920.FEED";
   
    public final static String plannedroadworks = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    public final static String currentroadworks = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    public final static String currentIncidents = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";




    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");

        Button incidentButton = findViewById(R.id.incidentButton);
        Button plannedButton = findViewById(R.id.plannedButton);
        Button currentButton = findViewById(R.id.currentButton);
        Button allButton = findViewById(R.id.allButton);


        incidentButton.setOnClickListener(this::readIncidents);
        plannedButton.setOnClickListener(this::readPlanned);
        currentButton.setOnClickListener(this::readCurrent);
        allButton.setOnClickListener(this::readAllFeeds);








    }

    public void checkConnection() {
        if(!isNetworkAvailable())
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
            
        }
        else if(isNetworkAvailable())
        {
            Toast.makeText(MainActivity.this,
                    "Loading", Toast.LENGTH_LONG).show();
        }
    


    }

    private void readAllFeeds(View v) {
        checkConnection();
        String urlSource = "all";
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, urlSource);
        startActivity(intent);
    }


    public void readPlanned (View v) {
        checkConnection();
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, plannedroadworks);
        startActivity(intent);

    }

    public void readIncidents (View v) {
        checkConnection();
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, currentIncidents);
        startActivity(intent);

    }

    public void readCurrent (View v) {
        checkConnection();
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, currentroadworks);
        startActivity(intent);

    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                        return true;
                    }
                }
            }
        }

        return false;

    }

    @Override
    public void onClick(View v) {

    }
}
