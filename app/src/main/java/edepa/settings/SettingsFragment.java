package edepa.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceFragmentCompat;

import edepa.app.NavigationActivity;
import edepa.modelview.R;
import edepa.cloud.Cloud;
import edepa.model.Preferences;

import static edepa.model.Preferences.LANG_KEY;
import static edepa.model.Preferences.USER_KEY;

/**
 * Las preferencias que se cambian en el fragmento son
 * automáticamente aplicadas y reflejadas en la instancia
 * de {@link edepa.model.Preferences}
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs_general);
    }

    /**
     * {@inheritDoc}
     * Este fragmento solo funciona si es colocado en una actividad
     * del tipo {@link NavigationActivity} de lo contrario el comportamiento
     * es indeterminado
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDividerHeight(1);
    }

    /**
     * {@inheritDoc}
     * El fondo es cambiado a blanco para no desentonar el
     * color de la fuente
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.app_white));
    }

    /**
     * Se coloca la escucha para cuando suceden cambios
     * en las preferecias
     * @see #onPause()
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Se dejan de escuchar los cambios en las
     * preferencias
     * @see #onResume()
     */
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     * Al cambiar la preferencia del idioma la aplicación se reinicia
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        NavigationActivity activity = (NavigationActivity) getActivity();

        // Se ha cambiado el lenguage
        if (key.equals(LANG_KEY)){
            String lang = Preferences.getStringPreference(getContext(), LANG_KEY);
            SettingsLanguage.setLanguage(getContext(), lang);
            String msg = getString(R.string.text_language_changed);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if (activity != null) activity.restartApplication(Cloud.CONFIG);
        }

        // Se ha cambiado el nombre de usuario
        else if (key.equals(USER_KEY)){
            String msg = getString(R.string.text_username_changed);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            if (activity != null) activity.showWelcomeMessage();
        }

    }

}
