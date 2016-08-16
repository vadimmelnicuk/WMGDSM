package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class FragmentAffectiva extends Fragment {

    public static LinearLayout layout;
    public static TextView DeviceNameLabel;
    public static TextView SmileLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_affectiva, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = (LinearLayout) getView().findViewById(R.id.modules_affectiva_layout);
        DeviceNameLabel = (TextView) getView().findViewById(R.id.device_name);
        SmileLabel = (TextView) getView().findViewById(R.id.smile_label);
    }
}
