package com.group26.ticketreservation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PrintTicketActivity extends AppCompatActivity {

    ticket ticket;
    TextView nameField;
    TextView sourceField;
    TextView destinationField;
    TextView departureField;
    TextView returnField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ticket);

        nameField = (TextView)findViewById(R.id.name_field);
        sourceField = (TextView)findViewById(R.id.source_field);
        destinationField = (TextView)findViewById(R.id.destination_field);
        departureField = (TextView)findViewById(R.id.departure_field);
        returnField = (TextView)findViewById(R.id.return_field);


        if(getIntent().getExtras() != null){
            ticket =  (ticket) getIntent().getExtras().getSerializable("TICKET");


            if(ticket != null){
                nameField.setText(ticket.getName());
                sourceField.setText(ticket.getSource());
                destinationField.setText(ticket.getDestination());
                departureField.setText(ticket.getDepature_date() + " " + ticket.getDeparture_time());

                if(ticket.getReturn_date() != null && !ticket.getReturn_date().isEmpty()){
                    returnField.setText(ticket.getReturn_date() + " " + ticket.getReturn_time());
                }
            }
        }


    }
}
