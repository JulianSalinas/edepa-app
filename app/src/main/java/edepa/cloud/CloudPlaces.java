package edepa.cloud;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import edepa.model.Place;

public class CloudPlaces extends CloudChild {

    /**
     * Interfaz que permite recibir los destinos de la BD
     */
    protected Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addLocation(Place place);
        void changeLocation(Place place);
        void removeLocation(Place place);
    }

    public static Query getLodgingQuery(){
        return Cloud.getInstance()
                .getReference("lodging")
                .orderByChild("name");
    }

    public static Query getRestaurantsQuery(){
        return Cloud.getInstance()
                .getReference("restaurants")
                .orderByChild("name");
    }

    public void connectLodging(){
        getLodgingQuery().addChildEventListener(this);
    }

    public void disconnectLodging(){
        getLodgingQuery().removeEventListener(this);
    }

    public void connectRestaurants(){
        getRestaurantsQuery().addChildEventListener(this);
    }

    public void disconnectRestaurants(){
        getRestaurantsQuery().removeEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        super.onChildAdded(dataSnapshot, s);
        Place lodging = dataSnapshot.getValue(Place.class);
        if (lodging != null){
            lodging.setKey(dataSnapshot.getKey());
            callbacks.addLocation(lodging);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        super.onChildChanged(dataSnapshot, s);
        Place lodging = dataSnapshot.getValue(Place.class);
        if (lodging != null){
            lodging.setKey(dataSnapshot.getKey());
            callbacks.changeLocation(lodging);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        super.onChildRemoved(dataSnapshot);
        Place lodging = dataSnapshot.getValue(Place.class);
        if (lodging != null){
            lodging.setKey(dataSnapshot.getKey());
            callbacks.removeLocation(lodging);
        }
    }

}
