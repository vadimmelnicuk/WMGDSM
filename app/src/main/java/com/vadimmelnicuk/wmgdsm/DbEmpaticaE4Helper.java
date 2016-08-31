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
public class DbEmpaticaE4Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "empatica.db";

    public static final String TABLE_ACCELERATION = "acceleration";
    public static final String TABLE_TEMPERATURE = "temperature";
    public static final String TABLE_BVP = "bvp";
    public static final String TABLE_IBI = "ibi";
    public static final String TABLE_GSR = "gsr";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SESSION = "session";
    public static final String COLUMN_X = "x";
    public static final String COLUMN_Y = "y";
    public static final String COLUMN_Z = "z";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_BVP = "bvp";
    public static final String COLUMN_IBI = "ibi";
    public static final String COLUMN_GSR = "gsr";

    private static final String SQL_CREATE_ACCELERATION_TABLE = "CREATE TABLE " + TABLE_ACCELERATION + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " REAL NOT NULL, " +
            COLUMN_X + " INTEGER NOT NULL, " +
            COLUMN_Y + " INTEGER NOT NULL, " +
            COLUMN_Z + " INTEGER NOT NULL" +
            ");";

    private static final String SQL_CREATE_TEMPERATURE_TABLE = "CREATE TABLE " + TABLE_TEMPERATURE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " REAL NOT NULL, " +
            COLUMN_TEMPERATURE + " REAL NOT NULL" +
            ");";

    private static final String SQL_CREATE_BVP_TABLE = "CREATE TABLE " + TABLE_BVP + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " REAL NOT NULL, " +
            COLUMN_BVP + " REAL NOT NULL" +
            ");";

    private static final String SQL_CREATE_IBI_TABLE = "CREATE TABLE " + TABLE_IBI + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " REAL NOT NULL, " +
            COLUMN_IBI + " REAL NOT NULL" +
            ");";

    private static final String SQL_CREATE_GSR_TABLE = "CREATE TABLE " + TABLE_GSR + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " REAL NOT NULL, " +
            COLUMN_GSR + " REAL NOT NULL" +
            ");";

    public DbEmpaticaE4Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACCELERATION_TABLE);
        db.execSQL(SQL_CREATE_TEMPERATURE_TABLE);
        db.execSQL(SQL_CREATE_BVP_TABLE);
        db.execSQL(SQL_CREATE_IBI_TABLE);
        db.execSQL(SQL_CREATE_GSR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void dropDb(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public void insertAcceleration(long session, int x, int y, int z, double timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_X, x);
        values.put(COLUMN_Y, z);
        values.put(COLUMN_Z, y);
        db.insert(TABLE_ACCELERATION, null, values);
    }

    public void insertTemperature(long session, float temperature, double timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_TEMPERATURE, temperature);
        db.insert(TABLE_TEMPERATURE, null, values);
    }

    public void insertBVP(long session, float bvp, double timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_BVP, bvp);
        db.insert(TABLE_BVP, null, values);
    }

    public void insertIBI(long session, float ibi, double timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_IBI, ibi);
        db.insert(TABLE_IBI, null, values);
    }

    public void insertGSR(long session, float gsr, double timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_GSR, gsr);
        db.insert(TABLE_GSR, null, values);
    }

    public void exportAcceleration(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_EMPATICAE4);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "Acceleration.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_ACCELERATION + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {Integer.toString(curCSV.getInt(0)), Long.toString(curCSV.getLong(1)), Double.toString(curCSV.getDouble(2)), Integer.toString(curCSV.getInt(3)), Integer.toString(curCSV.getInt(4)), Integer.toString(curCSV.getInt(5))};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public void exportTemperature(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_EMPATICAE4);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "Temperature.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_TEMPERATURE + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {Integer.toString(curCSV.getInt(0)), Long.toString(curCSV.getLong(1)), Double.toString(curCSV.getDouble(2)), Float.toString(curCSV.getFloat(3))};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public void exportBVP(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_EMPATICAE4);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "BVP.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_BVP + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {Integer.toString(curCSV.getInt(0)), Long.toString(curCSV.getLong(1)), Double.toString(curCSV.getDouble(2)), Float.toString(curCSV.getFloat(3))};
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
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_EMPATICAE4);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "IBI.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_IBI + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {Integer.toString(curCSV.getInt(0)), Long.toString(curCSV.getLong(1)), Double.toString(curCSV.getDouble(2)), Float.toString(curCSV.getFloat(3))};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public void exportGSR(long session) {
        String date = Main.dsmDb.timestampToDate(session);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "WMGDSM/" + date + "/" + DbDsmHelper.COLUMN_MODULES_EMPATICAE4);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "GSR.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_GSR + " WHERE session = ?", new String[] {String.valueOf(session)});
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {Integer.toString(curCSV.getInt(0)), Long.toString(curCSV.getLong(1)), Double.toString(curCSV.getDouble(2)), Float.toString(curCSV.getFloat(3))};
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
