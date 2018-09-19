package edepa.cloud;

import edepa.model.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;


public class CloudComments extends CloudChild {

    protected Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void addComment(Comment comment);
        void removeComment(Comment comment);
        void changeComment(Comment comment);
    }

    public void connect(Query query){
        query.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Comment comment = dataSnapshot.getValue(Comment.class);
        if (comment != null){
            comment.setKey(dataSnapshot.getKey());
            callbacks.addComment(comment);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Comment comment = dataSnapshot.getValue(Comment.class);
        if (comment != null){
            comment.setKey(dataSnapshot.getKey());
            callbacks.changeComment(comment);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Comment comment = dataSnapshot.getValue(Comment.class);
        if (comment != null){
            comment.setKey(dataSnapshot.getKey());
            callbacks.removeComment(comment);
        }
    }

}
