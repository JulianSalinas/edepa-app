package edepa.cloud;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;

/**
 * Esta clase es implementada con el único propósito de no
 * tener que reescribir los métodos onChildMoved y onChildCancelled
 * aún cuando estos no tengan código
 */
public abstract class CloudChild implements ChildEventListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String data = dataSnapshot.toString();
        Log.i(toString(), String.format("onChildAdded(%s)", data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String data = dataSnapshot.toString();
        Log.i(toString(), String.format("onChildChanged(%s)", data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String data = dataSnapshot.toString();
        Log.i(toString(), String.format("onChildRemoved(%s)", data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        String data = dataSnapshot.toString();
        Log.i(toString(), String.format("onChildMoved(%s)", data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        String error = databaseError.getMessage();
        Log.i(toString(), String.format("onChildAdded(%s)", error));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
