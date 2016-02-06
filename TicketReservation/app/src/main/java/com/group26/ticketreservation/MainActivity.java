package com.group26.ticketreservation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ticket> ticketList = new LinkedList<ticket>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Ticket Button Press
        findViewById(R.id.createTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTicketActivity.class);
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });

        // Edit Ticket Button Press
        findViewById(R.id.editTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditTicketActivity.class);
                startActivity(intent);
            }
        });

        // Delete Ticket Button Press
        findViewById(R.id.deleteTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteTicketActivity.class);
                startActivity(intent);
            }
        });

        // View Ticket Button Press
        findViewById(R.id.viewTicketButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewTicketActivity.class);
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
            case 1 : {
                if (resultCode == Activity.RESULT_OK) {
                    ticket ticketFromChildActivity = (ticket) data.getSerializableExtra("TICKET");
                    if(ticketFromChildActivity != null){
                        ticketList.add(ticketFromChildActivity);
                    }
                }
                break;
            }
        }
    }
}
