package com.example.cs4520_inclassassignments.inClass04;

import static com.example.cs4520_inclassassignments.inClass04.InClass04.msgBuilder;
import static com.example.cs4520_inclassassignments.inClass04.InClass04.msgWDataBuilder;

import android.os.Handler;

import java.util.ArrayList;

/**
 * @author: Winnie Phebus
 * Assignment 04
 */

public class DoHeavyWork implements Runnable {
    public final static int STATUS_BEGIN = 0;
    public final static int STATUS_NUMBERSRECV = 100;
    public final static int STATUS_MINFOUND = 101;
    public final static int STATUS_MAXFOUND = 102;
    public final static int STATUS_AVGFOUND = 103;
    public final static int STATUS_END = 105;

    public static final String ARR_MIN_KEY = "arr_min";
    public static final String ARR_MAX_KEY = "arr_max";
    public static final String ARR_AVG_KEY = "arr_avg";

    private final int arrCap;
    private final Handler heavyQueue;

    public DoHeavyWork(int arrCap, Handler heavyQueue) {
        this.arrCap = arrCap;
        this.heavyQueue = heavyQueue;
    }

    @Override
    public void run() {
        heavyQueue.sendMessage(msgBuilder(STATUS_BEGIN));

        ArrayList<Double> arr = HeavyWork.getArrayNumbers(heavyQueue, arrCap);
        heavyQueue.sendMessage(msgBuilder(STATUS_NUMBERSRECV));

        Double min = findMin(arr);
        heavyQueue.sendMessage(msgWDataBuilder(STATUS_MINFOUND, ARR_MIN_KEY, min));

        Double max = findMax(arr);
        heavyQueue.sendMessage(msgWDataBuilder(STATUS_MAXFOUND, ARR_MAX_KEY, max));

        Double avg = findAvg(arr);
        heavyQueue.sendMessage(msgWDataBuilder(STATUS_AVGFOUND, ARR_AVG_KEY, avg));

        // more tasks might follow, just capping it off at this point.
        heavyQueue.sendMessage(msgBuilder(STATUS_END));
    }

    //finds the lowest value in the given double ArrList
    private Double findMin(ArrayList<Double> arr) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < arrCap; i++) {
            min = Math.min(min, arr.get(i));
        }
        return min;
    }

    // finds the largest value in the given double Arrlist
    private Double findMax(ArrayList<Double> arr) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < arrCap; i++) {
            max = Math.max(max, arr.get(i));
        }
        return max;
    }

    // finds the average of the values in the given Double arrList
    private Double findAvg(ArrayList<Double> arr) {
        double avg = 0.0;
        for (int i = 0; i < arrCap; i++) {
            avg += arr.get(i);
        }
        return avg / arrCap;
    }
}
