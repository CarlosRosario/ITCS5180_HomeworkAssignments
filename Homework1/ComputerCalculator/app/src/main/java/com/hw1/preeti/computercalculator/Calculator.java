// Assignment# : Homework 1
// File Name: Calculator.java
// Authors: Carlos Rosario, Preeti Harkanth, Meredith Browne

package com.hw1.preeti.computercalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Calculator extends AppCompatActivity {

    EditText budget;
    RadioGroup memoryRadioGrp;
    int memoryValue = 2;
    RadioGroup storageRadioGrp;
    int storageValue = 250;
    CheckBox mouseCh, flashDriveCh, coolingPadCh, carryingCaseCh;
    SeekBar seekBar ;
    TextView seekBarStatus;
    int seekBarInc = 5;
    int seekBarDefault = 15;
    Switch deliverySwitch;
    TextView priceValue;
    TextView budgetInd;
    ArrayList checkBoxList;
    ArrayList<Boolean> finalCheckBoxList;
    int checkBoxCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        budget = (EditText)findViewById(R.id.value);

        memoryRadioGrp = (RadioGroup) findViewById(R.id.radioGroupMemory);
        memoryRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButton2Gb) {
                    memoryValue = 2;
                } else if (checkedId == R.id.radioButton4Gb) {
                    memoryValue = 4;
                } else if (checkedId == R.id.radioButton8Gb) {
                    memoryValue = 8;
                } else if (checkedId == R.id.radioButton16Gb) {
                    memoryValue = 16;
                }

            }
        });

        storageRadioGrp = (RadioGroup) findViewById(R.id.radioGroupStorage);
        storageRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButton250Gb) {
                    storageValue = 250;
                } else if (checkedId == R.id.radioButton500Gb) {
                    storageValue = 500;
                } else if (checkedId == R.id.radioButton750Gb) {
                    storageValue = 750;
                } else if (checkedId == R.id.radioButton1Tb) {
                    storageValue = 1000;
                }

            }
        });

        mouseCh = (CheckBox) findViewById(R.id.checkBoxMouse);
        flashDriveCh = (CheckBox) findViewById(R.id.checkBoxFlashDrive);
        coolingPadCh = (CheckBox) findViewById(R.id.checkBoxCoolingPad);
        carryingCaseCh = (CheckBox) findViewById(R.id.checkBoxCarryingCase);

        finalCheckBoxList = new ArrayList<Boolean>();
        checkBoxList = new ArrayList();

        finalCheckBoxList.add(mouseCh.isChecked());
        finalCheckBoxList.add(flashDriveCh.isChecked());
        finalCheckBoxList.add(coolingPadCh.isChecked());
        finalCheckBoxList.add(carryingCaseCh.isChecked());

        for (int i = 0; i < finalCheckBoxList.size(); i++) {
            if (finalCheckBoxList.get(i) == true) {
                checkBoxList.add(i);
            }
        }

        checkBoxCount = checkBoxList.size();



        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        seekBarStatus = (TextView) findViewById(R.id.tipIndicator);
         seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 progress = (Math.round(progress/seekBarInc)) * seekBarInc;
                 seekBar.setProgress(progress);
                 seekBarStatus.setText(progress + "%");
                 seekBarDefault = progress;
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         });

        deliverySwitch = (Switch)findViewById(R.id.switch1Delivery);
        priceValue = (TextView) findViewById(R.id.finalPriceValue);
        budgetInd = (TextView) findViewById(R.id.budgetIndicator);

        findViewById(R.id.calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budget.getText().toString().isEmpty() || budget.getText().toString().matches("") || Double.parseDouble((budget.getText().toString())) == 0) {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Enter Valid Value", Toast.LENGTH_LONG).show();
                } else {
                    int switchIn = 0;
                    if (deliverySwitch.isChecked()) {
                        switchIn = 1;
                    }

                    finalCheckBoxList = new ArrayList<Boolean>();
                    checkBoxList = new ArrayList();

                    finalCheckBoxList.add(mouseCh.isChecked());
                    finalCheckBoxList.add(flashDriveCh.isChecked());
                    finalCheckBoxList.add(coolingPadCh.isChecked());
                    finalCheckBoxList.add(carryingCaseCh.isChecked());

                    for (int i = 0; i < finalCheckBoxList.size(); i++) {
                        if (finalCheckBoxList.get(i) == true) {
                            checkBoxList.add(i);
                        }
                    }

                    checkBoxCount = checkBoxList.size();

                    double costPart1, costPart2, cost;

                    costPart1 =  ((10 * memoryValue) + (0.75 * storageValue) + (20 * checkBoxCount));
                    costPart2 =  (1 + (seekBarDefault / 100.0));
                    cost = ((costPart1 * costPart2) + (5.95 * switchIn));

                    priceValue.setText(Double.toString(cost));
                    Double intitalBudget = Double.parseDouble(budget.getText().toString());
                    if (intitalBudget >= cost) {
                        budgetInd.setText("WithIn Budget");
                        budgetInd.setPadding(5,2,5,2);
                        budgetInd.setBackgroundResource(R.drawable.rounded_corners_green);
                    } else {
                        budgetInd.setText("Over budget");
                        budgetInd.setPadding(5, 2, 5, 2);
                        budgetInd.setBackgroundResource(R.drawable.rounded_corners_red);
                    }

                }
            }
        });


        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget.getText().clear();
                memoryRadioGrp.check(R.id.radioButton2Gb);
                storageRadioGrp.check(R.id.radioButton250Gb);
                mouseCh.setChecked(false);
                flashDriveCh.setChecked(false);
                coolingPadCh.setChecked(false);
                carryingCaseCh.setChecked(false);
                seekBar.setProgress(15);
                deliverySwitch.setChecked(true);
                priceValue.setText("$0.00");
                budgetInd.setText("");
                budgetInd.setPadding(0,0,0,0);



            }
        });
    }
}
