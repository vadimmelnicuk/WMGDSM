package com.vadimmelnicuk.wmgdsm;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
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
    private long DELAY = 1000; // one second moving window
    private long mCounter;
    private boolean mClean;
    private final static boolean DISPLAY_LOGS = false;

    public static double RMSSD30;
    public static double RMSSD120;
    public static boolean RMSSDUpdated;

    public static ArrayList<Double[]> fs;

    HelperHRV(Context context) {
        mContext = context;
    }

    public void init() {
        initVariables();
        initThread();
        initTimers();

        // Reset rrGraphSeries
        FragmentHRV.rmssd30GraphSeries.resetData(new DataPoint[]{});
        FragmentHRV.rmssd120GraphSeries.resetData(new DataPoint[]{});
    }

    private void initVariables() {
        mCounter = 1;
        mClean = true;
        RMSSD30 = 0;
        RMSSD120 = 0;
        RMSSDUpdated = false;
        fs = new ArrayList<>();
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
        mTimer.scheduleAtFixedRate(mTimerTask, 0, DELAY);
    }

    public void stopTimer() {
        mTimer.cancel();
        mTimer.purge();
    }

    public void filterRR(final double rr, final long timestamp) {
        // TODO filter out flagged heart beats from the sample
        final ArrayList<Double> RRs = getRRSample(timestamp, 20);
        double filteredRR = rr;
        double mean = calculateMean(RRs);
        double sd = calculateStandardDeviation(RRs, mean);
        double tb = mean - (2 * sd);
        double tt = mean + (2 * sd);
        RRObject RRm1 = Main.polarH7Helper.RRs.get(Main.polarH7Helper.RRs.size()-1);    // RRs(n-1)
        RRObject RRm2 = Main.polarH7Helper.RRs.get(Main.polarH7Helper.RRs.size()-2);    // RRs(n-2)

        if(DISPLAY_LOGS) Log.d("HRV", "Thresholds TB: " + tb + " TT: " + tt + " ****** RR: " + rr + " RR0: " + RRm1.getRR() + " t" + RRm1.getFiltered() + " RR1: " + RRm2.getRR() + " t" + RRm2.getFiltered());

        if(mClean == false) {
            if(RRm1.getRR() < tb && rr > tt) {
                if(DISPLAY_LOGS) Log.e("HRV", "Extopic beat");
                Main.polarH7Helper.RRs.set(Main.polarH7Helper.RRs.size()-1, new RRObject(mean, RRm1.getTimestamp(), 2));
                filteredRR = rr + RRm1.getRR() - mean;
            } else if(RRm1.getRR() < 0.75*RRm2.getRR() && rr < 0.75*RRm2.getRR()) {
                // Changing condition to AND, because OR might cause an error during abrupt transition between high and low workload tasks
                if(DISPLAY_LOGS) Log.e("HRV", "Wrong detection");
                Main.polarH7Helper.RRs.remove(Main.polarH7Helper.RRs.size()-1);
                filteredRR = rr + RRm1.getRR();
            } else if(rr >= 2*RRm2.getRR()) {
                // Changing
                if(DISPLAY_LOGS) Log.e("HRV", "Missed detection");
                Main.polarH7Helper.RRs.add(new RRObject(mean, timestamp-1000, 3));
                filteredRR = rr - mean;
            } else {
                if(DISPLAY_LOGS) Log.e("HRV", "Passed error check");
                Main.polarH7Helper.RRs.set(Main.polarH7Helper.RRs.size()-1, new RRObject(RRm1.getRR(), RRm1.getTimestamp(), 0));
            }
            mClean = true;
        }

        if(filteredRR < tb) {
            if(DISPLAY_LOGS) Log.e("HRV", "RR: " + filteredRR + " below TB of " + tb);
            mClean = false;
        } else if (filteredRR > tt) {
            if(DISPLAY_LOGS) Log.e("HRV", "RR: " + filteredRR + " above TT of " + tt);
            mClean = false;
        }

        if(mClean) {
            Main.polarH7Helper.RRs.add(new RRObject(filteredRR, timestamp, 0));
        } else {
            Main.polarH7Helper.RRs.add(new RRObject(filteredRR, timestamp, 1));
        }
    }

    public ArrayList<Double> getRRSample(final long timestamp, final long sampleSize) {
        ArrayList<Double> rrs = new ArrayList<>();
        long timestampDiff = timestamp - (1000*sampleSize);

        for(int n = Main.polarH7Helper.RRs.size()-1; n > 0; n--) {
            RRObject rrObject = Main.polarH7Helper.RRs.get(n);
            if(rrObject.getTimestamp() > timestampDiff) {
                if(rrObject.getFiltered() != 1) {
                    // Do not include beats that were labelled unclean that is, 1
                    rrs.add(rrObject.getRR());
                }
            } else {
                break;
            }
        }
        return rrs;
    }

    private void analyse() {
        long currentTime = System.currentTimeMillis();
        // check if session has started and minimum of 5 seconds elapsed
        if(session_status && currentTime >= session_timestamp+5000) {
            RMSSD30 = calculateRMSSD(getRRSample(currentTime, 30));
            RMSSD120 = calculateRMSSD(getRRSample(currentTime, 120));

//            LombScargle ls = new LombScargle(getRRSample(currentTime, 30));
//            fs = ls.calculate();

            if(Main.syncData) {
                RMSSDUpdated = true;
            } else {
                // TODO implement hrv database table helper
            }

            if(Main.displayData) {
                updateLabel(FragmentHRV.rmssd30Label, "RMSSD 30s: " + String.format("%.03f", RMSSD30));
                updateLabel(FragmentHRV.rmssd120Label, "RMSSD 120s: " + String.format("%.03f", RMSSD120));

                mCounter += 1;
                FragmentHRV.rmssdGraph.getViewport().setMinX(mCounter-FragmentHRV.graphWidth);
                FragmentHRV.rmssdGraph.getViewport().setMaxX(mCounter);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentHRV.rmssd30GraphSeries.appendData(new DataPoint(mCounter, RMSSD30), true, FragmentHRV.graphWidth);
                        FragmentHRV.rmssd120GraphSeries.appendData(new DataPoint(mCounter, RMSSD120), true, FragmentHRV.graphWidth);
                    }
                });
            }
        }
    }

    private double calculateRMSSD(ArrayList<Double> RRs) {
        ArrayList<Double> RMSSDDiffSum = new ArrayList<>();
        double RMSSDSum = 0;

        for(int n = 0; n < RRs.size()-1; n++) {
            double r1 = RRs.get(n+1)-RRs.get(n);
            double r2 = Math.pow(r1, 2);
            RMSSDDiffSum.add(r2);
        }

        for(int n = 0; n < RMSSDDiffSum.size(); n++) {
            RMSSDSum += RMSSDDiffSum.get(n);
        }

        double r1 = 1/((double)RRs.size()-1);

        return Math.sqrt(r1*RMSSDSum);
    }

    private double calculateMean(ArrayList<Double> RRs) {
        double RRSum = 0;
        for(int n = 0; n < RRs.size(); n++) {
            RRSum += RRs.get(n);
        }
        return RRSum/RRs.size();
    }

    private double calculateStandardDeviation(ArrayList<Double> RRs, double mean) {
        ArrayList<Double> RRsMinusMeanSquared = new ArrayList<>();
        double RRsMinusMeanSquaredSum = 0;
        double RRsMinusMeanSquaredMean;

        for(int n = 0; n < RRs.size(); n++) {
            double r1 = RRs.get(n)-mean;
            double r2 = Math.pow(r1, 2);
            RRsMinusMeanSquared.add(r2);
        }

        for(int n = 0; n < RRsMinusMeanSquared.size(); n++) {
            RRsMinusMeanSquaredSum += RRsMinusMeanSquared.get(n);
        }

        RRsMinusMeanSquaredMean = RRsMinusMeanSquaredSum/RRsMinusMeanSquared.size();

        return Math.sqrt(RRsMinusMeanSquaredMean);
    }
}
