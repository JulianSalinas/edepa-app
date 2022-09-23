package edepa.settings;

import static edepa.model.Preferences.ALLOW_PHOTO_KEY;
import static edepa.model.Preferences.FAVORITES_KEY;
import static edepa.model.Preferences.LANG_KEY;
import static edepa.model.Preferences.USER_KEY;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import edepa.app.MainActivity;
import edepa.app.NavigationActivity;
import edepa.cloud.Cloud;
import edepa.cloud.CloudUsers;
import edepa.minilibs.DialogFancy;
import edepa.model.Preferences;
import edepa.modelview.R;

/**
 * Las preferencias que se cambian en el fragmento son automáticamente
 * aplicadas y reflejadas en la instancia de {@link edepa.model.Preferences}
 * Este fragmento solo funciona si está dentro de una instancia de la clase
 * {@link MainActivity} de lo contrario el comportamiento es indeterminado
 */
public class SettingsGeneralFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_general);
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
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDividerHeight(1);
        CloudUsers cloudUsers = new CloudUsers();
        cloudUsers.setUserProfileListener(userProfile -> {
            ((SwitchPreference) findPreference(ALLOW_PHOTO_KEY))
                    .setChecked(userProfile.getAllowPhoto() == null || userProfile.getAllowPhoto());
        });
        cloudUsers.requestCurrentUserInfo();
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
     * Al cambiar la preferencia del idioma la aplicación se reinicia
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        NavigationActivity activity = (NavigationActivity) getActivity();

        // Se ha cambiado el lenguage
        if (key.equals(LANG_KEY)) {
            showLanguageChangeDialog();
        }

        // Se ha cambiado el nombre de usuario
        else if (key.equals(USER_KEY)){
            String msg = getString(R.string.text_username_changed);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            String username = ((EditTextPreference) findPreference(USER_KEY)).getText();
            if (activity != null) {
                Cloud.getInstance().getReference(Cloud.USERS)
                        .child(Cloud.getInstance().getUserId())
                        .child("username").setValue(username);
                activity.showWelcomeMessage(username);
            }
        }

        else if (key.equals(FAVORITES_KEY)){
            if (activity != null && sharedPreferences.getBoolean(FAVORITES_KEY, true)) {
                activity.startDispatcher();
            }
            else if (activity != null){
                activity.cancelDispatcher();
            }
        }

        else if (key.equals(ALLOW_PHOTO_KEY)){
            boolean allow = sharedPreferences.getBoolean(ALLOW_PHOTO_KEY, true);
            Cloud.getInstance().getReference(Cloud.USERS)
                    .child(Cloud.getInstance().getUserId())
                    .child("allowPhoto").setValue(allow);
        }

    }

    /**
     * Muestra un dialogo para cambiar el idioma de la aplicación. Si el
     * usuario acepta se ejecuta la función {@link #changeLanguageAndRestart()}
     */
    public void showLanguageChangeDialog(){
        new DialogFancy.Builder()
                .setContext(getContext())
                .setStatus(DialogFancy.INFO)
                .setTitle(R.string.text_language_change)
                .setContent(R.string.text_language_change_content)
                .setExistsCancel(true)
                .setOnAcceptClick(v -> changeLanguageAndRestart())
                .build().show();
    }

    /**
     * Se guarda el idioma en las preferencias para que cuando se reinicie
     * la aplicación se aplicaquen los cambios
     */
    public void changeLanguageAndRestart(){
        NavigationActivity activity = (NavigationActivity) getActivity();
        String lang = Preferences.getStringPreference(getContext(), LANG_KEY);
        SettingsLanguage.setLanguage(getContext(), lang);
        if (activity != null) activity.recreate();
    }

}
