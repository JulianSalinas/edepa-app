package imagisoft.rommie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class ConfigView extends MainViewFragment {

    /**
     * Elementos visuales para editar la configuración
     */

    @BindView(R.id.about_view)
    View aboutView;

    @BindView(R.id.radio_english)
    RadioButton radioEnglish;

    @BindView(R.id.radio_espanish)
    RadioButton radioSpanish;

    @BindView(R.id.username_text_view)
    TextView usernameTextView;

    @BindView(R.id.username_view)
    View usernameView;

    @BindView(R.id.switch_notifications)
    Switch switchNotifications;

    /**
     * Por si el usuario cancela el cambio de idioma, se pueda colocar la
     * interfaz como estaba antes
     */
    boolean changeLang = true;

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

        if(getCurrentLang().equals("en")) {
            radioEnglish.setChecked(true);
            radioSpanish.setChecked(false);
        }
        else {
            radioEnglish.setChecked(false);
            radioSpanish.setChecked(true);
        }

        usernameTextView.setText(getCurrentUsername());
        usernameTextView.setFocusable(false);
        switchNotifications.setChecked(getCurrentAlarmState());

        setupRadioButtons();
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
    @OnClick(R.id.radio_english)
    public void setupRadioButtons(){
        setupRadioButtons2();
    }

    @OnClick(R.id.radio_espanish)
    public void setupRadioButtons2(){

        String lang = radioSpanish.isChecked() ? "es" : "en";

        if (!lang.equals(getCurrentLang())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.text_language_changed))
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog, id) -> changeLanguage(lang))
                    .setNegativeButton("CANCEL", (dialog, which) -> cancelLanguage());

            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    /**
     * Reinicia la aplicación con el idioma seleccionado
     * @param lang: "es" o "en"
     */
    public void changeLanguage(String lang){

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);
        setCurrentLang(lang);
        restartApplication();
        changeLang = true;

    }

    /**
     * Se coloca el radio a como estaba antes
     */
    public void cancelLanguage(){

        if(radioSpanish.isChecked()){
            radioSpanish.setChecked(false);
            radioEnglish.setChecked(true);
        }
        else{
            radioSpanish.setChecked(true);
            radioEnglish.setChecked(false);
        }

    }

    /**
     * Función que abre un dialogo para ingresar el nombre de usuario
     */

    @OnClick(R.id.username_text_view)
    public void openUsernameDialogClick(){
        openUsernameDialogAux();
    }

    @OnLongClick(R.id.username_text_view)
    public boolean openUsernameDialogLongClick(){
        openUsernameDialogAux();
        return true;
    }

    @OnClick(R.id.username_view)
    public void openUsernameDialogAux(){

        final Dialog dialog = new Dialog(getNavigation());
        dialog.setTitle(R.string.text_username);
        dialog.setContentView(R.layout.dialog_username);

        Button confirmButton = dialog.findViewById(R.id.confirm_button);
        EditText inputUsername = dialog.findViewById(R.id.input_username);
        inputUsername.setText(getCurrentUsername());

        confirmButton.setOnClickListener(v ->{
            String username = inputUsername.getEditableText().toString();
            setCurrentUsername(username);
            usernameTextView.setText(username);
            dialog.dismiss();
            showStatusMessage(getResources().getString(R.string.text_username_changed));
        });

        dialog.show();

    }

    /**
     * Al hacer click en el about se abre otro fragment con la información
     */
    @OnClick(R.id.about_view)
    public void openAbout(){
        getNavigation().switchFragment(new AboutView());
    }

}
