package com.vadimmelnicuk.wmgdsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class DbDsmHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dsm.db";

    public static final String TABLE_SESSION = "sessions";
    public static final String TABLE_SYNCDATA = "syncdata";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SESSION = "session";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SYNCDATA = "syncdata";
    public static final String COLUMN_MODULES_EMPATICAE4 = "empaticaE4";
    public static final String COLUMN_MODULES_POLARH7 = "polarH7";
    public static final String COLUMN_MODULES_AFFECTIVA = "affectiva";
    public static final String COLUMN_MODULES_HRV = "hrv";
    public static final String COLUMN_MODULES_PI = "pi";

    // Empatica E4 data
    public static final String COLUMN_X = "x";
    public static final String COLUMN_Y = "y";
    public static final String COLUMN_Z = "z";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_BVP = "bvp";
    public static final String COLUMN_IBI = "ibi";
    public static final String COLUMN_GSR = "gsr";

    // Polar H7 data
    public static final String COLUMN_BPM = "bpm";
    public static final String COLUMN_RR = "rr";

    // Affectiva data
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_ETHNICITY = "ethnicity";
    public static final String COLUMN_GLASSES = "glasses";

    public static final String COLUMN_ATTENTION = "attention";
    public static final String COLUMN_BROWFURROW = "browFurrow";
    public static final String COLUMN_BROWRAISE = "browRaise";
    public static final String COLUMN_INNERBROWRAISE = "innerBrowRaise";
    public static final String COLUMN_CHEEKRAISE = "cheekRaise";
    public static final String COLUMN_CHINRAISE = "chinRaise";
    public static final String COLUMN_DIMPER = "dimpler";
    public static final String COLUMN_EYECLOSURE = "eyeClosure";
    public static final String COLUMN_EYEWIDEN = "eyeWiden";
    public static final String COLUMN_LIDTIGHTEN = "lidTighten";
    public static final String COLUMN_JAWDROP = "jawDrop";
    public static final String COLUMN_LIPCORNERDEPRESSOR = "lipCornerDepressor";
    public static final String COLUMN_LIPPRESS = "lipPress";
    public static final String COLUMN_LIPPUCKER = "lipPucker";
    public static final String COLUMN_LIPSTRETCH = "lipStretch";
    public static final String COLUMN_LIPSUCK = "lipSuck";
    public static final String COLUMN_UPPERLIPRAISE = "upperLipRaise";
    public static final String COLUMN_MOUTHOPEN = "mouthOpen";
    public static final String COLUMN_NOSEWRINKLE = "noseWrinkle";
    public static final String COLUMN_SMILE = "smile";
    public static final String COLUMN_SMIRK = "smirk";

    public static final String COLUMN_ENGAGEMENT = "engagement";
    public static final String COLUMN_VALENCE = "valence";
    public static final String COLUMN_ANGER = "anger";
    public static final String COLUMN_CONTEMPT = "contempt";
    public static final String COLUMN_DISGUST = "disgust";
    public static final String COLUMN_FEAR = "fear";
    public static final String COLUMN_JOY = "joy";
    public static final String COLUMN_SADNESS = "sadness";
    public static final String COLUMN_SURPRISE = "surprise";

    public static final String COLUMN_FACEDISTANCE = "faceDistance";
    public static final String COLUMN_FACEORIENTATIONPITCH = "faceOrientationPitch";
    public static final String COLUMN_FACEORIENTATIONROLL = "faceOrientationRoll";
    public static final String COLUMN_FACEORIENTATIONYAW = "faceOrientationYaw";

    public static final String COLUMN_RMSSD30 = "rmssd30";
    public static final String COLUMN_RMSSD120 = "rmssd120";

    public static final String COLUMN_SCENARIO_STATE = "scenarioState";
    public static final String COLUMN_HEAD_LIGHT = "headLight";
    public static final String COLUMN_BRAKE_LIGHT = "brakeLight";
    public static final String COLUMN_REVERSE_LIGHT = "reverseLight";
    public static final String COLUMN_FOG_LIGHT = "fogLight";
    public static final String COLUMN_LEFT_INDICATOR = "leftIndicator";
    public static final String COLUMN_RIGHT_INDICATOR = "rightIndicator";
    public static final String COLUMN_HORN_ON = "hornOn";
    public static final String COLUMN_ENGINE_ON = "engineOn";
    public static final String COLUMN_WHEEL_SCREECHING = "wheelScreeching";
    public static final String COLUMN_HI_BEAM = "hiBeam";
    public static final String COLUMN_HAND_BRAKE = "handBrake";
    public static final String COLUMN_POSITION_LATITUDE = "positionLatitude";
    public static final String COLUMN_POSITION_LONGITUDE = "positionLongitude";
    public static final String COLUMN_POSITION_ELEVATION = "positionElevation";
    public static final String COLUMN_POSITION_X = "positionX";
    public static final String COLUMN_POSITION_Y = "positionY";
    public static final String COLUMN_POSITION_Z = "positionZ";
    public static final String COLUMN_ORIENTATION_X = "orientationX";
    public static final String COLUMN_ORIENTATION_Y = "orientationY";
    public static final String COLUMN_ORIENTATION_Z = "orientationZ";
    public static final String COLUMN_VELOCITY_X = "velocityX";
    public static final String COLUMN_VELOCITY_Y = "velocityY";
    public static final String COLUMN_VELOCITY_Z = "velocityZ";
    public static final String COLUMN_ACCELERATION_X = "accelerationX";
    public static final String COLUMN_ACCELERATION_Y = "accelerationY";
    public static final String COLUMN_ACCELERATION_Z = "accelerationZ";
    public static final String COLUMN_ANGULAR_VELOCITY_X = "angularVelocityX";
    public static final String COLUMN_ANGULAR_VELOCITY_Y = "angularVelocityY";
    public static final String COLUMN_ANGULAR_VELOCITY_Z = "angularVelocityZ";
    public static final String COLUMN_STEERING_ANGLE = "steeringAngle";
    public static final String COLUMN_RPM = "rpm";
    public static final String COLUMN_ACCELERATOR_PEDAL = "acceleratorPedal";
    public static final String COLUMN_BRAKE_PEDAL = "brakePedal";
    public static final String COLUMN_CLUTCH_PEDAL = "clutchPedal";
    public static final String COLUMN_STEERING_WHEEL_ANGLE = "steeringWheelAngle";
    public static final String COLUMN_GEAR = "gear";



    private static final String SQL_CREATE_SESSION_TABLE = "CREATE TABLE " + TABLE_SESSION + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
            COLUMN_SYNCDATA + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_EMPATICAE4 + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_POLARH7 + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_AFFECTIVA + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_HRV + " INTEGER DEFAULT 0," +
            COLUMN_MODULES_PI + " INTEGER DEFAULT 0" +
            ");";

    private static final String SQL_CREATE_SYNCDATA_TABLE = "CREATE TABLE " + TABLE_SYNCDATA + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " TEXT NOT NULL, " +
            COLUMN_TIMESTAMP + " TEXT NOT NULL, " +
            // Empatica data
            COLUMN_BVP + " TEXT, " +
            COLUMN_IBI + " TEXT, " +
            COLUMN_GSR + " TEXT, " +
            COLUMN_TEMPERATURE + " TEXT, " +
            COLUMN_X + " TEXT, " +
            COLUMN_Y + " TEXT, " +
            COLUMN_Z + " TEXT, " +
            // Polar H7 data
            COLUMN_BPM + " TEXT, " +
            COLUMN_RR + " TEXT, " +
            // Affectiva data
            COLUMN_GENDER + " TEXT, " +
            COLUMN_AGE + " TEXT, " +
            COLUMN_ETHNICITY + " TEXT, " +
            COLUMN_GLASSES + " TEXT, " +
            COLUMN_ATTENTION + " TEXT, " +
            COLUMN_BROWFURROW + " TEXT, " +
            COLUMN_BROWRAISE + " TEXT, " +
            COLUMN_INNERBROWRAISE + " TEXT, " +
            COLUMN_CHEEKRAISE + " TEXT, " +
            COLUMN_CHINRAISE + " TEXT, " +
            COLUMN_DIMPER + " TEXT, " +
            COLUMN_EYECLOSURE + " TEXT, " +
            COLUMN_EYEWIDEN + " TEXT, " +
            COLUMN_LIDTIGHTEN + " TEXT, " +
            COLUMN_JAWDROP + " TEXT, " +
            COLUMN_LIPCORNERDEPRESSOR + " TEXT, " +
            COLUMN_LIPPRESS + " TEXT, " +
            COLUMN_LIPPUCKER + " TEXT, " +
            COLUMN_LIPSTRETCH + " TEXT, " +
            COLUMN_LIPSUCK + " TEXT, " +
            COLUMN_UPPERLIPRAISE + " TEXT, " +
            COLUMN_MOUTHOPEN + " TEXT, " +
            COLUMN_NOSEWRINKLE + " TEXT, " +
            COLUMN_SMILE + " TEXT, " +
            COLUMN_SMIRK + " TEXT, " +
            COLUMN_ENGAGEMENT + " TEXT, " +
            COLUMN_VALENCE + " TEXT, " +
            COLUMN_ANGER + " TEXT, " +
            COLUMN_CONTEMPT + " TEXT, " +
            COLUMN_DISGUST + " TEXT, " +
            COLUMN_FEAR + " TEXT, " +
            COLUMN_JOY + " TEXT, " +
            COLUMN_SADNESS + " TEXT, " +
            COLUMN_SURPRISE + " TEXT, " +
            COLUMN_FACEDISTANCE + " TEXT, " +
            COLUMN_FACEORIENTATIONPITCH + " TEXT, " +
            COLUMN_FACEORIENTATIONROLL + " TEXT, " +
            COLUMN_FACEORIENTATIONYAW + " TEXT," +
            // HRV Analysis data
            COLUMN_RMSSD30 + " TEXT," +
            COLUMN_RMSSD120 + " TEXT," +
            // Driving performance through HIL
            COLUMN_SCENARIO_STATE + " TEXT," +
            COLUMN_HEAD_LIGHT + " TEXT," +
            COLUMN_BRAKE_LIGHT + " TEXT," +
            COLUMN_REVERSE_LIGHT + " TEXT," +
            COLUMN_FOG_LIGHT + " TEXT," +
            COLUMN_LEFT_INDICATOR + " TEXT," +
            COLUMN_RIGHT_INDICATOR + " TEXT," +
            COLUMN_HORN_ON + " TEXT," +
            COLUMN_ENGINE_ON + " TEXT," +
            COLUMN_WHEEL_SCREECHING + " TEXT," +
            COLUMN_HI_BEAM + " TEXT," +
            COLUMN_HAND_BRAKE + " TEXT," +
            COLUMN_POSITION_LATITUDE + " TEXT," +
            COLUMN_POSITION_LONGITUDE + " TEXT," +
            COLUMN_POSITION_ELEVATION + " TEXT," +
            COLUMN_POSITION_X + " TEXT," +
            COLUMN_POSITION_Y + " TEXT," +
            COLUMN_POSITION_Z + " TEXT," +
            COLUMN_ORIENTATION_X + " TEXT," +
            COLUMN_ORIENTATION_Y + " TEXT," +
            COLUMN_ORIENTATION_Z + " TEXT," +
            COLUMN_VELOCITY_X + " TEXT," +
            COLUMN_VELOCITY_Y + " TEXT," +
            COLUMN_VELOCITY_Z + " TEXT," +
            COLUMN_ACCELERATION_X + " TEXT," +
            COLUMN_ACCELERATION_Y + " TEXT," +
            COLUMN_ACCELERATION_Z + " TEXT," +
            COLUMN_ANGULAR_VELOCITY_X + " TEXT," +
            COLUMN_ANGULAR_VELOCITY_Y + " TEXT," +
            COLUMN_ANGULAR_VELOCITY_Z + " TEXT," +
            COLUMN_STEERING_ANGLE + " TEXT," +
            COLUMN_RPM + " TEXT," +
            COLUMN_ACCELERATOR_PEDAL + " TEXT," +
            COLUMN_BRAKE_PEDAL + " TEXT," +
            COLUMN_CLUTCH_PEDAL + " TEXT," +
            COLUMN_STEERING_WHEEL_ANGLE + " TEXT," +
            COLUMN_GEAR + " TEXT" +
            ");";

    public DbDsmHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SESSION_TABLE);
        db.execSQL(SQL_CREATE_SYNCDATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void dropDb(Context context) {
        Log.w("data", "Dropping Database: " + DATABASE_NAME);
        context.deleteDatabase(DATABASE_NAME);
    }

    public long startSession() {
        SQLiteDatabase db = this.getWritableDatabase();
        long currentTime = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, currentTime);
        values.put(COLUMN_SYNCDATA, Boolean.compare(Main.syncData, false));
        values.put(COLUMN_MODULES_EMPATICAE4, Boolean.compare(Main.modulesEmpaticaE4, false));
        values.put(COLUMN_MODULES_POLARH7, Boolean.compare(Main.modulesPolarH7, false));
        values.put(COLUMN_MODULES_AFFECTIVA, Boolean.compare(Main.modulesAffectiva, false));
        values.put(COLUMN_MODULES_HRV, Boolean.compare(Main.modulesHRV, false));
        values.put(COLUMN_MODULES_PI, Boolean.compare(Main.modulesPi, false));

        long sessionId = db.insert(TABLE_SESSION, null, values);
        long session = getSessionTimestamp(sessionId);
        return session;
    }

    public Cursor getSessionsCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SESSION + " ORDER BY " + COLUMN_ID + " DESC", null);
        return cursor;
    }

    public long getSessionTimestamp(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SESSION + " WHERE _id = ?", new String[] {Long.toString(id)});
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            long session = cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP));
            return session;
        }
        return 0;
    }

    public String timestampToDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("yyyyy-MM-dd HH:mm:ss", cal).toString();
        return date;
    }

    public void insertSyncData(final long session, final double timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);

        // Empatica E4 data insert
        if(Main.empaticaE4Helper.BVPUpdated) {
            values.put(COLUMN_BVP, Main.empaticaE4Helper.BVP);
            Main.empaticaE4Helper.BVPUpdated = false;
        }
        if(Main.empaticaE4Helper.IBIUpdated) {
            values.put(COLUMN_IBI, Main.empaticaE4Helper.IBI);
            Main.empaticaE4Helper.IBIUpdated = false;
        }
        if(Main.empaticaE4Helper.GSRUpdated) {
            values.put(COLUMN_GSR, Main.empaticaE4Helper.GSR);
            Main.empaticaE4Helper.GSRUpdated = false;
        }
        if(Main.empaticaE4Helper.temperatureUpdated) {
            values.put(COLUMN_TEMPERATURE, Main.empaticaE4Helper.temperature);
            Main.empaticaE4Helper.temperatureUpdated = false;
        }
        if(Main.empaticaE4Helper.accelerationUpdated) {
            values.put(COLUMN_X, Main.empaticaE4Helper.accelerationX);
            values.put(COLUMN_Y, Main.empaticaE4Helper.accelerationY);
            values.put(COLUMN_Z, Main.empaticaE4Helper.accelerationZ);
            Main.empaticaE4Helper.accelerationUpdated = false;
        }
        if(Main.polarH7Helper.BPMUpdated) {
            values.put(COLUMN_BPM, Main.polarH7Helper.BPM);
            Main.polarH7Helper.BPMUpdated = false;
        }
        if(Main.polarH7Helper.IBIUpdated) {
            if(Main.polarH7Helper.IBIs.isEmpty()) {
                Main.polarH7Helper.IBIs.clear();
                Main.polarH7Helper.IBIUpdated = false;
            } else {
                final double rr = Main.polarH7Helper.IBIs.get(0);
                values.put(COLUMN_RR, rr);
                Main.polarH7Helper.IBIs.remove(0);
            }
        }
        if(Main.affectivaHelper.faceUpdated) {
            values.put(COLUMN_GENDER, Main.affectivaHelper.face.appearance.getGender().toString());
            values.put(COLUMN_AGE, Main.affectivaHelper.face.appearance.getAge().toString());
            values.put(COLUMN_ETHNICITY, Main.affectivaHelper.face.appearance.getEthnicity().toString());
            values.put(COLUMN_GLASSES, Main.affectivaHelper.face.appearance.getGlasses().toString());
            values.put(COLUMN_ATTENTION, Main.affectivaHelper.face.expressions.getAttention());
            values.put(COLUMN_BROWFURROW, Main.affectivaHelper.face.expressions.getBrowFurrow());
            values.put(COLUMN_BROWRAISE, Main.affectivaHelper.face.expressions.getBrowRaise());
            values.put(COLUMN_INNERBROWRAISE, Main.affectivaHelper.face.expressions.getInnerBrowRaise());
            values.put(COLUMN_CHEEKRAISE, Main.affectivaHelper.face.expressions.getCheekRaise());
            values.put(COLUMN_CHINRAISE, Main.affectivaHelper.face.expressions.getChinRaise());
            values.put(COLUMN_DIMPER, Main.affectivaHelper.face.expressions.getDimpler());
            values.put(COLUMN_EYECLOSURE, Main.affectivaHelper.face.expressions.getEyeClosure());
            values.put(COLUMN_EYEWIDEN, Main.affectivaHelper.face.expressions.getEyeWiden());
            values.put(COLUMN_LIDTIGHTEN, Main.affectivaHelper.face.expressions.getLidTighten());
            values.put(COLUMN_JAWDROP, Main.affectivaHelper.face.expressions.getJawDrop());
            values.put(COLUMN_LIPCORNERDEPRESSOR, Main.affectivaHelper.face.expressions.getLipCornerDepressor());
            values.put(COLUMN_LIPPRESS, Main.affectivaHelper.face.expressions.getLipPress());
            values.put(COLUMN_LIPPUCKER, Main.affectivaHelper.face.expressions.getLipPucker());
            values.put(COLUMN_LIPSTRETCH, Main.affectivaHelper.face.expressions.getLipStretch());
            values.put(COLUMN_LIPSUCK, Main.affectivaHelper.face.expressions.getLipSuck());
            values.put(COLUMN_UPPERLIPRAISE, Main.affectivaHelper.face.expressions.getUpperLipRaise());
            values.put(COLUMN_MOUTHOPEN, Main.affectivaHelper.face.expressions.getMouthOpen());
            values.put(COLUMN_NOSEWRINKLE, Main.affectivaHelper.face.expressions.getNoseWrinkle());
            values.put(COLUMN_SMILE, Main.affectivaHelper.face.expressions.getSmile());
            values.put(COLUMN_SMIRK, Main.affectivaHelper.face.expressions.getSmirk());
            values.put(COLUMN_ENGAGEMENT, Main.affectivaHelper.face.emotions.getEngagement());
            values.put(COLUMN_VALENCE, Main.affectivaHelper.face.emotions.getValence());
            values.put(COLUMN_ANGER, Main.affectivaHelper.face.emotions.getAnger());
            values.put(COLUMN_CONTEMPT, Main.affectivaHelper.face.emotions.getContempt());
            values.put(COLUMN_DISGUST, Main.affectivaHelper.face.emotions.getDisgust());
            values.put(COLUMN_FEAR, Main.affectivaHelper.face.emotions.getFear());
            values.put(COLUMN_JOY, Main.affectivaHelper.face.emotions.getJoy());
            values.put(COLUMN_SADNESS, Main.affectivaHelper.face.emotions.getSadness());
            values.put(COLUMN_SURPRISE, Main.affectivaHelper.face.emotions.getSurprise());
            values.put(COLUMN_FACEDISTANCE, Main.affectivaHelper.face.measurements.getInterocularDistance());
            values.put(COLUMN_FACEORIENTATIONPITCH, Main.affectivaHelper.face.measurements.orientation.getPitch());
            values.put(COLUMN_FACEORIENTATIONROLL, Main.affectivaHelper.face.measurements.orientation.getRoll());
            values.put(COLUMN_FACEORIENTATIONYAW, Main.affectivaHelper.face.measurements.orientation.getYaw());
            Main.affectivaHelper.faceUpdated = false;
        }
        if(Main.hrvHelper.RMSSDUpdated) {
            values.put(COLUMN_RMSSD30, Main.hrvHelper.RMSSD30);
            values.put(COLUMN_RMSSD120, Main.hrvHelper.RMSSD120);
            Main.hrvHelper.RMSSDUpdated = false;
        }
        if(Main.modulesPi) {
            // Put scenario state outside of driving performance update condition so that it is recorded continuously
            values.put(COLUMN_SCENARIO_STATE, Main.piHelper.scenarioState);
        }
        if(Main.piHelper.dpUpdated) {
            values.put(COLUMN_HEAD_LIGHT, Main.piHelper.headLight);
            values.put(COLUMN_BRAKE_LIGHT, Main.piHelper.brakeLight);
            values.put(COLUMN_REVERSE_LIGHT, Main.piHelper.reverseLight);
            values.put(COLUMN_FOG_LIGHT, Main.piHelper.fogLight);
            values.put(COLUMN_LEFT_INDICATOR, Main.piHelper.leftIndicator);
            values.put(COLUMN_RIGHT_INDICATOR, Main.piHelper.rightIndicator);
            values.put(COLUMN_HORN_ON, Main.piHelper.hornOn);
            values.put(COLUMN_ENGINE_ON, Main.piHelper.engineOn);
            values.put(COLUMN_WHEEL_SCREECHING, Main.piHelper.wheelScreeching);
            values.put(COLUMN_HI_BEAM, Main.piHelper.hiBeamOn);
            values.put(COLUMN_HAND_BRAKE, Main.piHelper.handBrake);
            values.put(COLUMN_POSITION_LATITUDE, Main.piHelper.positionLatitude);
            values.put(COLUMN_POSITION_LONGITUDE, Main.piHelper.positionLongitude);
            values.put(COLUMN_POSITION_ELEVATION, Main.piHelper.positionElevation);
            values.put(COLUMN_POSITION_X, Main.piHelper.positionX);
            values.put(COLUMN_POSITION_Y, Main.piHelper.positionY);
            values.put(COLUMN_POSITION_Z, Main.piHelper.positionZ);
            values.put(COLUMN_ORIENTATION_X, Main.piHelper.orientationX);
            values.put(COLUMN_ORIENTATION_Y, Main.piHelper.orientationY);
            values.put(COLUMN_ORIENTATION_Z, Main.piHelper.orientationZ);
            values.put(COLUMN_VELOCITY_X, Main.piHelper.velocityX);
            values.put(COLUMN_VELOCITY_Y, Main.piHelper.velocityY);
            values.put(COLUMN_VELOCITY_Z, Main.piHelper.velocityZ);
            values.put(COLUMN_ACCELERATION_X, Main.piHelper.accelerationX);
            values.put(COLUMN_ACCELERATION_Y, Main.piHelper.accelerationY);
            values.put(COLUMN_ACCELERATION_Z, Main.piHelper.accelerationZ);
            values.put(COLUMN_ANGULAR_VELOCITY_X, Main.piHelper.angularVelocityX);
            values.put(COLUMN_ANGULAR_VELOCITY_Y, Main.piHelper.angularVelocityY);
            values.put(COLUMN_ANGULAR_VELOCITY_Z, Main.piHelper.angularVelocityZ);
            values.put(COLUMN_STEERING_ANGLE, Main.piHelper.steeringAngle);
            values.put(COLUMN_RPM, Main.piHelper.rpm);
            values.put(COLUMN_ACCELERATOR_PEDAL, Main.piHelper.acceleratorPedal);
            values.put(COLUMN_BRAKE_PEDAL, Main.piHelper.brakePedal);
            values.put(COLUMN_CLUTCH_PEDAL, Main.piHelper.clutchPedal);
            values.put(COLUMN_STEERING_WHEEL_ANGLE, Main.piHelper.steeringWheelAngle);
            values.put(COLUMN_GEAR, Main.piHelper.gear);
            Main.piHelper.dpUpdated = false;
        }

        db.insert(TABLE_SYNCDATA, null, values);
    }

    public void exportSyncData(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_SYNCDATA);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "data.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_SYNCDATA + " WHERE " + COLUMN_SESSION + " = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {
                        Integer.toString(curCSV.getInt(0)),
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5),
                        curCSV.getString(6),
                        curCSV.getString(7),
                        curCSV.getString(8),
                        curCSV.getString(9),
                        curCSV.getString(10),
                        curCSV.getString(11),
                        curCSV.getString(12),
                        curCSV.getString(13),
                        curCSV.getString(14),
                        curCSV.getString(15),
                        curCSV.getString(16),
                        curCSV.getString(17),
                        curCSV.getString(18),
                        curCSV.getString(19),
                        curCSV.getString(20),
                        curCSV.getString(21),
                        curCSV.getString(22),
                        curCSV.getString(23),
                        curCSV.getString(24),
                        curCSV.getString(25),
                        curCSV.getString(26),
                        curCSV.getString(27),
                        curCSV.getString(28),
                        curCSV.getString(29),
                        curCSV.getString(30),
                        curCSV.getString(31),
                        curCSV.getString(32),
                        curCSV.getString(33),
                        curCSV.getString(34),
                        curCSV.getString(35),
                        curCSV.getString(36),
                        curCSV.getString(37),
                        curCSV.getString(38),
                        curCSV.getString(39),
                        curCSV.getString(40),
                        curCSV.getString(41),
                        curCSV.getString(42),
                        curCSV.getString(43),
                        curCSV.getString(44),
                        curCSV.getString(45),
                        curCSV.getString(46),
                        curCSV.getString(47),
                        curCSV.getString(48),
                        curCSV.getString(49),
                        curCSV.getString(50),   // RMSSD30
                        curCSV.getString(51),   // RMSSD120
                        curCSV.getString(52),   // COLUMN_SCENARIO_STATE
                        curCSV.getString(53),
                        curCSV.getString(54),
                        curCSV.getString(55),
                        curCSV.getString(56),
                        curCSV.getString(57),
                        curCSV.getString(58),
                        curCSV.getString(59),
                        curCSV.getString(60),
                        curCSV.getString(61),
                        curCSV.getString(62),
                        curCSV.getString(63),
                        curCSV.getString(64),
                        curCSV.getString(65),
                        curCSV.getString(66),
                        curCSV.getString(67),
                        curCSV.getString(68),
                        curCSV.getString(69),
                        curCSV.getString(70),
                        curCSV.getString(71),
                        curCSV.getString(72),
                        curCSV.getString(73),
                        curCSV.getString(74),
                        curCSV.getString(75),
                        curCSV.getString(76),
                        curCSV.getString(77),
                        curCSV.getString(78),
                        curCSV.getString(79),
                        curCSV.getString(80),
                        curCSV.getString(81),
                        curCSV.getString(82),
                        curCSV.getString(83),
                        curCSV.getString(84),
                        curCSV.getString(85),
                        curCSV.getString(86),
                        curCSV.getString(87),
                        curCSV.getString(88)    // COLUMN_GEAR
                };
                csvWrite.writeNext(arrStr);

            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public ArrayList<Long> getRRSample(final long session, final long timestamp, final long sampleSize) {
        ArrayList<Long> rrs = new ArrayList<>();
        long timestampDiff = timestamp - (1000*sampleSize);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_SYNCDATA + " WHERE " + COLUMN_SESSION + " = ? AND " + COLUMN_TIMESTAMP + " > ? AND " + COLUMN_RR + " > 0", new String[] {String.valueOf(session), String.valueOf(timestampDiff)});
        if(cur.getCount() > 0) {
            while(cur.moveToNext()) {
                rrs.add(cur.getLong(11));
            }
        }
        cur.close();
        Log.d("HRV", "RRs Array: " + rrs.toString());
        return rrs;
    }
}
