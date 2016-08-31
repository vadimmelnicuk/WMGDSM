package com.vadimmelnicuk.wmgdsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class DbPolarH7Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "polarh7.db";

    public static final String TABLE_BPM = "bpm";
    public static final String TABLE_RR = "rr";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SESSION = "session";
    public static final String COLUMN_BPM = "bpm";
    public static final String COLUMN_RR = "rr";

    private static final String SQL_CREATE_BPM_TABLE = "CREATE TABLE " + TABLE_BPM + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
            COLUMN_BPM + " INTEGER NOT NULL" +
            ");";

    private static final String SQL_CREATE_RR_TABLE = "CREATE TABLE " + TABLE_RR + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
            COLUMN_RR + " REAL NOT NULL" +
            ");";

    public DbPolarH7Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BPM_TABLE);
        db.execSQL(SQL_CREATE_RR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void dropDb(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public void insertBPM(final long session, final long timestamp, final int bpm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_BPM, bpm);
        db.insert(TABLE_BPM, null, values);
    }

    public void insertRR(final long session, final long timestamp, final double rr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_RR, rr);
        db.insert(TABLE_RR, null, values);
    }

    public void exportBPM(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_POLARH7);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "BPM.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_BPM + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {Integer.toString(curCSV.getInt(0)), Long.toString(curCSV.getLong(1)), Long.toString(curCSV.getLong(2)), Long.toString(curCSV.getLong(3))};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public void exportIBI(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_POLARH7);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "RR.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_RR + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {Integer.toString(curCSV.getInt(0)), Long.toString(curCSV.getLong(1)), Long.toString(curCSV.getLong(2)), Double.toString(curCSV.getDouble(3))};
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
