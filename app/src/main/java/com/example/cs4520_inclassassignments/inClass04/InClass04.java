package com.example.cs4520_inclassassignments.inClass04;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs4520_inclassassignments.MainActivity;
import com.example.cs4520_inclassassignments.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Winnie Phebus
 * Assignment 04
 */

public class InClass04 extends AppCompatActivity {
    private TextView selectComplex;
    private SeekBar complexBar;
    private int arrMaxNum;
    private TextView minText;
    private TextView maxText;
    private TextView avgText;
    private Button genButton;
    private ProgressBar threadProgression;
    private ExecutorService threadPool;
    private Handler hwQueue;
    private double min;
    private double max;
    private double avg;

    public TextView getSelectComplex() {
        return selectComplex;
    }

    public void setSelectComplex(TextView selectComplex) {
        this.selectComplex = selectComplex;
    }

    public SeekBar getComplexBar() {
        return complexBar;
    }

    public void setComplexBar(SeekBar complexBar) {
        this.complexBar = complexBar;
    }

    public TextView getMinText() {
        return minText;
    }

    public void setMinText(TextView minText) {
        this.minText = minText;
    }

    public TextView getMaxText() {
        return maxText;
    }

    public void setMaxText(TextView maxText) {
        this.maxText = maxText;
    }

    public TextView getAvgText() {
        return avgText;
    }

    public void setAvgText(TextView avgText) {
        this.avgText = avgText;
    }

    public Button getGenButton() {
        return genButton;
    }

    public void setGenButton(Button genButton) {
        this.genButton = genButton;
    }

    public ProgressBar getThreadProgression() {
        return threadProgression;
    }

    public void setThreadProgression(ProgressBar threadProgression) {
        this.threadProgression = threadProgression;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class04);
        setTitle("Number Generator");

        initConditions();
    }

    // for keeping onCreate a little more concise, connects views->ids and also sets initial vals
    private void initConditions() {
        arrMaxNum = 8;
        selectComplex = findViewById(R.id.ic04_selectComplex);
        selectComplex.setText("Select Complexity: " + arrMaxNum + "times ");

        complexBar = findViewById(R.id.ic04_complexSeekBar);
        complexBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arrMaxNum = progress;
                selectComplex.setText("Select Complexity: " + arrMaxNum + " times ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        minText = findViewById(R.id.ic04_minTV);
        maxText = findViewById(R.id.ic04_maxTV);
        avgText = findViewById(R.id.ic04_avgTV);

        threadPool = Executors.newFixedThreadPool(2);
        hwQueue = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int progress = 0;
                int numCount = 0;
                boolean allNumsGenerated;

                switch (msg.what) {
                    case DoHeavyWork.STATUS_BEGIN:
                        threadProgression.setVisibility(View.VISIBLE);
                        allNumsGenerated = false;
                        break;
                    case HeavyWork.NUMBER_GOTTEN:
                        allNumsGenerated = false;
                        numCount = (int) msg.getData().getDouble(HeavyWork.ARR_I_KEY, 0.0);
                        break;
                    case DoHeavyWork.STATUS_NUMBERSRECV:
                        allNumsGenerated = true;
                        break;
                    case DoHeavyWork.STATUS_MINFOUND:
                        allNumsGenerated = true;
                        min = msg.getData().getDouble(DoHeavyWork.ARR_MIN_KEY);
                        break;
                    case DoHeavyWork.STATUS_MAXFOUND:
                        allNumsGenerated = true;
                        max = msg.getData().getDouble(DoHeavyWork.ARR_MAX_KEY);
                        break;
                    case DoHeavyWork.STATUS_AVGFOUND:
                        allNumsGenerated = true;
                        avg = msg.getData().getDouble(DoHeavyWork.ARR_AVG_KEY);
                        break;
                    case DoHeavyWork.STATUS_END:
                        allNumsGenerated = true;
                        threadProgression.setVisibility(View.INVISIBLE);
                        updateUI();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + msg.what);
                }
                if (allNumsGenerated) {
                    progress = msg.what;
                } else {
                    progress = numCount * 100 / arrMaxNum;
                }
                threadProgression.setProgress(progress);
                return false;
            }
        });

        genButton = findViewById(R.id.ic04_genButton);
        genButton.setOnClickListener(generateClicked());

        threadProgression = findViewById(R.id.ic04_progress);
        threadProgression.setVisibility(View.INVISIBLE);
    }

    // abstracting the message building process, returns the message with the given type
    public static Message msgBuilder(int whatType) {
        Message newMsg = new Message();
        newMsg.what = whatType;
        return newMsg;
    }

    // creates a Message that is also returning data to the Main Thread
    public static Message msgWDataBuilder(int whatType, String key, Double value) {
        Message newMsg = msgBuilder(whatType);
        Bundle data = new Bundle();
        data.putDouble(key, value);
        newMsg.setData(data);
        return newMsg;
    }

    // just rounds the given double
    private double qRound(double nn) {
        double nthPlace = 1000000.0;
        return (Math.round(nn * nthPlace) / nthPlace);
    }

    // updates the UI with the information taken from the thread
    // TODO: figure out how to String Resource with formatting
    private void updateUI() {
        minText.setText("Minimum: " + qRound(min));
        maxText.setText("Maximum: " + qRound(max));
        avgText.setText("Average: " + qRound(avg));
    }

    // responds to the generate button and creates a Worker thread to handle the HeavyWork numbers
    private View.OnClickListener generateClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrMaxNum == 0) {
                    MainActivity.showToast(
                            InClass04.this,
                            "Complexity 0 will generate no numbers. Try a value > 0, please!");
                } else {
                    threadPool.execute(new DoHeavyWork(arrMaxNum, hwQueue));
                }
            }
        };
    }


}