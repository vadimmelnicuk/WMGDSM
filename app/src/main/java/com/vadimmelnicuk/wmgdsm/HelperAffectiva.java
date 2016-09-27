package com.vadimmelnicuk.wmgdsm;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class HelperAffectiva extends Main implements Detector.ImageListener, CameraDetector.CameraEventListener {

    private Context mContext;
    public static CameraDetector detector;
    private SurfaceView cameraPreview;
    private int previewWidth = 50;
    private int previewHeight = 60;
    private int frameRate = 12;

    public static boolean faceUpdated;
    public static Face face;

    HelperAffectiva(Context context) {
        mContext = context;
    }

    public void init() {
        // Database init
        affectivaDb = new DbAffectivaHelper(mContext);

        // Helper init
        cameraPreview = Main.cameraView;

        detector = new CameraDetector(mContext, CameraDetector.CameraType.CAMERA_FRONT, cameraPreview, 1, Detector.FaceDetectorMode.LARGE_FACES);
        detector.setLicensePath("Affdex.license");

        detector.setDetectAllAppearances(true);
        detector.setDetectAllExpressions(true);
        detector.setDetectAllEmotions(true);
        detector.setDetectAllEmojis(false);

        detector.setMaxProcessRate(frameRate);
        detector.setImageListener(this);
        detector.setOnCameraEventListener(this);
    }

    public void fragmentOn() {
        toggleFragment(affectivaFragment, true);
    }

    public void cameraOn() {
        ViewGroup.LayoutParams params = Main.cameraView.getLayoutParams();
        params.width = previewWidth;
        params.height = previewHeight;
        Main.cameraView.setLayoutParams(params);
        Main.cameraView.setAlpha(1);
    }

    public void cameraOff() {
        ViewGroup.LayoutParams params = Main.cameraView.getLayoutParams();
        params.width = 1;
        params.height = 1;
        Main.cameraView.setLayoutParams(params);
        Main.cameraView.setAlpha(0);
    }

    @Override
    public void onImageResults(List<Face> list, Frame frame, float timestamp) {
        if (list == null) {
            if(Main.displayData) {
                updateLabel(FragmentAffectiva.DeviceNameLabel, "Affectiva - No frame present");
            }
            return;
        }
        if (list.size() == 0) {
            if(Main.displayData) {
                updateLabel(FragmentAffectiva.DeviceNameLabel, "Affectiva - No face detected");
            }
        } else {
            face = list.get(0);

            if(Main.session_status) {
                if(Main.syncData) {
                    faceUpdated = true;
                } else {
                    Main.affectivaDb.insertData(Main.session_timestamp, timestamp, face);
                }
            }

            if(Main.displayData) {
                updateLabel(FragmentAffectiva.DeviceNameLabel, "Affectiva - Face was detected");
                //Expressions
                updateLabel(FragmentAffectiva.AttentionLabel, "Attention: " + String.format("%.02f", face.expressions.getAttention()));
                updateLabel(FragmentAffectiva.BrowFurrowLabel, "Brow furrow: " + String.format("%.02f", face.expressions.getBrowFurrow()));
                updateLabel(FragmentAffectiva.BrowRaiseLabel, "Brow raise: " + String.format("%.02f", face.expressions.getBrowRaise()));
                updateLabel(FragmentAffectiva.InnerBrowRaiseLabel, "Inner brow raise: " + String.format("%.02f", face.expressions.getInnerBrowRaise()));
                updateLabel(FragmentAffectiva.CheekRaiseLabel, "Cheek raise: " + String.format("%.02f", face.expressions.getCheekRaise()));
                updateLabel(FragmentAffectiva.ChinRaiseLabel, "Chin raise: " + String.format("%.02f", face.expressions.getChinRaise()));
                updateLabel(FragmentAffectiva.DimplerLabel, "Dimpler: " + String.format("%.02f", face.expressions.getDimpler()));
                updateLabel(FragmentAffectiva.EyeClosureLabel, "Eye closure: " + String.format("%.02f", face.expressions.getEyeClosure()));
                updateLabel(FragmentAffectiva.EyeWidenLabel, "Eye widen: " + String.format("%.02f", face.expressions.getEyeWiden()));
                updateLabel(FragmentAffectiva.LidTightenLabel, "Lid tighten: " + String.format("%.02f", face.expressions.getLidTighten()));
                updateLabel(FragmentAffectiva.JawDropLabel, "Jaw drop: " + String.format("%.02f", face.expressions.getJawDrop()));
                updateLabel(FragmentAffectiva.LipCornerDepressorLabel, "Lip corner depressor: " + String.format("%.02f", face.expressions.getLipCornerDepressor()));
                updateLabel(FragmentAffectiva.LipPressLabel, "Lip press: " + String.format("%.02f", face.expressions.getLipPress()));
                updateLabel(FragmentAffectiva.LipPuckerLabel, "Lip pucker: " + String.format("%.02f", face.expressions.getLipPucker()));
                updateLabel(FragmentAffectiva.LipStretchLabel, "Lip stretch: " + String.format("%.02f", face.expressions.getLipStretch()));
                updateLabel(FragmentAffectiva.LipSuckLabel, "Lip suck: " + String.format("%.02f", face.expressions.getLipSuck()));
                updateLabel(FragmentAffectiva.UpperLipRaiseLabel, "Upper lip raise: " + String.format("%.02f", face.expressions.getUpperLipRaise()));
                updateLabel(FragmentAffectiva.MouthOpenLabel, "Mouth open: " + String.format("%.02f", face.expressions.getMouthOpen()));
                updateLabel(FragmentAffectiva.NoseWrinkleLabel, "Nose wrinkle: " + String.format("%.02f", face.expressions.getNoseWrinkle()));
                updateLabel(FragmentAffectiva.SmileLabel, "Smile: " + String.format("%.02f", face.expressions.getSmile()));
                updateLabel(FragmentAffectiva.SmirkLabel, "Smirk: " + String.format("%.02f", face.expressions.getSmirk()));

                //Emotions
                updateLabel(FragmentAffectiva.EngagementLabel, "Engagement: " + String.format("%.02f", face.emotions.getEngagement()));
                updateLabel(FragmentAffectiva.ValenceLabel, "Valence: " + String.format("%.02f", face.emotions.getValence()));
                updateLabel(FragmentAffectiva.AngerLabel, "Anger: " + String.format("%.02f", face.emotions.getAnger()));
                updateLabel(FragmentAffectiva.ContemptLabel, "Contempt: " + String.format("%.02f", face.emotions.getContempt()));
                updateLabel(FragmentAffectiva.DisgustLabel, "Disgust: " + String.format("%.02f", face.emotions.getDisgust()));
                updateLabel(FragmentAffectiva.FearLabel, "Fear: " + String.format("%.02f", face.emotions.getFear()));
                updateLabel(FragmentAffectiva.JoyLabel, "Joy: " + String.format("%.02f", face.emotions.getJoy()));
                updateLabel(FragmentAffectiva.SadnessLabel, "Sadness: " + String.format("%.02f", face.emotions.getSadness()));
                updateLabel(FragmentAffectiva.SurpriseLabel, "Surprise: " + String.format("%.02f", face.emotions.getSurprise()));

                //Face tracking
                updateLabel(FragmentAffectiva.FaceDistanceLabel, "Distance: " + String.format("%.02f", face.measurements.getInterocularDistance()));
                updateLabel(FragmentAffectiva.FaceOrientationPitchLabel, "Pitch: " + String.format("%.02f", face.measurements.orientation.getPitch()));
                updateLabel(FragmentAffectiva.FaceOrientationRollLabel, "Roll: " + String.format("%.02f", face.measurements.orientation.getRoll()));
                updateLabel(FragmentAffectiva.FaceOrientationYawLabel, "Yaw: " + String.format("%.02f", face.measurements.orientation.getYaw()));

                //Demographics and other
                updateLabel(FragmentAffectiva.GenderLabel, "Gender: " + face.appearance.getGender());
                updateLabel(FragmentAffectiva.AgeLabel, "Age: " + face.appearance.getAge());
                updateLabel(FragmentAffectiva.EthnicityLabel, "Ethnicity: " + face.appearance.getEthnicity());
                updateLabel(FragmentAffectiva.GlassesLabel, "Glasses: " + face.appearance.getGlasses());
            }
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onCameraSizeSelected(int width, int height, Frame.ROTATE rotate) {
        if (rotate == Frame.ROTATE.BY_90_CCW || rotate == Frame.ROTATE.BY_90_CW) {
            previewWidth = height;
            previewHeight = width;
        } else {
            previewHeight = height;
            previewWidth = width;
        }
        cameraPreview.requestLayout();
    }
}
