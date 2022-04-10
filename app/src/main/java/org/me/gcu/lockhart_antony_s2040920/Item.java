package org.me.gcu.lockhart_antony_s2040920;
//Lockhart_Antony_S2040920

import java.util.Date;

public class Item {
    public Item(String title, String link, String description, String location, String date, String author, String comments, String pubDate, Date endDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.location = location;
        this.date = date;
    }

    public  String title;
    public  String link;
    public String description;
    public  String location;
    public  String date;


    private final int daysToComplete = 0;
//        public final int uuid;

    public Item(String title, String description, String link, String location, String date) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.location = location;
        this.date = date;
//        Object uuid = UUID.randomUUID().hashCode();
    }

    public static <K, T> K getUuid(T t) {
        return (K) t;
    }



    }



