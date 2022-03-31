package org.me.gcu.lockhart_antony_s2040920;

import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class pullParser {
//    private List<FeedItem> feedItems =new ArrayList<FeedItem>();
//    private FeedItem feedItem;
    private String text;
    private static final String ns = null;



    public pullParser() throws XmlPullParserException {
    }

//    public List<FeedItem> getFeedItems(){
//        return feedItems;
//    }
    public List<Item> parse(InputStream is) throws XmlPullParserException, IOException{

        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            Log.e("tag before passing is ", parser.getName());
            return readFeed(parser);
        } finally {
            is.close();
        }
    }
//        return items;
//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser parser = factory.newPullParser();
//
//            parser.setInput(is, null);
//
//            int eventType = parser.getEventType();
//            parser.require(XmlPullParser.START_TAG,text,"channel");
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String tagname = parser.getName();
//                if(tagname!= null){
//                Log.e("MyTag", tagname);}
//
//                Integer isItem = 0;
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        if (tagname.equalsIgnoreCase("channel")) {
//                        feedItems = new ArrayList<FeedItem>();
//                        }
//                        else if (tagname.equalsIgnoreCase("item")) {
//                            // create a new instance of feedItem
//                            isItem = 1;
//                            parser.nextTag();
//                            feedItem = new FeedItem();
//                            if(isItem !=0){
//                                if (tagname.equalsIgnoreCase("title")) {
//                                    text=parser.nextText();
//                                    feedItem.setTitle(text);
//                                } else if (tagname.equalsIgnoreCase("description")) {
//                                    text=parser.nextText();
//                                    feedItem.setDescription(text);
//                                } else if (tagname.equalsIgnoreCase("geo:rss")) {
//                                    text=parser.nextText();
//                                    feedItem.setLocation(text);
//                                }else if (tagname.equalsIgnoreCase("published")) {
//                                    text=parser.nextText();
//                                    feedItem.setPublished(text);
//                                }
//
//                        }
//                       }
//
//                        break;
//
//                    case XmlPullParser.TEXT:
//                        text = parser.getText();
//                        break;
//
//                    case XmlPullParser.END_TAG:
//                        if (tagname.equalsIgnoreCase("item")) {
//                            // add feedItem object to list
//                            feedItems.add(feedItem);
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//                eventType = parser.next();
//            }
//
//        }
//        catch (XmlPullParserException e) {e.printStackTrace();}









    private ArrayList<Item> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Item> items = new ArrayList<Item>();
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, ns, "channel");

//        parser.require(XmlPullParser.START_TAG, null, "channel");
//        Log.e("parser name ", parser.getName());
//        parser.nextTag();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
//                parser.nextTag();
                continue;

            }

            String name = parser.getName();
//            while (name != "item"){parser.nextTag();}
            Log.e("tag ", name);
            // Starts by looking for the item tag
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
//                parser.nextTag();
            }
        }
        return items;
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


    public static class Item {
        public final String title;
        public final String link;
        public final String description;
        public final String location;
        public final String date;
//        public final int uuid;

        private Item(String title, String description, String link, String location, String date) {
            this.title = title;
            this.description = description;
            this.link = link;
            this.location = location;
            this.date = date;
//            this.uuid = Integer.parseInt(UUID.randomUUID().toString());
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String link = null;
        String location = null;
        String date = null;
//        int uuid = Integer.parseInt(UUID.randomUUID().toString());
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
                Log.e(parser.getName(),title);
            } else if (name.equals("description")) {
                description = readDescription(parser);
                Log.e(parser.getName(),description);
            } else if (name.equals("link")) {
                link = readLink(parser);
                Log.e(parser.getName(),link);
            }else if (name.equals("georss:point")) {
                location = readLocation(parser);
                Log.e(parser.getName(),location);
            }else if (name.equals("pubDate")) {
                date = readDate(parser);
                Log.e(parser.getName(),date);
            }
            else {
                skip(parser);
            }
        }
        return new Item(title, description, link, location, date);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;

    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readLocation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "georss:point");
        String location = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "georss:point");
        return location;
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return date;
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

}

//}
