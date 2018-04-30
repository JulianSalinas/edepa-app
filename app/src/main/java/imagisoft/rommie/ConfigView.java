package imagisoft.rommie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


public class ConfigView extends MainViewFragment {

    /**
     * Elementos visuales para editar la configuración
     */

    @BindView(R.id.about_view)
    View aboutView;

    @BindView(R.id.username_view)
    View usernameView;

    @BindView(R.id.language_view)
    View languageView;

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
        switchNotifications.setChecked(getCurrentAlarmState());
        setupSwitchNotifications();
        toolbarText = getToolbar().getTitle();
        getToolbar().setTitle(getResources().getString(R.string.nav_settings));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getToolbar().setTitle(toolbarText);
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
     * Función que abre un dialogo para cambiar el idioma
     */
    @OnClick(R.id.language_view)
    public void openLanguage(){

        final Dialog dialog = new Dialog(getNavigation());
        dialog.setTitle(R.string.text_language);
        dialog.setContentView(R.layout.dialog_lang);

        Window window = dialog.getWindow();

        window.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(R.color.app_white);

        Button englishButton = dialog.findViewById(R.id.english_button);
        Button spanishButton = dialog.findViewById(R.id.spanish_button);

        if (getCurrentLang().equals("es")){
            spanishButton.setBackgroundColor(getResources().getColor(R.color.app_accent));
            englishButton.setBackgroundColor(getResources().getColor(R.color.app_detail));
        }

        englishButton.setOnClickListener(v -> {
            if (!getCurrentLang().equals("en")){
                changeLanguage("en");
            }
        });

        spanishButton.setOnClickListener(v -> {
            if (!getCurrentLang().equals("es")){
                changeLanguage("es");
            }
        });

        dialog.show();

    }

    /**
     * Función que abre un dialogo para ingresar el nombre de usuario
     */
    @OnClick(R.id.username_view)
    public void openUsernameDialogAux(){

        final Dialog dialog = new Dialog(getNavigation());
        dialog.setTitle(R.string.text_username);
        dialog.setContentView(R.layout.dialog_username);

        Window window = dialog.getWindow();

        window.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(R.color.app_white);

        Button confirmButton = dialog.findViewById(R.id.confirm_button);
        EditText inputUsername = dialog.findViewById(R.id.input_username);

        inputUsername.setText(getCurrentUsername());

        confirmButton.setOnClickListener(v ->{
            String username = inputUsername.getEditableText().toString();
            setCurrentUsername(username);
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
