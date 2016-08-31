package com.vadimmelnicuk.wmgdsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.affectiva.android.affdex.sdk.detector.Face;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by vadimmelnicuk on 17/08/16.
 */
public class DbAffectivaHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "affectiva.db";

    public static final String TABLE_AFFECTIVA = "affectiva";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SESSION = "session";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_ETHNICITY = "ethnicity";
    public static final String COLUMN_GLASSES = "glasses";

    public static final String COLUMN_ATTENTION = "attention";
    public static final String COLUMN_BROWFURROW = "browfurrow";
    public static final String COLUMN_BROWRAISE = "browraise";
    public static final String COLUMN_INNERBROWRAISE = "innerbrowraise";
    public static final String COLUMN_CHEEKRAISE = "cheekraise";
    public static final String COLUMN_CHINRAISE = "chinraise";
    public static final String COLUMN_DIMPER = "dimpler";
    public static final String COLUMN_EYECLOSURE = "eyeclosure";
    public static final String COLUMN_EYEWIDEN = "eyewiden";
    public static final String COLUMN_LIDTIGHTEN = "lidtighten";
    public static final String COLUMN_JAWDROP = "jawdrop";
    public static final String COLUMN_LIPCORNERDEPRESSOR = "lipcornerdepressor";
    public static final String COLUMN_LIPPRESS = "lippress";
    public static final String COLUMN_LIPPUCKER = "lippucker";
    public static final String COLUMN_LIPSTRETCH = "lipstretch";
    public static final String COLUMN_LIPSUCK = "lipsuck";
    public static final String COLUMN_UPPERLIPRAISE = "UpperLipRaise";
    public static final String COLUMN_MOUTHOPEN = "mouthopen";
    public static final String COLUMN_NOSEWRINKLE = "nosewrinkle";
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

    public static final String COLUMN_FACEDISTANCE = "facedistance";
    public static final String COLUMN_FACEORIENTATIONPITCH = "faceorientationpitch";
    public static final String COLUMN_FACEORIENTATIONROLL = "faceorientationroll";
    public static final String COLUMN_FACEORIENTATIONYAW = "faceorientationyaw";

    private static final String SQL_CREATE_AFFECTIVA_TABLE = "CREATE TABLE " + TABLE_AFFECTIVA + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " REAL NOT NULL, " +
            COLUMN_GENDER + " TEXT NOT NULL, " +
            COLUMN_AGE + " TEXT NOT NULL, " +
            COLUMN_ETHNICITY + " TEXT NOT NULL, " +
            COLUMN_GLASSES + " TEXT NOT NULL, " +
            COLUMN_ATTENTION + " REAL NOT NULL, " +
            COLUMN_BROWFURROW + " REAL NOT NULL, " +
            COLUMN_BROWRAISE + " REAL NOT NULL, " +
            COLUMN_INNERBROWRAISE + " REAL NOT NULL, " +
            COLUMN_CHEEKRAISE + " REAL NOT NULL, " +
            COLUMN_CHINRAISE + " REAL NOT NULL, " +
            COLUMN_DIMPER + " REAL NOT NULL, " +
            COLUMN_EYECLOSURE + " REAL NOT NULL, " +
            COLUMN_EYEWIDEN + " REAL NOT NULL, " +
            COLUMN_LIDTIGHTEN + " REAL NOT NULL, " +
            COLUMN_JAWDROP + " REAL NOT NULL, " +
            COLUMN_LIPCORNERDEPRESSOR + " REAL NOT NULL, " +
            COLUMN_LIPPRESS + " REAL NOT NULL, " +
            COLUMN_LIPPUCKER + " REAL NOT NULL, " +
            COLUMN_LIPSTRETCH + " REAL NOT NULL, " +
            COLUMN_LIPSUCK + " REAL NOT NULL, " +
            COLUMN_UPPERLIPRAISE + " REAL NOT NULL, " +
            COLUMN_MOUTHOPEN + " REAL NOT NULL, " +
            COLUMN_NOSEWRINKLE + " REAL NOT NULL, " +
            COLUMN_SMILE + " REAL NOT NULL, " +
            COLUMN_SMIRK + " REAL NOT NULL, " +
            COLUMN_ENGAGEMENT + " REAL NOT NULL, " +
            COLUMN_VALENCE + " REAL NOT NULL, " +
            COLUMN_ANGER + " REAL NOT NULL, " +
            COLUMN_CONTEMPT + " REAL NOT NULL, " +
            COLUMN_DISGUST + " REAL NOT NULL, " +
            COLUMN_FEAR + " REAL NOT NULL, " +
            COLUMN_JOY + " REAL NOT NULL, " +
            COLUMN_SADNESS + " REAL NOT NULL, " +
            COLUMN_SURPRISE + " REAL NOT NULL, " +
            COLUMN_FACEDISTANCE + " REAL NOT NULL, " +
            COLUMN_FACEORIENTATIONPITCH + " REAL NOT NULL, " +
            COLUMN_FACEORIENTATIONROLL + " REAL NOT NULL, " +
            COLUMN_FACEORIENTATIONYAW + " REAL NOT NULL" +
            ");";

    public DbAffectivaHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_AFFECTIVA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void dropDb(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public void insertData(final long session, final float timestamp, Face face) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);

        values.put(COLUMN_GENDER, face.appearance.getGender().toString());
        values.put(COLUMN_AGE, face.appearance.getAge().toString());
        values.put(COLUMN_ETHNICITY, face.appearance.getEthnicity().toString());
        values.put(COLUMN_GLASSES, face.appearance.getGlasses().toString());

        values.put(COLUMN_ATTENTION, face.expressions.getAttention());
        values.put(COLUMN_BROWFURROW, face.expressions.getBrowFurrow());
        values.put(COLUMN_BROWRAISE, face.expressions.getBrowRaise());
        values.put(COLUMN_INNERBROWRAISE, face.expressions.getInnerBrowRaise());
        values.put(COLUMN_CHEEKRAISE, face.expressions.getCheekRaise());
        values.put(COLUMN_CHINRAISE, face.expressions.getChinRaise());
        values.put(COLUMN_DIMPER, face.expressions.getDimpler());
        values.put(COLUMN_EYECLOSURE, face.expressions.getEyeClosure());
        values.put(COLUMN_EYEWIDEN, face.expressions.getEyeWiden());
        values.put(COLUMN_LIDTIGHTEN, face.expressions.getLidTighten());
        values.put(COLUMN_JAWDROP, face.expressions.getJawDrop());
        values.put(COLUMN_LIPCORNERDEPRESSOR, face.expressions.getLipCornerDepressor());
        values.put(COLUMN_LIPPRESS, face.expressions.getLipPress());
        values.put(COLUMN_LIPPUCKER, face.expressions.getLipPucker());
        values.put(COLUMN_LIPSTRETCH, face.expressions.getLipStretch());
        values.put(COLUMN_LIPSUCK, face.expressions.getLipSuck());
        values.put(COLUMN_UPPERLIPRAISE, face.expressions.getUpperLipRaise());
        values.put(COLUMN_MOUTHOPEN, face.expressions.getMouthOpen());
        values.put(COLUMN_NOSEWRINKLE, face.expressions.getNoseWrinkle());
        values.put(COLUMN_SMILE, face.expressions.getSmile());
        values.put(COLUMN_SMIRK, face.expressions.getSmirk());

        values.put(COLUMN_ENGAGEMENT, face.emotions.getEngagement());
        values.put(COLUMN_VALENCE, face.emotions.getValence());
        values.put(COLUMN_ANGER, face.emotions.getAnger());
        values.put(COLUMN_CONTEMPT, face.emotions.getContempt());
        values.put(COLUMN_DISGUST, face.emotions.getDisgust());
        values.put(COLUMN_FEAR, face.emotions.getFear());
        values.put(COLUMN_JOY, face.emotions.getJoy());
        values.put(COLUMN_SADNESS, face.emotions.getSadness());
        values.put(COLUMN_SURPRISE, face.emotions.getSurprise());

        values.put(COLUMN_FACEDISTANCE, face.measurements.getInterocularDistance());
        values.put(COLUMN_FACEORIENTATIONPITCH, face.measurements.orientation.getPitch());
        values.put(COLUMN_FACEORIENTATIONROLL, face.measurements.orientation.getRoll());
        values.put(COLUMN_FACEORIENTATIONYAW, face.measurements.orientation.getYaw());

        db.insert(TABLE_AFFECTIVA, null, values);
    }

    public void exportData(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_AFFECTIVA);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "AFFECTIVA.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_AFFECTIVA + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {
                        Integer.toString(curCSV.getInt(0)),
                        Long.toString(curCSV.getLong(1)),
                        Float.toString(curCSV.getFloat(2)),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5),
                        curCSV.getString(6),
                        Float.toString(curCSV.getFloat(7)),
                        Float.toString(curCSV.getFloat(8)),
                        Float.toString(curCSV.getFloat(9)),
                        Float.toString(curCSV.getFloat(10)),
                        Float.toString(curCSV.getFloat(11)),
                        Float.toString(curCSV.getFloat(12)),
                        Float.toString(curCSV.getFloat(13)),
                        Float.toString(curCSV.getFloat(14)),
                        Float.toString(curCSV.getFloat(15)),
                        Float.toString(curCSV.getFloat(16)),
                        Float.toString(curCSV.getFloat(17)),
                        Float.toString(curCSV.getFloat(18)),
                        Float.toString(curCSV.getFloat(19)),
                        Float.toString(curCSV.getFloat(20)),
                        Float.toString(curCSV.getFloat(21)),
                        Float.toString(curCSV.getFloat(22)),
                        Float.toString(curCSV.getFloat(23)),
                        Float.toString(curCSV.getFloat(24)),
                        Float.toString(curCSV.getFloat(25)),
                        Float.toString(curCSV.getFloat(26)),
                        Float.toString(curCSV.getFloat(27)),
                        Float.toString(curCSV.getFloat(28)),
                        Float.toString(curCSV.getFloat(29)),
                        Float.toString(curCSV.getFloat(30)),
                        Float.toString(curCSV.getFloat(31)),
                        Float.toString(curCSV.getFloat(32)),
                        Float.toString(curCSV.getFloat(33)),
                        Float.toString(curCSV.getFloat(34)),
                        Float.toString(curCSV.getFloat(35)),
                        Float.toString(curCSV.getFloat(36)),
                        Float.toString(curCSV.getFloat(37)),
                        Float.toString(curCSV.getFloat(38)),
                        Float.toString(curCSV.getFloat(39)),
                        Float.toString(curCSV.getFloat(40))
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
