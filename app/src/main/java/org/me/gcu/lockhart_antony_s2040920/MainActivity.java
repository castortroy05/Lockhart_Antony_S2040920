package org.me.gcu.lockhart_antony_s2040920;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
//    private TextView rawDataDisplay;
    private ListView listView;

    private Button startButton;
    private Button incidentButton;
    private Button plannedButton;
    private Button currentButton;
    private String result = "";
//    private String url1="";
    // Traffic Scotland Roadworks XML links
    private String urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String planned ="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String current ="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidents ="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
//    private String feedUrl = "";
    private ArrayList<pullParser.Item> items;


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");

        incidentButton = (Button)findViewById(R.id.incidentButton);
        plannedButton = (Button)findViewById(R.id.plannedButton);
        currentButton = (Button)findViewById(R.id.currentButton);
        ListView listView = (ListView) findViewById(R.id.listView1);

//        startButton.setOnClickListener(this);
        incidentButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.e("MyTag","in incident button");
            urlSource = incidents;
            startProgress(urlSource);
            listView.refreshDrawableState();
//            obj = new parseXML(urlSource);
//            obj.fetchXML();
        }
    });
        plannedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                urlSource = planned;
                // obj = new parseXML(urlSource);
                startProgress(urlSource);
//                obj.fetchXML();
            }
        });
        currentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                urlSource = current;
                startProgress(urlSource);
//                obj = new parseXML(urlSource);
//                obj.fetchXML();
            }
        });

//        Log.e("MyTag","after startButton");
        // More Code goes here

    }

    public void startProgress(String urlSource)
    {
        // Run network access on a separate thread;
        this.urlSource = urlSource;
        new Thread(new Task(urlSource)).start();


    } //

    @Override
    public void onClick(View v)
    {
        Log.e("MyTag","in onClick");
        startProgress(urlSource);


        Log.e("MyTag","after startProgress");
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
//            BufferedReader in = null;
//            String inputLine = "";


            Log.e("MyTag","in run");
            if(items != null) {
                items.clear();
            }
            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                HttpURLConnection conn = (HttpURLConnection) aurl.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                InputStream stream = conn.getInputStream();
//                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                //
                // Now read the data. Make sure that there are no specific headers
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                pullParser parser = new pullParser();
//                StringBuilder sb = new StringBuilder();
//                String inline = "";
//                while ((inline = in.readLine()) != null) {
//                    sb.append(inline);
//                    Log.e("data", inline );
//                }
//                byte[] bytes = sb.toString().getBytes();
//                /*
//                 * Get ByteArrayInputStream from byte array.
//                 */
//                InputStream inputStream = new ByteArrayInputStream(bytes);

                items = (ArrayList<pullParser.Item>) parser.parse(stream);
                Log.e("parsing complete", items.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
                listView.setAdapter(adapter);

//                lv.setAdapter(adapter);
//                while ((inputLine = in.readLine()) != null)
//                {
//
//                    result = result + inputLine;
//                    Log.e("MyTag",inputLine);
//
//                }
//                in.close();
            }
            catch (IOException | XmlPullParserException ae)
            {
                Log.e("MyTag", "ioexception in run " + ae);
            }

            //
            // Now that you have the xml data you can parse it
            //

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {

//                    ArrayList listItems = new ArrayList(items);


                    Log.d("UI thread", "I am the UI thread");
//                    rawDataDisplay.setText(result);
                }
            });
        }

    }


}

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}