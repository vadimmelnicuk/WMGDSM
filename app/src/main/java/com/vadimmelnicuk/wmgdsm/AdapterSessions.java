package com.vadimmelnicuk.wmgdsm;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class AdapterSessions extends CursorAdapter {

    public AdapterSessions(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_session, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView timestamp = (TextView) view.findViewById(R.id.timestamp);
        final long sessionTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DbDsmHelper.COLUMN_TIMESTAMP));
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(sessionTimestamp);
        final String date = DateFormat.format("yyyyy-MM-dd HH:mm:ss", cal).toString();
        timestamp.setText(date);

        final int sessionSyncData = cursor.getInt(cursor.getColumnIndexOrThrow(DbDsmHelper.COLUMN_SYNCDATA));
        final int sessionModuleEmpaticaE4 = cursor.getInt(cursor.getColumnIndexOrThrow(DbDsmHelper.COLUMN_MODULES_EMPATICAE4));
        final int sessionModulePolarH7 = cursor.getInt(cursor.getColumnIndexOrThrow(DbDsmHelper.COLUMN_MODULES_POLARH7));
        final int sessionModuleAffectiva = cursor.getInt(cursor.getColumnIndexOrThrow(DbDsmHelper.COLUMN_MODULES_AFFECTIVA));

        Button exportButton = (Button) view.findViewById(R.id.export_button);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                if(sessionSyncData != 0) {
                    Main.dsmDb.exportSyncData(sessionTimestamp);
                } else {
                    if(sessionModuleEmpaticaE4 != 0) {
                        Log.e("data", "empatica stuff was exported");
                        Main.empaticaDb.exportAcceleration(sessionTimestamp);
                        Main.empaticaDb.exportTemperature(sessionTimestamp);
                        Main.empaticaDb.exportBVP(sessionTimestamp);
                        Main.empaticaDb.exportIBI(sessionTimestamp);
                        Main.empaticaDb.exportGSR(sessionTimestamp);
                    }
                    if(sessionModulePolarH7 != 0) {
                        Log.e("data", "polar stuff was exported");
                        Main.polarDb.exportBPM(sessionTimestamp);
                        Main.polarDb.exportIBI(sessionTimestamp);
                    }
                    if(sessionModuleAffectiva != 0) {
                        Log.e("data", "affectiva stuff was exported");
                        Main.affectivaDb.exportData(sessionTimestamp);
                    }
                }
                view.setEnabled(true);
                Toast.makeText(view.getContext(), "Session from " + date + " was exported", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
