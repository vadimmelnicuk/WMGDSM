package com.vadimmelnicuk.wmgdsm;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
    private int previewWidth = 0;
    private int previewHeight = 0;

    HelperAffectiva(Context context) {
        mContext = context;
    }

    public void init() {
        // Database init

        // Helper init
        cameraPreview = Main.cameraView;

        detector = new CameraDetector(mContext, CameraDetector.CameraType.CAMERA_FRONT, cameraPreview, 1, Detector.FaceDetectorMode.LARGE_FACES);
        detector.setLicensePath("Affdex.license");
        detector.setDetectSmile(true);
        detector.setImageListener(this);
        detector.setOnCameraEventListener(this);
    }

    @Override
    public void onImageResults(List<Face> list, Frame frame, float v) {
        if (list == null) {
            Log.i("Affectiva", "list is empty");
            return;
        }
        if (list.size() == 0) {
            updateLabel(FragmentAffectiva.SmileLabel, "No Face");
        } else {
            Face face = list.get(0);
            updateLabel(FragmentAffectiva.SmileLabel, "Smile: " + face.expressions.getSmile());
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
