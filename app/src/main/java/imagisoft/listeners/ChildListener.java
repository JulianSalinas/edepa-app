package imagisoft.listeners;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;

/**
 * Implementación para evitar que las clases tengan que tener
 * estos métodos vacios
 */
public class ChildListener implements ChildEventListener{

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
