package imagisoft.rommie;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.aesthetic.Aesthetic;
import com.kizitonwose.colorpreferencecompat.ColorPreferenceCompat;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterOpacitySlider;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

import imagisoft.miscellaneous.ColorConverter;

import static imagisoft.rommie.CustomColor.APP_ACCENT_DARK;
import static imagisoft.rommie.CustomColor.APP_PRIMARY;


public class ThemePicker extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener{

    /**
     * Se obtiene las preferencias para obtener los colores
     */
    private SharedPreferences prefs;
    private CharSequence toolbarText;
    private MainActivityNavigation activity;

    public ThemePicker() {
        // se requiere el constructor vacio
    }

    /**
     * Se inicializan las preferencias
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity = (MainActivityNavigation) getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        toolbarText = activity.getToolbar().getTitle();
        activity.getToolbar().setTitle(getResources().getString(R.string.nav_palette));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View colorView = inflater.inflate(R.layout.dialog_color, null);

        // Color por defecto para inicializar el último color usado en la vista
        int color = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getInt(APP_PRIMARY.toString(), Color.BLACK);

        // Se la pantalla para seleccionar
        final LobsterPicker lobsterPicker = colorView.findViewById(R.id.lobster_picker);
        LobsterShadeSlider shadeSlider = colorView.findViewById(R.id.shade_slider);
        LobsterOpacitySlider opacitySlider = colorView.findViewById(R.id.opacity_slider);

        lobsterPicker.addDecorator(shadeSlider);
        lobsterPicker.addDecorator(opacitySlider);
        lobsterPicker.setColorHistoryEnabled(true);
        lobsterPicker.setHistory(color);
        lobsterPicker.setColor(color);

        // Se muestra la pantalla para selecciona el color
        new AlertDialog.Builder(getActivity())
                .setView(colorView)
                .setTitle(getResources().getString(R.string.text_choose_color))
                .setPositiveButton(getResources().getString(R.string.text_save), (dialogInterface, i) -> {
                    ((ColorPreferenceCompat) preference).setValue(lobsterPicker.getColor());
                    changeCustomColor(preference.getKey());
                })
                .setNegativeButton(getResources().getString(R.string.text_cancel), null)
                .show();
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
