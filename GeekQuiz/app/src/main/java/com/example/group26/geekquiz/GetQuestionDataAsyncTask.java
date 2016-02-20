package com.example.group26.geekquiz;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 2/20/2016.
 */
public class GetQuestionDataAsyncTask extends AsyncTask<RequestParams, Void, List<Question>> {

    IGetQuestionData activity;

    public GetQuestionDataAsyncTask(IGetQuestionData activity){
        this.activity = activity;
    }

    @Override
    protected List<Question> doInBackground(RequestParams... params) {

        BufferedReader bufferedReader = null;
        List<Question> completeQuestionData = new ArrayList<Question>();
        try {

            for(RequestParams param: params){
                HttpURLConnection connection = param.setupConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                Question question = parseAndReturnQuestionData(sb.toString());
                completeQuestionData.add(question);
            }

            return completeQuestionData;

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public Question parseAndReturnQuestionData(String questionDataString){
        boolean doesImageURLExist = false;
        Log.d("demo", questionDataString);

        String[] questionDataSplitBySemiColon = questionDataString.split(";");
        int questionDataArrayLength = questionDataSplitBySemiColon.length;
        int lastElementInQuestionDataArrayIndex = questionDataArrayLength - 1;

//        for(String s: questionDataSplitBySemiColon){
//            Log.d("demo", s);
//        }

        // Parse question data in order to create a Question object

        // Grab question sequence number
        int questionSequence = Integer.parseInt(questionDataSplitBySemiColon[0]);

        // Grab questionText
        String questionText = questionDataSplitBySemiColon[1];

        // Attempt to grab image url (if one exists)
        String imageUrl = "";
        if(questionDataSplitBySemiColon[lastElementInQuestionDataArrayIndex].contains(("http"))){
            doesImageURLExist = true;
            imageUrl = questionDataSplitBySemiColon[lastElementInQuestionDataArrayIndex];
        }

        // Create Answer/Score pairs
        int lastElementForAnswerScorePairs = 0;
        if(doesImageURLExist){
            lastElementForAnswerScorePairs = lastElementInQuestionDataArrayIndex - 1;
        }
        else {
            lastElementForAnswerScorePairs = lastElementInQuestionDataArrayIndex;
        }

        List<AnswerScorePair> answerScorePairs = new ArrayList<AnswerScorePair>();
        for(int i = 2; i <= lastElementForAnswerScorePairs; i+=2){
            // 'i' will be the question -> this will always be even
            // 'i+1' will be the score -> this will always be odd

            String potentialAnswer = questionDataSplitBySemiColon[i];
            int score = Integer.parseInt(questionDataSplitBySemiColon[i+1]);
            answerScorePairs.add(new AnswerScorePair(potentialAnswer,score));
        }

        // Create and return final question object
        Question question = new Question(questionSequence, questionText, answerScorePairs, imageUrl);
        return question;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProcessing();
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        super.onPostExecute(questions);
        activity.setQuestions(questions);
        activity.finishProcessing();
    }

    public interface IGetQuestionData {
        void setQuestions(List<Question> questions);
        void showProcessing();
        void finishProcessing();
    }
}
