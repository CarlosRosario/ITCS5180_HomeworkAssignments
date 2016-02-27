package com.example.group26.imdb_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = (EditText)findViewById(R.id.findMoviesEditText);

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText != null && searchText.getText() != null && !searchText.getText().toString().isEmpty()){
                    Intent searchMovieActivityIntent = new Intent(MainActivity.this, SearchMovieActivity.class);
                    searchMovieActivityIntent.putExtra(Constants.SEARCH_KEY, searchText.getText().toString());
                    startActivity(searchMovieActivityIntent);
                }
            }
        });
    }
}
