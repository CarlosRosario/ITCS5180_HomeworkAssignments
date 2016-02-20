package com.group26.ticketreservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PrintTicketActivity extends AppCompatActivity {

    ticket ticket;
    TextView nameField;
    TextView sourceField;
    TextView destinationField;
    TextView departureField;
    TextView returnField;
    TextView returnFieldLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ticket);

        nameField = (TextView)findViewById(R.id.name_field);
        sourceField = (TextView)findViewById(R.id.source_field);
        destinationField = (TextView)findViewById(R.id.destination_field);
        departureField = (TextView)findViewById(R.id.departure_field);
        returnField = (TextView)findViewById(R.id.return_field);
        returnFieldLabel = (TextView) findViewById(R.id.return_label);


        if(getIntent().getExtras() != null){
            ticket =  (ticket) getIntent().getExtras().getSerializable(MainActivity.TICKET);


            if(ticket != null){
                nameField.setText(ticket.getName());
                sourceField.setText(ticket.getSource());
                destinationField.setText(ticket.getDestination());
                departureField.setText(ticket.getDeparture_date() + " " + ticket.getDeparture_time());

                if (ticket.getReturn_date() != null && !ticket.getReturn_date().isEmpty()) {

                    returnField.setText(ticket.getReturn_date() + " " + ticket.getReturn_time());
                } else {
                    returnFieldLabel.setVisibility(View.INVISIBLE);
                }
            }
        }

        findViewById(R.id.button_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the Main Activity (not the CreateTicketActivity)
                Intent returnBtn = new Intent(getApplicationContext(), MainActivity.class);
                returnBtn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(returnBtn);

                finish();
            }
        });


    }
}
