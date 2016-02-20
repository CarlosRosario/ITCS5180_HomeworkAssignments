package com.example.group26.geekquiz;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by Carlos on 2/20/2016.
 */
public class Question implements Parcelable {



    public int questionSequence;
    public String questionText;
    public List<AnswerScorePair> allPossibleAnswersWithAssociatedScores;
    public String imageUrl;


    public Question(int questionSequence, String questionText, List<AnswerScorePair> allPossibleAnswersWithAssociatedScores, String imageUrl){
        this.questionSequence = questionSequence;
        this.questionText = questionText;
        this.allPossibleAnswersWithAssociatedScores = allPossibleAnswersWithAssociatedScores;
        this.imageUrl = imageUrl;
    }

    public Question(Parcel parcel){
        this.questionSequence = parcel.readInt();
        this.questionText = parcel.readString();
        this.allPossibleAnswersWithAssociatedScores = parcel.readArrayList(null);
        this.imageUrl = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(questionSequence);
        dest.writeString(questionText);
        dest.writeList(allPossibleAnswersWithAssociatedScores);
        dest.writeString(imageUrl);
    }

    public static Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
