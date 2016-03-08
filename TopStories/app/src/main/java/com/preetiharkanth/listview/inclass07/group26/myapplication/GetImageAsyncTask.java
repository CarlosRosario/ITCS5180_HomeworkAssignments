package com.preetiharkanth.listview.inclass07.group26.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by meredithbrowne on 2/29/16.
 */
public class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    IGetImage activity;

    public GetImageAsyncTask(IGetImage activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProcessing();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.finishProcessing();
        activity.setImage(bitmap);
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try {
            Log.d("downloading image...", params[0]);
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            Bitmap image = BitmapFactory.decodeStream(connection.getInputStream()); // use this code to return an image
            return image;
        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    public interface IGetImage {
        void setImage(Bitmap b);
        void showProcessing();
        void finishProcessing();
    }
}
