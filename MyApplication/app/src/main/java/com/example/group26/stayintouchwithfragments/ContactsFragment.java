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
public class ContactsFragment extends Fragment {

    FirebaseSerialize firebase;
    List<User> contacts = new ArrayList<User>();
    ListView listView;
    ContactAdapter contactAdapter;
    ContactsFragment.OnFragmentInteractionListener myListener;


    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ContactsFragment.OnFragmentInteractionListener){
            myListener = (ContactsFragment.OnFragmentInteractionListener)context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement ContactsFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        // Get passed firebase reference
        firebase = (FirebaseSerialize)getArguments().getSerializable("Firebase");

        // Grab list of all users (not including the currently logged in/auth'd user)
        contacts.clear(); // avoid adding duplicate contacts to the screen
        firebase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String currentlyLoggedInUserEmail = firebase.getAuth().getProviderData().get("email").toString();

                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    User contact = userSnapShot.getValue(User.class);
                    if(!contact.getEmail().equals(currentlyLoggedInUserEmail)) {
                        contacts.add(contact);

//                    Log.d("user", "User email: " + contact.getEmail());
//                    Log.d("user", "User name: " + contact.getFullName());
//                    Log.d("user", "User password: " + contact.getPassword());
//                    Log.d("user", "User phone: " + contact.getPhoneNumber());
                    }
                }
                contactAdapter = new ContactAdapter(getActivity(), R.layout.contacts_fragment_custom_listview_layout, contacts);
                listView = (ListView) view.findViewById(R.id.contactsFragmentListView);
                listView.setAdapter(contactAdapter);
                contactAdapter.setNotifyOnChange(true);

                // Clicking on a contact logic
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User selectedContact = contacts.get(position);
                        myListener.navigateToViewMessagesFragment(selectedContact);
                    }
                });

                // Long clicking on a contact logic
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        User selectedContact = contacts.get(position);
                        myListener.navigateToViewContactFragment(selectedContact);
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener{
        void navigateToViewMessagesFragment(User selectedContact);
        void navigateToViewContactFragment(User selectedContact);
    }
}
