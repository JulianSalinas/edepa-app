package imagisoft.rommie;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Switch;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.view.LayoutInflater;
import android.support.design.widget.TextInputEditText;

import java.util.Locale;

import butterknife.BindView;
import imagisoft.edepa.Preferences;


public class ConfigView extends MainViewFragment {

    /**
     * Elementos visuales para editar la configuración
     */
    @BindView(R.id.radio_english) RadioButton radioEnglish;
    @BindView(R.id.radio_espanish) RadioButton radioSpanish;
    @BindView(R.id.input_username) TextInputEditText inputUsername;
    @BindView(R.id.switch_notifications) Switch switchNotifications;

    /**
     * Se crea la vista que contiene la configuracíón
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.config_view);
    }

    /**
     * Justo después de crear el fragmento se configuran las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);

        if(getCurrentLang().equals(Locale.ENGLISH.getLanguage()))
             radioEnglish.setChecked(true);
        else radioSpanish.setChecked(true);

        inputUsername.setText(getCurrentUsername());
        switchNotifications.setChecked(getCurrentAlarmState());

        setupRadioButtons();
        setupInputUsername();
        setupSwitchNotifications();

    }

    /**
     * Función que apaga o enciende las notificaciones
     */
    private void setupSwitchNotifications(){

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setCurrentAlarmState(isChecked);
            String status = isChecked ?
                    getResources().getString(R.string.text_notifications_enabled):
                    getResources().getString(R.string.text_notifications_disabled);
            getNavigation().showStatusMessage(status);
        });

    }

    /**
     * Prepara los radioButtons para recibir la acción de cambiar el idioma
     * Solo es necesario un radioButton por que son dos idiomas
     */
    private void setupRadioButtons(){

        radioSpanish.setOnCheckedChangeListener((buttonView, isChecked) -> {

            String lang = radioSpanish.isChecked() ? "es" : "en";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.text_language_changed))
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog, id) -> {

                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();

                        conf.locale = new Locale(lang);
                        res.updateConfiguration(conf, dm);

                        Intent refresh = new Intent(getActivity(), MainViewNavigation.class);
                        startActivity(refresh);
                        getActivity().finish();

                        setCurrentLang(lang);

                    })
                    .setNegativeButton("CANCEL", (dialog, which) -> {

                        if(radioSpanish.isChecked()){
                            radioSpanish.setChecked(false);
                            radioEnglish.setChecked(true);
                        }
                        else{
                            radioSpanish.setChecked(true);
                            radioEnglish.setChecked(false);
                        }

                    });

            AlertDialog alert = builder.create();
            alert.show();

        });

    }

    /**
     * Función que cuando el usuario termina de escribir, el nombre de
     * usuario se cambia automáticamente
     */
    private void setupInputUsername() {
        inputUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus)
                setCurrentUsername(inputUsername.getEditableText().toString());
        });
    }

}
