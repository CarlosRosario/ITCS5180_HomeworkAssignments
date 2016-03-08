package com.preetiharkanth.listview.inclass07.group26.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Ajay Wisawe on 2/29/2016.
 */
public class GetTopStoryDataAsyncTask extends AsyncTask<String, Void, List<Story>> {

    IGetTopStoryData activity;


    public GetTopStoryDataAsyncTask(IGetTopStoryData activity){
        this.activity = activity;
    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.startProcessing();
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param stories The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(List<Story> stories) {
        super.onPostExecute(stories);
        activity.setStoryData(stories);
        activity.finishProcessing();
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected List<Story> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int statusCode = connection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();

                while(line != null){
                    Log.d("testing", line );
                    sb.append(line);
                    line = reader.readLine();
                }

                try {
                    Log.d("testing", sb.toString());
                    // Testing that the process dialog indeed works
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    return TopStoryJSONParser.parseStories(sb.toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }




        return null;
    }

    public interface IGetTopStoryData{
        public void setStoryData(List<Story> stories);
        public void startProcessing();
        public void finishProcessing();
    }
}
