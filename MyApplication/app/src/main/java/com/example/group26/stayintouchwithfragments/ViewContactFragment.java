package com.example.group26.stayintouchwithfragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewContactFragment extends Fragment {

    User selectedContact;

    public ViewContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_contact, container, false);

        // Get passed User
        selectedContact = (User)getArguments().getSerializable("SelectedContact");

        TextView fullNameAtTop = (TextView)view.findViewById(R.id.viewContactFirstAndLastName);
        ImageView profileImage = (ImageView)view.findViewById(R.id.viewContactImageView);
        TextView nameAtMiddle = (TextView)view.findViewById(R.id.viewContactNameText);
        TextView phoneNumber = (TextView)view.findViewById(R.id.viewContactPhoneNumberText);
        TextView email = (TextView)view.findViewById(R.id.viewContactEmailIdText);

        fullNameAtTop.setText(selectedContact.getFullName());
        nameAtMiddle.setText(" " + selectedContact.getFullName());
        phoneNumber.setText(" " + selectedContact.getPhoneNumber());
        email.setText(" " + selectedContact.getEmail());

        byte[] decodedImageByteArray = Base64.decode(selectedContact.getBase64Picture(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
        profileImage.setImageBitmap(decodedImage);

        return view;
    }
}
