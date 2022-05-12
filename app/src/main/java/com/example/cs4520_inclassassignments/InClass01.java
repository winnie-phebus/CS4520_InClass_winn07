package com.example.cs4520_inclassassignments;

// @author: Winnie Phebus
// Assignment 01

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InClass01 extends AppCompatActivity {
    final String TAG = "InClass01";
    EditText weight;
    EditText heightFeet;
    EditText heightIn;
    Button calcBMI;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class01);
        setTitle("BMI Calculator");
        
        weight = findViewById(R.id.ic01_weight);
        heightFeet = findViewById(R.id.ic01_heightFt);
        heightIn = findViewById(R.id.ic01_heightIn);
        display = findViewById(R.id.ic01_promptText);
        
        calcBMI = findViewById(R.id.IC01calcBMI);
        calcBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] vals = validateInput(weight, heightFeet, heightIn);
                if (vals[0] == -10){
                    // making a Toast here
                    showToast("Invalid inputs. Non-negative integers preferred.");
                } else {
                    double bmi = calculateBMI(vals[0],vals[1]);
                    System.out.println(bmi+", w:"+vals[0]+", h:"+vals[1]);
                    showToast("BMI calculated.");
                    display.setText(BMIResponse(bmi));
                }
            }
        });
    }

    // abstracted basic Toast functionality for my own convenience
    private void showToast(String toastMsg) {
        Toast.makeText(InClass01.this, toastMsg, Toast.LENGTH_SHORT).show();
    }

    // basically makes sure that the user properly input their responses
    private int[] validateInput(EditText weight, EditText heightFeet, EditText heightIn) {
        if ((weight.getText().toString().equals(""))
                || (heightFeet.getText().toString().equals(""))
                || (heightIn.getText().toString().equals(""))){
            return new int[]{-10};
        }

        int inW = toInt(weight);
        int inHFt = toInt(heightFeet);
        int inHIn = toInt(heightIn);

        if (inW <=0 || inHFt <=0 || inHIn < 0){
            return new int[]{-10};
        }

        /*if ((weight.getText() == null) || inW <=0){
            return new int[]{-10, 1};
        } else if ((heightFeet.getText() == null) || inHFt <=0){
            return new int[]{-10, 2};
        } else if ((heightIn.getText() == null) || inHIn < 0){

        }*/

        return new int[]{inW, findHeightIn(inHFt, inHIn)};
    }

    // converts given EditText val to an Integer
    private int toInt(EditText inputVal){
        return Integer.parseInt(inputVal.getText().toString());
    }

    // converts the height given in Ft,In to the total height in Inches
    private int findHeightIn(int heightFeet, int heightIn) {
        return 12*heightFeet + heightIn;
    }

    // calculates the BMI for the given weight and height in inches
    private double calculateBMI(int lbs, int inHeight){
        return Math.round((lbs * 1.0 / mySquare(inHeight)) * 703.0 * 100.0) / 100.0 ;
    }

    // simple squaring function
    private int mySquare(int given) {
        return given * given;
    }

    // formats the app response to the calculated bmi
    private String BMIResponse(double bmi){
        String response = "Your BMI: "+bmi+"\nYou are ";

        if (bmi<18.5){
            response += "Underweight.";
        } else if (bmi <= 24.9) {
            response += "Normal weight.";
        } else if (bmi <= 29.9) {
            response += "Overweight.";
        } else {
            response += "Obese.";
        }
        return response;
    }
}