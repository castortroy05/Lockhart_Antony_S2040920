package org.me.gcu.lockhart_antony_s2040920;

public class TrafficStatusItem extends Item {
    public String status;

    public TrafficStatusItem(String id, String timestamp, String location, String status) {
        super(id, "TrafficStatus", timestamp);
        this.location = location;
        this.status = status;
    }
}