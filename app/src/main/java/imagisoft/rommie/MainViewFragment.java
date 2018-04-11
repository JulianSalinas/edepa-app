package imagisoft.rommie;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

public class MainViewFragment extends Fragment {

    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public int FADE_ANIMATION = 0;
    public int SLIDE_ANIMATION = 1;

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
