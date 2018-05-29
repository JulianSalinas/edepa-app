package imagisoft.modelview;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;

import com.google.firebase.database.DatabaseReference;
import imagisoft.model.Message;


public abstract class MessagesAdapterOnline
        extends MessagesAdapter implements ChildEventListener{

    /**
     * Referencia donde se encuentran los mensajes
     */
    protected DatabaseReference reference;

    /**
     * Se obtiene el usuario actual o que envía
     */
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public MessagesAdapterOnline(ActivityFragment view) {
        super(view);
        setupReference();
        reference.addChildEventListener(this);
    }


    /**
     * Las subclases debe la referencia donde se extraen
     * cada uno de los mensajes
     */
    protected abstract void setupReference();

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