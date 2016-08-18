package com.vadimmelnicuk.wmgdsm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private static final String SQL_CREATE_AFFECTIVA_TABLE = "CREATE TABLE " + TABLE_AFFECTIVA + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SESSION + " INTEGER NOT NULL, " +
            COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
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
}
