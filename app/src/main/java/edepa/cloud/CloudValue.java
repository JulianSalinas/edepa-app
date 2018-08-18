package edepa.cloud;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Esta clase es implementada con el único propósito de no
 * tener que reescribir los métodos onDataChange y onCancelled
 * aún cuando estos no tengan código
 */
public abstract class CloudValue implements ValueEventListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String data = dataSnapshot.toString();
        Log.i(toString(), String.format("onDataChange(%s)", data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        String error = databaseError.getMessage();
        Log.e(toString(), String.format("onDataChange(%s)", error));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
