package imagisoft.rommie;

import android.util.Log;
import imagisoft.edepa.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public abstract class MessagesViewAdapterOnline extends MessagesViewAdapter {

    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public MessagesViewAdapterOnline(MainViewFragment view) {
        super(view);
    }

    /**
     * Recibe los mensajes de firebase y los coloca en la vista
     */
    protected class MessageViewAdapterChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            msgs.add(dataSnapshot.getValue(Message.class));
            notifyItemInserted(msgs.size()-1);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // No requerido
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            // No requerido
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // No requerido
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(view.getTag(), databaseError.toString());
        }

    }

}