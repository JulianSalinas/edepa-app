package edepa.pagers;

import android.support.v4.app.Fragment;

import edepa.schedule.ScheduleOngoing;
import edepa.model.Preferences;


public class TabbedFragmentByType extends TabbedFragmentDefault {

    @Override
    public TabbedAdapter getTabbedAdapter(){
        String currentView = Preferences.getStringPreference(
                getContext(), Preferences.VIEW_KEY);
        return currentView == null || Preferences.VIEW_DEFAULT.equals(currentView) ?
                new TabbedAdapter() : new TabbedAdapterByType();
    }

    public class TabbedAdapterByType extends TabbedFragmentDefault.TabbedAdapter {

        /**
         * Sirve para instanciar cada uno de los tabs
         * @param position: Posición del tab
         * @return PagerScheduleByType | PagerFavoritesByType | ScheduleOngoing
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new PagerScheduleByType();
                case 1: return new PagerFavoritesByType();
                default: return new ScheduleOngoing();
            }
        }

    }

}
