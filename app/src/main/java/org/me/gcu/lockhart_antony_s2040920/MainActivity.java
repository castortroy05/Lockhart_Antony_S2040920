package org.me.gcu.lockhart_antony_s2040920;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private ListView listView;
    private Button startButton;
    private Button incidentButton;
    private Button plannedButton;
    private Button currentButton;
    private String result = "";
    private String url1="";
    // Traffic Scotland Planned Roadworks XML link
    private String urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String planned ="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String current ="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidents ="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String feedUrl = "";
    private List<parseXML> parseXMLS;
    private parseXML obj;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
//        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
//        startButton = (Button)findViewById(R.id.startButton);
        incidentButton = (Button)findViewById(R.id.incidentButton);
        plannedButton = (Button)findViewById(R.id.plannedButton);
        currentButton = (Button)findViewById(R.id.currentButton);
        listView = (ListView) findViewById(R.id.listView1);

//        startButton.setOnClickListener(this);
        incidentButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            urlSource = incidents;
            obj = new parseXML(urlSource);
//            obj.fetchXML();
        }
    });
        plannedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                urlSource = planned;
                obj = new parseXML(urlSource);
//                obj.fetchXML();
            }
        });
        currentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                urlSource = current;
                obj = new parseXML(urlSource);
//                obj.fetchXML();
            }
        });

//        Log.e("MyTag","after startButton");
        // More Code goes here


//        List<plannedRoadwork> plannedRoadworks = null;
//        try {
//            pullParser parser = new pullParser();
//            URL source = new URL(urlSource);
//            URLConnection cnct = source.openConnection();
//            HttpURLConnection cnHttp = (HttpURLConnection) cnct;
//            if(cnHttp.getResponseCode())
//            BufferedReader bf = new BufferedReader((new InputStreamReader(cnct.getInputStream())));
//            StringBuilder sb = new StringBuilder();
//            String inline = "";
//            while ((inline = bf.readLine()) != null) {
//                sb.append(inline);
//            }
//            byte[] bytes = sb.toString().getBytes();
//            /*
//             * Get ByteArrayInputStream from byte array.
//             */
//            InputStream inputStream = new ByteArrayInputStream(bytes);
//            plannedRoadworks = parser.parse(inputStream);
//
//            ArrayAdapter<plannedRoadwork> adapter =new ArrayAdapter<plannedRoadwork>
//
//                    (this,android.R.layout.simple_list_item_1, plannedRoadworks);
//            listView.setAdapter(adapter);
//
//        } catch (IOException e) {e.printStackTrace();}




    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();


    } //

    @Override
    public void onClick(View v)
    {
        Log.e("MyTag","in onClick");
        startProgress();


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
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                //
                // Now read the data. Make sure that there are no specific hedrs
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                while ((inputLine = in.readLine()) != null)
                {

                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
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
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);
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