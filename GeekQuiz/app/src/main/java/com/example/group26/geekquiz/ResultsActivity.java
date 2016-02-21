package com.example.group26.geekquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meredithbrowne on 2/20/16.
 */
public class ResultsActivity extends AppCompatActivity {

    List<Question> completeQuestionData = new ArrayList<Question>();

    int score = 0;
    TextView result_field;
    ImageView result_image;
    TextView result_description;
    public static final String RETAKEQUIZ = "RETAKEQUIZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        if(getIntent().getExtras().getInt(QuizActivity.RUNNING_SCORE) != 0){
            score = getIntent().getExtras().getInt(QuizActivity.RUNNING_SCORE);
        }

        if(getIntent().getExtras().getSerializable(WelcomeActivity.QUESTION_DATA) != null) {
            Parcelable[] parcelableQuestionArray = (Parcelable[]) getIntent().getExtras().getParcelableArray(WelcomeActivity.QUESTION_DATA);
            for (int i = 0; i < parcelableQuestionArray.length; i++) {
                Question question = (Question) parcelableQuestionArray[i];
                completeQuestionData.add(question);
            }
        }

        result_field = (TextView) findViewById(R.id.titleTextField);

        if(result_field == null){
            Log.d("null", "result_field is null...not sure how");
        }
        result_image = (ImageView) findViewById(R.id.result_image);
        result_description = (TextView) findViewById(R.id.result_description);

        if (score <= 10) {
            // "Non-Geek";
            result_field.setText(R.string.non_geek);
            result_image.setImageResource(R.drawable.nongeek);
            result_description.setText(R.string.non_geek_description);
        } else if (score <= 50) {
            // "Semi-Geek";
            result_field.setText(R.string.semi_geek);
            result_image.setImageResource(R.drawable.semigeek);
            result_description.setText(R.string.semi_geek_description);
        } else {
            // "Uber-Geek";
            result_field.setText(R.string.uber_geek);
            result_image.setImageResource(R.drawable.ubergeek);
            result_description.setText(R.string.uber_geek_description);
        }

        // quit button - finish current activity and send the user to the Welcome activity
        findViewById(R.id.button_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent welcomeActivityIntent = new Intent(ResultsActivity.this, WelcomeActivity.class);
                startActivity(welcomeActivityIntent);
                finish();
            }
        });

        // finish the current activity and send the user back to the Quiz activity to Re-take the quiz
        findViewById(R.id.button_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retakeQuizIntent = new Intent(ResultsActivity.this, QuizActivity.class);


                // Convert question list into a question array so that we can pass it via putExtra()
                Question[] questionArray = new Question[completeQuestionData.size()];
                for(int i = 0; i < completeQuestionData.size(); i++){
                    questionArray[i] = completeQuestionData.get(i);
                }

                retakeQuizIntent.putExtra(WelcomeActivity.QUESTION_DATA, questionArray);
                startActivity(retakeQuizIntent);
                finish();
            }
        });
    }
}