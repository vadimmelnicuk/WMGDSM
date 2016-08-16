package com.vadimmelnicuk.wmgdsm;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class ActivitySessions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        Cursor sessions = Main.dsmDb.getSessionsCursor();
        AdapterSessions sessionsAdapter = new AdapterSessions(this, sessions, 0);
        ListView list = (ListView) findViewById(R.id.sessions_list);
        list.setAdapter(sessionsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
