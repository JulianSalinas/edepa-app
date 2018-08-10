package edepa.loaders;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;

/**
 * Esta clase es implementada con el único propósito de no
 * tener que reescribir los métodos onChildMoved y onChildCancelled
 * aún cuando estos no tengan código
 */
public class BaseLoader implements ChildEventListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i(toString(), "onChildAdded(DataSnapshot, String)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.i(toString(), "onChildChanged(DataSnapshot, String)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.i(toString(), "onChildRemoved(DataSnapshot)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(toString(), "onChildMoved(DataSnapshot, String)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), "onCancelled(DatabaseError)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
