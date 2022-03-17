package org.me.gcu.lockhart_antony_s2040920;

import android.util.Xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class parseXML {
    public int id;
    public String road;
    public String description;
    public String published;
    public String location;
    public String urlString;
    public static final String ns = null;

    public parseXML(String urlSource) {
        this.urlString = urlSource;
    }


    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List items = new ArrayList();

        parser.require(XmlPullParser.START_TAG, urlString, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                items.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static class Item {
        public int id;
        public String road;
        public String description;
        public String published;
        public String location;
        public String link;
        public String category;

        private Item(String title, String description, String published, String location, String link, String category) {
            this.id = Integer.parseInt(UUID.randomUUID().toString());
            this.road = title;
            this.description = description;
            this.published = published;
            this.location = location;
            this.link = link;
            this.category = category;
        }
    }

    // Parses the contents of an item. If it encounters a title, description,link, location or publishDate, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Item readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, urlString, "item");

        String road = null;
        String description = null;
        String published = null;
        String location = null;
        String link = null;
        String category = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                road = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Item(road, description, published, location, link, category);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, urlString, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, urlString, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, urlString, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")){
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, urlString, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, urlString, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, urlString, "description");
        return description;
    }
    private String readLocation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, urlString, "georss:point");
        String location = readText(parser);
        parser.require(XmlPullParser.END_TAG, urlString, "georss:point");
        return location;
    }
    private String readPublished(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, urlString, "pubDate");
        String published = readText(parser);
        parser.require(XmlPullParser.END_TAG, urlString, "pubDate");
        return published;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }




//    public void parseXMLAndStoreIt(XmlPullParser myParser) {
//        int event;
//        String text=null;
//        List entries = new ArrayList();
//
//        try {
//            event = myParser.getEventType();
//
//            while (event != XmlPullParser.END_DOCUMENT) {
//                String name=myParser.getName();
//
//                switch (event){
//                    case XmlPullParser.START_TAG:
//                        break;
//
//                    case XmlPullParser.TEXT:
//                        text = myParser.getText();
//                        break;
//
//                    case XmlPullParser.END_TAG:
//
//                        if(name.equals("title")){
//                            road = text;
//                        }
//
//                        else if(name.equals("pubDate")){
//                            published = text;
//                        }
//
//                        else if(name.equals("georss:point")){
//                            location = text;
//                        }
//                        else if(name.equals("description")){
//                            description = text;
//                        }
//                        else if(name.equals("link")){
//                            link = text;
//                        }
//
//                        else{
//                        }
//
//                        break;
//                }
//
//                event = myParser.next();
//            }
//
//            parsingComplete = false;
//        }
//
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void fetchXML(){
//        Thread thread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//
//                try {
//                    URL url = new URL(urlString);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                    conn.setReadTimeout(10000 /* milliseconds */);
//                    conn.setConnectTimeout(15000 /* milliseconds */);
//                    conn.setRequestMethod("GET");
//                    conn.setDoInput(true);
//
//                    // Starts the query
//                    conn.connect();
//                    InputStream stream = conn.getInputStream();
//
//                    xmlFactoryObject = XmlPullParserFactory.newInstance();
//                    XmlPullParser myparser = xmlFactoryObject.newPullParser();
//
//                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//                    myparser.setInput(stream, null);
//
//                    parseXMLAndStoreIt(myparser);
//                    stream.close();
//                }
//
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//    }
}
