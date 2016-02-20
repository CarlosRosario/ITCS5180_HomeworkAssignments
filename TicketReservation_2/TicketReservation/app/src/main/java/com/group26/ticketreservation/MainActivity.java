package com.group26.ticketreservation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public final static int CREATE_TICKET_ACTIVITY = 1;
    public final static int EDIT_TICKET_ACTIVITY = 2;
    public final static int DELETE_TICKET_ACTIVITY = 3;
    public final static int VIEW_TICKET_ACTIVITY = 4;

    public final static String TICKET = "TICKET";
    public final static String TICKETLIST = "TICKETLIST";

    private List<ticket> ticketList = new LinkedList<ticket>();
    private Map<String, ticket> ticketMap = new HashMap<String, ticket>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Ticket Button Press
        findViewById(R.id.createTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTicketActivity.class);
                startActivityForResult(intent, CREATE_TICKET_ACTIVITY);
                //startActivity(intent);
            }
        });

        // Edit Ticket Button Press
        findViewById(R.id.editTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditTicketActivity.class);

                // Convert ticket Linked List into a ticket Array so that we can pass it to the EditTicketActivity
                ticket[] ticketListArray = new ticket[ticketList.size()];
                for(int i = 0; i < ticketList.size(); i++){
                    ticketListArray[i] = ticketList.get(i);
                }
                intent.putExtra(TICKETLIST, ticketListArray);
                startActivityForResult(intent, EDIT_TICKET_ACTIVITY);
                //startActivity(intent);
            }
        });

        // Delete Ticket Button Press
        findViewById(R.id.deleteTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteTicketActivity.class);

                // Convert ticket Linked List into a ticket Array so that we can pass it to the DeleteTicketActivity
                ticket[] ticketListArray = new ticket[ticketList.size()];
                for(int i = 0; i < ticketList.size(); i++){
                    ticketListArray[i] = ticketList.get(i);
                }
                intent.putExtra(TICKETLIST, ticketListArray);
                startActivityForResult(intent, DELETE_TICKET_ACTIVITY);
                //startActivity(intent);
            }
        });

        // View Ticket Button Press
        findViewById(R.id.viewTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewTicketActivity.class);
                // Convert ticket Linked List into a ticket Array so that we can pass it to the DeleteTicketActivity
                ticket[] ticketListArray = new ticket[ticketList.size()];
                for(int i = 0; i < ticketList.size(); i++){
                    ticketListArray[i] = ticketList.get(i);
                }
                intent.putExtra(TICKETLIST, ticketListArray);
                //startActivityForResult(intent, DELETE_TICKET_ACTIVITY);
                startActivity(intent);
            }
        });

        // Finish Button Press
        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the application..?
            }
        });

        findViewById(R.id.customerCareNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:999-999-9999"));

                // Safely attempt to make phone call
                try{
                    startActivity(intent);
                }
                catch(Exception e){
                    Log.d("No Permissions:" , e.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CREATE_TICKET_ACTIVITY : {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ticket ticketFromChildActivity = (ticket) data.getSerializableExtra(TICKET);
                    if(ticketFromChildActivity != null){
                        ticketList.add(ticketFromChildActivity);
                        ticketMap.put(ticketFromChildActivity.getName(), ticketFromChildActivity);
                    }
                }
                break;
            }

            case EDIT_TICKET_ACTIVITY : {
                if (resultCode == Activity.RESULT_OK) {
                    ticket ticketFromEditActivity = (ticket) data.getSerializableExtra(TICKET);
                    if(ticketFromEditActivity != null){
                        ticket oldTicket = ticketMap.get(ticketFromEditActivity.getName());
                        ticketList.remove(oldTicket);
                        ticketList.add(ticketFromEditActivity);
                        ticketMap.put(ticketFromEditActivity.getName(), ticketFromEditActivity);
                    }
                }
                break;
            }

            case DELETE_TICKET_ACTIVITY: {
                if(resultCode == Activity.RESULT_OK){
                    ticket ticketFromDeleteActivity = (ticket) data.getSerializableExtra(TICKET);
                    if(ticketFromDeleteActivity != null){
                        ticket originalTicket = ticketMap.get(ticketFromDeleteActivity.getName());
                        ticketList.remove(originalTicket);
                        ticketMap.remove(originalTicket.getName());
                    }
                }
                break;
            }
        }
    }
}
