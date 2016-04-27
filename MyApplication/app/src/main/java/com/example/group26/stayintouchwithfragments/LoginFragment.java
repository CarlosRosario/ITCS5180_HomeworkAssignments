package com.example.group26.stayintouchwithfragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    FirebaseSerialize firebase;
    LoginFragment.OnFragmentInteractionListener myListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof LoginFragment.OnFragmentInteractionListener){
            myListener = (LoginFragment.OnFragmentInteractionListener)context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement LoginFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Get passed firebase reference
        firebase = (FirebaseSerialize)getArguments().getSerializable("Firebase");

        // Check if there is a current user session
        boolean isThereACurrentUserSession = isCurrentUserAuthenticated(firebase);
        if(isThereACurrentUserSession){
            // Start Conversation activity, and finish the login activity
            myListener.navigateToConversationsFragment();
        }

        // Get references to the email/password EditTexts
        final EditText emailEditText = (EditText)view.findViewById(R.id.loginFragmentEmailEditText);
        final EditText passwordEditText = (EditText)view.findViewById(R.id.loginFragmentPasswordEditText);

        // Login button logic
        view.findViewById(R.id.loginFragmentLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailEditText.getText() == null || passwordEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter an email address to login", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwordEditText.getText() == null || passwordEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter a password to login", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String typedEmail = emailEditText.getText().toString();
                String typedPassword = passwordEditText.getText().toString();

                firebase.authWithPassword(typedEmail, typedPassword, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        myListener.navigateToConversationsFragment();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getActivity(), "Unsuccessful authentication", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Signup button logic
        view.findViewById(R.id.loginFragmentNewAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.navigateToSignUpFragment();
            }
        });

        return view;
    }

    public boolean isCurrentUserAuthenticated(Firebase firebase){

        AuthData authData = firebase.getAuth();

        if(authData != null){
            return true;
        }
        else {
            return false;
        }
    }

    public interface OnFragmentInteractionListener{
        void navigateToConversationsFragment();
        void navigateToSignUpFragment();
    }

}
