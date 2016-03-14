package com.preetiharkanth.listview.inclass07.group26.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Wisawe on 2/29/2016.
 */
public class TopStoryJSONParser {

    static List<Story> parseStories(String in) throws JSONException {
        List<Story> storyList = new ArrayList<Story>();

        JSONObject root = new JSONObject(in);
        JSONArray storyJSONArray = root.getJSONArray("results");

        for(int i = 0; i < storyJSONArray.length(); i++){
            JSONObject movieJSONObject = storyJSONArray.getJSONObject(i);
            Story story = Story.createStory(movieJSONObject);

            storyList.add(story);
        }

        // for debugging purposes
        for(Story s: storyList){
            Log.d("stories", s.getTitle());
            Log.d("stories", s.getThumbUrl());
            Log.d("stories", s.getNormalUrl());
        }

        return storyList;
    }
}
