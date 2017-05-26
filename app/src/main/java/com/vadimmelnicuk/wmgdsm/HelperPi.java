package com.vadimmelnicuk.wmgdsm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by vadimmelnicuk on 22/05/2017.
 */

public class HelperPi extends Main {

    private Context mContext;
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothServerSocket mServerSocket = null;
    private BluetoothSocket mSocket = null;
    private InputStream mInStream = null;
    private OutputStream mOutStream = null;
    private static final UUID MY_UUID = UUID.fromString("56e8a14a-80b3-11e5-8bcf-feff819cdc9f");
    private static final String MY_NAME = "WMGDSM";

    private final String RUN_MESSAGE = "4294901772";
    private final String STOP_MESSAGE = "4294901773";
    private final String VEHICLE_UPDATE_MESSAGE = "4294901765";
    private final String EVENT_IN_V2 = "4294901781";

    public static boolean dpUpdated = false;
    public static int scenarioState = 0;
    public static String messageId;
    public static String vehicleId;
    public static String headLight;
    public static String brakeLight;
    public static String reverseLight;
    public static String fogLight;
    public static String leftIndicator;
    public static String rightIndicator;
    public static String hornOn;
    public static String engineOn;
    public static String wheelScreeching;
    public static String hiBeamOn;
    public static String handBrake;
    public static String positionLatitude;
    public static String positionLongitude;
    public static String positionElevation;
    public static String positionX;
    public static String positionY;
    public static String positionZ;
    public static String orientationX;
    public static String orientationY;
    public static String orientationZ;
    public static String velocityX;
    public static String velocityY;
    public static String velocityZ;
    public static String accelerationX;
    public static String accelerationY;
    public static String accelerationZ;
    public static String angularVelocityX;
    public static String angularVelocityY;
    public static String angularVelocityZ;
    public static String steeringAngle;
    public static String rpm;
    public static String acceleratorPedal;
    public static String brakePedal;
    public static String clutchPedal;
    public static String steeringWheelAngle;
    public static String gear;

    HelperPi(Context context) {
        mContext = context;
    }

    public void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = new Handler();
        connect();

    }

    public void connect() {
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(MY_NAME, MY_UUID);
        } catch (IOException e) {
            Log.e("PI", "Socket's listen method failed", e);
        }
    }

    public void accept() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = mServerSocket.accept();
                    Log.d("PI", "Socket connection accepted");
                    modulesPiConnected = true;
                    updateLabel(FragmentModules.modulesPiButton, "Connected");
                    updateImage(FragmentModules.modulesPiIndicator, R.drawable.circle_green);
                } catch (IOException e) {
                    Log.e("PI", "Socket's accept method failed", e);
                }

                if (mSocket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    Log.d("PI", "Socket " + mSocket.toString());
                    initStreams();
                    read();
                }
            }
        });
        thread.start();
    }

    public void disconnect() {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            Log.e("PI", "Could not close the socket", e);
        }
    }

    private void initStreams() {
        try {
            mInStream = mSocket.getInputStream();
        } catch (IOException e) {
            Log.e("PI", "Error occurred when creating input stream", e);
        }
        try {
            mOutStream = mSocket.getOutputStream();
        } catch (IOException e) {
            Log.e("PI", "Error occurred when creating output stream", e);
        }
    }

    private void read() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(mSocket.isConnected()) {
                        try {
                            // Read from the InputStream
                            byte[] readBuffer = new byte[1024];
                            int bytes = mInStream.read(readBuffer);
                            String message = new String(readBuffer, 0, bytes);
                            String[] messageArray = message.split(",");

                            if(messageArray.length > 0){
                                messageId = messageArray[0];

                                if(messageId.equals(RUN_MESSAGE)) {
                                    scenarioState = 1;
                                    Log.d("PI", "Scenario run");
                                }
                                else if(messageId.equals(STOP_MESSAGE)) {
                                    scenarioState = 0;
                                    Log.d("PI", "Scenario stop");
                                }
                                else if(messageId.equals(EVENT_IN_V2)) {
                                    String param1 = messageArray[1];
                                    String param2 = messageArray[2];
                                    String param3 = messageArray[3];
                                    Log.d("PI", "nBack " + param1 + param2 + param3);

                                    if(param1.equals("1")) {
                                        Main.nbackHelper.startTimer();
                                    }
                                }
                                else if(messageId.equals(VEHICLE_UPDATE_MESSAGE)) {
                                    vehicleId = messageArray[1];
                                    headLight = messageArray[2];
                                    brakeLight = messageArray[3];
                                    reverseLight = messageArray[4];
                                    fogLight = messageArray[5];
                                    leftIndicator = messageArray[6];
                                    rightIndicator = messageArray[7];
                                    hornOn = messageArray[8];
                                    engineOn = messageArray[9];
                                    wheelScreeching = messageArray[10];
                                    hiBeamOn = messageArray[11];
                                    handBrake = messageArray[12];
                                    positionLatitude = messageArray[13];
                                    positionLongitude = messageArray[14];
                                    positionElevation = messageArray[15];
                                    positionX = messageArray[16];
                                    positionY = messageArray[17];
                                    positionZ = messageArray[18];
                                    orientationX = messageArray[19];
                                    orientationY = messageArray[20];
                                    orientationZ = messageArray[21];
                                    velocityX = messageArray[22];
                                    velocityY = messageArray[23];
                                    velocityZ = messageArray[24];
                                    accelerationX = messageArray[25];
                                    accelerationY = messageArray[26];
                                    accelerationZ = messageArray[27];
                                    angularVelocityX = messageArray[28];
                                    angularVelocityY = messageArray[29];
                                    angularVelocityZ = messageArray[30];
                                    steeringAngle = messageArray[31];
                                    rpm = messageArray[32];
                                    acceleratorPedal = messageArray[33];
                                    brakePedal = messageArray[34];
                                    clutchPedal = messageArray[35];
                                    steeringWheelAngle = messageArray[36];
                                    gear = messageArray[37];
                                    dpUpdated = true;
//                                    Log.d("PI", "Message: " + message + " bytes " + bytes);
                                }
                            }
                        } catch (IOException e) {
                            Log.d("PI", "Input stream was disconnected", e);
                        }
                    } else {
                        break;
                    }
                }
            }
        });
        thread.start();
    }
}
