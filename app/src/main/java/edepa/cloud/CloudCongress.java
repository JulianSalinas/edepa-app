package edepa.cloud;

import android.util.Log;
import edepa.model.Congress;

import com.google.firebase.database.DataSnapshot;


public class CloudCongress extends CloudValue {

    /**
     * Interfaz para recibir actualizaciones de la
     * información del congreso
     */
    private Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void updateCongress(Congress congress);
    }

    /**
     * Se conecta con la BD para recibir actualizaciones
     * @see #disconnect()
     */
    public void connect(){
        Cloud.getInstance()
                .getReference(Cloud.CONGRESS)
                .addValueEventListener(this);
    }

    /**
     * Se desconecta de la BD para dejar de recibir acutalizaciones
     * @see #disconnect()
     */
    public void disconnect(){
        Cloud.getInstance()
                .getReference(Cloud.CONGRESS)
                .removeEventListener(this);
    }
    /**
     * Se ha recibido un nuevo cambio en la información
     * del congreso. Se invoca depués de {@link #connect()}
     * y cada vez que la sección congress de la BD se actualiza
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Congress congress = dataSnapshot.getValue(Congress.class);
        if(congress != null) callbacks.updateCongress(congress);
        Log.i(toString(), String.format("onDataChange(%s)", congress));
    }

}
