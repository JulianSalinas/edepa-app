package edepa.cloud;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import edepa.minilibs.RegexSearcher;
import edepa.model.UserProfile;


public class CloudUsers extends CloudValue {

    private UserDataListener userProfileListener;

    public interface UserDataListener {
        void onUserInfoReady(UserProfile userProfile);
    }

    public void setUserProfileListener(UserDataListener userProfileListener) {
        this.userProfileListener = userProfileListener;
    }

    public void requestUserInfo(String userid){
        getUserReference(userid).keepSynced(true);
        getUserReference(userid).addListenerForSingleValueEvent(this);
    }

    public void requestCurrentUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getUserReference(user.getUid()).keepSynced(true);
            getUserReference(user.getUid()).addListenerForSingleValueEvent(this);
        }
    }

    public static DatabaseReference getUserReference(String userid){
        return Cloud.getInstance()
                .getReference(Cloud.USERS)
                .child(userid).getRef();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && dataSnapshot.getValue() == null){
            getUserReference(user.getUid())
                    .child("username")
                    .setValue(user.getDisplayName());

            getUserReference(user.getUid())
                    .child("allowPhoto")
                    .setValue(true);

        }
        if (user != null){

            if (user.getPhotoUrl() != null) {
                getUserReference(user.getUid())
                        .child("photoUrl")
                        .setValue(user.getPhotoUrl().toString());
            }

            getUserReference(user.getUid())
                    .child("email")
                    .setValue(user.getEmail());

        }
        if (userProfileListener != null && dataSnapshot.getValue() != null){
            UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
            if (userProfile != null) {
                userProfile.setUserid(dataSnapshot.getKey());
                userProfileListener.onUserInfoReady(userProfile);
            }
        }
    }

    /**
     * Usada para para obtener el nombre de pila de usuario
     * Este se extrae de la información que se obtiene con el Login
     * @return Nombre de pila del usuario
     */
    private String getDefaultUsername(String displayName){
        if (displayName == null) return "";
        return RegexSearcher.findFirstName(displayName);
    }

}
