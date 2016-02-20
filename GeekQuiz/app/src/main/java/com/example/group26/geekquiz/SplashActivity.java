package com.example.group26.geekquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    boolean haveWeDecidedToNavigateToWelcomeActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // After 8 seconds elapse, navigate to the Welcome Activity
        Thread splashTimer = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(8000); // Wait for 8 seconds
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    if(!haveWeDecidedToNavigateToWelcomeActivity){
                        haveWeDecidedToNavigateToWelcomeActivity = true;
                        // Navigate to Welcome Activity
                        Intent welcomeActivityIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(welcomeActivityIntent);
                        finish();
                    }
                }
            }
        });
        splashTimer.start();

        // If Start Quiz button is clicked, navigate to the Welcome Activity
        findViewById(R.id.startQuizButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveWeDecidedToNavigateToWelcomeActivity = true;
                // Navigate to Welcome Activity
                Intent welcomeActivityIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(welcomeActivityIntent);
                finish();
            }
        });
    }
}
