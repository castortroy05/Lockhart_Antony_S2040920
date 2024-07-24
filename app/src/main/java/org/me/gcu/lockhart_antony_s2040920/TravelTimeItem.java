package org.me.gcu.lockhart_antony_s2040920;

public class TravelTimeItem extends Item {
    public String route;
    public int currentTime;
    public int freeFlowTime;

    public TravelTimeItem(String id, String timestamp, String route, int currentTime, int freeFlowTime) {
        super(id, "TravelTime", timestamp);
        this.route = route;
        this.currentTime = currentTime;
        this.freeFlowTime = freeFlowTime;
    }
}