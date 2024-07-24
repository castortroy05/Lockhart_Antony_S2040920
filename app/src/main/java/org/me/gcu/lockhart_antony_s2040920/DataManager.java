package org.me.gcu.lockhart_antony_s2040920;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "traffic_data.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_ROADWORKS = "roadworks";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_TRAFFIC_STATUS = "traffic_status";
    private static final String TABLE_TRAVEL_TIME = "travel_time";
    private static final String TABLE_VMS = "vms";

    // Common column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_PUBLICATION_TIME = "publication_time";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_SITE_REFERENCE = "site_reference";
    private static final String COLUMN_MEASUREMENT_TIME = "measurement_time";
    private static final String COLUMN_TRAFFIC_STATUS = "traffic_status";
    private static final String COLUMN_TRAVEL_TIME = "travel_time";
    private static final String COLUMN_FREE_FLOW_TRAVEL_TIME = "free_flow_travel_time";
    private static final String COLUMN_VMS_UNIT_REFERENCE = "vms_unit_reference";
    private static final String COLUMN_MESSAGE_TIME = "message_time";
    private static final String COLUMN_MESSAGE_TEXT = "message_text";

    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String TEXT_PRIMARY_KEY = " TEXT PRIMARY KEY";
    private static final String TEXT = " TEXT";
    private static final String REAL = " REAL";
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";

    public DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(createRoadworksTable());
        db.execSQL(createEventsTable());
        db.execSQL(createTrafficStatusTable());
        db.execSQL(createTravelTimeTable());
        db.execSQL(createVMSTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL(DROP_TABLE_IF_EXISTS + TABLE_ROADWORKS);
        db.execSQL(DROP_TABLE_IF_EXISTS + TABLE_EVENTS);
        db.execSQL(DROP_TABLE_IF_EXISTS + TABLE_TRAFFIC_STATUS);
        db.execSQL(DROP_TABLE_IF_EXISTS + TABLE_TRAVEL_TIME);
        db.execSQL(DROP_TABLE_IF_EXISTS + TABLE_VMS);
        // Create tables again
        onCreate(db);
    }

    private String createRoadworksTable() {
        return CREATE_TABLE + TABLE_ROADWORKS + "("
                + COLUMN_ID + TEXT_PRIMARY_KEY + ","
                + COLUMN_TYPE + TEXT + ","
                + COLUMN_PUBLICATION_TIME + TEXT + ","
                + COLUMN_DESCRIPTION + TEXT + ","
                + COLUMN_LOCATION + TEXT + ","
                + COLUMN_START_DATE + TEXT + ","
                + COLUMN_END_DATE + TEXT + ")";
    }

    private String createEventsTable() {
        return CREATE_TABLE + TABLE_EVENTS + "("
                + COLUMN_ID + TEXT_PRIMARY_KEY + ","
                + COLUMN_TYPE + TEXT + ","
                + COLUMN_PUBLICATION_TIME + TEXT + ","
                + COLUMN_DESCRIPTION + TEXT + ","
                + COLUMN_LOCATION + TEXT + ")";
    }

    private String createTrafficStatusTable() {
        return CREATE_TABLE + TABLE_TRAFFIC_STATUS + "("
                + COLUMN_ID + TEXT_PRIMARY_KEY + ","
                + COLUMN_PUBLICATION_TIME + TEXT + ","
                + COLUMN_SITE_REFERENCE + TEXT + ","
                + COLUMN_MEASUREMENT_TIME + TEXT + ","
                + COLUMN_TRAFFIC_STATUS + TEXT + ")";
    }

    private String createTravelTimeTable() {
        return CREATE_TABLE + TABLE_TRAVEL_TIME + "("
                + COLUMN_ID + TEXT_PRIMARY_KEY + ","
                + COLUMN_PUBLICATION_TIME + TEXT + ","
                + COLUMN_SITE_REFERENCE + TEXT + ","
                + COLUMN_MEASUREMENT_TIME + TEXT + ","
                + COLUMN_TRAVEL_TIME + REAL + ","
                + COLUMN_FREE_FLOW_TRAVEL_TIME + REAL + ")";
    }

    private String createVMSTable() {
        return CREATE_TABLE + TABLE_VMS + "("
                + COLUMN_ID + TEXT_PRIMARY_KEY + ","
                + COLUMN_PUBLICATION_TIME + TEXT + ","
                + COLUMN_VMS_UNIT_REFERENCE + TEXT + ","
                + COLUMN_MESSAGE_TIME + TEXT + ","
                + COLUMN_MESSAGE_TEXT + TEXT + ")";
    }

    // Insert methods for each type
    public void insertRoadwork(Roadwork roadwork) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, roadwork.getId());
        values.put(COLUMN_TYPE, roadwork instanceof CurrentRoadwork ? "Current" : "Future");
        values.put(COLUMN_PUBLICATION_TIME, roadwork.getPublicationTime());
        values.put(COLUMN_DESCRIPTION, roadwork.getDescription());
        values.put(COLUMN_LOCATION, roadwork.getLocation());
        values.put(COLUMN_START_DATE, String.valueOf(roadwork.getStartDate()));
        values.put(COLUMN_END_DATE, String.valueOf(roadwork.getEndDate()));
        db.insert(TABLE_ROADWORKS, null, values);
        db.close();
    }

    public void insertUnplannedEvent(UnplannedEvent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, event.getId());
        values.put(COLUMN_TYPE, "Unplanned");
        values.put(COLUMN_PUBLICATION_TIME, event.getPublicationTime());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_LOCATION, event.getLocation());
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public void insertTrafficStatus(TrafficStatusMeasurement status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, status.getId());
        values.put(COLUMN_PUBLICATION_TIME, status.getPublicationTime());
        values.put(COLUMN_SITE_REFERENCE, status.getSiteReference());
        values.put(COLUMN_MEASUREMENT_TIME, status.getMeasurementTime());
        values.put(COLUMN_TRAFFIC_STATUS, status.getTrafficStatus());
        db.insert(TABLE_TRAFFIC_STATUS, null, values);
        db.close();
    }

    public void insertTravelTime(TravelTimeMeasurement travelTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, travelTime.getId());
        values.put(COLUMN_PUBLICATION_TIME, travelTime.getPublicationTime());
        values.put(COLUMN_SITE_REFERENCE, travelTime.getSiteReference());
        values.put(COLUMN_MEASUREMENT_TIME, travelTime.getMeasurementTime());
        values.put(COLUMN_TRAVEL_TIME, travelTime.getTravelTime());
        values.put(COLUMN_FREE_FLOW_TRAVEL_TIME, travelTime.getFreeFlowTravelTime());
        db.insert(TABLE_TRAVEL_TIME, null, values);
        db.close();
    }

    public void insertVMS(VMSUnit vms) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (VMSMessage message : vms.getMessages()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, vms.getId() + "_" + message.getTimeLastSet()); // Composite key
            values.put(COLUMN_PUBLICATION_TIME, vms.getPublicationTime());
            values.put(COLUMN_VMS_UNIT_REFERENCE, vms.getVmsUnitReference());
            values.put(COLUMN_MESSAGE_TIME, message.getTimeLastSet());
            values.put(COLUMN_MESSAGE_TEXT, message.getTextContent());
            db.insert(TABLE_VMS, null, values);
        }
        db.close();
    }

    public List<UnplannedEvent> getAllUnplannedEvents() {
        List<UnplannedEvent> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SELECT_ALL_FROM + TABLE_EVENTS + " WHERE " + COLUMN_TYPE + " = 'Unplanned'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String publicationTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLICATION_TIME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));

                UnplannedEvent event = new UnplannedEvent(id, publicationTime, description, location);
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return events;
    }

    public List<TrafficStatusMeasurement> getAllTrafficStatuses() {
        List<TrafficStatusMeasurement> statuses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SELECT_ALL_FROM + TABLE_TRAFFIC_STATUS;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String publicationTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLICATION_TIME));
                String siteReference = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SITE_REFERENCE));
                String measurementTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEASUREMENT_TIME));
                String trafficStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRAFFIC_STATUS));

                TrafficStatusMeasurement status = new TrafficStatusMeasurement(id, publicationTime, siteReference, measurementTime, trafficStatus);
                statuses.add(status);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return statuses;
    }

    public List<TravelTimeMeasurement> getAllTravelTimes() {
        List<TravelTimeMeasurement> travelTimes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SELECT_ALL_FROM + TABLE_TRAVEL_TIME;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String publicationTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLICATION_TIME));
                String siteReference = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SITE_REFERENCE));
                String measurementTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEASUREMENT_TIME));
                double travelTime = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TRAVEL_TIME));
                double freeFlowTravelTime = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FREE_FLOW_TRAVEL_TIME));

                TravelTimeMeasurement measurement = new TravelTimeMeasurement(id, publicationTime, siteReference, measurementTime, travelTime, freeFlowTravelTime);
                travelTimes.add(measurement);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return travelTimes;
    }

    public List<VMSUnit> getAllVMSUnits() {
        List<VMSUnit> vmsUnits = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SELECT_ALL_FROM + TABLE_VMS;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String publicationTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLICATION_TIME));
                String vmsUnitReference = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VMS_UNIT_REFERENCE));
                String messageTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TIME));
                String messageText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TEXT));

                VMSMessage message = new VMSMessage(messageTime, messageText);
                List<VMSMessage> messages = new ArrayList<>();
                messages.add(message);

                VMSUnit vmsUnit = new VMSUnit(id, publicationTime, vmsUnitReference, messages);
                vmsUnits.add(vmsUnit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return vmsUnits;
    }

    public List<Roadwork> getAllRoadworks() {
        List<Roadwork> roadworks = new ArrayList<>();
        String selectQuery = SELECT_ALL_FROM + TABLE_ROADWORKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String publicationTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLICATION_TIME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));

                Roadwork roadwork;
                if ("Current".equals(type)) {
                    roadwork = new CurrentRoadwork(id, publicationTime, description, location, startDate, endDate);
                } else {
                    roadwork = new FutureRoadwork(id, publicationTime, description, location, startDate, endDate);
                }
                roadworks.add(roadwork);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return roadworks;
    }

    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ROADWORKS, null, null);
        db.delete(TABLE_EVENTS, null, null);
        db.delete(TABLE_TRAFFIC_STATUS, null, null);
        db.delete(TABLE_TRAVEL_TIME, null, null);
        db.delete(TABLE_VMS, null, null);
        db.close();
    }
}