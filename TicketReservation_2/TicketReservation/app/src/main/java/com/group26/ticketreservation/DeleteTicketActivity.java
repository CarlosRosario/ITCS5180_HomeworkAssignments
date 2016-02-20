package com.group26.ticketreservation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DeleteTicketActivity extends AppCompatActivity {


    private List<ticket> ticketList = new LinkedList<ticket>();
    private Map<String, ticket> ticketMap = new HashMap<String, ticket>();
    CharSequence[] ticketCharSequence;

    private ticket selectedTicket;
    private EditText nameField;
    private EditText sourceField;
    private EditText destinationField;
    private RadioButton typeOfTripRadioButton;
    private EditText departureDateField;
    private EditText departureTimeField;
    private EditText returnDateField;
    private EditText returnTimeField;
    private RadioButton oneWayRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_ticket);


        if(getIntent().getExtras().getSerializable(MainActivity.TICKETLIST) != null){
            ticket[] ticketListArray = (ticket[])getIntent().getExtras().getSerializable(MainActivity.TICKETLIST);
            ticketList = new LinkedList<ticket>(Arrays.asList(ticketListArray));

            ticketCharSequence = new CharSequence[ticketList.size()];
            for(int i = 0; i < ticketList.size(); i++){
                ticket tempTicket = ticketList.get(i);
                if(tempTicket != null){
                    ticketCharSequence[i] = tempTicket.getName();
                    ticketMap.put(tempTicket.getName(), tempTicket);
                }
            }
        }

        nameField = (EditText) findViewById(R.id.nameEditText);
        sourceField = (EditText) findViewById(R.id.sourceEditText);
        destinationField = (EditText) findViewById(R.id.destinationEditText);
        departureDateField = (EditText) findViewById(R.id.departureDateEditText);
        departureTimeField = (EditText) findViewById(R.id.departureTimeEditText);
        returnDateField = (EditText) findViewById(R.id.returnDateEditText);
        returnTimeField = (EditText) findViewById(R.id.returnTimeEditText);

        final AlertDialog.Builder ticketSelector = new AlertDialog.Builder(this);
        ticketSelector.setTitle("Pick a Ticket").setItems(ticketCharSequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // populate activity with ticket data
                selectedTicket = ticketMap.get(ticketList.get(which).getName());

                // Populate name
                nameField.setText(selectedTicket.getName());
                nameField.setEnabled(false); // You cannot edit the name of a ticket - if you need to do this you can simply create a new ticket.

                // Populate source
                sourceField.setText(selectedTicket.getSource());

                // Populate destination
                destinationField.setText(selectedTicket.getDestination());

                // Populate radio button
                if (selectedTicket.isTripIsOneWay()) {
                    typeOfTripRadioButton = (RadioButton) findViewById(R.id.oneWayRadioButton);
                } else {
                    typeOfTripRadioButton = (RadioButton) findViewById(R.id.roundTripButton);
                }
                typeOfTripRadioButton.setChecked(true);

                // Populate departure date & time fields
                departureDateField.setText(selectedTicket.getDeparture_date());
                departureTimeField.setText(selectedTicket.getDeparture_time());

                // Populate return date & time fields if available from ticket object
                if (selectedTicket.getReturn_date() != null && !selectedTicket.getReturn_date().isEmpty()) {
                    returnDateField.setVisibility(View.VISIBLE);
                    returnDateField.setText(selectedTicket.getReturn_date());
                }
                else {
                    returnDateField.setVisibility(View.INVISIBLE);
                }

                if (selectedTicket.getReturn_time() != null && !selectedTicket.getReturn_time().isEmpty()) {
                    returnTimeField.setVisibility(View.VISIBLE);
                    returnTimeField.setText(selectedTicket.getReturn_time());
                }
                else {
                    returnTimeField.setVisibility(View.INVISIBLE);
                }
            }
        });

        final AlertDialog pickTicket = ticketSelector.create();
        findViewById(R.id.edit_text_selectbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTicket.show();
            }
        });

        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = getIntent();
                resultIntent.putExtra(MainActivity.TICKET, selectedTicket);
                setResult(Activity.RESULT_OK, resultIntent);

//                ticketList.remove(selectedTicket);
//                ticketMap.clear();
//
//                ticketCharSequence = new CharSequence[ticketList.size()];
//                for(int i = 0; i < ticketList.size(); i++){
//                    ticket tempTicket = ticketList.get(i);
//                    if(tempTicket != null){
//                        ticketCharSequence[i] = tempTicket.getName();
//                        ticketMap.put(tempTicket.getName(), tempTicket);
//                    }
//                }

                finish();
            }
        });

        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
