package com.preetiharkanth.listview.inclass07.group26.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TopStoriesActivity extends AppCompatActivity implements  GetTopStoryDataAsyncTask.IGetTopStoryData{

    List<Story> stories = new ArrayList<Story>();
    String category;
    String baseUrl = "http://api.nytimes.com/svc/topstories/v1/";
    //4ec26f049ce3e63cf7f39bec8bc4a193:5:74582549

    ProgressDialog progressDialog;
    ListView listView;
    //colorAdaptor.setNotifyOnChange(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stories);



        if(getIntent() != null && getIntent().getExtras() != null){
            category = getIntent().getExtras().getString("CATEGORY");

            String url = baseUrl + category + ".json?api-key=4ec26f049ce3e63cf7f39bec8bc4a193:5:74582549";
            new GetTopStoryDataAsyncTask(TopStoriesActivity.this).execute(url);
        }
    }

    @Override
    public void setStoryData(final List<Story> stories) {
        this.stories = stories;

        StoryAdaptor storyAdaptor = new StoryAdaptor(this,R.layout.row_item_layout,stories);
        listView  = (ListView) findViewById(R.id.topStoryListView);
        listView.setAdapter(storyAdaptor);
        storyAdaptor.setNotifyOnChange(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopStoriesActivity.this,DetailsActivity.class);
                intent.putExtra("STORY",stories.get(position));
                startActivity(intent);
               // Log.d("demo", "position is " + position + ", Value is " + stories.get(position).toString());
            }
        });

    }

    @Override
    public void startProcessing() {
        progressDialog = new ProgressDialog(TopStoriesActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading Stories..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void finishProcessing() {
        progressDialog.dismiss();
    }
}
