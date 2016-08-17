package com.vadimmelnicuk.wmgdsm;

import android.app.Activity;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class HelperEmpaticaE4 extends Main implements EmpaDataDelegate, EmpaStatusDelegate {

    private Context mContext;
    public static BluetoothDevice empaticaE4;
    public static String empaticaE4Name;
    public static EmpaDeviceManager deviceManager;
    private int REQUEST_ENABLE_BT = 1;

    HelperEmpaticaE4(Context context) {
        mContext = context;
    }

    public void init() {
        // Database init
        Main.empaticaDb = new DbEmpaticaE4Helper(mContext);
        // Create a new EmpaDeviceManager. MainActivity is both its data and status delegate.
        deviceManager = new EmpaDeviceManager(mContext, this, this);
        // Initialize the Device Manager using your API key. You need to have Internet access at this point.
        deviceManager.authenticateWithAPIKey(mContext.getResources().getString(R.string.empatica_api_key));
    }

    @Override
    public void didDiscoverDevice(BluetoothDevice bluetoothDevice, String deviceName, int rssi, boolean allowed) {
        // Check if the discovered device can be used with your API key. If allowed is always false,
        // the device is not linked with your API key. Please check your developer area at
        // https://www.empatica.com/connect/developer.php
        if (allowed) {
            // Stop scanning and try to connect
            deviceManager.stopScanning();
            empaticaE4 = bluetoothDevice;
            empaticaE4Name = deviceName;
            try {
                // Connect to the device
                deviceManager.connectDevice(empaticaE4);
                updateLabel(FragmentModules.modulesEmpaticaE4Button, "Connecting...");
            } catch (ConnectionNotAllowedException e) {
                // This should happen only if you try to connect when allowed == false.
                Toast.makeText(mContext, "Sorry, you can't connect to this device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void didRequestEnableBluetooth() {
        // Request the user to enable Bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The user chose not to enable Bluetooth
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            // You should deal with this
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void didUpdateSensorStatus(EmpaSensorStatus status, EmpaSensorType type) {
        // No need to implement this right now
    }

    @Override
    public void didUpdateStatus(EmpaStatus status) {
        // The device manager is ready for use
        if (status == EmpaStatus.READY) {
            updateButton(FragmentModules.modulesEmpaticaE4Button, true);
            // The device manager has established a connection
        } else if (status == EmpaStatus.CONNECTED) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Main.modulesEmpaticaE4Connected = true;
                    updateLabel(FragmentModules.modulesEmpaticaE4Button, "Connected");
                    updateImage(FragmentModules.modulesEmpaticaE4Indicator, R.drawable.circle_green);
                    updateLabel(FragmentEmpaticaE4.DeviceNameLabel, String.format(empaticaE4Name));
                    toggleFragment(Main.empaticaE4Fragment, true);
                }
            });
            // The device manager disconnected from a device
        } else if (status == EmpaStatus.DISCONNECTED) {
            //Hide data if device was disconnected
        }
    }

    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {
        updateLabel(FragmentEmpaticaE4.AccelerationXLabel, "x: " + x);
        updateLabel(FragmentEmpaticaE4.AccelerationYLabel, "y: " + y);
        updateLabel(FragmentEmpaticaE4.AccelerationZLabel, "z: " + z);
        if(Main.session_status) {
            Main.empaticaDb.insertAcceleration(Main.session_timestamp, x, y, z, timestamp);
        }
    }

    @Override
    public void didReceiveBatteryLevel(float battery, double timestamp) {
        updateLabel(FragmentEmpaticaE4.BatteryLabel, String.format("Battery: %.0f %%", battery * 100));
    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {
        updateLabel(FragmentEmpaticaE4.bvpLabel, "BVP: " + String.format("%.03f", bvp));
        if(Main.session_status) {
            Main.empaticaDb.insertBVP(Main.session_timestamp, bvp, timestamp);
        }
    }

    @Override
    public void didReceiveIBI(float ibi, double timestamp) {
        updateLabel(FragmentEmpaticaE4.ibiLabel, "IBI: " + String.format("%.03f", ibi));
        if(Main.session_status) {
            Main.empaticaDb.insertIBI(Main.session_timestamp, ibi, timestamp);
        }
    }

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {
        updateLabel(FragmentEmpaticaE4.edaLabel, "EDA: " + String.format("%.03f", gsr));
        if(Main.session_status) {
            Main.empaticaDb.insertGSR(Main.session_timestamp, gsr, timestamp);
        }
    }

    @Override
    public void didReceiveTemperature(float temperature, double timestamp) {
        updateLabel(FragmentEmpaticaE4.temperatureLabel, "Temperature: " + String.format("%.03f", temperature));
        if(Main.session_status) {
            Main.empaticaDb.insertTemperature(Main.session_timestamp, temperature, timestamp);
        }
    }
}
