package edepa.cloud;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import edepa.model.Event;
import edepa.model.Lodging;

public class CloudLodging extends CloudChild {

    /**
     * Interfaz que permite recibir los destinos de la BD
     */
    protected Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addLocation(Lodging lodging);
        void changeLocation(Lodging lodging);
        void removeLocation(Lodging lodging);
    }

    public static Query getLodgingQuery(){
        return Cloud.getInstance()
                .getReference(Cloud.CONGRESS)
                .child("lodging")
                .orderByChild("name");
    }

    public void connect(){
        getLodgingQuery().addChildEventListener(this);
    }

    public void disconnect(){
        getLodgingQuery().removeEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        super.onChildAdded(dataSnapshot, s);
        Lodging lodging = dataSnapshot.getValue(Lodging.class);
        if (lodging != null){
            lodging.setKey(dataSnapshot.getKey());
            callbacks.addLocation(lodging);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        super.onChildChanged(dataSnapshot, s);
        Lodging lodging = dataSnapshot.getValue(Lodging.class);
        if (lodging != null){
            lodging.setKey(dataSnapshot.getKey());
            callbacks.changeLocation(lodging);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        super.onChildRemoved(dataSnapshot);
        Lodging lodging = dataSnapshot.getValue(Lodging.class);
        if (lodging != null){
            lodging.setKey(dataSnapshot.getKey());
            callbacks.removeLocation(lodging);
        }
    }

}
