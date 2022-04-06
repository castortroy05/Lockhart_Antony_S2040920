package org.me.gcu.lockhart_antony_s2040920;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
        allButton.setOnClickListener(this::readall);

    }

    private void readall(View v) {
        String urlSource = "all";
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, urlSource);
        startActivity(intent);
    }


    public void readPlanned (View v) {
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, plannedroadworks);
        startActivity(intent);

    }

    public void readIncidents (View v) {
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, currentIncidents);
        startActivity(intent);

    }

    public void readCurrent (View v) {
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, currentroadworks);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

    }
}
