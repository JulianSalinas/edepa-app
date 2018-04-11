package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;

import imagisoft.edepa.LocaleManager;


public class ConfigView extends MainViewFragment {

    /**
     * Elementos visuales para editar la configuración
     */
    private RadioButton radioEspanish;
    private Switch switchNotifications;
    private TextInputEditText inputUsername;

    /**
     * Se crea la vista que contiene la configuracíón
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.config_view, container, false);
        radioEspanish = view.findViewById(R.id.radio_espanish);
        inputUsername = view.findViewById(R.id.input_username);
        switchNotifications = view.findViewById(R.id.switch_notifications);
        return view;

    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        setupRadioButtons();
        setupInputUsername();
        setupSwitchNotifications();

    }

    /**
     * Función que apaga o encienda las notificaciones
     */
    private void setupSwitchNotifications(){

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

        radioEspanish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(radioEspanish.isChecked()) {
                showStatusMessage("Idioma cambiado a español");
                LocaleManager.getInstance().changeLocale(getContext(), "es");
            }
            else {
                showStatusMessage("Language changed to English");
                LocaleManager.getInstance().changeLocale(getContext(), "en");
            }
        });

    }

    /**
     * Función que cuando el usuario termina de escribir, el nombre de
     * usuario se cambia automaticamente
     */
    private void setupInputUsername() {

        inputUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                // TODO: Cambiar aquí el nombre de usuario
            }
        });

    }

}
