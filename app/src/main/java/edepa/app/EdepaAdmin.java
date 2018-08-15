package edepa.app;

import edepa.model.Cloud;

import android.util.Log;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class EdepaAdmin implements ValueEventListener{

    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    private AdminPermissionListener listener;

    public interface AdminPermissionListener {
        void onPermissionGranted();
        void onPermissionDenied();
    }

    public void setAdminPermissionListener(AdminPermissionListener listener) {
        this.listener = listener;
    }

    public void requestAdminPermission(){
        Query adminReference = getAdminReference();
        if(adminReference != null)
            adminReference.addListenerForSingleValueEvent(this);
    }

    public Query getAdminReference(){
        Cloud cloud = Cloud.getInstance();
        String uid = cloud.getAuth().getUid();
        return uid == null ? null : cloud.getReference(Cloud.ADMINS).child(uid);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String name = dataSnapshot.getValue(String.class);
        this.isAdmin = name != null;
        if(isAdmin) listener.onPermissionGranted();
        else listener.onPermissionDenied();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

}
