package imagisoft.modelview.about;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import imagisoft.model.Cloud;
import imagisoft.modelview.views.ImageFragment;


public class MapFragment extends ImageFragment implements ValueEventListener{

    private static final String IMG_KEY = "minimap";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Cloud.getInstance()
                .getReference(Cloud.CONFIG).child(IMG_KEY)
                .addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        imageUrl = dataSnapshot.getValue(String.class);
        loadAsyncImage();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Error
    }

}
