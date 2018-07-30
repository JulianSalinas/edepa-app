package imagisoft.modelview.chat;

import android.util.Log;
import imagisoft.model.Message;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;


public class ChatFirebase extends ChatAdapter
        implements ChildEventListener, LifecycleObserver {

    public ChatFirebase(ChatFragment chatFragment) {
        super(chatFragment);
        chatFragment.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void connectListener() {
        chatFragment
                .getMainActivity()
                .getChatReference()
                .orderByChild("time")
                .addChildEventListener(this);
        Log.i(toString(), "connectListener()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disconnectListener() {
        chatFragment
                .getMainActivity()
                .getChatReference()
                .removeEventListener(this);
        Log.i(toString(), "disconnectListener()");
    }

    @Override
    public void removeMessage(Message msg){
        chatFragment
                .getMainActivity()
                .getChatReference()
                .child(msg.getKey())
                .removeValue();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message receivedMessage = dataSnapshot.getValue(Message.class);
        if (receivedMessage != null){
            receivedMessage.setKey(dataSnapshot.getKey());
            super.addMessage(receivedMessage);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Message receivedMessage = dataSnapshot.getValue(Message.class);
        if (receivedMessage != null){
            receivedMessage.setKey(dataSnapshot.getKey());
            super.changeMessage(receivedMessage);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Message receivedMessage = dataSnapshot.getValue(Message.class);
        if (receivedMessage != null){
            receivedMessage.setKey(dataSnapshot.getKey());
            super.removeMessage(receivedMessage);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i(toString(), "onChildMoved()");
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
