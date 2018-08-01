package imagisoft.modelview.news;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import imagisoft.model.Cloud;
import imagisoft.model.NewsItem;

public class NewsFirebase extends NewsAdapter implements ChildEventListener {

    public NewsFirebase(NewsFragment fragment) {
        super(fragment);
        Cloud.getInstance()
                .getReference(Cloud.NEWS)
                .orderByChild("time")
                .addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        NewsItem item = dataSnapshot.getValue(NewsItem.class);
        if(item != null) {
            item.setKey(dataSnapshot.getKey());
            addItem(item);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        NewsItem item = dataSnapshot.getValue(NewsItem.class);
        if(item != null) {
            item.setKey(dataSnapshot.getKey());
            changeItem(item);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        NewsItem item = dataSnapshot.getValue(NewsItem.class);
        if(item != null) {
            item.setKey(dataSnapshot.getKey());
            removeItem(item);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
