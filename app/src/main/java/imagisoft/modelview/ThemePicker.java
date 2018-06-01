package imagisoft.modelview;

import android.os.Bundle;
import android.view.View;
import android.app.Dialog;
import android.view.Window;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.v7.preference.Preference;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.afollestad.aesthetic.Aesthetic;
import com.madrapps.pikolo.HSLColorPicker;
import imagisoft.misc.ColorConverter;
import com.kizitonwose.colorpreferencecompat.ColorPreferenceCompat;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;
import static imagisoft.modelview.DefaultColor.APP_ACCENT_DARK;


public class ThemePicker extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener{

    /**
     * Se obtiene las preferencias para obtener los colores
     */
    private SharedPreferences prefs;

    private int tabLayoutVisibility;
    private int toolbarVisibility;
    private CharSequence toolbarText;
    private ActivityNavigation activity;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity = (ActivityNavigation) getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        toolbarText = activity.getToolbar().getTitle();
        tabLayoutVisibility = activity.getTabLayout().getVisibility();
        toolbarVisibility = activity.getToolbar().getVisibility();
        activity.getToolbar().setTitle(getResources().getString(R.string.nav_palette));
        activity.getTabLayout().setVisibility(View.GONE);
    }

    /**
     * Al destruirse la vista se coloca el título que la aplicación
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.getToolbar().setVisibility(toolbarVisibility);
        activity.getTabLayout().setVisibility(tabLayoutVisibility);
        activity.getToolbar().setTitle(toolbarText);
    }

    /**
     * Se coloca el evento onClick a cada una de las preferencias
     */
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.color_preferences, rootKey);
        for(DefaultColor color: DefaultColor.values())
            findPreference(color.toString()).setOnPreferenceClickListener(this);
    }

    /**
     * Función colocada a cada una de la preferencias para cambiar los colores
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        showColorDialog(preference);
        return true;
    }

    /**
     * Se muestra una pantalla para que el usuario escoja el color
     * @param preference: Se elige el color para esta preferencia en particular
     */
    private void showColorDialog(final Preference preference) {

        final Dialog dialog = new Dialog(activity);
        dialog.setTitle(getResources().getString(R.string.text_choose_color));
        dialog.setContentView(R.layout.dialog_color);

        Window window = dialog.getWindow();

        window.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(R.color.app_white);

        ImageView imageOk = dialog.findViewById(R.id.ok_button);
        imageOk.setOnClickListener(v -> dialog.dismiss());

        ImageViewCompat.setImageTintList(imageOk,
                ColorStateList.valueOf(((ColorPreferenceCompat) preference).getValue()));

        // Se la pantalla para seleccionar
        final HSLColorPicker colorPicker = dialog.findViewById(R.id.color_picker);
        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener(){
            @Override
            public void onColorSelected(int color) {
                ((ColorPreferenceCompat) preference).setValue(color);
                changeCustomColor(preference.getKey());
                ImageViewCompat.setImageTintList(imageOk, ColorStateList.valueOf(color));
            }
        });

        dialog.findViewById(R.id.ok_button).setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    /**
     * Se sustituye en color de la aplicación en tiempo real con Aesthics
     * @param preferenceKey: Nombre de la preferencia que contiene el color
     */
    public void changeCustomColor(String preferenceKey){

        Aesthetic theme = Aesthetic.get();

        DefaultColor colorRes = DefaultColor.valueOf(preferenceKey.toUpperCase());
        int color = prefs.getInt(preferenceKey, colorRes.getColor());

        switch (DefaultColor.valueOf(preferenceKey.toUpperCase())){

            case APP_PRIMARY:
                theme.colorPrimary(color);
                break;

            case APP_PRIMARY_DARK:
                theme.colorPrimaryDark(color);
                activity.getWindow()
                        .setStatusBarColor(color);
                break;

            case APP_ACCENT:
                theme.colorAccent(color);
                break;

            case APP_ACCENT_DARK:
                theme.colorNavigationBar(color);
                activity.getWindow()
                        .setNavigationBarColor(color);
                break;

        }

        theme.apply();

    }

}
