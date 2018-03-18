package imagisoft.rommie;

import android.support.v4.app.Fragment;

public class ActivityMainFrag extends Fragment {

    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public int FADE_ANIMATION = 0;
    public int SLIDE_ANIMATION = 1;

    /**
     * Coloca en la pantalla un fragmento previamente creado usando un animación
     * @param fragment Asociado a la opción elegida por el usuario
     */
    void switchFragment(Fragment fragment, int animation){
        ActivityMainNav activity = (ActivityMainNav) getActivity();
        activity.switchFragment(fragment, animation);
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        ActivityMainNav activity = (ActivityMainNav) getActivity();
        activity.showStatusMessage(msg);
    }

}
