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
 * Created by vadimmelnicuk on 16/08/16.
 */
public class FragmentPolarH7 extends Fragment {

    public static TextView DeviceNameLabel;
    public static TextView bpmLabel;
    public static TextView ibiLabel;
    public static GraphView rrGraph;
    public static LineGraphSeries<DataPoint> rrGraphSeries = new LineGraphSeries<>();
    public static int graphWidth = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_polarh7, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeviceNameLabel = (TextView) getView().findViewById(R.id.device_name);
        bpmLabel = (TextView) getView().findViewById(R.id.bpm_label);
        ibiLabel = (TextView) getView().findViewById(R.id.ibi_label);
        rrGraph = (GraphView) getView().findViewById(R.id.rr_graph);

        rrGraphSeries.setAnimated(false);
        rrGraph.getGridLabelRenderer().setTextSize(20f);
        rrGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        rrGraph.getViewport().setXAxisBoundsManual(true);
        rrGraph.addSeries(rrGraphSeries);
    }
}
