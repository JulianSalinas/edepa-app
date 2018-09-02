package edepa.settings;

import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;

import edepa.modelview.R;
import edepa.model.Preferences;
import edepa.app.NavigationActivity;

import com.afollestad.aesthetic.Aesthetic;
import com.kunzisoft.androidclearchroma.ChromaPreferenceFragmentCompat;
import static edepa.model.Preferences.THEME_KEY;


public class SettingsThemeFragment extends ChromaPreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String ACCENT_COLOR = "ACCENT_COLOR";
    public static final String PRIMARY_COLOR = "PRIMARY_COLOR";
    public static final String PRIMARY_DARK_COLOR = "PRIMARY_DARK_COLOR";

    /**
     * Se coloca el evento onClick a cada una de las preferencias
     */
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        addPreferencesFromResource(R.xml.settings_theme);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDividerHeight(1);
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
        PreferenceManager manager = getPreferenceManager();
        SharedPreferences preferences = manager.getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     * Se dejan de escuchar los cambios en las preferencias
     */
    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager manager = getPreferenceManager();
        SharedPreferences preferences = manager.getSharedPreferences();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     * Al cambiar un color se invoca a la Lib Aesthetics para que cambie
     * el tema actual de la aplicación
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        NavigationActivity activity = (NavigationActivity) getActivity();
        if (key.equals(THEME_KEY) && activity != null) activity.recreate();

        // Se pregunta si el usuario tiene activo el tema personalizado
        else if(activity != null && Preferences.getBooleanPreference(
                activity, Preferences.THEME_KEY, false)) {

            Aesthetic.get()
                    .colorPrimary(sharedPreferences.getInt(PRIMARY_COLOR,
                            ContextCompat.getColor(activity, R.color.app_primary)))
                    .colorPrimaryDark(sharedPreferences.getInt(PRIMARY_DARK_COLOR,
                            ContextCompat.getColor(activity, R.color.app_primary_dark)))
                    .colorAccent(sharedPreferences.getInt(ACCENT_COLOR,
                            ContextCompat.getColor(activity, R.color.app_accent)))
                    .apply();
        }
    }

}
