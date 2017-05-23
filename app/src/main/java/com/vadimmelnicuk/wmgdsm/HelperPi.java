package com.vadimmelnicuk.wmgdsm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
    private byte[] mReadBuffer;
    private static final UUID MY_UUID = UUID.fromString("56e8a14a-80b3-11e5-8bcf-feff819cdc9f");
    private static final String MY_NAME = "WMGDSM";

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

    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
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
        mReadBuffer = new byte[1024];

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(mSocket.isConnected()) {
                        try {
                            // Read from the InputStream.
                            int bytes = mInStream.read(mReadBuffer);
                            // Send the obtained bytes to the UI activity.
                            Message readMsg = mHandler.obtainMessage(MessageConstants.MESSAGE_READ, bytes, -1, mReadBuffer);
                            String readMessage = new String(mReadBuffer, 0, bytes);
                            readMsg.sendToTarget();
                            Log.d("PI", "Message: " + readMessage);
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
