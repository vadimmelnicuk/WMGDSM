package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class FragmentModules extends Fragment {

    private TextView modulesLabel;
    private RelativeLayout modulesEmpaticaE4Layout;
    private RelativeLayout modulesPolarH7Layout;
    private RelativeLayout modulesAffectivaLayout;
    private RelativeLayout modulesHRVLayout;
    private RelativeLayout modulesPiLayout;
    private RelativeLayout modulesNbackLayout;

    public static ImageView modulesEmpaticaE4Indicator;
    public static ImageView modulesPolarH7Indicator;
    public static ImageView modulesAffectivaIndicator;
    public static ImageView modulesHRVIndicator;
    public static ImageView modulesPiIndicator;
    public static ImageView modulesNbackIndicator;

    public static Button modulesEmpaticaE4Button;
    public static Button modulesPolarH7Button;
    public static Button modulesAffectivaButton;
    public static Button modulesPiButton;
    public static Button modulesNbackButton0;
    public static Button modulesNbackButton1;
    public static Button modulesNbackButton2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modules, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        modulesLabel = (TextView) getView().findViewById(R.id.modules_label);
        modulesEmpaticaE4Layout = (RelativeLayout) getView().findViewById(R.id.modules_empaticaE4);
        modulesPolarH7Layout = (RelativeLayout) getView().findViewById(R.id.modules_polarH7);
        modulesAffectivaLayout = (RelativeLayout) getView().findViewById(R.id.modules_affectiva);
        modulesHRVLayout = (RelativeLayout) getView().findViewById(R.id.modules_hrv);
        modulesPiLayout = (RelativeLayout) getView().findViewById(R.id.modules_pi);
        modulesNbackLayout = (RelativeLayout) getView().findViewById(R.id.modules_nback);

        modulesEmpaticaE4Indicator = (ImageView) getView().findViewById(R.id.modules_empaticaE4_indicator);
        modulesPolarH7Indicator = (ImageView) getView().findViewById(R.id.modules_polarH7_indicator);
        modulesAffectivaIndicator = (ImageView) getView().findViewById(R.id.modules_affectiva_indicator);
        modulesHRVIndicator = (ImageView) getView().findViewById(R.id.modules_hrv_indicator);
        modulesPiIndicator = (ImageView) getView().findViewById(R.id.modules_pi_indicator);
        modulesNbackIndicator = (ImageView) getView().findViewById(R.id.modules_nback_indicator);

        modulesEmpaticaE4Button = (Button) getView().findViewById(R.id.modules_empaticaE4_button);
        modulesPolarH7Button = (Button) getView().findViewById(R.id.modules_polarH7_button);
        modulesAffectivaButton = (Button) getView().findViewById(R.id.modules_affectiva_button);
        modulesPiButton = (Button) getView().findViewById(R.id.modules_pi_button);
        modulesNbackButton0 = (Button) getView().findViewById(R.id.modules_nback_button_0);
        modulesNbackButton1 = (Button) getView().findViewById(R.id.modules_nback_button_1);
        modulesNbackButton2 = (Button) getView().findViewById(R.id.modules_nback_button_2);

        if(Main.modulesEmpaticaE4 || Main.modulesPolarH7 || Main.modulesAffectiva) {
            if(Main.modulesEmpaticaE4) {
                modulesEmpaticaE4Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modulesEmpaticaE4Button.setEnabled(false);
                        modulesEmpaticaE4Button.setText("Scanning...");

                        Main.empaticaE4Helper.deviceManager.startScanning();
                    }
                });
                modulesEmpaticaE4Layout.setVisibility(View.VISIBLE);
            }
            if(Main.modulesPolarH7) {
                modulesPolarH7Button.setEnabled(true);
                modulesPolarH7Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modulesPolarH7Button.setEnabled(false);
                        modulesPolarH7Button.setText("Scanning...");

                        Main.polarH7Helper.startScan();
                    }
                });
                modulesPolarH7Layout.setVisibility(View.VISIBLE);
            }
            if(Main.modulesAffectiva) {
                modulesAffectivaButton.setEnabled(true);
                modulesAffectivaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modulesAffectivaButton.setEnabled(false);
                        modulesAffectivaButton.setText("Connecting...");

                        if (!Main.affectivaHelper.detector.isRunning()) {
                            Main.affectivaHelper.detector.start();
                            if(Main.displayData) {
                                Main.affectivaHelper.fragmentOn();
                                if(Main.displayCamera) {
                                    Main.affectivaHelper.cameraOn();
                                }
                            }
                            Main.modulesAffectivaConnected = true;
                            modulesAffectivaButton.setText("Connected");
                            modulesAffectivaIndicator.setImageResource(R.drawable.circle_green);
                        }
                    }
                });
                modulesAffectivaLayout.setVisibility(View.VISIBLE);
            }
            if(Main.modulesHRV) {
                Main.modulesHRVConnected = true;
                Main.hrvHelper.startTimer();
                modulesHRVIndicator.setImageResource(R.drawable.circle_green);
                modulesHRVLayout.setVisibility(View.VISIBLE);
            }
            if(Main.modulesPi) {
                modulesPiButton.setEnabled(true);
                modulesPiButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivityForResult(discoverableIntent, 0x1);
                        modulesPiButton.setEnabled(false);
                        modulesPiButton.setText("Waiting...");
                        Main.piHelper.accept();
                    }
                });
                modulesPiLayout.setVisibility(View.VISIBLE);
            }
            if(Main.modulesNBack) {
                Main.modulesNBackConnected = true;
                modulesNbackButton0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Main.nbackHelper.run(0);
                    }
                });
                modulesNbackButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Main.nbackHelper.run(1);
                    }
                });
                modulesNbackButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Main.nbackHelper.run(2);
                    }
                });
                modulesNbackIndicator.setImageResource(R.drawable.circle_green);
                modulesNbackLayout.setVisibility(View.VISIBLE);
            }
        } else {
            modulesLabel.setText("Please select some modules in the settings section.");
        }
    }
}
