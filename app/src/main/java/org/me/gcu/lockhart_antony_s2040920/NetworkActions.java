package org.me.gcu.lockhart_antony_s2040920;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
//import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
import java.util.List;
import org.me.gcu.lockhart_antony_s2040920.PullParser.Item;

public class NetworkActions extends Activity {
    public static String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

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
//            URL aurl;
//            URLConnection yc;
            try {
                result = loadXmlFromNetwork(url);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();

            }

            Log.e("MyTag","in run");
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

//                lv.setAdapter(adapter);
//                while ((inputLine = in.readLine()) != null)
//                {
//
//                    result = result + inputLine;
//                    Log.e("MyTag",inputLine);
//
//                }
//                in.close();
//            }
//            catch (IOException | XmlPullParserException ae)
//            {
//                Log.e("MyTag", "ioexception in run " + ae);
//            }

            //
            // Now that you have the xml data you can parse it
            //

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            NetworkActions.this.runOnUiThread(() -> {
                setContentView(R.layout.activity_results);
                // Displays the HTML string in the UI via a WebView
                WebView myWebView = (WebView) findViewById(R.id.webview);
                myWebView.loadData(result, "text/html", null);

//                    ArrayList listItems = new ArrayList(items);


                Log.d("UI thread", "I am the UI thread");
//                    rawDataDisplay.setText(result);
            });
        }

    }


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
            items = XmlParser.parse(stream);
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
            htmlString.append("<p><a href='");
            htmlString.append(item.link);
            htmlString.append("'>").append(item.title).append("</a></p>");
            htmlString.append(item.date);
            htmlString.append(item.location);
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
