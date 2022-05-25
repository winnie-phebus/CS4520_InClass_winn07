package com.example.cs4520_inclassassignments;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class HeavyWork {
    public static final String ARR_I_KEY = "arr[i]";
    static final int COUNT = 9000000;
    static final int NUMBER_GOTTEN = 10;
    static ArrayList<Double> getArrayNumbers(Handler hqueue, int n){
        ArrayList<Double> returnArray = new ArrayList<>();

        for (int i=0; i<n; i++){
            returnArray.add(getNumber());
            Log.d("IC04 HW", "Number found, sending msg with i: "+i);
            hqueue.sendMessage(InClass04.msgWDataBuilder(NUMBER_GOTTEN,ARR_I_KEY,(double) i));
        }

        return returnArray;
    }

    static double getNumber(){
        double num = 0;
        Random rand = new Random();
        for(int i=0;i<COUNT; i++){
            num = num + rand.nextDouble() ;
        }
        return num / ((double) COUNT);
    }
}