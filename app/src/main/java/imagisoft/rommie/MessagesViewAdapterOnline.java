package imagisoft.rommie;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;

import imagisoft.edepa.Message;
import imagisoft.edepa.Timestamp;
import imagisoft.miscellaneous.DateConverter;


public abstract class MessagesViewAdapterOnline extends MessagesViewAdapter {

    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public MessagesViewAdapterOnline(MainActivityFragment view) {
        super(view);
    }

    /**
     * Recibe los mensajes de firebase y los coloca en la vista
     */
    protected class MessageViewAdapterChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            Message receivedMessage = dataSnapshot.getValue(Message.class);
            if (receivedMessage != null){
                msgs.add(receivedMessage);
                notifyItemInserted(msgs.size()-1);
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Message receivedMessage = dataSnapshot.getValue(Message.class);
            int index = msgs.indexOf(receivedMessage);
            msgs.set(index, receivedMessage);
            notifyItemChanged(index);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Message receivedMessage = dataSnapshot.getValue(Message.class);
            int index = msgs.indexOf(receivedMessage);
            msgs.remove(receivedMessage);
            notifyItemRemoved(index);
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