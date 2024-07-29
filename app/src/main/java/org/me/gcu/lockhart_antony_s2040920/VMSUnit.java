package org.me.gcu.lockhart_antony_s2040920;

import java.util.List;

public class VMSUnit extends DatexItem {
    private String id;
    private String publicationTime;
    private String vmsUnitReference;
    private List<VMSMessage> messages;

    public VMSUnit(String id, String publicationTime, String vmsUnitReference, List<VMSMessage> messages) {
        this.id = id;
        this.publicationTime = publicationTime;
        this.vmsUnitReference = vmsUnitReference;
        this.messages = messages;
    }

    // Getters (and potentially setters) would go here

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getVmsUnitReference() {
        return vmsUnitReference;
    }

    public void setVmsUnitReference(String vmsUnitReference) {
        this.vmsUnitReference = vmsUnitReference;
    }

    public List<VMSMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<VMSMessage> messages) {
        this.messages = messages;
    }
}