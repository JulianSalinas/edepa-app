package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.view.LayoutInflater;
import android.support.design.widget.TextInputEditText;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.config_view, container, false);
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        bindViews();
        setupRadioButtons();
        setupInputUsername();
        setupSwitchNotifications();
    }

    /**
     * Enlaza todas las vistas del fragmento con sus clases
     * Aquí se consulta la última configuración guardada
     */
    private void bindViews(){
        assert getView() != null;
        radioEspanish = getView().findViewById(R.id.radio_espanish);
        inputUsername = getView().findViewById(R.id.input_username);
        switchNotifications = getView().findViewById(R.id.switch_notifications);
    }

    /**
     * Funcion que apaga o encienda las notificaciones
     * TODO: Se debe llamar al controlador
     */
    private void setupSwitchNotifications(){

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ?
                    getResources().getString(R.string.text_notifications_enabled):
                    getResources().getString(R.string.text_notifications_disabled);
            MainViewNavigation activity = (MainViewNavigation) getActivity();
            activity.showStatusMessage(status);
        });

    }

    /**
     * Prepara los radioButtons para recibir la acción de cambiar el idioma
     * Solo es necesario un radioButton por que son dos idiomas
     */
    private void setupRadioButtons(){

        radioEspanish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(radioEspanish.isChecked())
                showStatusMessage("Idioma cambiado a español");
            else
                showStatusMessage("Language changed to English");
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
