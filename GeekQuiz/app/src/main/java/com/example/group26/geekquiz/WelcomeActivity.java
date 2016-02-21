package com.example.group26.geekquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements GetQuestionDataAsyncTask.IGetQuestionData {

    List<Question> completeQuestionData = new ArrayList<Question>();
    //ProgressDialog progressDialog;
    private ProgressBar progressBar;
    public static final String QUESTION_DATA = "QUESTION_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        if(isConnectedOnline()){
            RequestParams param0 = new RequestParams("GET", "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php");
            param0.addParam("qid", "0");

            RequestParams param1 = new RequestParams("GET", "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php");
            param1.addParam("qid", "1");

            RequestParams param2 = new RequestParams("GET", "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php");
            param2.addParam("qid", "2");

            RequestParams param3 = new RequestParams("GET", "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php");
            param3.addParam("qid", "3");

            RequestParams param4 = new RequestParams("GET", "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php");
            param4.addParam("qid", "4");

            RequestParams param5 = new RequestParams("GET", "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php");
            param5.addParam("qid", "5");

            RequestParams param6 = new RequestParams("GET", "http://dev.theappsdr.com/apis/spring_2016/hw3/index.php");
            param6.addParam("qid", "6");

            new GetQuestionDataAsyncTask(this).execute(param0, param1, param2, param3, param4, param5, param6);
        }
        else {
            Toast.makeText(WelcomeActivity.this, "Failed to connect to network", Toast.LENGTH_LONG).show();
        }


        // Start quiz button
        findViewById(R.id.startQuizButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent quizActivityIntent = new Intent(WelcomeActivity.this, QuizActivity.class);

                // Convert question list into a question array so that we can pass it via putExtra()
                Question[] questionArray = new Question[completeQuestionData.size()];
                for(int i = 0; i < completeQuestionData.size(); i++){
                    questionArray[i] = completeQuestionData.get(i);
                }
                quizActivityIntent.putExtra(QUESTION_DATA, questionArray);
                startActivity(quizActivityIntent);
                finish();
            }
        });

        // Exit button
        findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void setQuestions(List<Question> questions) {
        this.completeQuestionData = questions;

        // For debugging purposes only
        if(this.completeQuestionData != null){
            for(Question q: this.completeQuestionData){
                Log.d("demo2", "Question #" + String.valueOf(q.questionSequence));
                Log.d("demo2", q.questionText);

                if(q.allPossibleAnswersWithAssociatedScores != null){
                    for(AnswerScorePair asp: q.allPossibleAnswersWithAssociatedScores){
                        Log.d("demo2", asp.answer);
                        Log.d("demo2", String.valueOf(asp.score));
                    }
                }
                Log.d("demo2", q.imageUrl);
            }
        }
    }

    @Override
    public void showProcessing() {
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.loadingQuestionsTextView).setVisibility(View.VISIBLE);
    }

    @Override
    public void finishProcessing() {
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.loadingQuestionsTextView).setVisibility(View.GONE);
        findViewById(R.id.startQuizButton).setEnabled(true);
    }

    // Method to check if device is connected to network
    private boolean isConnectedOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // We can use networkInfo.getType() to figure out what kind of network the device is connected to (wifi, ethernet, bluetooth, etc)
        if(networkInfo != null && networkInfo.isConnected()){
            return  true;
        }
        return false;
    }
}
