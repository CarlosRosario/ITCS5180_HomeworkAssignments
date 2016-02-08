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
import java.util.Calendar;
import java.util.Locale;

public class CreateTicketActivity extends AppCompatActivity {

    CharSequence[] cities = {"Albany, NY", "Houston, TX", "Portland, OR", "Atlanta, GA", "Las Vegas, NV",
            "Raleigh, NC", "Boston, MA", "Los Angeles, CA", "San Jose, CA", "Charlotte, NC",
            "Miami, FL", "Washington, DC", "Chicago, IL", "Myrtle Beach, SC", "Greenville, SC", "New York, NY"};

    EditText destinationEditText;
    EditText sourceEditText;
    EditText departureDate;
    EditText returnDate;
    EditText departureTime;
    EditText returnTime;
    EditText nameEditText;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        sourceEditText = (EditText) findViewById(R.id.sourceEditText);
        sourceEditText.setKeyListener(null);
        destinationEditText = (EditText) findViewById(R.id.destinationEditText);
        destinationEditText.setKeyListener(null);


        AlertDialog.Builder source = new AlertDialog.Builder(this);


        source.setTitle("Source")
                .setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String destination = destinationEditText.getText().toString();

                        switch (which) {
                            case 0:
                                sourceEditText.setText("Albany, NY");
                                break;
                            case 1:
                                sourceEditText.setText("Houston, TX");
                                break;
                            case 2:
                                sourceEditText.setText("Portland, OR");
                                break;
                            case 3:
                                sourceEditText.setText("Atlanta, GA");
                                break;
                            case 4:
                                sourceEditText.setText("Las Vegas, NV");
                                break;
                            case 5:
                                sourceEditText.setText("Raleigh, NC");
                                break;
                            case 6:
                                sourceEditText.setText("Boston, MA");
                                break;
                            case 7:
                                sourceEditText.setText("Los Angeles, CA");
                                break;
                            case 8:
                                sourceEditText.setText("San Jose, CA");
                                break;
                            case 9:
                                sourceEditText.setText("Charlotte, NC");
                                break;
                            case 10:
                                sourceEditText.setText("Miami, FL");
                                break;
                            case 11:
                                sourceEditText.setText("Washington, DC");
                                break;
                            case 12:
                                sourceEditText.setText("Chicago, IL");
                                break;
                            case 13:
                                sourceEditText.setText("Myrtle Beach, SC");
                                break;
                            case 14:
                                sourceEditText.setText("Greenville, SC");
                                break;
                            case 15:
                                sourceEditText.setText("New York, NY");
                                break;

                        }
                        if (destination.equals(sourceEditText.getText().toString())) {
                            Toast.makeText(CreateTicketActivity.this, "Source and Destination cannot be the same", Toast.LENGTH_SHORT).show();
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

                        String sourceDestination = sourceEditText.getText().toString();

                        switch (which) {
                            case 0:
                                destinationEditText.setText("Albany, NY");
                                break;
                            case 1:
                                destinationEditText.setText("Houston, TX");
                                break;
                            case 2:
                                destinationEditText.setText("Portland, OR");
                                break;
                            case 3:
                                destinationEditText.setText("Atlanta, GA");
                                break;
                            case 4:
                                destinationEditText.setText("Las Vegas, NV");
                                break;
                            case 5:
                                destinationEditText.setText("Raleigh, NC");
                                break;
                            case 6:
                                destinationEditText.setText("Boston, MA");
                                break;
                            case 7:
                                destinationEditText.setText("Los Angeles, CA");
                                break;
                            case 8:
                                destinationEditText.setText("San Jose, CA");
                                break;
                            case 9:
                                destinationEditText.setText("Charlotte, NC");
                                break;
                            case 10:
                                destinationEditText.setText("Miami, FL");
                                break;
                            case 11:
                                destinationEditText.setText("Washington, DC");
                                break;
                            case 12:
                                destinationEditText.setText("Chicago, IL");
                                break;
                            case 13:
                                destinationEditText.setText("Myrtle Beach, SC");
                                break;
                            case 14:
                                destinationEditText.setText("Greenville, SC");
                                break;
                            case 15:
                                destinationEditText.setText("New York, NY");
                                break;

                        }
                        if (sourceDestination.equals(destinationEditText.getText().toString())) {
                            Toast.makeText(CreateTicketActivity.this, "Source and Destination cannot be the same", Toast.LENGTH_SHORT).show();
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

        departureDate = (EditText) findViewById(R.id.departureDateEditText);
        departureDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTicketActivity.this, departureDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        returnDate = (EditText) findViewById(R.id.returnDateEditText);
        returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTicketActivity.this, returnDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        departureTime = (EditText) findViewById(R.id.depTimeEditText);
        departureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(CreateTicketActivity.this, departureDateTimePicker, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

        returnTime = (EditText) findViewById(R.id.returnTime);
        returnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateTicketActivity.this, returnTimePicker, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });



        // Radio Button Events
        final RadioButton oneWayRadioButton = (RadioButton)findViewById(R.id.oneWayRadioButton);
        oneWayRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(oneWayRadioButton.isChecked()){
                    // If one way radio button is checked, hide the return date fields
                    findViewById(R.id.returnDateEditText).setVisibility(View.INVISIBLE);
                    findViewById(R.id.returnTime).setVisibility(View.INVISIBLE);
                }
            }
        });

        final RadioButton roundTripButton = (RadioButton)findViewById(R.id.roundTripButton);
        roundTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(roundTripButton.isChecked()){
                    // If round trip button is checked, make sure the return date fields are visible
                    findViewById(R.id.returnDateEditText).setVisibility(View.VISIBLE);
                    findViewById(R.id.returnTime).setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.printSummaryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket t;
                if(roundTripButton.isChecked()){
                    t = new ticket(nameEditText.getText().toString(), sourceEditText.getText().toString(), destinationEditText.getText().toString(), departureDate.getText().toString(),
                            departureTime.getText().toString(), oneWayRadioButton.isChecked(), returnDate.getText().toString(), returnTime.getText().toString());
                }
                else {
                    t = new ticket(nameEditText.getText().toString(), sourceEditText.getText().toString(), destinationEditText.getText().toString(), departureDate.getText().toString(),
                            departureTime.getText().toString(), oneWayRadioButton.isChecked());
                }

                // Pass ticket object back to main activity - the main activity should then add "this" ticket back to the linked list
                Intent resultIntent = getIntent();
                resultIntent.putExtra(MainActivity.TICKET, t);
                setResult(Activity.RESULT_OK, resultIntent);

                // Pass ticket object to print ticket activity so that we populate ticket data and display it
                Intent printActivityIntent = new Intent(CreateTicketActivity.this, PrintTicketActivity.class);
                printActivityIntent.putExtra(MainActivity.TICKET, t);
                startActivity(printActivityIntent);

                finish();

            }
        });
    }

    private void updateDepartureLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        departureDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateReturnLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        returnDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void UpdateDepartureTimeLabel() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        departureTime.setText(sdf.format(myCalendar.getTime()));
    }

    private void UpdateReturnTimeLabel() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        returnTime.setText(sdf.format(myCalendar.getTime()));
    }
}

