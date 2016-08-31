package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    public static SharedPreferences preferences;
    public static boolean session_status = false;
    public static long session_timestamp;

    // Helpers
    public static HelperEmpaticaE4 empaticaE4Helper;
    public static HelperPolarH7 polarH7Helper;
    public static HelperAffectiva affectivaHelper;
    public static DbDsmHelper dsmDb;
    public static DbEmpaticaE4Helper empaticaDb;
    public static DbPolarH7Helper polarDb;
    public static DbAffectivaHelper affectivaDb;

    // States
    public static boolean modulesEmpaticaE4 = false;
    public static boolean modulesEmpaticaE4Connected = false;
    public static boolean modulesPolarH7 = false;
    public static boolean modulesPolarH7Connected = false;
    public static boolean modulesAffectiva = false;
    public static boolean modulesAffectivaConnected = false;
    public static boolean displayData = false;
    public static boolean displayCamera = false;
    public static boolean syncData = false;

    // UI
    public static Fragment empaticaE4Fragment;
    public static Fragment polarH7Fragment;
    public static Fragment affectivaFragment;
    public static SurfaceView cameraView;
    public static TextView sessionLabel;
    public static Button sessionControlButton;
    public static Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Variables init
        initVars();

        // Load button listeners
        loadButtonListeners();

        // DSM main database init
        dsmDb = new DbDsmHelper(getApplicationContext());

        // Hide unused fragments
        toggleFragment(empaticaE4Fragment, false);
        toggleFragment(polarH7Fragment, false);
        toggleFragment(affectivaFragment, false);

        // Init modules
        if(modulesEmpaticaE4) {
            empaticaE4Helper = new HelperEmpaticaE4(getApplicationContext());
            empaticaE4Helper.init();
        }
        if(modulesPolarH7) {
            polarH7Helper = new HelperPolarH7(getApplicationContext());
            polarH7Helper.init();
        }
        if(modulesAffectiva) {
            affectivaHelper = new HelperAffectiva(getApplicationContext());
            affectivaHelper.init();
        }

        // Delete sessions
//        dsmDb.dropDb(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, ActivityPreferences.class));
                return true;
            case R.id.action_sessions:
                startActivity(new Intent(this, ActivitySessions.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(modulesEmpaticaE4) {
            empaticaE4Helper.deviceManager.stopScanning();
        }
        if(modulesPolarH7) {

        }
        if(modulesAffectiva) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(modulesEmpaticaE4) {
            empaticaE4Helper.deviceManager.disconnect();
        }
        if(modulesPolarH7) {
            polarH7Helper.disconnect();
        }
        if(modulesAffectiva) {
            if(affectivaHelper.detector.isRunning()) {
                affectivaHelper.detector.stop();
                affectivaHelper.detector.reset();
            }
        }
    }

    private void initVars() {
        // Init preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        modulesEmpaticaE4 = preferences.getBoolean("pref_modules_empaticaE4", false);
        modulesPolarH7 = preferences.getBoolean("pref_modules_polarH7", false);
        modulesAffectiva = preferences.getBoolean("pref_modules_affectiva", false);
        displayData = preferences.getBoolean("pref_display_data", false);
        displayCamera = preferences.getBoolean("pref_display_camera", false);
        syncData = preferences.getBoolean("pref_sync_data", false);

        // Initialize vars that reference UI components
        empaticaE4Fragment = getFragmentManager().findFragmentById(R.id.fragment_empaticae4);
        polarH7Fragment = getFragmentManager().findFragmentById(R.id.fragment_polarh7);
        affectivaFragment = getFragmentManager().findFragmentById(R.id.fragment_affectiva);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        sessionLabel = (TextView) findViewById(R.id.session_label);
        sessionControlButton = (Button) findViewById(R.id.session_control);
        resetButton = (Button) findViewById(R.id.resetButton);
    }

    private void loadButtonListeners() {
        sessionControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session_status) {
                    // End session
                    endSession();
                    if(modulesEmpaticaE4) {
                        empaticaE4Helper.deviceManager.disconnect();
                        toggleFragment(empaticaE4Fragment, false);
                        updateLabel(FragmentModules.modulesEmpaticaE4Button, "Connect");
                        updateButton(FragmentModules.modulesEmpaticaE4Button, true);
                        updateImage(FragmentModules.modulesEmpaticaE4Indicator, R.drawable.circle_red);
                    }
                    if(modulesPolarH7) {
                        polarH7Helper.disconnect();
                        toggleFragment(polarH7Fragment, false);
                        updateLabel(FragmentModules.modulesPolarH7Button, "Connect");
                        updateButton(FragmentModules.modulesPolarH7Button, true);
                        updateImage(FragmentModules.modulesPolarH7Indicator, R.drawable.circle_red);
                    }
                    if(modulesAffectiva) {
                        if(affectivaHelper.detector.isRunning()) {
                            affectivaHelper.detector.stop();
                            affectivaHelper.detector.reset();
                        }
                        toggleFragment(affectivaFragment, false);
                        affectivaHelper.cameraOff();
                        updateLabel(FragmentModules.modulesAffectivaButton, "Connect");
                        updateButton(FragmentModules.modulesAffectivaButton, true);
                        updateImage(FragmentModules.modulesAffectivaIndicator, R.drawable.circle_red);
                    }
                } else {
                    if(modulesEmpaticaE4 || modulesPolarH7 || modulesAffectiva) {
                        boolean modulesEmpaticaE4Ready, modulesPolarH7Ready, modulesAffectivaReady;

                        if(modulesEmpaticaE4) {
                            modulesEmpaticaE4Ready = modulesEmpaticaE4Connected;
                        } else {
                            modulesEmpaticaE4Ready = true;
                        }

                        if(modulesPolarH7) {
                            modulesPolarH7Ready = modulesPolarH7Connected;
                        } else {
                            modulesPolarH7Ready = true;
                        }

                        if(modulesAffectiva) {
                            modulesAffectivaReady = modulesAffectivaConnected;
                        } else {
                            modulesAffectivaReady = true;
                        }

                        if(modulesEmpaticaE4Ready && modulesPolarH7Ready && modulesAffectivaReady) {
                            initSession();
                        } else {
                            Toast.makeText(Main.this, "Please connect all the modules from the list", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Main.this, "Please select some modules in the settings section", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void initSession() {
        // Session init
        session_timestamp = dsmDb.startSession();
        String date = dsmDb.timestampToDate(session_timestamp);
        updateLabel(sessionLabel, "Session: " + date);
        sessionControlButton.setText(R.string.button_end_session);
        session_status = true;
        Toast.makeText(Main.this, "The session has started", Toast.LENGTH_SHORT).show();
    }

    private void endSession() {
        sessionControlButton.setText(R.string.button_start_session);
        session_status = false;
        updateLabel(sessionLabel, "");
        Toast.makeText(Main.this, "The session has ended", Toast.LENGTH_SHORT).show();
    }

    // UI Thread Functions
    public void updateLabel(final TextView label, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                label.setText(text);
            }
        });
    }

    public void updateLabel(final Button button, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setText(text);
            }
        });
    }

    public void updateButton(final Button button, final boolean state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
    }

    public void updateImage(final ImageView image, final int res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(res);
            }
        });
    }

    public void toggleFragment(final Fragment fragment, final boolean sh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(sh) {
                    fragment.getView().setVisibility(View.VISIBLE);
                } else {
                    fragment.getView().setVisibility(View.GONE);
                }
            }
        });
    }
}
