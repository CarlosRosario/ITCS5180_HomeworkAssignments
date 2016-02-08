package com.group26.ticketreservation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ViewTicketActivity extends AppCompatActivity {


    private List<ticket> ticketList = new LinkedList<ticket>();

    private EditText nameField;
    private EditText sourceField;
    private EditText destinationField;
    private EditText departureDateField;
    private EditText departureTimeField;
    private EditText returnDateField;
    private EditText returnTimeField;
    private RadioButton oneWayRadioButton;
    private RadioButton roundTripRadioButton;

    int ticketIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

        if(getIntent().getExtras().getSerializable(MainActivity.TICKETLIST) != null){
            ticket[] ticketListArray = (ticket[])getIntent().getExtras().getSerializable(MainActivity.TICKETLIST);
            ticketList = new LinkedList<ticket>(Arrays.asList(ticketListArray));
        }

        nameField = (EditText) findViewById(R.id.nameEditText);
        sourceField = (EditText) findViewById(R.id.sourceEditText);
        destinationField = (EditText) findViewById(R.id.destinationEditText);
        departureDateField = (EditText) findViewById(R.id.departureDateEditText);
        departureTimeField = (EditText) findViewById(R.id.departureTimeEditText);
        returnDateField = (EditText) findViewById(R.id.returnDateEditText);
        returnTimeField = (EditText) findViewById(R.id.returnTimeEditText);
        oneWayRadioButton = (RadioButton) findViewById(R.id.oneWayRadioButton);
        roundTripRadioButton = (RadioButton) findViewById(R.id.roundTripRadioButton);

        findViewById(R.id.previousImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketIndex --;
                if(ticketIndex < 0){
                    ticketIndex = 0;
                }

                if(ticketList != null){
                    try {
                        ticket tempTicket = ticketList.get(ticketIndex);
                        populateFields(tempTicket);
                    }
                    catch(Exception ex)
                    {

                    }
                }

            }
        });

        findViewById(R.id.nextImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketIndex++;
                if(ticketIndex > ticketList.size() -1){
                    ticketIndex = ticketList.size() -1 ;
                }
                if(ticketList != null){
                    try {
                        ticket tempTicket = ticketList.get(ticketIndex);
                        populateFields(tempTicket);
                    }
                    catch(Exception ex)
                    {

                    }
                }
            }
        });

        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        findViewById(R.id.beginImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticketIndex = 0;
                if(ticketList != null){
                    try {
                        ticket tempTicket = ticketList.get(ticketIndex);
                        populateFields(tempTicket);
                    }
                    catch(Exception ex)
                    {

                    }
                }

            }
        });

        findViewById(R.id.lastImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketIndex = ticketList.size() - 1;
                if(ticketList != null){
                    try {
                        ticket tempTicket = ticketList.get(ticketIndex);
                        populateFields(tempTicket);
                    }
                    catch(Exception ex)
                    {

                    }
                }
            }
        });
    }

    public void populateFields(ticket t){
        if(t != null){
            nameField.setText(t.getName());
            sourceField.setText(t.getSource());
            destinationField.setText(t.getDestination());
            departureDateField.setText(t.getDeparture_date());
            departureTimeField.setText(t.getDeparture_time());

            if(t.isTripIsOneWay()){
                oneWayRadioButton.setChecked(true);
                roundTripRadioButton.setChecked(false);
            }
            else {
                roundTripRadioButton.setChecked(true);
                oneWayRadioButton.setChecked(false);
            }

            if(t.getReturn_date()!=null && !t.getReturn_date().isEmpty()){
                returnDateField.setText(t.getReturn_date());
            }
            else {
                returnDateField.setText("");
            }

            if(t.getReturn_time() != null && !t.getReturn_time().isEmpty()){
                returnTimeField.setText(t.getReturn_time());
            }
            else {
                returnTimeField.setText("");
            }
        }
    }
}
