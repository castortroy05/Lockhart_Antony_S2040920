package org.me.gcu.lockhart_antony_s2040920;

public class Constants {
    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Base URL for the DATEX II traffic data API
    public static final String DATEX_BASE_URL = "https://datex2.trafficscotland.org/rest/2.3/publications/";

    // Endpoints for different types of traffic information
    public static final String PLANNED_ROADWORKS = "FutureRoadworks/Content.xml";
    public static final String CURRENT_ROADWORKS = "CurrentRoadworks/Content.xml";
    public static final String CURRENT_INCIDENTS = "UnplannedEvents/Content.xml";
    public static final String TRAFFIC_STATUS = "TrafficStatusData/content.xml";
    public static final String TRAVEL_TIME = "TravelTimeData/content.xml";
    public static final String VMS = "VMS/content.xml";

    // Database related constants
    public static final String DATABASE_NAME = "traffic_data.db";
    public static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_ROADWORKS = "roadworks";
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_TRAFFIC_STATUS = "traffic_status";
    public static final String TABLE_TRAVEL_TIME = "travel_time";
    public static final String TABLE_VMS = "vms";

    // Common column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PUBLICATION_TIME = "publication_time";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";

    // Date format for parsing and displaying dates
    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
}