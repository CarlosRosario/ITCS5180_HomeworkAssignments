package com.example.group26.imdb_app;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class MovieWebView extends AppCompatActivity {

    String imdbID;
    String url;
    WebView webView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_web_view);

        progressDialog = new ProgressDialog(MovieWebView.this);

        if(getIntent().getExtras() != null){
            imdbID = getIntent().getExtras().getString(Constants.IMDB_ID);
            url = Constants.IMDB_BASE + imdbID;
        }

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        // load from a URL
        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
        }
    }

    public void showProcessing() {
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading Movie");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void finishProcessing() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    //boolean loadingFinished = true;
    //boolean redirect = false;
    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(progressDialog != null && !progressDialog.isShowing()){
                showProcessing();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(webView.getProgress() == 100){
                finishProcessing();

                TextView urlTextView = (TextView)findViewById(R.id.urlTextView);
                urlTextView.setText(webView.getUrl());
                urlTextView.setVisibility(View.VISIBLE);
            }



        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
