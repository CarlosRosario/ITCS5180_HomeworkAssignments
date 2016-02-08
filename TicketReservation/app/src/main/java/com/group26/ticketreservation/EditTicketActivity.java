package com.group26.ticketreservation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditTicketActivity extends AppCompatActivity {

    private List<ticket> ticketList = new LinkedList<ticket>();
    private Map<String, ticket> ticketMap = new HashMap<String, ticket>();
    CharSequence[] ticketCharSequence;

    CharSequence[] cities = {"Albany, NY", "Houston, TX", "Portland, OR", "Atlanta, GA", "Las Vegas, NV",
            "Raleigh, NC", "Boston, MA", "Los Angeles, CA", "San Jose, CA", "Charlotte, NC",
            "Miami, FL", "Washington, DC", "Chicago, IL", "Myrtle Beach, SC", "Greenville, SC", "New York, NY"};

    private EditText nameField;
    private EditText sourceField;
    private EditText destinationField;
    private RadioButton typeOfTripRadioButton;
    private EditText departureDateField;
    private EditText departureTimeField;
    private EditText returnDateField;
    private EditText returnTimeField;
    private RadioButton oneWayRadioButton;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        if(getIntent().getExtras().getSerializable(MainActivity.TICKETLIST) != null){
            ticket[] ticketListArray = (ticket[])getIntent().getExtras().getSerializable(MainActivity.TICKETLIST);
            ticketList = Arrays.asList(ticketListArray);

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

        AlertDialog.Builder source = new AlertDialog.Builder(this);

        // Set up Source List
        source.setTitle("Source")
                .setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String destination = destinationField.getText().toString();

                        switch (which) {
                            case 0:
                                sourceField.setText("Albany, NY");
                                break;
                            case 1:
                                sourceField.setText("Houston, TX");
                                break;
                            case 2:
                                sourceField.setText("Portland, OR");
                                break;
                            case 3:
                                sourceField.setText("Atlanta, GA");
                                break;
                            case 4:
                                sourceField.setText("Las Vegas, NV");
                                break;
                            case 5:
                                sourceField.setText("Raleigh, NC");
                                break;
                            case 6:
                                sourceField.setText("Boston, MA");
                                break;
                            case 7:
                                sourceField.setText("Los Angeles, CA");
                                break;
                            case 8:
                                sourceField.setText("San Jose, CA");
                                break;
                            case 9:
                                sourceField.setText("Charlotte, NC");
                                break;
                            case 10:
                                sourceField.setText("Miami, FL");
                                break;
                            case 11:
                                sourceField.setText("Washington, DC");
                                break;
                            case 12:
                                sourceField.setText("Chicago, IL");
                                break;
                            case 13:
                                sourceField.setText("Myrtle Beach, SC");
                                break;
                            case 14:
                                sourceField.setText("Greenville, SC");
                                break;
                            case 15:
                                sourceField.setText("New York, NY");
                                break;

                        }
                        if (destination.equals(sourceField.getText().toString())) {
                            Toast.makeText(EditTicketActivity.this, "Source and Destination cannot be the same", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        final AlertDialog salert = source.create();
        findViewById(R.id.sourceEditText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salert.show();
                //progress.dismiss();
            }
        });


        AlertDialog.Builder destination = new AlertDialog.Builder(this);
        destination.setTitle("Destination")
                .setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String sourceDestination = sourceField.getText().toString();

                        switch (which) {
                            case 0:
                                destinationField.setText("Albany, NY");
                                break;
                            case 1:
                                destinationField.setText("Houston, TX");
                                break;
                            case 2:
                                destinationField.setText("Portland, OR");
                                break;
                            case 3:
                                destinationField.setText("Atlanta, GA");
                                break;
                            case 4:
                                destinationField.setText("Las Vegas, NV");
                                break;
                            case 5:
                                destinationField.setText("Raleigh, NC");
                                break;
                            case 6:
                                destinationField.setText("Boston, MA");
                                break;
                            case 7:
                                destinationField.setText("Los Angeles, CA");
                                break;
                            case 8:
                                destinationField.setText("San Jose, CA");
                                break;
                            case 9:
                                destinationField.setText("Charlotte, NC");
                                break;
                            case 10:
                                destinationField.setText("Miami, FL");
                                break;
                            case 11:
                                destinationField.setText("Washington, DC");
                                break;
                            case 12:
                                destinationField.setText("Chicago, IL");
                                break;
                            case 13:
                                destinationField.setText("Myrtle Beach, SC");
                                break;
                            case 14:
                                destinationField.setText("Greenville, SC");
                                break;
                            case 15:
                                destinationField.setText("New York, NY");
                                break;

                        }
                        if (sourceDestination.equals(destinationField.getText().toString())) {
                            Toast.makeText(EditTicketActivity.this, "Source and Destination cannot be the same", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        final AlertDialog dalert = destination.create();
        findViewById(R.id.destinationEditText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dalert.show();
                //progress.dismiss();
            }
        });

        AlertDialog.Builder ticketSelector = new AlertDialog.Builder(this);
        ticketSelector.setTitle("Pick a Ticket").setItems(ticketCharSequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // populate activity with ticket data
                ticket ticketSelected = ticketMap.get(ticketList.get(which).getName());

                // Populate name
                nameField.setText(ticketSelected.getName());
                nameField.setEnabled(false); // You cannot edit the name of a ticket - if you need to do this you can simply create a new ticket.

                // Populate source
                sourceField.setText(ticketSelected.getSource());

                // Populate destination
                destinationField.setText(ticketSelected.getDestination());

                // Populate radio button
                if (ticketSelected.isTripIsOneWay()) {
                    typeOfTripRadioButton = (RadioButton) findViewById(R.id.oneWayRadioButton);
                } else {
                    typeOfTripRadioButton = (RadioButton) findViewById(R.id.roundTripButton);
                }
                typeOfTripRadioButton.setChecked(true);

                // Populate departure date & time fields
                departureDateField.setText(ticketSelected.getDeparture_date());
                departureTimeField.setText(ticketSelected.getDeparture_time());

                // Populate return date & time fields if available from ticket object
                if (ticketSelected.getReturn_date() != null && !ticketSelected.getReturn_date().isEmpty()) {
                    returnDateField.setVisibility(View.VISIBLE);
                    returnDateField.setText(ticketSelected.getReturn_date());
                }
                else {
                    returnDateField.setVisibility(View.VISIBLE);
                }

                if (ticketSelected.getReturn_time() != null && !ticketSelected.getReturn_time().isEmpty()) {
                    returnTimeField.setVisibility(View.VISIBLE);
                    returnTimeField.setText(ticketSelected.getReturn_time());
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

        findViewById(R.id.editTextSaveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket t;
                oneWayRadioButton = (RadioButton) findViewById(R.id.oneWayRadioButton);

                if (!oneWayRadioButton.isChecked()) {
                    t = new ticket(nameField.getText().toString(), sourceField.getText().toString(), destinationField.getText().toString(), departureDateField.getText().toString(),
                            departureTimeField.getText().toString(), oneWayRadioButton.isChecked(), returnDateField.getText().toString(), returnTimeField.getText().toString());
                } else {
                    t = new ticket(nameField.getText().toString(), sourceField.getText().toString(), destinationField.getText().toString(), departureDateField.getText().toString(),
                            departureTimeField.getText().toString(), oneWayRadioButton.isChecked());
                }

                // Pass ticket object back to main activity - the main activity should then add "this" ticket back to the linked list
                Intent resultIntent = getIntent();
                resultIntent.putExtra(MainActivity.TICKET, t);
                setResult(Activity.RESULT_OK, resultIntent);

                // Pass ticket object to print ticket activity so that we populate ticket data and display it
                Intent printActivityIntent = new Intent(EditTicketActivity.this, PrintTicketActivity.class);
                printActivityIntent.putExtra(MainActivity.TICKET, t);
                startActivity(printActivityIntent);

                finish();
            }
        });

        final RadioButton oneWayRadioButton = (RadioButton)findViewById(R.id.oneWayRadioButton);
        oneWayRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oneWayRadioButton.isChecked()) {
                    // If one way radio button is checked, hide the return date fields
                    findViewById(R.id.returnDateEditText).setVisibility(View.INVISIBLE);
                    findViewById(R.id.returnTimeEditText).setVisibility(View.INVISIBLE);
                }
            }
        });

        final RadioButton roundTripButton = (RadioButton)findViewById(R.id.roundTripButton);
        roundTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roundTripButton.isChecked()) {
                    // If round trip button is checked, make sure the return date fields are visible
                    findViewById(R.id.returnDateEditText).setVisibility(View.VISIBLE);
                    findViewById(R.id.returnTimeEditText).setVisibility(View.VISIBLE);
                }
            }
        });


        final DatePickerDialog.OnDateSetListener departureDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDepartureLabel();
            }
        };

        final DatePickerDialog.OnDateSetListener returnDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateReturnLabel();
            }
        };

        final TimePickerDialog.OnTimeSetListener departureDateTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);

                UpdateDepartureTimeLabel();
            }
        };

        final TimePickerDialog.OnTimeSetListener returnTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                UpdateReturnTimeLabel();
            }
        };

        departureDateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTicketActivity.this, departureDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        returnDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTicketActivity.this, returnDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        departureTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(EditTicketActivity.this, departureDateTimePicker, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

        returnTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditTicketActivity.this, returnTimePicker, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

    }


    private void updateDepartureLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        departureDateField.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateReturnLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        returnDateField.setText(sdf.format(myCalendar.getTime()));
    }

    private void UpdateDepartureTimeLabel() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        departureTimeField.setText(sdf.format(myCalendar.getTime()));
    }

    private void UpdateReturnTimeLabel() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        returnTimeField.setText(sdf.format(myCalendar.getTime()));
    }

}
