package com.example.aran2;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiaryContent {
    public String state;
    public String emotion;
    public String content;
    public ArrayList<String> imageUri;
    public String day;
    public DiaryContent(String state,String day, String emotion, String content, ArrayList<String> imageUri){
        this.day = day;
        this.state = state;
        this.emotion = emotion;
        this.content = content;
        this.imageUri = imageUri;
    }

    public DiaryContent(){

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImageUri() {
        return imageUri;
    }

    public void setImageUri(ArrayList<String> imageUri) {
        this.imageUri = imageUri;
    }

    public void setDay(String day){
        this.day = day;
    }

    public String getDay(){
        return this.day;
    }
}
