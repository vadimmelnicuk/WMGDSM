package com.vadimmelnicuk.wmgdsm;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vadimmelnicuk on 10/04/2017.
 */

public class HelperHRV extends Main {

    private static Thread mThread;
    private static TimerTask mTimerTask;
    private static Timer mTimer;
    private Context mContext;
    private long delay = 1000; // one second moving window
    private long mCounter = 1;

    public static double RMSSD30 = 0;
    public static double RMSSD120 = 0;
    public static boolean RMSSDUpdated = false;

    HelperHRV(Context context) {
        mContext = context;
    }

    public void init() {
        initThread();
        initTimers();
    }

    private void initThread() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // check if Polar H7 is connected
                if(modulesPolarH7Connected) {
                    analyse();
                }
            }
        });
    }

    private void initTimers() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mThread.run();
            }
        };
    }

    public void startTimer() {
        mTimer.scheduleAtFixedRate(mTimerTask, 0, delay);
    }

    public void stopTimer() {
        mTimer.cancel();
        mTimer.purge();
    }

    private void analyse() {
        long currentTime = System.currentTimeMillis();
        // check if session has started and minimum of 5 seconds elapsed
        if(session_status && currentTime >= session_timestamp+5000) {
            RMSSD30 = calculateRMSSD(dsmDb.getRRSample(session_timestamp, currentTime, 30));
            RMSSD120 = calculateRMSSD(dsmDb.getRRSample(session_timestamp, currentTime, 120));

            if(Main.syncData) {
                RMSSDUpdated = true;
            } else {
                // TODO implement hrv database table helper
            }

            if(Main.displayData) {
                updateLabel(FragmentHRV.rmssd30Label, "RMSSD 30s: " + String.format("%.03f", RMSSD30));
                updateLabel(FragmentHRV.rmssd120Label, "RMSSD 120s: " + String.format("%.03f", RMSSD120));

                mCounter += 1;
                FragmentHRV.rmssd30Graph.getViewport().setMinX(mCounter-FragmentHRV.rmssd30GraphWidth);
                FragmentHRV.rmssd30Graph.getViewport().setMaxX(mCounter);
                FragmentHRV.rmssd30GraphSeries.appendData(new DataPoint(mCounter, RMSSD30), true, FragmentHRV.rmssd30GraphWidth);
            }
        }
    }

    private double calculateRMSSD(ArrayList<Long> rrs) {
        ArrayList<Double> RMSSDDiffSum = new ArrayList<>();
        double RMSSDSum = 0;

        for(int n = 0; n < rrs.size()-1; n++) {
            double r1 = rrs.get(n+1)-rrs.get(n);
            double r2 = Math.pow(r1, 2);
            RMSSDDiffSum.add(r2);
        }

        for(int n = 0; n < RMSSDDiffSum.size(); n++) {
            RMSSDSum += RMSSDDiffSum.get(n);
        }

        double r1 = 1/((double)rrs.size()-1);

        return Math.sqrt(r1*RMSSDSum);
    }
}
