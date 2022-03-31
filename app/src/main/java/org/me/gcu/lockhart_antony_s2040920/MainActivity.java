package org.me.gcu.lockhart_antony_s2040920;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.TextView;


//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.util.ArrayList;

//import org.me.gcu.lockhart_antony_s2040920.PullParser.Item;


//import android.widget.ListView;

//import org.xmlpull.v1.XmlPullParserException;

//import java.net.URL;
//import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
//    private TextView rawDataDisplay;
//    private ListView listView;

//    private Button startButton;
//    private Button currentButton;
//    private final String result = "";
//    private String url1="";
    // Traffic Scotland Roadworks XML links
    public final static String EXTRA_FEED ="org.me.gcu.lockhart_antony_s2040920.FEED";
//    private final String urlSource="";
    //    private String feedUrl = "";
//    private ArrayList<Item> items;


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");

        Button incidentButton = findViewById(R.id.incidentButton);
        Button plannedButton = findViewById(R.id.plannedButton);
        Button currentButton = findViewById(R.id.currentButton);
//        ListView listView = findViewById(R.id.listView1);

        incidentButton.setOnClickListener(v -> {
            Log.e("MyTag","in incident button");
            readIncidents(v);
//            urlSource = incidents;
//            startProgress(urlSource);
//            listView.refreshDrawableState();
//            obj = new parseXML(urlSource);
//            obj.fetchXML();
        });
        //                urlSource = planned;
        //                // obj = new parseXML(urlSource);
        //                startProgress(urlSource);
        //                obj.fetchXML();
        plannedButton.setOnClickListener(this::readPlanned);
        //                urlSource = current;
        //                startProgress(urlSource);
        //                obj = new parseXML(urlSource);
        //                obj.fetchXML();
        currentButton.setOnClickListener(this::readCurrent);

//        Log.e("MyTag","after startButton");
        // More Code goes here

    }

    public void readPlanned (View v) {
        String urlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, urlSource);
        startActivity(intent);

    }

    public void readIncidents (View v) {
        String urlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, urlSource);
        startActivity(intent);

    }

    public void readCurrent (View v) {
        String urlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
        Intent intent = new Intent (this, NetworkActions.class);
        intent.putExtra(EXTRA_FEED, urlSource);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

    }


//    public void startProgress(String urlSource)
//    {
//        // Run network access on a separate thread;
//        this.urlSource = urlSource;
//        new Thread(new Task(urlSource)).start();
//
//
//    } //

//    @Override
//    public void onClick(View v)
//    {
//        Log.e("MyTag","in onClick");
//        startProgress(urlSource);
//
//
//        Log.e("MyTag","after startProgress");
//    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
//    public static class Task implements Runnable
//    {
//        private String url;
//
//        public Task(String aurl)
//        {
//            url = aurl;
//        }
//        @Override
//        public void run()
//        {
//
//            URL aurl;
//            URLConnection yc;
////            BufferedReader in = null;
////            String inputLine = "";
//
//
//            Log.e("MyTag","in run");
//            if(items != null) {
//                items.clear();
//            }
//            try
//            {
//                Log.e("MyTag","in try");
//                aurl = new URL(url);
//                yc = aurl.openConnection();
//                HttpURLConnection conn = (HttpURLConnection) aurl.openConnection();
//                conn.setReadTimeout(10000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                // Starts the query
//                conn.connect();
//                InputStream stream = conn.getInputStream();
////                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//                Log.e("MyTag","after ready");
//                //
//                // Now read the data. Make sure that there are no specific headers
//                // in the data file that you need to ignore.
//                // The useful data that you need is in each of the item entries
//                //
//                PullParser parser = new PullParser();
//                items = (ArrayList<PullParser.Item>) parser.parse(stream);
//                Log.e("parsing complete", items.toString());
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
//                listView.setAdapter(adapter);
//
////                lv.setAdapter(adapter);
////                while ((inputLine = in.readLine()) != null)
////                {
////
////                    result = result + inputLine;
////                    Log.e("MyTag",inputLine);
////
////                }
////                in.close();
//            }
//            catch (IOException | XmlPullParserException ae)
//            {
//                Log.e("MyTag", "ioexception in run " + ae);
//            }
//
//            //
//            // Now that you have the xml data you can parse it
//            //
//
//            // Now update the TextView to display raw XML data
//            // Probably not the best way to update TextView
//            // but we are just getting started !
//
//            MainActivity.this.runOnUiThread(new Runnable()
//            {
//                public void run() {
//
////                    ArrayList listItems = new ArrayList(items);
//
//
//                    Log.d("UI thread", "I am the UI thread");
////                    rawDataDisplay.setText(result);
//                }
//            });
//        }
//
//    }


}

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}