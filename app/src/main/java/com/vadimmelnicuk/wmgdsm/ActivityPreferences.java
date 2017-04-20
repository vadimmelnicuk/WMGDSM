package com.vadimmelnicuk.wmgdsm;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class ActivityPreferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new preferenceFragment()).commit();
    }

    public static class preferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference saveButton = (Preference) getPreferenceManager().findPreference("pref_save");
            saveButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg) {
                    // TODO - implement save preferences functionality
                    getActivity().finish();
                    return true;
                }
            });

            Preference deleteDataButton = (Preference) getPreferenceManager().findPreference("pref_delete_data");
            deleteDataButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg) {
                    Context context = getActivity().getApplicationContext();
                    Main.dsmDb.dropDb(context);

                    if(Main.modulesEmpaticaE4) {
                        Main.empaticaDb.dropDb(context);
                    }
                    if(Main.modulesPolarH7) {
                        Main.polarDb.dropDb(context);
                    }
                    if(Main.modulesAffectiva) {
                        Main.affectivaDb.dropDb(context);
                    }

                    getActivity().finish();
                    return true;
                }
            });
        }
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
