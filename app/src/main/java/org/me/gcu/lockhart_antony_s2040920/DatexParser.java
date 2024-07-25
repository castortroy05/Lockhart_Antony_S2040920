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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Parser for DATEX II XML data.
 */
public class DatexParser {
    private static final String NS = null;
    private static final String TAG_SITUATION = "situation";
    private static final String TAG_SITUATION_RECORD = "situationRecord";
    private static final String TAG_PUBLICATION_TIME = "publicationTime";
    public static final String D_2_LOGICAL_MODEL = "d2LogicalModel";
    public static final String SITE_MEASUREMENTS = "siteMeasurements";


    public static List<CurrentRoadwork> parseCurrentRoadworks(InputStream in) throws XmlPullParserException, IOException {
        return parseRoadworks(in, true);
    }

    public static List<FutureRoadwork> parseFutureRoadworks(InputStream in) throws XmlPullParserException, IOException {
        return parseRoadworks(in, false);
    }

    public static List<UnplannedEvent> parseUnplannedEvents(InputStream in) throws XmlPullParserException, IOException {
        try (in) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readUnplannedEvents(parser);
        }
    }

    public static List<TrafficStatusMeasurement> parseTrafficStatus(InputStream in) throws XmlPullParserException, IOException {
        try (in) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readTrafficStatus(parser);
        }
    }

    public static List<TravelTimeMeasurement> parseTravelTime(InputStream in) throws XmlPullParserException, IOException {
        try (in) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readTravelTime(parser);
        }
    }

    public static List<VMSUnit> parseVMS(InputStream in) throws XmlPullParserException, IOException {
        try (in) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readVMS(parser);
        }
    }

    private static <T extends Roadwork> List<T> parseRoadworks(InputStream in, boolean isCurrent) throws XmlPullParserException, IOException {
        try (in) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readRoadworks(parser, isCurrent);
        }
    }

    private static <T extends Roadwork> List<T> readRoadworks(XmlPullParser parser, boolean isCurrent) throws XmlPullParserException, IOException {
        List<T> roadworks = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NS, D_2_LOGICAL_MODEL);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_SITUATION)) {
                roadworks.add(readRoadwork(parser, isCurrent));
            } else {
                skip(parser);
            }
        }
        return roadworks;
    }
    @SuppressWarnings("unchecked")
    private static <T extends Roadwork> T readRoadwork(XmlPullParser parser, boolean isCurrent) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, TAG_SITUATION);
        AtomicReference<String> id = new AtomicReference<>();
        String publicationTime = null;
        final String[] description = {null};
        final String[] location = {null};
        final String[] startDate = {null};
        final String[] endDate = {null};

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TAG_SITUATION_RECORD:
                    readSituationRecord(parser, item -> {
                        id.set(item.getId());
                        description[0] = item.getDescription();
                        location[0] = item.getLocation();
                        startDate[0] = item.getStartDate().toString();
                        endDate[0] = item.getEndDate().toString();
                    });
                    break;
                case TAG_PUBLICATION_TIME:
                    publicationTime = readPublicationTime(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        if (isCurrent) {
            return (T) new CurrentRoadwork(id.get(), publicationTime, description[0], location[0], startDate[0], endDate[0]);
        } else {
            return (T) new FutureRoadwork(id.get(), publicationTime, description[0], location[0], startDate[0], endDate[0]);
        }
    }

    private static List<UnplannedEvent> readUnplannedEvents(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<UnplannedEvent> events = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NS, D_2_LOGICAL_MODEL);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_SITUATION)) {
                events.add(readUnplannedEvent(parser));
            } else {
                skip(parser);
            }
        }
        return events;
    }

    private static UnplannedEvent readUnplannedEvent(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, TAG_SITUATION);
        final String[] id = {null};
        String publicationTime = null;
        final String[] description = {null};
        final String[] location = {null};

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TAG_SITUATION_RECORD:
                    readSituationRecord(parser, item -> {
                        id[0] = item.getId();
                        description[0] = item.getDescription();
                        location[0] = item.getLocation();
                    });
                    break;
                case TAG_PUBLICATION_TIME:
                    publicationTime = readPublicationTime(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new UnplannedEvent(id[0], publicationTime, description[0], location[0]);
    }

    private static List<TrafficStatusMeasurement> readTrafficStatus(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<TrafficStatusMeasurement> measurements = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NS, D_2_LOGICAL_MODEL);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(SITE_MEASUREMENTS)) {
                measurements.add(readTrafficStatusMeasurement(parser));
            } else {
                skip(parser);
            }
        }
        return measurements;
    }

    private static TrafficStatusMeasurement readTrafficStatusMeasurement(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, SITE_MEASUREMENTS);
        String id;
        String publicationTime;
        String siteReference = null;
        String measurementTime = null;
        String trafficStatus = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "measurementSiteReference":
                    siteReference = readText(parser);
                    break;
                case "measurementTimeDefault":
                    measurementTime = readText(parser);
                    break;
                case "trafficStatus":
                    trafficStatus = readText(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        id = siteReference + "_" + measurementTime;
        publicationTime = measurementTime;

        return new TrafficStatusMeasurement(id, publicationTime, siteReference, measurementTime, trafficStatus);
    }

    private static List<TravelTimeMeasurement> readTravelTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<TravelTimeMeasurement> measurements = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NS, D_2_LOGICAL_MODEL);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(SITE_MEASUREMENTS)) {
                measurements.add(readTravelTimeMeasurement(parser));
            } else {
                skip(parser);
            }
        }
        return measurements;
    }

    private static TravelTimeMeasurement readTravelTimeMeasurement(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, SITE_MEASUREMENTS);
        String id;
        String publicationTime;
        String siteReference = null;
        String measurementTime = null;
        double travelTime = 0;
        double freeFlowTravelTime = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "measurementSiteReference":
                    siteReference = readText(parser);
                    break;
                case "measurementTimeDefault":
                    measurementTime = readText(parser);
                    break;
                case "travelTime":
                    travelTime = Double.parseDouble(readText(parser));
                    break;
                case "freeFlowTravelTime":
                    freeFlowTravelTime = Double.parseDouble(readText(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        id = siteReference + "_" + measurementTime;
        publicationTime = measurementTime;

        return new TravelTimeMeasurement(id, publicationTime, siteReference, measurementTime, travelTime, freeFlowTravelTime);
    }

    private static List<VMSUnit> readVMS(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<VMSUnit> vmsUnits = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NS, D_2_LOGICAL_MODEL);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("vmsUnit")) {
                vmsUnits.add(readVMSUnit(parser));
            } else {
                skip(parser);
            }
        }
        return vmsUnits;
    }

    private static VMSUnit readVMSUnit(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, "vmsUnit");
        String id;
        String publicationTime;
        String vmsUnitReference = null;
        List<VMSMessage> messages = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "vmsUnitReference":
                    vmsUnitReference = readText(parser);
                    break;
                case "vmsMessage":
                    messages.add(readVMSMessage(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        id = vmsUnitReference;
        publicationTime = messages.isEmpty() ? null : messages.get(0).getTimeLastSet();

        return new VMSUnit(id, publicationTime, vmsUnitReference, messages);
    }

    private static VMSMessage readVMSMessage(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, "vmsMessage");
        String timeLastSet = null;
        String textContent = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "timeLastSet":
                    timeLastSet = readText(parser);
                    break;
                case "textLine":
                    textContent = readText(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new VMSMessage(timeLastSet, textContent);
    }

    private static void readSituationRecord(XmlPullParser parser, ItemCallback callback) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, TAG_SITUATION_RECORD);
        String id = null;
        String description = null;
        String location = null;
        final String[] startDate = {null};
        final String[] endDate = {null};

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "situationRecordCreationReference":
                    id = readText(parser);
                    break;
                case "generalPublicComment":
                    description = readComment(parser);
                    break;
                case "groupOfLocations":
                    location = readGroupOfLocations(parser);
                    break;
                case "validity":
                    readValidity(parser, (start, end) -> {
                        startDate[0] = start;
                        endDate[0] = end;
                    });
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        callback.onItemParsed(new Item(id, null, description, location, null) {
            @Override
            public Date getStartDate() {
                return parseDate(startDate[0]);
            }

            @Override
            public Date getEndDate() {
                return parseDate(endDate[0]);
            }
        });
    }

    private static String readComment(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, "generalPublicComment");
        String comment = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("comment")) {
                comment = readText(parser);
            } else {
                skip(parser);
            }
        }
        return comment;
    }

    private static String readGroupOfLocations(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, "groupOfLocations");
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
        parser.require(XmlPullParser.START_TAG, NS, "locationForDisplay");
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

    private static void readValidity(XmlPullParser parser, ValidityCallback callback) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, "validity");
        final String[] startDate = {null};
        final String[] endDate = {null};
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("validityTimeSpecification")) {
                readValidityTimeSpecification(parser, (start, end) -> {
                    startDate[0] = start;
                    endDate[0] = end;
                });
            } else {
                skip(parser);
            }
        }
        callback.onValidityParsed(startDate[0], endDate[0]);
    }

    private static void readValidityTimeSpecification(XmlPullParser parser, ValidityCallback callback) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, "validityTimeSpecification");
        String startDate = null;
        String endDate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "overallStartTime":
                    startDate = readText(parser);
                    break;
                case "overallEndTime":
                    endDate = readText(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        callback.onValidityParsed(startDate, endDate);
    }

    private static String readPublicationTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, TAG_PUBLICATION_TIME);
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, NS, TAG_PUBLICATION_TIME);
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

    private static Date parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
            return format.parse(dateString);
        } catch (ParseException e) {
            Log.e("DatexParser", "Error parsing date: " + dateString, e);
            return null;
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
                default:
                    throw new IllegalStateException("Unexpected value: " + parser.next());
            }
        }
    }

    private interface ItemCallback {
        void onItemParsed(Item item);
    }

    private interface ValidityCallback {
        void onValidityParsed(String startDate, String endDate);
    }
}