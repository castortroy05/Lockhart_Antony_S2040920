package org.me.gcu.lockhart_antony_s2040920;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataLoader {
    private final DataManager dataManager;
    private final ExecutorService executorService;
    private String authHeader;

    public DataLoader(DataManager dataManager) {
        this.dataManager = dataManager;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void loadAllData(String authHeader, LoadDataCallback callback) {
        this.authHeader = authHeader;
        executorService.execute(new LoadDataTask(callback));
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setRequestProperty("Authorization", authHeader);
        conn.connect();
        return conn.getInputStream();
    }

    private void processData(String url, InputStream stream) throws XmlPullParserException, IOException {
        switch (getEndpointType(url)) {
            case CURRENT_ROADWORKS:
                List<CurrentRoadwork> currentRoadworks = DatexParser.parseCurrentRoadworks(stream);
                for (CurrentRoadwork roadwork : currentRoadworks) {
                    dataManager.insertRoadwork(roadwork);
                }
                break;
            case PLANNED_ROADWORKS:
                List<FutureRoadwork> futureRoadworks = DatexParser.parseFutureRoadworks(stream);
                for (FutureRoadwork roadwork : futureRoadworks) {
                    dataManager.insertRoadwork(roadwork);
                }
                break;
            case CURRENT_INCIDENTS:
                List<UnplannedEvent> events = DatexParser.parseUnplannedEvents(stream);
                for (UnplannedEvent event : events) {
                    dataManager.insertUnplannedEvent(event);
                }
                break;
            case TRAFFIC_STATUS:
                List<TrafficStatusMeasurement> statuses = DatexParser.parseTrafficStatus(stream);
                for (TrafficStatusMeasurement status : statuses) {
                    dataManager.insertTrafficStatus(status);
                }
                break;
            case TRAVEL_TIME:
                List<TravelTimeMeasurement> travelTimes = DatexParser.parseTravelTime(stream);
                for (TravelTimeMeasurement travelTime : travelTimes) {
                    dataManager.insertTravelTime(travelTime);
                }
                break;
            case VMS:
                List<VMSUnit> vmsUnits = DatexParser.parseVMS(stream);
                for (VMSUnit vmsUnit : vmsUnits) {
                    dataManager.insertVMS(vmsUnit);
                }
                break;
            default:
                Log.w("DataLoader", "Unknown URL type: " + url);
                break;
        }
    }

    private EndpointType getEndpointType(String url) {
        if (url.contains(Constants.CURRENT_ROADWORKS)) {
            return EndpointType.CURRENT_ROADWORKS;
        } else if (url.contains(Constants.PLANNED_ROADWORKS)) {
            return EndpointType.PLANNED_ROADWORKS;
        } else if (url.contains(Constants.CURRENT_INCIDENTS)) {
            return EndpointType.CURRENT_INCIDENTS;
        } else if (url.contains("TrafficStatusData")) {
            return EndpointType.TRAFFIC_STATUS;
        } else if (url.contains("TravelTimeData")) {
            return EndpointType.TRAVEL_TIME;
        } else if (url.contains("VMS")) {
            return EndpointType.VMS;
        } else {
            return EndpointType.UNKNOWN;
        }
    }

    private class LoadDataTask implements Runnable {
        private final LoadDataCallback callback;

        LoadDataTask(LoadDataCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            boolean success = true;
            try {
                String[] urls = {
                        Constants.DATEX_BASE_URL + Constants.CURRENT_ROADWORKS,
                        Constants.DATEX_BASE_URL + Constants.PLANNED_ROADWORKS,
                        Constants.DATEX_BASE_URL + Constants.CURRENT_INCIDENTS,
                        Constants.DATEX_BASE_URL + "TrafficStatusData/content.xml",
                        Constants.DATEX_BASE_URL + "TravelTimeData/content.xml",
                        Constants.DATEX_BASE_URL + "VMS/content.xml"
                };

                for (String url : urls) {
                    InputStream stream = downloadUrl(url);
                    processData(url, stream);
                    stream.close();
                }
            } catch (IOException | XmlPullParserException e) {
                Log.e("DataLoader", "Error loading data", e);
                success = false;
            }

            final boolean finalSuccess = success;
            if (finalSuccess) {
                callback.onDataLoaded();
            } else {
                callback.onError("Error loading data");
            }
        }
    }

    private enum EndpointType {
        CURRENT_ROADWORKS,
        PLANNED_ROADWORKS,
        CURRENT_INCIDENTS,
        TRAFFIC_STATUS,
        TRAVEL_TIME,
        VMS,
        UNKNOWN
    }

    public interface LoadDataCallback {
        void onDataLoaded();
        void onError(String message);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}