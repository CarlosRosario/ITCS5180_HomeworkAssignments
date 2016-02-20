package com.example.group26.geekquiz;

import java.io.Serializable;

/**
 * Created by Carlos on 2/20/2016.
 */
public class AnswerScorePair implements Serializable {

    public String answer;
    public int score;

    public AnswerScorePair(String answer, int score){
        this.answer = answer;
        this.score = score;
    }
}
