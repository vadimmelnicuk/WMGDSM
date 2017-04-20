package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by vadimmelnicuk on 10/04/2017.
 */

public class FragmentHRV extends Fragment {

    public static TextView DeviceNameLabel;
    public static TextView rmssd30Label;
    public static TextView rmssd120Label;
    public static GraphView rmssd30Graph;
    public static LineGraphSeries<DataPoint> rmssd30GraphSeries = new LineGraphSeries<>();
    public static int rmssd30GraphWidth = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hrv, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeviceNameLabel = (TextView) getView().findViewById(R.id.device_name);
        rmssd30Label = (TextView) getView().findViewById(R.id.rmssd30_label);
        rmssd120Label = (TextView) getView().findViewById(R.id.rmssd120_label);
        rmssd30Graph = (GraphView) getView().findViewById(R.id.rmssd30_graph);

        rmssd30Graph.getGridLabelRenderer().setTextSize(20f);
        rmssd30Graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        rmssd30Graph.getViewport().setXAxisBoundsManual(true);
        rmssd30GraphSeries.setAnimated(true);
        rmssd30Graph.addSeries(rmssd30GraphSeries);
    }
}
