package imagisoft.rommie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import imagisoft.edepa.Preferences;

import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;


public class MainViewFragment extends Fragment {

    Preferences prefs = Preferences.getInstance();
    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public final int FADE_ANIMATION = 0;
    public final int SLIDE_ANIMATION = 1;

    protected CharSequence toolbarText;

    /**
     * Es un invocada cuando un fragmento ocupa colocar un listener
     * de firebase
     */
    public MainViewFirebase getFirebase(){
        return (MainViewFirebase) getActivity();
    }

    /**
     * Es un invocada cuando un fragmento ocupa ocultar o mostrar la toolbar
     */
    public MainViewFirebase getNavigation(){
        return (MainViewNavigation) getActivity();
    }

    /**
     * Obtiene el lenguaje de la configuracíon. Si
     * es la primera vez, guarda en la configuración el idioma del dispositivo
     * @return "es" o "en"
     */
    public String getCurrentLang(){

        String lang = prefs.getStringPreference(getActivity(), Preferences.LANG_KEY_VALUE);

        if(lang == null) {
            lang = Locale.getDefault().getLanguage();
            setCurrentLang(lang);
        }

        return lang;

    }

    public void setCurrentLang(String lang){
        prefs.setPreference(getActivity(), Preferences.LANG_KEY_VALUE, lang);
    }

    /**
     * Obtiene el actual nombre de usuario, si el la primera vez toma
     * el usuario desde el login por defecto
     */
    public String getCurrentUsername(){

        String username = prefs.getStringPreference(getActivity(), Preferences.USER_KEY_VALUE);

        if(username == null) {
            username = getDefaultUsername(getActivity());
            setCurrentUsername(username);
        }

        return username;

    }

    public void setCurrentUsername(String username){
        prefs.setPreference(getActivity(), Preferences.USER_KEY_VALUE, username);
    }

    /**
     * Obtiene el estado actual de la alarma
     */
    public Boolean getCurrentAlarmState(){
        return prefs.getBooleanPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE);
    }

    public void setCurrentAlarmState(boolean state){
        prefs.setPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE, state);
    }

    /**
     * Cuando el usuario hace login con el # de teléfono, su nombre está vacío por lo que
     * hay que crear uno. Al hacer login con Google o correo el nombre por defecto que se
     * coloca es el de dicha cuenta.
     * @param context: Actividad desde donde se llama la aplicación
     */
    public String getDefaultUsername(Context context){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        return user.getDisplayName() == null ||
                !user.getDisplayName().isEmpty() ?
                context.getResources().getString(R.string.text_anonymous) : user.getDisplayName();

    }

    /**
     * Enlaza los componentes visuales con sus vistas
     */
    protected View inflate(LayoutInflater inflater, ViewGroup container, int resource){
        View view = inflater.inflate(resource, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){
        switchFragment(fragment, FADE_ANIMATION);
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado usando un animación
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment, int animation){
        assert getActivity() != null;
        MainViewNavigation activity = (MainViewNavigation) getActivity();
        activity.switchFragment(fragment, animation);
    }

    /**
     * Reinicia la aplicación. Se utiliza cuando se cambia el idioma o el tema
     */
    public void restartApplication(){
        Intent refresh = new Intent(getActivity(), MainViewNavigation.class);
        startActivity(refresh);
        getNavigation().finish();
    }

    /**
     * Oculta o muestra la toolbar para fragmentos que lo requieren
     * @param show: True si se debe mostrar
     */

    public void setToolbarVisible(boolean show){
        ActionBar toolbar = getNavigation().getSupportActionBar();
        if(toolbar != null) {
            if (!show) toolbar.hide();
            else toolbar.show();
        }
    }

    public void setStatusBarColor(int color){
        MainViewNavigation activity = (MainViewNavigation) getActivity();
        activity.getWindow().setStatusBarColor(color);
    }

    public int getStatusBarColor(){
        MainViewNavigation activity = (MainViewNavigation) getActivity();
        return activity.getWindow().getStatusBarColor();
    }

    public Toolbar getToolbar(){
        return getNavigation().getToolbar();
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        assert getActivity() != null;
        MainViewNavigation activity = (MainViewNavigation) getActivity();
        activity.showStatusMessage(msg);
    }

}
