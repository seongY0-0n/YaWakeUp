package org.techtown.yawakeup;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private String documentId;
    private String title;
    private String date;
    private String time;
    private String place;
    private String count;
    private String contents;


    public Post(){

    }

    public Post(String documentId, String title, String date, String time, String place, String count, String contents) {
        this.documentId = documentId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.place = place;
        this.count = count;
        this.contents = contents;
    }

    public String getDocumentId() { return documentId; }

    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getPlace() { return place; }

    public void setPlace(String place) { this.place = place; }

    public String getCount() { return count; }

    public void setCount(String count) { this.count = count; }

    public String getContents() { return contents; }

    public void setContents(String contents) { this.contents = contents; }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("documentId", documentId);
        result.put("title", title);
        result.put("date", date);
        result.put("time", time);
        result.put("place", place);
        result.put("count", count);
        result.put("contents", contents);

        return result;
    }
}
