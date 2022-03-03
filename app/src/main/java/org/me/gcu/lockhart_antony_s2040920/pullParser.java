//package org.me.gcu.lockhart_antony_s2040920;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//
//public class pullParser {
//    private List<parseXML> parseXMLS =new ArrayList<parseXML>();
//    private parseXML plannedRoadWork;
//    private String text;
//
//    public List<parseXML> getPlannedRoadworks(){
//        return parseXMLS;
//    }
//    public List<parseXML> parse(InputStream is){
//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser  parser = factory.newPullParser();
//
//            parser.setInput(is, null);
//
//            int eventType = parser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String tagname = parser.getName();
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        if (tagname.equalsIgnoreCase("item")) {
//                            // create a new instance of plannedRoadWork
//                            plannedRoadWork = new parseXML();
//                        }
//                        break;
//
//                    case XmlPullParser.TEXT:
//                        text = parser.getText();
//                        break;
//
//                    case XmlPullParser.END_TAG:
//                        if (tagname.equalsIgnoreCase("item")) {
//                            // add plannedRoadwork object to list
//                            parseXMLS.add(plannedRoadWork);
//                        }  else if (tagname.equalsIgnoreCase("title")) {
//                            parseXML.setRoad(text);
//                        } else if (tagname.equalsIgnoreCase("description")) {
//                            parseXML.setDescription(text);
//                        } else if (tagname.equalsIgnoreCase("geo:rss")) {
//                            parseXML.setLocation(text);
//                        }else if (tagname.equalsIgnoreCase("published")) {
//                            parseXML.setPublished(text);
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//                eventType = parser.next();
//            }
//
//        } catch (XmlPullParserException e) {e.printStackTrace();}
//        catch (IOException e) {e.printStackTrace();}
//
//        return parseXMLS;
//    }
//
//}
