package org.me.gcu.lockhart_antony_s2040920;

public class TrafficEvent extends Item {
    public String startDate;
    public String endDate;

    public TrafficEvent(String id, String type, String timestamp, String startDate, String endDate) {
        super(id, type, timestamp);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
