package imagisoft.rommie;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.aesthetic.Aesthetic;
import com.kizitonwose.colorpreferencecompat.ColorPreferenceCompat;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterOpacitySlider;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import imagisoft.miscellaneous.ColorConverter;

import static imagisoft.rommie.CustomColor.APP_ACCENT_DARK;
import static imagisoft.rommie.CustomColor.APP_PRIMARY;


public class ThemePicker extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener{

    /**
     * Se obtiene las preferencias para obtener los colores
     */
    private SharedPreferences prefs;

    private int tabLayoutVisibility;
    private CharSequence toolbarText;
    private MainActivityNavigation activity;

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity = (MainActivityNavigation) getActivity();
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
        activity.getToolbar().setTitle(getResources().getString(R.string.nav_palette));
        activity.getTabLayout().setVisibility(View.GONE);
    }

    /**
     * Al destruirse la vista se coloca el título que la aplicación
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.getTabLayout().setVisibility(tabLayoutVisibility);
        activity.getToolbar().setTitle(toolbarText);
    }

    /**
     * Se coloca el evento onClick a cada una de las preferencias
     */
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        for(CustomColor color: CustomColor.values())
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

        CustomColor colorRes = CustomColor.valueOf(preferenceKey.toUpperCase());
        int color = prefs.getInt(preferenceKey, colorRes.getColor());

        switch (CustomColor.valueOf(preferenceKey.toUpperCase())){

            case APP_PRIMARY:
                theme.colorPrimary(color);
                color = ColorConverter.darken(color, 12);
                prefs.edit().putInt(APP_ACCENT_DARK.toString(), color).apply();
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
