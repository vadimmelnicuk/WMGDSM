package com.vadimmelnicuk.wmgdsm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class HelperPolarH7 extends Main {

    private Context mContext;
    public static BluetoothDevice polarH7;
    public static List<BluetoothGattCharacteristic> characteristics;
    private BluetoothManager BLEManager;
    private BluetoothAdapter BLEAdapter;
    private BluetoothLeScanner BLEScanner;
    public static BluetoothGatt BLEGatt;
    private ScanSettings BLEScanSettings;
    private List<ScanFilter> BLEFilters;
    public final static UUID HEART_RATE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public final static UUID HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    public static boolean BPMUpdated = false;
    public static boolean IBIUpdated = false;
    public static int BPM;
    public static ArrayList<Double> IBIs  = new ArrayList<>();
    public static ArrayList<RRObject> RRs = new ArrayList<>();
    public static double cleanRR;

    private long mCounter = 1;

    HelperPolarH7(Context context) {
        mContext = context;
    }

    public void init() {
        // Database init
        polarDb = new DbPolarH7Helper(mContext);

        BLEManager = (BluetoothManager) mContext.getSystemService(mContext.getApplicationContext().BLUETOOTH_SERVICE);
        BLEAdapter = BLEManager.getAdapter();

        if (BLEAdapter != null || BLEAdapter.isEnabled()) {
            BLEScanner = BLEAdapter.getBluetoothLeScanner();
            BLEScanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
            ScanFilter filter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(HEART_RATE_SERVICE)).build();
            BLEFilters = new ArrayList<ScanFilter>();
            BLEFilters.add(0, filter);
        }

        // Reset rrGraphSeries
        FragmentPolarH7.rrGraphSeries.resetData(new DataPoint[]{});
    }

    public void disconnect() {
        if(modulesPolarH7Connected) {
            BLEGatt.close();
        }
    }

    public void startScan() {
        BLEScanner.startScan(BLEFilters, BLEScanSettings, BLEScanCallback);
//        Log.i("Polar H7 Scanning: ", "started");
    }

    public void stopScan() {
        BLEScanner.stopScan(BLEScanCallback);
//        Log.i("Polar H7 Scanning: ", "finished");
    }

    private ScanCallback BLEScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
//            Log.i("Polar H7 Scan result", result.toString());
            BluetoothDevice btDevice = result.getDevice();
//            Log.i("Polar H7 Device", btDevice.getName());
            if(btDevice.getName().equals(mContext.getResources().getString(R.string.polar_id))) {
                stopScan();
//                Log.i("Polar H7 Scan", "connecting");
                polarH7 = btDevice;
                updateLabel(FragmentModules.modulesPolarH7Button, "Connecting...");
                connectToDevice(btDevice);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
//                Log.i("Polar H7 Scan Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Polar H7 Scan Failed", "Error Code: " + errorCode);
        }
    };

    public void connectToDevice(BluetoothDevice device) {
        if (BLEGatt == null) {
//            Log.e("Polar H7", "Connecting to Gatt.");
            BLEGatt = device.connectGatt(mContext, false, gattCallback);
        } else {
//            Log.e("Polar H7", "Gatt already exists.");
            BLEGatt = device.connectGatt(mContext, false, gattCallback);
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            Log.i("onConnectionStateChange", "Polar H7 Gatt Status: " + status);

            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("Polar H7", "STATE_CONNECTED");
                    modulesPolarH7Connected = true;
                    updateLabel(FragmentModules.modulesPolarH7Button, "Connected");
                    updateImage(FragmentModules.modulesPolarH7Indicator, R.drawable.circle_green);
                    if(Main.displayData) {
                        toggleFragment(polarH7Fragment, true);
                    }
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("Polar H7", "STATE_DISCONNECTED");
                    modulesPolarH7Connected = false;
                    updateLabel(FragmentModules.modulesPolarH7Button, "Connect");
                    updateButton(FragmentModules.modulesPolarH7Button, true);
                    updateImage(FragmentModules.modulesPolarH7Indicator, R.drawable.circle_red);
                    toggleFragment(polarH7Fragment, false);
                    gatt.disconnect();
                    break;
                default:
                    Log.e("Polar H7", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                characteristics = service.getCharacteristics();
//                Log.i("onServicesDiscovered", "Polar H7 Services " + service.toString());
            }
            BluetoothGattCharacteristic hr = services.get(2).getCharacteristics().get(0);
            for (BluetoothGattDescriptor descriptor : hr.getDescriptors()) {
                //find descriptor UUID that matches Client Characteristic Configuration (0x2902) and then call setValue on that descriptor
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                BLEGatt.writeDescriptor(descriptor);
            }
            gatt.setCharacteristicNotification(hr, true);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            Log.i("onCharacteristicRead", "Polar H7 Characteristic " + characteristic.toString());
            byte[] value = characteristic.getValue();
            String v = new String(value);
//            Log.i("onCharacteristicRead", "Polar H7 Value: " + v);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            //read the characteristic data
            if(HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
                int flag = characteristic.getProperties();
                int BPMFormat;
                int BPMOffset;
                long currentTime = System.currentTimeMillis();

                if((flag & 1) != 0) {
                    BPMFormat = BluetoothGattCharacteristic.FORMAT_UINT16;
                    BPMOffset = 2;
                } else {
                    BPMFormat = BluetoothGattCharacteristic.FORMAT_UINT8;
                    BPMOffset = 1;
                }

                if(characteristic.getValue().length > 2) {
                    final int bpm = characteristic.getIntValue(BPMFormat, 1);

                    if(session_status) {
                        if(Main.syncData) {
                            BPM = bpm;
                            BPMUpdated = true;
                        } else {
                            polarDb.insertBPM(session_timestamp, currentTime, bpm);
                        }
                    }

                    if(Main.displayData) {
                        updateLabel(FragmentPolarH7.bpmLabel, "BPM: " + bpm);
                    }

                    for(int n = 2; n < characteristic.getValue().length; n += 2) {
                        final double rr = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1+BPMOffset+n-2)/1024.0*1000.0;

                        // Error when using without HRV analysis: Attempt to invoke virtual method 'void com.vadimmelnicuk.wmgdsm.HelperHRV.filterRR(double, long)' on a null object reference
                        // Below is the fix for the time being
//                        cleanRR = rr;

                        if(RRs.size() > 10) {
                            Main.hrvHelper.filterRR(rr, currentTime);
                            cleanRR = RRs.get(RRs.size()-2).getRR();
                        } else {
                            RRs.add(new RRObject(rr, currentTime, 0));
                            cleanRR = RRs.get(RRs.size()-1).getRR();
                        }

                        if(session_status) {
                            if(Main.syncData) {
                                IBIs.add(cleanRR);
                                IBIUpdated = true;
                            } else {
                                polarDb.insertRR(session_timestamp, currentTime, cleanRR);
                            }
                        }

                        if(Main.displayData) {
                            updateLabel(FragmentPolarH7.ibiLabel, "IBI: " + String.format("%.03f", cleanRR));

                            mCounter += 1;

                            FragmentPolarH7.rrGraph.getViewport().setMinX(mCounter-FragmentPolarH7.graphWidth);
                            FragmentPolarH7.rrGraph.getViewport().setMaxX(mCounter);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentPolarH7.rrGraphSeries.appendData(new DataPoint(mCounter, cleanRR), true, FragmentPolarH7.graphWidth);
                                }
                            });
                        }
                    }
                }
            }
        }
    };
}
