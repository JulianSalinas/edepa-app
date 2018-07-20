package imagisoft.modelview;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import imagisoft.model.Message;

public class ChatFirebase extends ChatAdapter
        implements ChildEventListener, LifecycleObserver {

    public ChatFirebase(ChatFragment chatFragment) {
        super(chatFragment);
        chatFragment.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        chatFragment.activity
                .getChatReference()
                .orderByChild("time")
                .addChildEventListener(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectListener() {
        chatFragment.activity
                .getChatReference()
                .removeEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message receivedMessage = dataSnapshot.getValue(Message.class);
        if (receivedMessage != null){
            receivedMessage.setKey(dataSnapshot.getKey());
            msgs.add(receivedMessage);
            notifyItemInserted(msgs.size()-1);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Message receivedMessage = dataSnapshot.getValue(Message.class);
        if (receivedMessage != null){
            receivedMessage.setKey(dataSnapshot.getKey());
            int index = msgs.indexOf(receivedMessage);
            msgs.set(index, receivedMessage);
            notifyItemChanged(index);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Message receivedMessage = dataSnapshot.getValue(Message.class);
        if (receivedMessage != null){
            receivedMessage.setKey(dataSnapshot.getKey());
            int index = msgs.indexOf(receivedMessage);
            msgs.remove(receivedMessage);
            notifyItemRemoved(index);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(toString(), s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.toString());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
