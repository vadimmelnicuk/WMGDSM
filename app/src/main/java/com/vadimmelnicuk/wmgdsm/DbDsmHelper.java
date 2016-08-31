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

    private static final String SQL_CREATE_SESSION_TABLE = "CREATE TABLE " + TABLE_SESSION + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
            COLUMN_SYNCDATA + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_EMPATICAE4 + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_POLARH7 + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_AFFECTIVA + " INTEGER DEFAULT 0" +
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
            COLUMN_FACEORIENTATIONYAW + " TEXT" +
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
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_SYNCDATA + " WHERE session = ?", new String[] {String.valueOf(session)});
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
}
