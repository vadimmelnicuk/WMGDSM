package com.vadimmelnicuk.wmgdsm;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
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

    private boolean DEBUG = false;
    private Context mContext;
    private static Thread mThread;
    private static TimerTask mTimerTask;
    private static Timer mTimer;
    private Random randomGenerator = new Random();
    private static MediaPlayer mPlayerBeep;
    private static MediaPlayer mPlayerBeep10;
    private static MediaPlayer mPlayerTakeControl10;
    private static SpeechRecognizer recognizer;
    private int mTransitionTime = 10000; // 10s
    private int mMessageDisplayTime = 7000; // 7s
    private int mTestDelay = 7000; // 7s
    private int mNumberDelay = 2250; // 2.25s
    private int mNumberCounter = 1;
    private int mTestsCounter = 1;
    private int mHMIProgressCounter = 5;

    public static boolean nbackUpdated = false;
    public static boolean nbackRunning = false;
    public static int nbackLevel = 0;   // Type of n-back i.e., zero, one, or two n-back
    public static int nbackNumber = 0;
    public static int nbackNextNumber = 0;
    public static int nbackScore = 0;
    public static int nbackProb = 0;
    public static String nbackResponse;

    HelperNBack(Context context) {
        mContext = context;
    }

    public void init() {
        mPlayerBeep = MediaPlayer.create(mContext, R.raw.beep);
        mPlayerBeep10 = MediaPlayer.create(mContext, R.raw.beep10);
        mPlayerTakeControl10 = MediaPlayer.create(mContext, R.raw.takecontrol10);
        initRecognizer();
        initThread();
        changeNumber();
    }

    private void initThread() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mNumberCounter > prefNbackNumbers) {
                    mNumberCounter = 1;
                    stopTimer();

                    if(mTestsCounter >= prefNbackTests) {
                        mTestsCounter = 1;
                        nbackRunning = false;
                        // TODO implement transition to manual here + HMI
                        if(piHelper.scenarioState == 1) {
                            showWarning();

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    piHelper.sendManualControlMessage();
                                }
                            }, mTransitionTime);
                        }

                        toggleRelativeLayout(nbackLayout, false);
                    } else {
                        mTestsCounter += 1;
                        showMessage();

                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startTimer();
                            }
                        }, mTestDelay);
                    }
                } else {
                    mNumberCounter += 1;

                    nbackNumber = nbackNextNumber;
                    updateLabel(nbackLabel, Integer.toString(nbackNumber));
                    updateLabel(nbackResult, "N");
                    changeNumber();

                    startListening();
                    playSoundBeep();
                    if(mNumberCounter-1 >= nbackLevel) {
                        toggleImageView(nbackCircle, true);
                        animateCircle(mNumberDelay);
                    } else {
                        toggleImageView(nbackCircle, false);
                    }
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
            Log.d("NBack", e.toString());
        }
    }

    public void run(int level) {
        nbackRunning = true;
        nbackLevel = level;

        updateLabel(nbackTypeLabel, "N-Back " + nbackLevel);
        if(Main.displayNbackResult == false) {
            toggleTextView(nbackResult, false);
        }
        toggleRelativeLayout(nbackLayout, true);
        showMessage();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTimer();
            }
        }, mMessageDisplayTime);
    }

    public void showMessage() {
        updateLabel(nbackMessage, "Prepare for\nN-Back " + nbackLevel);
        toggleRelativeLayout(nbackMessageLayout, true);
        toggleImageView(nbackCircle, true);
        animateCircle(mMessageDisplayTime);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPlayerBeep10.start();
            }
        });
    }

    public void showWarning() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable animation = new AnimationDrawable();
                animation.addFrame(HMITakeControl, 10000);
                animation.setOneShot(true);
                HMIImage.setImageDrawable(animation);
                HMIMessage.setText("Take control");
                mPlayerTakeControl10.start();
                animation.start();
            }
        });
        toggleTextView(HMIMessage, true);
        toggleImageView(HMIImage, true);
        animateHMIProgress();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleTextView(HMIMessage, false);
                toggleTextView(HMIProgress, false);
                toggleImageView(HMIImage, false);
            }
        }, mTransitionTime);
    }

    public void startTimer() {
        initTimers();
        mTimer.scheduleAtFixedRate(mTimerTask, 0, mNumberDelay);
        toggleRelativeLayout(nbackMessageLayout, false);
    }

    public void stopTimer() {
        mTimer.cancel();
        mTimer.purge();
    }

    public void changeNumber() {
        nbackNextNumber = randomGenerator.nextInt(10);
    }

    public void playSoundBeep() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPlayerBeep.start();
            }
        });
    }

    public void animateCircle(final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int cx = nbackCircle.getWidth() / 2;
                int cy = nbackCircle.getHeight() / 2;
                int radius = (int) Math.hypot(cx, cy);
                Animator anim = ViewAnimationUtils.createCircularReveal(nbackCircle, cx, cy, radius, 0);
                anim.setDuration(duration);
                anim.start();
            }
        });
    }

    public void animateHMIProgress() {
        mHMIProgressCounter = 5;
        final Timer timer = new Timer();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateLabel(HMIProgress, String.valueOf(mHMIProgressCounter));
                mHMIProgressCounter -= 1;
            }
        });
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                thread.run();
            }
        };


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleTextView(HMIProgress, true);
                timer.scheduleAtFixedRate(task, 0, 1000);
            }
        }, mTransitionTime/2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.cancel();
                timer.purge();
            }
        }, mTransitionTime);
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
                }, mNumberDelay - 250);
            }
        });
    }

    @Override
    public void onBeginningOfSpeech() {
        if(DEBUG) {
            Log.d("NBack", "Speech Begun");
        }
    }

    @Override
    public void onEndOfSpeech() {
        if(DEBUG) {
            Log.d("NBack", "Speech Ended");
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            nbackResponse = hypothesis.getHypstr();
            nbackScore = hypothesis.getBestScore();
            nbackProb = hypothesis.getProb();
            if(DEBUG) {
                Log.d("NBack", "Partial Results: " + nbackResponse + " " + nbackScore + " " + nbackProb);
            }
            updateLabel(nbackResult, "P");
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            nbackResponse = hypothesis.getHypstr();
            nbackScore = hypothesis.getBestScore();
            nbackProb = hypothesis.getProb();
            nbackUpdated = true;
            if(DEBUG) {
                Log.d("NBack", "Final Result: " + nbackResponse + " " + nbackScore + " " + nbackProb);
            }
            updateLabel(nbackResult, "F: " + nbackResponse);
        }
    }

    @Override
    public void onError(Exception e) {
        if(DEBUG) {
            Log.d("NBack", "Error " + e.getMessage());
        }
    }

    @Override
    public void onTimeout() {

    }
}
