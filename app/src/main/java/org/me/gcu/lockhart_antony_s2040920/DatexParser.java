package org.me.gcu.lockhart_antony_s2040920;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Parser for DATEX II XML data.
 */
public class DatexParser {
    private static final String ns = null;

    /**
     * Parses the input stream containing DATEX II XML data.
     *
     * @param in The input stream to parse.
     * @return A list of Item objects parsed from the XML.
     * @throws XmlPullParserException If there's an error parsing the XML.
     * @throws IOException If there's an error reading the input stream.
     */
    public static List<Item> parse(InputStream in) throws XmlPullParserException, IOException {
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

    private static List<Item> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Item> items = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "d2LogicalModel");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("situation")) {
                items.add(readSituation(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    private static Item readSituation(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "situation");
        final String[] title = {null};
        final String[] description = {null};
        final String[] location = {null};
        String pubDate = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "situationRecord":
                    readSituationRecord(parser, item -> {
                        title[0] = item.title;
                        description[0] = item.description;
                        location[0] = item.location;
                    });
                    break;
                case "publicationTime":
                    pubDate = readPublicationTime(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new Item(title[0], null, description[0], location[0], pubDate);
    }

    private static void readSituationRecord(XmlPullParser parser, ItemCallback callback) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "situationRecord");
        String title = null;
        String description = null;
        String location = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "situationRecordType":
                    title = readText(parser);
                    break;
                case "comment":
                    description = readComment(parser);
                    break;
                case "groupOfLocations":
                    location = readGroupOfLocations(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        callback.onItemParsed(new Item(title, null, description, location, null));
    }

    private static String readComment(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "comment");
        String comment = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("values")) {
                comment = readText(parser);
            } else {
                skip(parser);
            }
        }
        return comment;
    }

    private static String readGroupOfLocations(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "groupOfLocations");
        String location = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("locationForDisplay")) {
                location = readLocationForDisplay(parser);
            } else {
                skip(parser);
            }
        }
        return location;
    }

    private static String readLocationForDisplay(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "locationForDisplay");
        String latitude = null;
        String longitude = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "latitude":
                    latitude = readText(parser);
                    break;
                case "longitude":
                    longitude = readText(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return latitude != null && longitude != null ? latitude + " " + longitude : null;
    }

    private static String readPublicationTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "publicationTime");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "publicationTime");
        return formatDate(pubDate);
    }

    private static String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
            Date date = inputFormat.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.UK);
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e("DatexParser", "Error parsing date: " + dateString, e);
            return dateString;
        }
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    private interface ItemCallback {
        void onItemParsed(Item item);
    }
}