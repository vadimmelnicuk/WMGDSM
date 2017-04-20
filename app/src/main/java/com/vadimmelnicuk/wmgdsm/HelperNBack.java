package com.vadimmelnicuk.wmgdsm;

import android.animation.Animator;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.os.Handler;
import android.util.Log;
import android.view.ViewAnimationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * Created by vadimmelnicuk on 19/04/2017.
 */

public class HelperNBack extends Main implements RecognitionListener {

    private Context mContext;
    private static Thread mThread;
    private static TimerTask mTimerTask;
    private static Timer mTimer;
    private Random randomGenerator = new Random();
    private Ringtone ringtone;
    private static MediaPlayer mPlayer;
    private long mCounter = 1;
    private static SpeechRecognizer recognizer;

    public static int nbackNumber = 0;
    public static boolean nbackUpdated = false;
    public static int nbackType = 0;
    public static int nbackDelay = 2250; // 2.25s
    public static int nbackTests = 10;

    HelperNBack(Context context) {
        mContext = context;
    }

    public void init() {
        mPlayer = MediaPlayer.create(mContext, R.raw.beep);
        initRecognizer();
        initThread();
        initTimers();
        updateLabel(nbackTypeLabel, nbackType + " back");
        toggleRelativeLayout(nbackLayout, true);

        startTimer();
    }

    private void initThread() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mCounter > nbackTests) {
                    mCounter = 1;
                    stopTimer();
                } else {
                    startListening();
                    playSound();
                    animateCircle();
                    changeNumber();
                    Log.d("NBack", "number: " + nbackNumber + " counter: " + mCounter);
                    mCounter += 1;
                }
            }
        });
    }

    private void initTimers() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mThread.run();
            }
        };
    }

    private void initRecognizer() {
        try {
            Assets assets = new Assets(mContext);
            File assetDir = assets.syncAssets();
            recognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetDir, "en-us-ptm"))
                    .setDictionary(new File(assetDir, "numbers.dict"))
//                    .setRawLogDir(assetDir)  // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                    .getRecognizer();
            recognizer.addListener(this);
            // Create grammar-based search for number recognition
            File numberGrammar = new File(assetDir, "numbers.gram");
            recognizer.addGrammarSearch("numbers", numberGrammar);
        } catch (IOException e) {
            Log.d("Sphinx", e.toString());
        }
    }

    public void startTimer() {
        mTimer.scheduleAtFixedRate(mTimerTask, 0, nbackDelay);
    }

    public void stopTimer() {
        mTimer.cancel();
        mTimer.purge();
        toggleRelativeLayout(nbackLayout, false);
    }

    public void changeNumber() {
        nbackNumber = randomGenerator.nextInt(nbackTests);
        updateLabel(nbackLabel, Integer.toString(nbackNumber));
        nbackUpdated = true;
    }

    public void playSound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPlayer.start();
            }
        });
    }

    public void animateCircle() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int cx = nbackCircle.getWidth() / 2;
                int cy = nbackCircle.getHeight() / 2;
                int radius = (int) Math.hypot(cx, cy);
                Animator anim = ViewAnimationUtils.createCircularReveal(nbackCircle, cx, cy, radius, 0);
                anim.setDuration(nbackDelay);
                anim.start();
            }
        });
    }

    public void startListening() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recognizer.startListening("numbers");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recognizer.stop();
                    }
                }, nbackDelay-250);
            }
        });
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("NBack", "Sphinx Begun");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("NBack", "Sphinx Ended");
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Log.d("NBack", "Sphinx Partial Results: " + text);
            updateLabel(nbackResult, "P");
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Log.d("NBack", "Sphinx Final Results: " + text);
            updateLabel(nbackResult, "F: " + text);
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }
}
