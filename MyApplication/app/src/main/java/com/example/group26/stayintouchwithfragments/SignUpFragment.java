package com.example.group26.stayintouchwithfragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.PriorityQueue;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    FirebaseSerialize firebase;
    SignUpFragment.OnFragmentInteractionListener myListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SignUpFragment.OnFragmentInteractionListener){
            myListener = (SignUpFragment.OnFragmentInteractionListener)context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement SignUpFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Get passed firebase reference
        firebase = (FirebaseSerialize)getArguments().getSerializable("Firebase");

        // Get references to edit texts on signup fragment
        final EditText fullNameEditText = (EditText)view.findViewById(R.id.signupFragmentNameEditText);
        final EditText emailEditText = (EditText)view.findViewById(R.id.signupFragmentEmailEditText);
        final EditText phoneNumberEditText = (EditText)view.findViewById(R.id.signupFragmentPhoneNumberEditText);
        final EditText passwordEditText = (EditText)view.findViewById(R.id.signupFragmentPasswordEditText);
        final EditText confirmedPasswordEditText = (EditText)view.findViewById(R.id.signupFragmentPasswordConfirmationEditText);

        // Signup button logic
        view.findViewById(R.id.signupFragmentSignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // User input validations
                if(fullNameEditText.getText() == null || fullNameEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(emailEditText.getText() == null || emailEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(phoneNumberEditText.getText() == null || phoneNumberEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwordEditText.getText() == null || passwordEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(confirmedPasswordEditText.getText() == null || confirmedPasswordEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String fullName = fullNameEditText.getText().toString();
                final String signUpEmail = emailEditText.getText().toString();
                final String phoneNumber = phoneNumberEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String confirmPassword = confirmedPasswordEditText.getText().toString();

                // Make sure that password/confirm password fields are the same
                if (password.equals(confirmPassword)) {

                    // Save user as registered user and to JSON store
                    firebase.createUser(signUpEmail, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> stringObjectMap) {

                            //Firebase newUserRef = firebase.child("users").child(fullName.trim().toLowerCase().replace(" ", ""));
                            Firebase newUserRef = firebase.child("users").child(signUpEmail.toLowerCase().replaceAll("[@.]", "+"));

                            User newUser = new User();
                            newUser.setFullName(fullName);
                            newUser.setEmail(signUpEmail);
                            newUser.setPhoneNumber(phoneNumber);
                            newUser.setPassword(password);
                            newUser.setBase64Picture(((MainActivity) getActivity()).getDummyProfilePic());

                            newUserRef.setValue(newUser);
                            Toast.makeText(getActivity(), "Successfully created new account", Toast.LENGTH_SHORT).show();
                            myListener.returnToLoginFragment();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            int errorCode = firebaseError.getCode();

                            switch (errorCode) {

                                case FirebaseError.EMAIL_TAKEN:
                                    Toast.makeText(getActivity(), "Email is already taken", Toast.LENGTH_SHORT).show();
                                    break;

                                default:
                                    Toast.makeText(getActivity(), firebaseError.toString(), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cancel button logic
        view.findViewById(R.id.signupFragmentCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.returnToLoginFragment();
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        public void returnToLoginFragment();
    }

}
