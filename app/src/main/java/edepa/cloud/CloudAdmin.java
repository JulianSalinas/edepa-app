package edepa.cloud;

import android.util.Log;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class CloudAdmin extends CloudValue {

    /**
     * Permite determinar si el usuario actual
     * tiene permiso de administrador
     */
    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    /**
     * Listener que permite aplicar a los objetos ajustar
     * su comportamiento cuando se les concede permisos
     */
    private AdminPermissionListener adminPermissionListener;

    public void setAdminPermissionListener(AdminPermissionListener adminPermissionListener) {
        this.adminPermissionListener = adminPermissionListener;
    }

    public interface AdminPermissionListener {
        void onPermissionGranted();
        void onPermissionDenied();
    }

    /**
     * Se solicita de manera asincrona a la BD
     * permisos de administrador
     */
    public void requestAdminPermission(){
        Query adminReference = getAdminReference();
        if(adminReference != null)
            adminReference.addListenerForSingleValueEvent(this);
    }

    /**
     * Obtiene una referencia a la sección de administradores
     * para revisar si el usuario actual está presente
     * @return Query
     */
    public Query getAdminReference(){
        Cloud cloud = Cloud.getInstance();
        String uid = cloud.getUserId();
        return uid == null ? null :
                cloud.getReference(Cloud.ADMINS).child(uid);
    }

    /**
     * Se recibe la respuesta para {@link #requestAdminPermission()}
     * @param dataSnapshot: Si su valor no es null quiere decir
     * que el usuario se encuentra en la lista de administradores
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String name = dataSnapshot.getValue(String.class);
        admin = name != null;
        if(isAdmin()) adminPermissionListener.onPermissionGranted();
        else adminPermissionListener.onPermissionDenied();
    }

    /**
     * Ha ocurrido un error al consultar los permisos
     * y se tiene que denegar los permisos de administrador
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(toString(), databaseError.getMessage());
        adminPermissionListener.onPermissionDenied();
    }

}
