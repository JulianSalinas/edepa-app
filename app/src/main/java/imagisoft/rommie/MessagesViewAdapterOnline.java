package imagisoft.rommie;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

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
            String receivedDate = DateConverter.extractDate(receivedMessage.getTime());

            if (lastMessage == null) {
                lastMessage = receivedMessage;
                msgs.add(new Timestamp(receivedMessage.getTime()));
                notifyItemInserted(msgs.size()-1);;
            }

            String lastReceivedDate = DateConverter.extractDate(lastMessage.getTime());

            if(receivedDate.equals(lastReceivedDate)){
                msgs.add(receivedMessage);
                notifyItemInserted(msgs.size()-1);
            }
            else {
                msgs.add(new Timestamp(receivedMessage.getTime()));
                notifyItemInserted(msgs.size()-1);
                msgs.add(receivedMessage);
                notifyItemInserted(msgs.size()-1);
            }

            lastMessage = receivedMessage;

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