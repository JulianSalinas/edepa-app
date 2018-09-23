package edepa.info;

import edepa.cloud.CloudPlaces;


public class InfoNearPlacesFragment extends InfoLodgingFragment {

    @Override
    public void connectData() {
        CloudPlaces cloud = new CloudPlaces();
        cloud.setCallbacks(this);
        cloud.connectNearPlaces();
    }

}
