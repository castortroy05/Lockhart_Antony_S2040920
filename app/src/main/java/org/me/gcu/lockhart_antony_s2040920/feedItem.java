package org.me.gcu.lockhart_antony_s2040920;

public class feedItem {



    public int id;
    public String road;
    public String description;
    public String published;
    private String location;
    public String urlString;
    public String ns = null;

    public feedItem(int id, String road, String description, String published, String location, String urlString) {
        this.id = id;
        this.road = road;
        this.description = description;
        this.published = published;
        this.location = location;
        this.urlString = urlString;
    }



    public void setLocation(String location) {
        this.location = location;
    }
}
