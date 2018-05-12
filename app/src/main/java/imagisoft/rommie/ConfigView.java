package imagisoft.rommie;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.app.Dialog;
import android.view.Window;
import android.view.Gravity;
import android.widget.Switch;
import android.widget.Button;
import android.widget.EditText;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.content.res.Resources;
import android.content.res.Configuration;

import com.afollestad.aesthetic.Aesthetic;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.OnClick;
import imagisoft.miscellaneous.ColorConverter;
import io.reactivex.disposables.Disposable;


public class ConfigView extends MainActivityFragment {

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
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        resource = R.layout.config_view;
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        switchNotifications.setChecked(getCurrentAlarmState());
        setupSwitchNotifications();
        setToolbarText(R.string.nav_settings);
        setTabLayoutVisibility(View.GONE);
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
            activity.showStatusMessage(status);
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

    }

    /**
     * Función que abre un dialogo para cambiar el idioma
     */
    @SuppressLint("CheckResult")
    @OnClick(R.id.language_view)
    public void openLanguage(){

        final Dialog dialog = new Dialog(activity);
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

        Aesthetic.get()
                .colorAccent()
                .take(1)
                .subscribe((Integer color) -> {
                    if (getCurrentLang().equals("es")) {
                        spanishButton.setBackgroundColor(color);
                        englishButton.setBackgroundColor(ColorConverter.lighten(color, 12));
                    }
                    else {
                        spanishButton.setBackgroundColor(ColorConverter.lighten(color, 12));
                        englishButton.setBackgroundColor(color);
                    }
                });


        englishButton.setOnClickListener(v -> {
            if (!getCurrentLang().equals("en"))
                changeLanguage("en");

        });

        spanishButton.setOnClickListener(v -> {
            if (!getCurrentLang().equals("es"))
                changeLanguage("es");
        });

        dialog.show();

    }

    /**
     * Función que abre un dialogo para ingresar el nombre de usuario
     */
    @OnClick(R.id.username_view)
    public void openUsernameDialogAux(){

        final Dialog dialog = new Dialog(activity);
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
        activity.switchFragment(new AboutView());
    }

}
