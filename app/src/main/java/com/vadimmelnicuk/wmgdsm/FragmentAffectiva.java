package com.vadimmelnicuk.wmgdsm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class FragmentAffectiva extends Fragment {

    public static TextView DeviceNameLabel;

    public static TextView AttentionLabel;
    public static TextView BrowFurrowLabel;
    public static TextView BrowRaiseLabel;
    public static TextView InnerBrowRaiseLabel;
    public static TextView CheekRaiseLabel;
    public static TextView ChinRaiseLabel;
    public static TextView DimplerLabel;
    public static TextView EyeClosureLabel;
    public static TextView EyeWidenLabel;
    public static TextView LidTightenLabel;
    public static TextView JawDropLabel;
    public static TextView LipCornerDepressorLabel;
    public static TextView LipPressLabel;
    public static TextView LipPuckerLabel;
    public static TextView LipStretchLabel;
    public static TextView LipSuckLabel;
    public static TextView UpperLipRaiseLabel;
    public static TextView MouthOpenLabel;
    public static TextView NoseWrinkleLabel;
    public static TextView SmileLabel;
    public static TextView SmirkLabel;

    public static TextView EngagementLabel;
    public static TextView ValenceLabel;
    public static TextView AngerLabel;
    public static TextView ContemptLabel;
    public static TextView DisgustLabel;
    public static TextView FearLabel;
    public static TextView JoyLabel;
    public static TextView SadnessLabel;
    public static TextView SurpriseLabel;

    public static TextView FaceDistanceLabel;
    public static TextView FaceOrientationPitchLabel;
    public static TextView FaceOrientationRollLabel;
    public static TextView FaceOrientationYawLabel;

    public static TextView GenderLabel;
    public static TextView AgeLabel;
    public static TextView EthnicityLabel;
    public static TextView GlassesLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_affectiva, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeviceNameLabel = (TextView) getView().findViewById(R.id.device_name);

        AttentionLabel = (TextView) getView().findViewById(R.id.affectiva_attention_label);
        BrowFurrowLabel = (TextView) getView().findViewById(R.id.affectiva_brow_furrow_label);
        BrowRaiseLabel = (TextView) getView().findViewById(R.id.affectiva_brow_raise_label);
        InnerBrowRaiseLabel = (TextView) getView().findViewById(R.id.affectiva_inner_brow_raise_label);
        CheekRaiseLabel = (TextView) getView().findViewById(R.id.affectiva_cheek_raise_label);
        ChinRaiseLabel = (TextView) getView().findViewById(R.id.affectiva_chin_raise_label);
        DimplerLabel = (TextView) getView().findViewById(R.id.affectiva_dimpler_label);
        EyeClosureLabel = (TextView) getView().findViewById(R.id.affectiva_eye_closure_label);
        EyeWidenLabel = (TextView) getView().findViewById(R.id.affectiva_eye_widen_label);
        LidTightenLabel = (TextView) getView().findViewById(R.id.affectiva_lid_tighten_label);
        JawDropLabel = (TextView) getView().findViewById(R.id.affectiva_jaw_drop_label);
        LipCornerDepressorLabel = (TextView) getView().findViewById(R.id.affectiva_lip_corner_depressor_label);
        LipPressLabel = (TextView) getView().findViewById(R.id.affectiva_lip_press_label);
        LipPuckerLabel = (TextView) getView().findViewById(R.id.affectiva_lip_pucker_label);
        LipStretchLabel = (TextView) getView().findViewById(R.id.affectiva_lip_stretch_label);
        LipSuckLabel = (TextView) getView().findViewById(R.id.affectiva_lip_suck_label);
        UpperLipRaiseLabel = (TextView) getView().findViewById(R.id.affectiva_upper_lip_raise_label);
        MouthOpenLabel = (TextView) getView().findViewById(R.id.affectiva_mouth_open_label);
        NoseWrinkleLabel = (TextView) getView().findViewById(R.id.affectiva_nose_wrinkle_label);
        SmileLabel = (TextView) getView().findViewById(R.id.affectiva_smile_label);
        SmirkLabel = (TextView) getView().findViewById(R.id.affectiva_smirk_label);

        EngagementLabel = (TextView) getView().findViewById(R.id.affectiva_engagement_label);
        ValenceLabel = (TextView) getView().findViewById(R.id.affectiva_valence_label);
        AngerLabel = (TextView) getView().findViewById(R.id.affectiva_anger_label);
        ContemptLabel = (TextView) getView().findViewById(R.id.affectiva_contempt_label);
        DisgustLabel = (TextView) getView().findViewById(R.id.affectiva_disgust_label);
        FearLabel = (TextView) getView().findViewById(R.id.affectiva_fear_label);
        JoyLabel = (TextView) getView().findViewById(R.id.affectiva_joy_label);
        SadnessLabel = (TextView) getView().findViewById(R.id.affectiva_sadness_label);
        SurpriseLabel = (TextView) getView().findViewById(R.id.affectiva_suprise_label);

        FaceDistanceLabel = (TextView) getView().findViewById(R.id.affectiva_face_distance_label);
        FaceOrientationPitchLabel = (TextView) getView().findViewById(R.id.affectiva_face_orientation_pitch_label);
        FaceOrientationRollLabel = (TextView) getView().findViewById(R.id.affectiva_face_orientation_roll_label);
        FaceOrientationYawLabel = (TextView) getView().findViewById(R.id.affectiva_face_orientation_yaw_label);

        GenderLabel = (TextView) getView().findViewById(R.id.affectiva_gender_label);
        AgeLabel = (TextView) getView().findViewById(R.id.affectiva_age_label);
        EthnicityLabel = (TextView) getView().findViewById(R.id.affectiva_ethnicity_label);
        GlassesLabel = (TextView) getView().findViewById(R.id.affectiva_glasses_label);
    }
}
