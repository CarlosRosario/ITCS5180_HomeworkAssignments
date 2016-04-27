package com.example.group26.stayintouchwithfragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {


    FirebaseSerialize firebase;
    User currentlyLoggedInUser;
    List<Conversation> conversations = new ArrayList<Conversation>();
    ListView listView;
    ConversationAdapter conversationAdapter;
    ConversationFragment.OnFragmentInteractionListener myListener;

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_converstaion, container, false);

        // Get passed in firebase reference
        firebase = (FirebaseSerialize)getArguments().getSerializable("Firebase");

        // Get passed in User
        currentlyLoggedInUser = (User)getArguments().getSerializable("CurrentLoggedInUser");

        conversations.clear(); // make sure we don't add duplicate conversations to the UI
        firebase.child("Conversations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot conversationSnapshot : dataSnapshot.getChildren()){
                    Conversation conversation = conversationSnapshot.getValue(Conversation.class);

                    if(conversation.getParticipant1().equals(currentlyLoggedInUser.getFullName())){
                        if(!conversation.getDeletedBy().equals(currentlyLoggedInUser.getFullName())){
                            if(!conversation.getIsArchived_by_participant1().equals("true")){
                                conversations.add(conversation);
                            }
                        }
                    }
                    else if(conversation.getParticipant2().equals(currentlyLoggedInUser.getFullName())){
                        if(!conversation.getDeletedBy().equals(currentlyLoggedInUser.getFullName())){
                            if(!conversation.getIsArchived_by_participant2().equals("true")){
                                conversations.add(conversation);
                            }
                        }
                    }
                }

                conversationAdapter = new ConversationAdapter(getActivity(), R.layout.conversations_fragment_custom_listview_layout, conversations, currentlyLoggedInUser);
                listView = (ListView) view.findViewById(R.id.conversationsFragmentListView);
                listView.setAdapter(conversationAdapter);
                conversationAdapter.setNotifyOnChange(true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Conversation conversation = conversations.get(position);
                        myListener.navigateToViewMessagesFragment(conversation);
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ConversationFragment.OnFragmentInteractionListener){
            myListener = (ConversationFragment.OnFragmentInteractionListener)context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement ConversationFragment.OnFragmentInteractionListener");
        }

    }

    public interface OnFragmentInteractionListener{
        public void navigateToViewMessagesFragment(Conversation conversation);
    }

}
