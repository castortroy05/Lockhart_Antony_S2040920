package org.me.gcu.lockhart_antony_s2040920;

public class VMSMessage {
    private String timeLastSet;
    private String textContent;

    public VMSMessage(String timeLastSet, String textContent) {
        this.timeLastSet = timeLastSet;
        this.textContent = textContent;
    }

    // Getters (and potentially setters) would go here

    public String getTimeLastSet() {
        return timeLastSet;
    }

    public void setTimeLastSet(String timeLastSet) {
        this.timeLastSet = timeLastSet;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}