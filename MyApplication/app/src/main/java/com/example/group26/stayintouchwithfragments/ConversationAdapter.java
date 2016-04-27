package com.example.group26.stayintouchwithfragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

/**
 * Created by Carlos on 4/27/2016.
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> {

    List<Conversation> mData;
    Context mContext;
    int mResource;
    User currentlyLoggedInUser;
    View thisView;
    String otherContactName;

    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            thisView = convertView;
        }

        final Conversation conversation = mData.get(position);
        getContactInformation(conversation, currentlyLoggedInUser);
        redBubbleLogic(conversation, currentlyLoggedInUser);

        return convertView;
    }

    public ConversationAdapter(Context context, int resource, List<Conversation> objects, User currentlyLoggedInUser){
        super(context, resource, objects);
        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
        this.currentlyLoggedInUser = currentlyLoggedInUser;
    }

    private void getContactInformation(Conversation currentConversation, User currentlyLoggedInUser){

        String otherContactEmail = "";
        if(currentConversation.getParticipant1Email().equals(currentlyLoggedInUser.getEmail())){
            otherContactEmail = currentConversation.getParticipant2Email();
        }
        else if (currentConversation.getParticipant2Email().equals(currentlyLoggedInUser.getEmail())){
            otherContactEmail = currentConversation.getParticipant1Email();
        }

        final Firebase firebase = new Firebase("https://stayintouchfrag-5180.firebaseio.com/");
        firebase.child("users").child(otherContactEmail.replaceAll("[@.]", "+")).addListenerForSingleValueEvent(new RetrieveContactListener());
    }

    private void redBubbleLogic(Conversation currentConversation, User currentlyLoggedInUser){

        if(currentConversation.getParticipant1().equals(currentlyLoggedInUser.getFullName())){
            otherContactName = currentConversation.getParticipant2();
        }
        else if (currentConversation.getParticipant2().equals(currentlyLoggedInUser.getFullName())){
            otherContactName = currentConversation.getParticipant1();
        }

        final Firebase firebase = new Firebase("https://stayintouchfrag-5180.firebaseio.com/");
        firebase.child("Messages").addListenerForSingleValueEvent(new RetrieveRelevantMessagesListener());

    }

    private class RetrieveContactListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User otherUser = dataSnapshot.getValue(User.class);
            hydrate(otherUser);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    private class RetrieveRelevantMessagesListener implements ValueEventListener{
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()){
                Message message = messageSnapshot.getValue(Message.class);
                if((message.getSender().equals(currentlyLoggedInUser.getFullName()) || message.getSender().equals(otherContactName)) && (message.getReceiver().equals(otherContactName) || message.getReceiver().equals(currentlyLoggedInUser.getFullName())) ){
                    if(message.isMessage_read() == false){
                        setRedBubble();
                    }
                }
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    private void hydrate(User user){

        // Set contact profile picture
        ImageView profilePictureImageView = (ImageView)thisView.findViewById(R.id.conversationsActivityProfileImageView);
        String base64ProfilePicture = user.getBase64Picture();
        if(base64ProfilePicture != null && !base64ProfilePicture.isEmpty()){
            byte[] decodedImageByteArray = Base64.decode(base64ProfilePicture, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
            profilePictureImageView.setImageBitmap(decodedImage);
        }

        // Set contact first and last name
        TextView fullNameTextview = (TextView)thisView.findViewById(R.id.conversationsActivityFullNameTextView);
        String fullName = user.getFullName();
        if(fullName != null && !fullName.isEmpty()){
            fullNameTextview.setText(fullName);
        }
    }

    private void setRedBubble(){
        ImageView redBubbleImageView = (ImageView)thisView.findViewById(R.id.conversationsActivityRedBubbleImageView);
        redBubbleImageView.setBackgroundResource(R.drawable.redbubble);
    }
}
