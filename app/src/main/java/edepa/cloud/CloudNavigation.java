package edepa.cloud;

import com.google.firebase.database.DataSnapshot;


public class CloudNavigation extends CloudValue {

    private CloudNavigationListener navigationListener;

    public interface CloudNavigationListener {
        void onInfoStateChanged(boolean state);
        void onNewsStateChanged(boolean state);
        void onChatStateChanged(boolean state);
        void onPaletteStateChanged(boolean state);
        void onPeopleStateChanged(boolean state);
    }

    private CloudNavigationPreferencesListener navigationPreferencesListener;

    public interface CloudNavigationPreferencesListener {
        void onNavigationPreferenceChanged(String key, boolean state);
    }

    public void setNavigationListener(
            CloudNavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public void setNavigationPreferencesListener(
            CloudNavigationPreferencesListener navigationPreferencesListener) {
        this.navigationPreferencesListener = navigationPreferencesListener;
    }

    public void requestNavigationSections(){
        String [] sections = {"info", "news", "chat", "palette", "people", "comments"};
        for(String section : sections) {
            Cloud.getInstance()
                    .getReference(Cloud.CONFIG)
                    .child(section)
                    .addValueEventListener(this);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        super.onDataChange(dataSnapshot);
        String section = dataSnapshot.getKey();
        Boolean state = dataSnapshot.getValue(Boolean.class);
        if(section != null && state != null){
            if (navigationListener != null) {
                if (section.equals("info"))
                    navigationListener.onInfoStateChanged(state);
                else if (section.equals("news"))
                    navigationListener.onNewsStateChanged(state);
                else if (section.equals("chat"))
                    navigationListener.onChatStateChanged(state);
                else if (section.equals("palette"))
                    navigationListener.onPaletteStateChanged(state);
                else if (section.equals("people"))
                    navigationListener.onPeopleStateChanged(state);
            }
            if (navigationPreferencesListener != null){
                navigationPreferencesListener.onNavigationPreferenceChanged(section, state);
            }
        }
    }

}
