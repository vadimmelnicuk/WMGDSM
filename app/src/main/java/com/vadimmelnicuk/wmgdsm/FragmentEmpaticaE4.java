package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class FragmentEmpaticaE4 extends Fragment {

    public static TextView DeviceNameLabel;
    public static TextView BatteryLabel;
    public static TextView AccelerationXLabel;
    public static TextView AccelerationYLabel;
    public static TextView AccelerationZLabel;
    public static TextView bvpLabel;
    public static TextView ibiLabel;
    public static TextView edaLabel;
    public static TextView temperatureLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empaticae4, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeviceNameLabel = (TextView) getView().findViewById(R.id.device_name);
        BatteryLabel = (TextView) getView().findViewById(R.id.battery_label);
        AccelerationXLabel = (TextView) getView().findViewById(R.id.acceleration_x);
        AccelerationYLabel = (TextView) getView().findViewById(R.id.acceleration_y);
        AccelerationZLabel = (TextView) getView().findViewById(R.id.acceleration_z);
        bvpLabel = (TextView) getView().findViewById(R.id.bvp_label);
        ibiLabel = (TextView) getView().findViewById(R.id.ibi_label);
        edaLabel = (TextView) getView().findViewById(R.id.eda_label);
        temperatureLabel = (TextView) getView().findViewById(R.id.temperature_label);
    }
}
