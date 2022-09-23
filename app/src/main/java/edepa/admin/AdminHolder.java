package edepa.admin;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import edepa.cloud.Cloud;
import edepa.cloud.CloudAdmin;
import edepa.model.UserProfile;
import edepa.modelview.R;

public class AdminHolder extends RecyclerView.ViewHolder
        implements CloudAdmin.AdminPermissionListener {

    @BindView(R.id.admins_item_avatar)
    ImageView itemAvatar;

    @BindView(R.id.admins_item_username)
    TextView itemUsername;

    @BindView(R.id.admins_item_email)
    TextView itemEmail;

    @BindView(R.id.admins_item_checkbox)
    CheckBox itemCheckbox;

    private UserProfile userProfile;

    @OnCheckedChanged(R.id.admins_item_checkbox)
    public void changeAdminPermission(boolean checked){
        Cloud.getInstance()
                .getReference(Cloud.ADMINS)
                .child(userProfile.getUserid())
                .setValue(checked ? userProfile.getUsername() : null);
    }

    public AdminHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UserProfile userProfile){

        this.userProfile = userProfile;
        itemUsername.setText(userProfile.getUsername());
        itemEmail.setText(userProfile.getEmail());
        boolean allowPhoto = userProfile.getAllowPhoto() != null && userProfile.getAllowPhoto();

        Glide.with(itemView.getContext().getApplicationContext())
                .load(allowPhoto ?
                        userProfile.getPhotoUrl() :
                        R.drawable.img_user)
                .apply(new RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.img_user)
                        .error(R.drawable.img_user))
                .into(itemAvatar);

        CloudAdmin cloudAdmin = new CloudAdmin();
        cloudAdmin.setAdminPermissionListener(this);
        cloudAdmin.requestAdminPermission(userProfile.getUserid());
    }

    @Override
    public void onPermissionGranted() {
        itemCheckbox.setChecked(true);
    }

    @Override
    public void onPermissionDenied() {
        itemCheckbox.setChecked(false);
    }

}
