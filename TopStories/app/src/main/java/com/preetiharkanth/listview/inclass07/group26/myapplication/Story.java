package com.preetiharkanth.listview.inclass07.group26.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ajay Wisawe on 2/29/2016.
 */
public class Story implements Serializable{

    String title = "null_title";
    String abstract_text = "null abstract_text";
    String byline = "null byline";
    String created_date = "null created_date";
    String thumbUrl = "null thumbUrl";
    String normalUrl = "null normalUrl";

    public static Story createStory(JSONObject json) throws JSONException{
        Story story = new Story();

        story.setTitle(json.getString("title"));
        story.setAbstract_text(json.getString("abstract"));
        story.setByline(json.getString("byline"));
        story.setCreated_date(json.getString("created_date"));

        try{
            JSONArray multimedia = json.getJSONArray("multimedia");

            for(int i = 0; i < multimedia.length(); i++){
                JSONObject multimediaJSONObject = multimedia.getJSONObject(i);

                if(multimediaJSONObject != null){
                    if(multimediaJSONObject.getString("format").equals("Standard Thumbnail")){
                        story.setThumbUrl(multimediaJSONObject.getString("url"));
                    } else if(multimediaJSONObject.getString("format").equals("Normal")){
                        story.setNormalUrl(multimediaJSONObject.getString("url"));
                    }
                }
            }
        } catch(JSONException e){
            e.printStackTrace();
        }



        return story;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract_text() {
        return abstract_text;
    }

    public void setAbstract_text(String abstract_text) {
        this.abstract_text = abstract_text;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getNormalUrl() {
        return normalUrl;
    }

    public void setNormalUrl(String normalUrl) {
        this.normalUrl = normalUrl;
    }
}
