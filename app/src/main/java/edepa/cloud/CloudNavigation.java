package edepa.cloud;

import com.google.firebase.database.DataSnapshot;


public class CloudNavigation extends CloudValue {

    private CloudNavigationListener navigationListener;

    public interface CloudNavigationListener {
        void onInfoStateChange(boolean state);
        void onNewsStateChange(boolean state);
        void onChatStateChange(boolean state);
        void onPaletteStateChange(boolean state);
        void onPeopleStateChange(boolean state);
        void onCommentsStateChange(boolean state);
    }

    public void setNavigationListener(CloudNavigationListener navigationListener) {
        this.navigationListener = navigationListener;
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
            if (section.equals("info"))
                navigationListener.onInfoStateChange(state);
            else if (section.equals("news"))
                navigationListener.onNewsStateChange(state);
            else if (section.equals("chat"))
                navigationListener.onChatStateChange(state);
            else if (section.equals("palette"))
                navigationListener.onPaletteStateChange(state);
            else if (section.equals("people"))
                navigationListener.onPeopleStateChange(state);
            else if (section.equals("comments"))
                navigationListener.onCommentsStateChange(state);
        }
    }

}
