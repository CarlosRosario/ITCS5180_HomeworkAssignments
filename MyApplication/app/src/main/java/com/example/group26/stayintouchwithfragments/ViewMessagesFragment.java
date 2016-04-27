package com.example.group26.stayintouchwithfragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMessagesFragment extends Fragment {

    FirebaseSerialize firebase;
    EditText messageEditText;
    List<Message> messageList = new ArrayList<Message>();
    ListView listView;
    MessageAdapter messageAdapter;
    User currentlyLoggedInUser, selectedContact;

    public ViewMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_messages, container, false);

        // Get passed firebase reference
        firebase = (FirebaseSerialize)getArguments().getSerializable("Firebase");

        // Get passed selected contact User reference
        selectedContact = (User)getArguments().getSerializable("SelectedContact");

        // Get the currently logged in User reference
        currentlyLoggedInUser = (User)getArguments().getSerializable("CurrentlyLoggedInUser");

        // Get Reference to message EditText
        messageEditText = (EditText)view.findViewById(R.id.viewMessagesMessageEditText);

        // Initialize listview with custom adapter
        messageAdapter = new MessageAdapter(getActivity(), R.layout.viewmessages_fragment_custom_listview_layout, messageList, currentlyLoggedInUser.getFullName(), this);
        listView = (ListView) view.findViewById(R.id.viewMessagesListView);
        listView.setAdapter(messageAdapter);
        messageAdapter.setNotifyOnChange(true);

        firebase.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messageAdapter.clear();
                for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()){
                    Message message = messageSnapshot.getValue(Message.class);
//                    Log.d("test1", "Message Sender = " + message.getSender());
//                    Log.d("test1", "Message Receiver = " + message.getReceiver());
//                    Log.d("test1", "Current user name = " + currentlyLoggedInUser.getFullName());
//                    Log.d("test1", "Selected Contact name = " + selectedContact.getFullName());
                    if((message.getSender().equals(currentlyLoggedInUser.getFullName()) || message.getSender().equals(selectedContact.getFullName())) && (message.getReceiver().equals(selectedContact.getFullName()) || message.getReceiver().equals(currentlyLoggedInUser.getFullName())) ){
//                        Log.d("test2", "Message Sender = " + message.getSender());
//                        Log.d("test2", "Message Receiver = " + message.getReceiver());
//                        Log.d("test2", "Current user name = " + currentlyLoggedInUser.getFullName());
//                        Log.d("test2", "Selected Contact name = " + selectedContact.getFullName());
                        messageAdapter.add(message);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        // Send button logic
        view.findViewById(R.id.viewMessagesFragmentSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageEditText.getText() != null) {
                    String messageText = messageEditText.getText().toString();
                    if (messageText != null && !messageText.isEmpty()) {

                        if (messageText.length() > 140) {
                            Toast.makeText(getActivity(), "Please enter 140 characters or less", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Add a message object to JSON Store
                        Message newMessage = new Message();
                        newMessage.setMessage_text(messageText);
                        newMessage.setDeletedBy("");
                        newMessage.setMessage_read(false);
                        newMessage.setReceiver(selectedContact.getFullName());
                        newMessage.setSender(currentlyLoggedInUser.getFullName());
                        newMessage.setTimeStamp(new Date().toString());
                        firebase.child("Messages").push().setValue(newMessage);
                        messageAdapter.add(newMessage);

                        // Check if this is a new conversation. If it is, add a conversation object to the JSON Store
                        final Firebase conversationsRef = firebase.child("Conversations");
                        conversationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                boolean isNewConversation = true;
                                for (DataSnapshot conversationSnapShot : dataSnapshot.getChildren()) {
                                    boolean participant1Found = false;
                                    boolean participant2Found = false;
                                    Conversation conversation = conversationSnapShot.getValue(Conversation.class);

                                    if (conversation.getParticipant1().equals(selectedContact.getFullName()) || conversation.getParticipant1().equals(currentlyLoggedInUser.getFullName())) {
                                        participant1Found = true;
                                    }

                                    if (conversation.getParticipant2().equals(selectedContact.getFullName()) || conversation.getParticipant2().equals(currentlyLoggedInUser.getFullName())) {
                                        participant2Found = true;
                                    }

                                    if (participant1Found && participant2Found) {
                                        isNewConversation = false;
                                        break;
                                    }
                                }

                                if (isNewConversation) {
                                    Conversation newConversation = new Conversation();
                                    Firebase newConversationRef = conversationsRef.push();
                                    String key = newConversationRef.getKey();
                                    newConversation.setConversationID(key);
                                    newConversation.setParticipant1(currentlyLoggedInUser.getFullName());
                                    newConversation.setParticipant2(selectedContact.getFullName());
                                    newConversation.setParticipant1Email(currentlyLoggedInUser.getEmail());
                                    newConversation.setParticipant2Email(selectedContact.getEmail());
                                    newConversation.setDeletedBy("");
                                    newConversation.setIsArchived_by_participant1("false");
                                    newConversation.setIsArchived_by_participant2("false");
                                    newConversationRef.setValue(newConversation);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Please enter some text to send", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter some text to send", Toast.LENGTH_SHORT).show();
                }
                messageEditText.setText("");
            }
        });

    return view;
    }

    public void removeMessage(Message message) {
        messageAdapter.remove(message);
    }

}
