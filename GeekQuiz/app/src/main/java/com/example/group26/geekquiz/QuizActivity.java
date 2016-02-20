package com.example.group26.geekquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    List<Question> completeQuestionData = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if(getIntent().getExtras().getSerializable(WelcomeActivity.QUESTION_DATA) != null){
            Question[] questionArray = (Question[])getIntent().getExtras().getSerializable(WelcomeActivity.QUESTION_DATA);
            completeQuestionData = Arrays.asList(questionArray);

            // Randomize the list OR choose random index OF the list
        }


    }
}
