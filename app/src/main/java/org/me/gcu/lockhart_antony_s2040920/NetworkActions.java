package org.me.gcu.lockhart_antony_s2040920;

//import static android.R.layout.simple_list_item_1;

import android.app.Activity;
//import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
//import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
//import android.view.View;
//import android.view.View.OnClickListener;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;

import org.me.gcu.lockhart_antony_s2040920.Item;

public class NetworkActions extends Activity {
    public static String result = null;
    public ArrayList<Item> loadedItems = new ArrayList<>();
    private ListView lv;
//    Button mapButton = findViewById(R.);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

//        mapButton.setOnClickListener(this::loadMap);
        Intent intent = getIntent();
        String urlSource = intent.getStringExtra(MainActivity.EXTRA_FEED);

        new Thread(new Task(urlSource)).start();



    }



    private class Task implements Runnable
    {
        private final String url;

        public Task(String aurl)
        {
            url = aurl;

        }
        @Override
        public void run()
        {
            try {
                result = loadXmlFromNetwork(url);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();

            }

            Log.e("MyTag","in run");
//

            NetworkActions.this.runOnUiThread(this::run2);
        }

        private void run2() {
            setContentView(R.layout.activity_results);
//                Log.d("loadedItems", String.valueOf(loadedItems));
            // Displays the HTML string in the UI via a WebView
//                WebView myWebView = findViewById(R.id.webview);
//                myWebView.loadData(result, "text/html", null);
            //Declaration part
            ArrayAdapter<Item> adapter;
            //                assert listItems != null;
//                ArrayList listItems = new ArrayList<String>(loadedItems.to);
            lv = findViewById(R.id.listView1);

            //arraylist Append
            adapter = new ItemAdapter(NetworkActions.this, R.layout.list_item, loadedItems
            );
            lv.setAdapter(adapter);


            Log.d("UI thread", "I am the UI thread");
//                    rawDataDisplay.setText(result);
        }
    }
//    public void loadMap(View v) {
//        Intent intent = new Intent(this, MapsActivity.class);
//        for (Item loadedItem : loadedItems) {
//
//
//        }
//
//        startActivity(intent);
//    }

    private String loadXmlFromNetwork(String urlString)
            throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        PullParser XmlParser = new PullParser();
        List<Item> items;

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>").append(getResources().getString(R.string.app_name)).append("</h3>");

        try {
            stream = downloadUrl(urlString);
            items = PullParser.parse(stream);
            loadedItems.addAll(items);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }


        // Store retrieve each item and format it as an html string
        for (Item item : items) {
            htmlString.append("<h2>").append(item.title).append("</h2>");
            htmlString.append(item.description);
            String location= item.location;
            String[] latlong = location.split(" ");
            htmlString.append("<p><a href='");
            htmlString.append("https://www.google.com/maps?z=12&t=k&q=").append("loc:").append(latlong[0]).append("+").append(latlong[1]).append("&").append(item.title);
            htmlString.append("'>").append(item.title).append("</a></p>");
            htmlString.append(item.date);
//            htmlString.append("<p><a href='");
//            htmlString.append("https://www.google.com/maps?q=");
//            htmlString.append(item.location);
//            htmlString.append("'>");
//            htmlString.append("map link");
//            htmlString.append("</a></p>");
            // }
        }
        return htmlString.toString();
    }
    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

}
