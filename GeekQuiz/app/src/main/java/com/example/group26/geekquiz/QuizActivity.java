package com.example.group26.geekquiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity implements GetImageAsyncTask.IGetImage{

    int currentQuestionIndex = 0;
    int totalNumberOfQuestions;
    int runningGeekScore = 0;
    private Random rand = new Random();
    public static final String RUNNING_SCORE = "RUNNING_SCORE";

    List<Question> completeQuestionData = new ArrayList<Question>();
    List<Integer> randomIndexList = new ArrayList<Integer>();

    LinearLayout imageLinearLayout; // This linear layout holds either the indeterminate progress bar OR an imageView
    TextView questionTextView, questionNumber;
    RadioGroup answerSelectionRadioGroup;
    ImageView image;

    Question question;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Instantiate ImvageView object that will be used to display question images (if one exists)
        image = new ImageView(QuizActivity.this);

        // Lets go ahead and grab the first question index
        if(getIntent().getExtras().getSerializable(WelcomeActivity.QUESTION_DATA) != null){
            Parcelable[] parcelableQuestionArray = (Parcelable[])getIntent().getExtras().getParcelableArray(WelcomeActivity.QUESTION_DATA);
            for(int i = 0; i < parcelableQuestionArray.length; i++) {
                Question question = (Question) parcelableQuestionArray[i];
                completeQuestionData.add(question);
            }

            // Generate a list of random integers between 0 and 6 - this list will be used to determine a random question order for better user experience.
            randomIndexList = generateRandomIndexes(0,6);

            // Debugging purposes
//            for(int randomNumber: randomIndexList){
//                Log.d("demo3", String.valueOf(randomNumber));
//            }

            imageLinearLayout = (LinearLayout) findViewById(R.id.quizContainer);
            questionTextView = (TextView) findViewById(R.id.questionTextView);
            questionNumber = (TextView) findViewById(R.id.questionNumberTextView);

            // Grab question object
            question = completeQuestionData.get(randomIndexList.get(currentQuestionIndex));

            questionTextView.setText(question.questionText);
            questionNumber.setText("Q" + question.questionSequence);

            answerSelectionRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            answerSelectionRadioGroup.setOrientation(RadioGroup.VERTICAL);

            totalNumberOfQuestions = question.allPossibleAnswersWithAssociatedScores.size();
            for(int i = 0 ; i< totalNumberOfQuestions;i++){
                RadioButton radioButton =  new RadioButton(QuizActivity.this);
                radioButton.setText(question.allPossibleAnswersWithAssociatedScores.get(i).answer);
                radioButton.setTag(question.allPossibleAnswersWithAssociatedScores.get(i).score);
                radioButton.setTextSize(10);
                answerSelectionRadioGroup.addView(radioButton);

            }
            if(question.imageUrl != null && !question.imageUrl.isEmpty()){
                new GetImageAsyncTask(QuizActivity.this).execute(question.imageUrl);
            }

            // Next button click event
            findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // First thing, lets make sure that user has indeed selected an answer choice
                    if(answerSelectionRadioGroup.getCheckedRadioButtonId() == -1){
                        Toast.makeText(QuizActivity.this, "Please select an option..", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        // Before updating the UI, we need to keep track of the score for the answer selected
                        RadioButton selectedRadioButton = (RadioButton)answerSelectionRadioGroup.findViewById(answerSelectionRadioGroup.getCheckedRadioButtonId());

                        String scoreAsString = "";
                        if(selectedRadioButton != null && selectedRadioButton.getTag() != null){
                            scoreAsString = selectedRadioButton.getTag().toString();
                            runningGeekScore += Integer.parseInt(scoreAsString);
                            Log.d("score", String.valueOf(runningGeekScore));
                        }

                        // If this is the last question in the quiz, we need to go ahead and start the result activity - making sure that we pass it the accumulative score.
                        if(currentQuestionIndex == completeQuestionData.size() - 1){
                            Intent resultsActivityIntent = new Intent(QuizActivity.this, ResultsActivity.class);
                            resultsActivityIntent.putExtra(RUNNING_SCORE, runningGeekScore);

                            // Convert question list into a question array so that we can pass it via putExtra()
                            Question[] questionArray = new Question[completeQuestionData.size()];
                            for(int i = 0; i < completeQuestionData.size(); i++){
                                questionArray[i] = completeQuestionData.get(i);
                            }

                            resultsActivityIntent.putExtra(WelcomeActivity.QUESTION_DATA, questionArray);
                            startActivity(resultsActivityIntent);

                            finish();
                        }
                        else {
                            // Now, lets work on the UI
                            imageLinearLayout.removeAllViewsInLayout();
                            questionTextView.setText("");
                            answerSelectionRadioGroup.clearCheck();
                            answerSelectionRadioGroup.removeAllViews();

                            currentQuestionIndex++;
                            question = completeQuestionData.get(randomIndexList.get(currentQuestionIndex));
                            questionTextView = (TextView) findViewById(R.id.questionTextView);
                            questionNumber = (TextView) findViewById(R.id.questionNumberTextView);

                            questionTextView.setText(question.questionText);
                            questionNumber.setText("Q" + question.questionSequence);

                            answerSelectionRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                            answerSelectionRadioGroup.setOrientation(RadioGroup.VERTICAL);

                            totalNumberOfQuestions = question.allPossibleAnswersWithAssociatedScores.size();
                            for(int i = 0 ; i< totalNumberOfQuestions;i++){
                                RadioButton radioButton =  new RadioButton(QuizActivity.this);
                                radioButton.setText(question.allPossibleAnswersWithAssociatedScores.get(i).answer);
                                radioButton.setTag(question.allPossibleAnswersWithAssociatedScores.get(i).score);
                                radioButton.setTextSize(10);
                                answerSelectionRadioGroup.addView(radioButton);

                            }
                            if(question.imageUrl != null && !question.imageUrl.isEmpty()){
                                new GetImageAsyncTask(QuizActivity.this).execute(question.imageUrl);
                            }
                        }
                    }
                }
            });

            // Quit button event
            findViewById(R.id.quitButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
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
        // Dynamically create and show progress bar in specified area
        ProgressBar progressBar = new ProgressBar(QuizActivity.this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        imageLinearLayout.addView(progressBar);
    }

    @Override
    public void finishProcessing() {
        imageLinearLayout.removeAllViewsInLayout();
    }

    // This method will generate the random indexes so that the questions appear to be in random order every time the app is run.
    public List<Integer> generateRandomIndexes(int min,int max){

        Set<Integer> generated = new LinkedHashSet<>(); // Need to use LinkedHashSet in order to remember the order of which the random indexes were inserted into the set.
        while(generated.size() < completeQuestionData.size()){
            int randomInt = rand.nextInt((max-min)+1)+min;
            //Log.d("rand", String.valueOf(randomInt));
            generated.add(randomInt);
        }
        List<Integer> returnList = new ArrayList<Integer>();

        for(int randomIndex: generated){
//            Log.d("rand", "generated: " + String.valueOf(randomIndex));
            returnList.add(randomIndex);
        }
        return returnList;
    }
}
