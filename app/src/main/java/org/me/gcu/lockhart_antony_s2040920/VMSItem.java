package org.me.gcu.lockhart_antony_s2040920;

public class VMSItem extends Item {
    public String message;

    public VMSItem(String id, String timestamp, String location, String message) {
        super(id, "VMS", timestamp);
        this.location = location;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}