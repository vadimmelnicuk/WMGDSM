package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import org.w3c.dom.Text;

public class Main extends AppCompatActivity {

    public static SharedPreferences preferences;
    public static boolean session_status = false;
    public static long session_timestamp = 0;

    // Helpers
    public static HelperEmpaticaE4 empaticaE4Helper;
    public static HelperPolarH7 polarH7Helper;
    public static HelperAffectiva affectivaHelper;
    public static HelperHRV hrvHelper;
    public static HelperNBack nbackHelper;
    public static HelperPi piHelper;
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
    public static boolean modulesHRV = false;
    public static boolean modulesHRVConnected = false;
    public static boolean modulesNBack = false;
    public static boolean modulesNBackConnected = false;
    public static boolean modulesPi = false;
    public static boolean modulesPiConnected = false;
    public static boolean modulesAIUI = false;
    public static boolean modulesAIUIConnected = false;
    public static boolean syncData = false;
    public static boolean displayData = false;
    public static boolean displayCamera = false;
    public static boolean displayNbackResult = false;
    public static int prefNbackTests;
    public static int prefNbackNumbers;

    // UI
    public static Fragment empaticaE4Fragment;
    public static Fragment polarH7Fragment;
    public static Fragment affectivaFragment;
    public static Fragment hrvFragment;
    public static SurfaceView cameraView;
    public static TextView sessionLabel;
    public static Button sessionControlButton;
    public static Button resetButton;
    public static ImageView HMIImage;
    public static TextView HMIMessage;
    public static TextView HMIProgress;

    // Bitmaps
    public static Drawable HMIEmpty;
    public static Drawable HMIAutonomousModeEngaged;
    public static Drawable HMIInfrontCar;
    public static Drawable HMIRightArrow;
    public static Drawable HMIRightArrowTurn;
    public static Drawable HMISwerve;
    public static Drawable HMIEmergency;
    public static Drawable HMITakeControl;

    // N-Back
    public static RelativeLayout nbackLayout;
    public static TextView nbackLabel;
    public static TextView nbackTypeLabel;
    public static ImageView nbackCircle;
    public static TextView nbackResult;
    public static RelativeLayout nbackMessageLayout;
    public static TextView nbackMessage;

    // AIUI
    public static RelativeLayout aiuiLayout;
    public static ImageView aiuiIndicator;
    public static TextView aiuiScoreLabel;
    public static TextView aiuiBonusLabel;
    public static TextView aiuiReasonLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Variables init
        initVars();

        // Load button listeners
        loadButtonListeners();

        // Load bitmaps
        loadBitMaps();

        // DSM main database init
        dsmDb = new DbDsmHelper(getApplicationContext());

        // Hide all fragments
        toggleFragment(empaticaE4Fragment, false);
        toggleFragment(polarH7Fragment, false);
        toggleFragment(affectivaFragment, false);
        toggleFragment(hrvFragment, false);
        toggleRelativeLayout(nbackLayout, false);

        // Init modules
        if(modulesPi) {
            piHelper = new HelperPi(getApplicationContext());
            piHelper.init();
        }
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
        if(modulesHRV) {
            hrvHelper = new HelperHRV(getApplicationContext());
            hrvHelper.init();
        }
        if(modulesNBack) {
            nbackHelper = new HelperNBack(getApplicationContext());
            nbackHelper.init();
        }
        if(modulesAIUI) {

        }

