package imagisoft.modelview;

import android.widget.*;

import imagisoft.modelview.activity.MainActivityFragment;


public class ConfigFragment extends MainActivityFragment
        implements CompoundButton.OnCheckedChangeListener{

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void setupResource() {

    }

    @Override
    public void setupActivityView() {

    }

//    /**
//     * Elementos visuales para editar la configuración
//     */
//    @BindView(R.id.about_view)
//    View aboutView;
//
//    @BindView(R.id.username_view)
//    View usernameView;
//
//    @BindView(R.id.language_view)
//    View languageView;
//
//    @BindView(R.id.switch_notifications)
//    Switch switchNotifications;
//
//    /**
//     * Se define cúal es el layout que va a utilizar
//     */
//    @Override
//    public void setupResource() {
//        this.resource = R.layout.config_view;
//    }
//
//    /**
//     * Se configura la vista después de que la actividad se reinicia
//     * ya sea por cambio de idioma o al girar la pantalla
//     * @param savedInstanceState: No se utiliza
//     */
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        switchNotifications.setChecked(getCurrentAlarmState());
//        switchNotifications.setOnCheckedChangeListener(this);
//    }
//
//    @Override
//    public void setupActivityView() {
//        setToolbarText(R.string.nav_settings);
//        setToolbarVisibility(VISIBLE);
//        setTabLayoutVisibility(GONE);
//    }
//
//    /**
//     * Función que apaga o enciende las notificaciones
//     */
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        setCurrentAlarmState(isChecked);
//        String status = isChecked ?
//                getResources().getString(R.string.text_notifications_enabled):
//                getResources().getString(R.string.text_notifications_disabled);
//        activity.showMessage(status);
//    }
//
//    /**
//     * Reinicia la aplicación con el idioma seleccionado
//     * @param lang: "es" o "en"
//     */
//    public void changeLanguage(String lang){
//
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//
//        conf.locale = new Locale(lang);
//        res.updateConfiguration(conf, dm);
//        setCurrentLang(lang);
//
//        restartApplication();
//
//    }
//
//    /**
//     * Crea un nuevo dialog a partir un layout
//     * @param title: R.string.<resource>
//     * @param layout: R.layout.<resource>
//     * @return Dialog creado y configurado
//     */
//    public Dialog createDialog(int title, int layout){
//
//        final Dialog dialog = new Dialog(activity);
//        dialog.setTitle(title);
//        dialog.setContentView(layout);
//        setupDialog(dialog);
//        return dialog;
//
//    }
//
//    /**
//     * Ajusta el dialogo para que se vea bien
//     * @param dialog: Dialog previamente creado
//     */
//    public void setupDialog(final Dialog dialog){
//
//        Window window = dialog.getWindow();
//        if (window != null) {
//
//            window.setLayout(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            window.setGravity(Gravity.CENTER);
//            window.setBackgroundDrawableResource(R.color.app_white);
//        }
//
//    }
//
//    /**
//     * Función que abre un dialogo para cambiar el idioma
//     */
//    @SuppressLint("CheckResult")
//    @OnClick(R.id.language_view)
//    public void openLanguage(){
//
//        final Dialog dialog = createDialog(R.string.text_language, R.layout.dialog_lang);
//        Button englishButton = dialog.findViewById(R.id.english_button);
//        Button spanishButton = dialog.findViewById(R.id.spanish_button);
//
//        Aesthetic.get()
//                .colorAccent()
//                .take(1)
//                .subscribe((Integer color) -> {
//
//                    spanishButton.setBackgroundColor(
//                            getCurrentLang().equals("es") ? color :
//                                    ColorConverter.lighten(color, 12));
//
//                    englishButton.setBackgroundColor(
//                            getCurrentLang().equals("en") ? color :
//                                    ColorConverter.lighten(color, 12));
//
//                });
//
//
//        englishButton.setOnClickListener(v -> {
//            if (!getCurrentLang().equals("en"))
//                changeLanguage("en");
//        });
//
//        spanishButton.setOnClickListener(v -> {
//            if (!getCurrentLang().equals("es"))
//                changeLanguage("es");
//        });
//
//        dialog.show();
//
//    }
//
//    /**
//     * Función que abre un dialogo para ingresar el nombre de usuario
//     */
//    @OnClick(R.id.username_view)
//    public void openUsernameDialog(){
//
//        final Dialog dialog =
//                createDialog(R.string.text_username, R.layout.dialog_username);
//
//        EditText inputUsername = dialog.findViewById(R.id.input_username);
//        inputUsername.setText(getCurrentUsername());
//
//        dialog.findViewById(R.id.confirm_button).setOnClickListener(v ->{
//            String username = inputUsername.getEditableText().toString();
//            setCurrentUsername(username);
//            showMessage(R.string.text_username_changed);
//            dialog.dismiss();
//        });
//
//        dialog.show();
//
//    }
//
//    /**
//     * Al hacer click en el about se abre otro fragment con la información
//     */
//    @OnClick(R.id.about_view)
//    public void openAbout(){
//        activity.switchFragment(new AboutFragment());
//    }

}
