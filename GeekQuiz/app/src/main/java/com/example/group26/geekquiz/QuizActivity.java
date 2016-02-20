package com.example.group26.geekquiz;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity implements GetImageAsyncTask.IGetImage{

    List<Question> completeQuestionData = new ArrayList<Question>();
    List<Integer> randomIndexList = new ArrayList<Integer>();
    int currentQuestionIndex = 0;
    LinearLayout imageLinearLayout;
    TextView questionTextView, questionNumber;
    RadioGroup optionRadioGroup;
    int noOfOptions;
    Question question;

    ImageView image;


    int randIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        image = new ImageView(QuizActivity.this);

        // Lets go ahead and grab the first question index
        if(getIntent().getExtras().getSerializable(WelcomeActivity.QUESTION_DATA) != null){
            Parcelable[] parcelableQuestionArray = (Parcelable[])getIntent().getExtras().getParcelableArray(WelcomeActivity.QUESTION_DATA);
            for(int i = 0; i < parcelableQuestionArray.length; i++) {
                Question question = (Question) parcelableQuestionArray[i];
                completeQuestionData.add(question);
            }

            randomIndexList = randInt(0,6);

            imageLinearLayout = (LinearLayout) findViewById(R.id.quizContainer);

            question = completeQuestionData.get(randomIndexList.get(currentQuestionIndex));
            questionTextView = (TextView) findViewById(R.id.questionTextView);
            questionNumber = (TextView) findViewById(R.id.questionNumberTextView);

            questionTextView.setText(question.questionText);
            questionNumber.setText("Q" + question.questionSequence);

            optionRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            optionRadioGroup.setOrientation(RadioGroup.VERTICAL);

            noOfOptions = question.allPossibleAnswersWithAssociatedScores.size();
            for(int i = 0 ; i<noOfOptions;i++){
                RadioButton rb =  new RadioButton(QuizActivity.this);
                rb.setText(question.allPossibleAnswersWithAssociatedScores.get(i).answer);
                optionRadioGroup.addView(rb);

            }
            new GetImageAsyncTask(QuizActivity.this).execute(question.imageUrl);


            findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageLinearLayout.removeAllViewsInLayout();
                    questionTextView.setText("");
                    optionRadioGroup.clearCheck();
                    optionRadioGroup.removeAllViews();

                    currentQuestionIndex++;
                    question = completeQuestionData.get(randomIndexList.get(currentQuestionIndex));
                    questionTextView = (TextView) findViewById(R.id.questionTextView);
                    questionNumber = (TextView) findViewById(R.id.questionNumberTextView);

                    questionTextView.setText(question.questionText);
                    questionNumber.setText("Q" + question.questionSequence);

                    optionRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                    optionRadioGroup.setOrientation(RadioGroup.VERTICAL);

                    noOfOptions = question.allPossibleAnswersWithAssociatedScores.size();
                    for(int i = 0 ; i<noOfOptions;i++){
                        RadioButton rb =  new RadioButton(QuizActivity.this);
                        rb.setText(question.allPossibleAnswersWithAssociatedScores.get(i).answer);
                        optionRadioGroup.addView(rb);

                    }
                    new GetImageAsyncTask(QuizActivity.this).execute(question.imageUrl);
                }
            });




            // Randomize the list OR choose random index OF the list
        }


    }

    @Override
    public void setImage(Bitmap b) {
        image.setImageResource(android.R.color.transparent);
        if(b != null){
            image.setImageBitmap(b);
            imageLinearLayout.addView(image);
        }
    }

    @Override
    public void showProcessing() {

    }

    @Override
    public void finishProcessing() {

    }

    public  List<Integer> randInt(int min,int max){
        Random rand = new Random();
        Set<Integer> generated = new HashSet<Integer>();
        while(generated.size() < rand.nextInt(max) +1){
            int randomInt = rand.nextInt((max-min)+1)+min;
            generated.add(randomInt);
        }

        List<Integer> returnList = new ArrayList<Integer>();

        for(int randomIndex: generated){
            returnList.add(randomIndex);
        }
        return returnList;
    }
}