        Log.d("AppState", "Init complete");
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
        if(modulesHRV) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                if(HMIImage.getVisibility() == View.VISIBLE) {
                    HMIImage.setVisibility(View.GONE);
                } else {
                    HMIImage.setImageDrawable(HMIAutonomousModeEngaged);
                    HMIImage.setVisibility(View.VISIBLE);
                }
                return true;
            case KeyEvent.KEYCODE_2:
                HMIImage.setImageDrawable(HMIInfrontCar);
                return true;
            case KeyEvent.KEYCODE_3:
                AnimationDrawable animation = new AnimationDrawable();
                animation.addFrame(HMIRightArrowTurn, 500);
                animation.addFrame(HMIRightArrow, 500);
                animation.addFrame(HMIRightArrowTurn, 500);
                animation.addFrame(HMIRightArrow, 500);
                animation.addFrame(HMIRightArrowTurn, 500);
                animation.addFrame(HMIRightArrow, 500);
                animation.addFrame(HMIRightArrowTurn, 500);
                animation.addFrame(HMIRightArrow, 500);
                animation.addFrame(HMISwerve, 5000);
                animation.addFrame(HMIEmpty, 10000);
                animation.setOneShot(true);
                HMIImage.setImageDrawable(animation);
                animation.start();
                return true;
            default:
                return false;
        }
    }

    private void initVars() {
        // Init preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        modulesEmpaticaE4 = preferences.getBoolean("pref_modules_empaticaE4", false);
        modulesPolarH7 = preferences.getBoolean("pref_modules_polarH7", false);
        modulesAffectiva = preferences.getBoolean("pref_modules_affectiva", false);
        modulesHRV = preferences.getBoolean("pref_hrv_analysis", false);
        modulesNBack = preferences.getBoolean("pref_modules_nback", false);
        modulesPi = preferences.getBoolean("pref_modules_pi", false);
        modulesAIUI = preferences.getBoolean("pref_modules_aiui", false);
        syncData = preferences.getBoolean("pref_sync_data", false);
        displayData = preferences.getBoolean("pref_display_data", false);
        displayCamera = preferences.getBoolean("pref_display_camera", false);
        displayNbackResult = preferences.getBoolean("pref_nback_result", false);
        prefNbackTests = Integer.parseInt(preferences.getString("pref_nback_tests", "1"));
        prefNbackNumbers = Integer.parseInt(preferences.getString("pref_nback_numbers", "1"));

        // Initialize vars that reference UI components
        empaticaE4Fragment = getFragmentManager().findFragmentById(R.id.fragment_empaticae4);
        polarH7Fragment = getFragmentManager().findFragmentById(R.id.fragment_polarh7);
        affectivaFragment = getFragmentManager().findFragmentById(R.id.fragment_affectiva);
        hrvFragment = getFragmentManager().findFragmentById(R.id.fragment_hrv);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        sessionLabel = (TextView) findViewById(R.id.session_label);
        sessionControlButton = (Button) findViewById(R.id.session_control);
        resetButton = (Button) findViewById(R.id.resetButton);
        HMIImage = (ImageView) findViewById(R.id.HMIImage);
        HMIMessage = (TextView) findViewById(R.id.HMI_message);
        HMIProgress = (TextView) findViewById(R.id.HMI_progress);
        nbackLayout = (RelativeLayout) findViewById(R.id.nback);
        nbackLabel = (TextView) findViewById(R.id.nback_label);
        nbackTypeLabel = (TextView) findViewById(R.id.nback_type_label);
        nbackCircle = (ImageView) findViewById(R.id.nback_circle);
        nbackResult = (TextView) findViewById(R.id.nback_result);
        nbackMessageLayout = (RelativeLayout) findViewById(R.id.nback_message_layout);
        nbackMessage = (TextView) findViewById(R.id.nback_message);
        aiuiLayout = (RelativeLayout) findViewById(R.id.aiui);
        aiuiIndicator = (ImageView) findViewById(R.id.aiui_circle);
        aiuiScoreLabel = (TextView) findViewById(R.id.aiui_score_label);
        aiuiBonusLabel = (TextView) findViewById(R.id.aiui_bonus_label);
        aiuiReasonLabel = (TextView) findViewById(R.id.aiui_reason_label);
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
                    if(modulesHRV) {
                        hrvHelper.stopTimer();
                        toggleFragment(hrvFragment, false);
                    }
                    if(modulesPi) {
                        piHelper.disconnect();
                    }
                    if(modulesAIUI) {

                    }
                } else {
                    if(modulesEmpaticaE4 || modulesPolarH7 || modulesAffectiva) {
                        boolean modulesEmpaticaE4Ready, modulesPolarH7Ready, modulesAffectivaReady, modulesHRVReady;

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

                        if(modulesHRV) {
                            modulesHRVReady = modulesHRVConnected;
                            if(Main.displayData && modulesPolarH7Connected) {
                                toggleFragment(hrvFragment, true);
                            }
                        } else {
                            modulesHRVReady = true;
                        }

                        if(modulesEmpaticaE4Ready && modulesPolarH7Ready && modulesAffectivaReady && modulesHRVReady) {
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
                Log.d("Reset", "RESET");

                session_status = false;

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
                if(modulesHRV) {
                    hrvHelper.stopTimer();
                }

                finish();
                startActivity(getIntent());
            }
        });
    }

    private void loadBitMaps() {
        HMIEmpty = getDrawable(R.drawable.hmi_empty);
        HMIAutonomousModeEngaged = getDrawable(R.drawable.hmi_autonomous_mode_engaged);
        HMIInfrontCar = getDrawable(R.drawable.hmi_infront_car);
        HMIRightArrow = getDrawable(R.drawable.hmi_right_arrow);
        HMIRightArrowTurn = getDrawable(R.drawable.hmi_right_arrow_turn);
        HMISwerve = getDrawable(R.drawable.hmi_swerve);
        HMIEmergency = getDrawable(R.drawable.hmi_emergency);
        HMITakeControl = getDrawable(R.drawable.hmi_take_control_red);
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

    public void toggleRelativeLayout(final RelativeLayout layout, final boolean sh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(sh) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void toggleTextView(final TextView view, final boolean sh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(sh) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        });
    }

    public void toggleImageView(final ImageView view, final boolean sh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(sh) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        });
    }
}
