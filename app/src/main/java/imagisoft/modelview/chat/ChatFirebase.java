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
                .getActivityCustom()
                .getChatReference()
                .orderByChild("time")
                .addChildEventListener(this);
        Log.i(toString(), "connectListener()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disconnectListener() {
        chatFragment
                .getActivityCustom()
                .getChatReference()
                .removeEventListener(this);
        Log.i(toString(), "disconnectListener()");
    }

    @Override
    public void removeMessage(Message msg){
        chatFragment
                .getActivityCustom()
                .getChatReference()
                .child(msg.getKey())
                .removeValue();
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
