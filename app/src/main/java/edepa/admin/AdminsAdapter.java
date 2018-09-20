package edepa.admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import edepa.model.UserProfile;
import edepa.modelview.R;

public class AdminsAdapter extends RecyclerView.Adapter implements ChildEventListener {

    private List<UserProfile> userProfiles;

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }

    public AdminsAdapter() {
        this.userProfiles = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admins_item, parent, false);
        return new AdminHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int truePosition = holder.getAdapterPosition();
        UserProfile userProfile = userProfiles.get(truePosition);
        AdminHolder adminHolder = (AdminHolder) holder;
        adminHolder.bind(userProfile);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
        if (userProfile != null){
            userProfile.setUserid(dataSnapshot.getKey());
            int index = userProfiles.indexOf(userProfile);
            if (index == -1){
                userProfiles.add(userProfile);
                notifyItemInserted(userProfiles.size() - 1);
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
        if (userProfile != null){
            userProfile.setUserid(dataSnapshot.getKey());
            int index = userProfiles.indexOf(userProfile);
            if (index != -1){
                userProfiles.set(index, userProfile);
                notifyItemChanged(index);
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
        if (userProfile != null){
            userProfile.setUserid(dataSnapshot.getKey());
            int index = userProfiles.indexOf(userProfile);
            if (index != -1){
                userProfiles.remove(userProfile);
                notifyItemRemoved(index);
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
