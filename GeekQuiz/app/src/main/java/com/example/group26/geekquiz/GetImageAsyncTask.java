package com.example.group26.geekquiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by crosario on 2/20/2016.
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
