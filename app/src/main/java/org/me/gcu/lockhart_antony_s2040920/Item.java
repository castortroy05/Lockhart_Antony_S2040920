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


//    public void setDescription(String description)  {
//
//        if(description.contains("<br />")){
//
//        String[] info = description.split("<br />");
//
//        //Log.e("FULLINFO", description.toString());
//
//        String endDateStr = "";
//        String startDateStr;
//
//        startDateStr = info[0];
//
//        if(info.length > 1)
//            endDateStr = info[1];
//
//        if(info.length>2){
//
//            String[] di = info[2].split(": ");
//
//            String delayInformation = "";
//            if(di.length>1)
//                delayInformation = di[1];
//        }
//
//        startDateStr = startDateStr.substring(12);
//        startDateStr.trim();
//        //startDateStr = startDateStr.
//        endDateStr = endDateStr.substring(10);
//        Date startDate = null;
//        Date endDate = null;
//
//        try {
//            startDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(startDateStr);
//            endDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(endDateStr);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//
//            Date startDate1 = startDate;
//        this.endDate = endDate;
//        }
//        this.description = description;
//    }
    }



