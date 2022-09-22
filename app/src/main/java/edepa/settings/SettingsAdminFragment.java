package edepa.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import edepa.app.NavigationActivity;
import edepa.cloud.Cloud;
import edepa.cloud.CloudNavigation;
import edepa.modelview.R;


public class SettingsAdminFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        CloudNavigation.CloudNavigationPreferencesListener {

    private SharedPreferences preferences;
    public static final String ADMINS_KEY = "admins";

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

        findPreference(ADMINS_KEY).setOnPreferenceClickListener(preference -> {
            if (getActivity() != null && getActivity() instanceof NavigationActivity)
                return ((NavigationActivity) getActivity()).openAdminsFragment();
            else Log.e(getClass().getSimpleName(), "Admins fragment was not open");
            return false;
        });
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
        String [] sections = {"info", "news", "chat", "people", "comments"};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(sections));
        if(list.contains(key)) {
            boolean available = sharedPreferences.getBoolean(key, true);
            Cloud.getInstance().getReference(Cloud.CONFIG).child(key).setValue(available);
        }
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
