package edepa.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import edepa.cloud.Cloud;
import edepa.cloud.CloudNavigation;
import edepa.modelview.R;


public class SettingsAdminFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        CloudNavigation.CloudNavigationPreferencesListener {

    private SharedPreferences preferences;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_admin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDividerHeight(1);

        PreferenceManager manager = getPreferenceManager();
        preferences = manager.getSharedPreferences();

        CloudNavigation cloudNavigation = new CloudNavigation();
        cloudNavigation.setNavigationPreferencesListener(this);
        cloudNavigation.requestNavigationSections();
    }

    /**
     * {@inheritDoc}
     * El fondo es cambiado a blanco para no desentonar con el color de la
     * fuente #R.color.app_primary_font
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.app_white));
    }

    /**
     * {@inheritDoc}
     * Se coloca la escucha para cuando suceden cambios en las preferencias
     */
    @Override
    public void onResume() {
        super.onResume();
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     * Se dejan de escuchar los cambios en las preferencias
     */
    @Override
    public void onPause() {
        super.onPause();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     * Al cambiar la preferencia del idioma la aplicación se reinicia
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean available = sharedPreferences.getBoolean(key, true);
        Cloud.getInstance().getReference(Cloud.CONFIG).child(key).setValue(available);
    }

    @Override
    public void onNavigationPreferenceChanged(String key, boolean state) {
        preferences.edit().putBoolean(key, state).apply();
        Preference preference = findPreference(key);
        if (preference != null && preference instanceof SwitchPreference) {
            ((SwitchPreference) preference).setChecked(state);
        }
    }

}
