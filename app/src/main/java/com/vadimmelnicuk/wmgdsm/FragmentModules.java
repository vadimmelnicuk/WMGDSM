package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
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
    public static ImageView modulesEmpaticaE4Indicator;
    public static ImageView modulesPolarH7Indicator;
    public static ImageView modulesAffectivaIndicator;
    public static Button modulesEmpaticaE4Button;
    public static Button modulesPolarH7Button;
    public static Button modulesAffectivaButton;

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
        modulesEmpaticaE4Indicator = (ImageView) getView().findViewById(R.id.modules_empaticaE4_indicator);
        modulesPolarH7Indicator = (ImageView) getView().findViewById(R.id.modules_polarH7_indicator);
        modulesAffectivaIndicator = (ImageView) getView().findViewById(R.id.modules_affectiva_indicator);
        modulesEmpaticaE4Button = (Button) getView().findViewById(R.id.modules_empaticaE4_button);
        modulesPolarH7Button = (Button) getView().findViewById(R.id.modules_polarH7_button);
        modulesAffectivaButton = (Button) getView().findViewById(R.id.modules_affectiva_button);

        if(Main.modulesEmpaticaE4 || Main.modulesPolarH7 || Main.modulesAffectiva) {
            modulesLabel.setText("Modules");
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
                            Main.affectivaHelper.fragmentOn();
                            Main.modulesAffectivaConnected = true;
                            modulesAffectivaButton.setText("Connected");
                            modulesAffectivaIndicator.setImageResource(R.drawable.circle_green);
                        }
                    }
                });
                modulesAffectivaLayout.setVisibility(View.VISIBLE);
            }
        } else {
            modulesLabel.setText("Please select some modules in the settings section.");
        }
    }
}
