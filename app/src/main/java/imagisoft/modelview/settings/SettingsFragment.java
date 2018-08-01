package imagisoft.modelview.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;


import java.util.Objects;

import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainNavigation;

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs_general);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDividerHeight(1);
        ((MainNavigation) Objects
                .requireNonNull(getActivity())).activeTabbedMode();
    }



}
