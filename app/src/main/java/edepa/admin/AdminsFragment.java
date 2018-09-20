package edepa.admin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import edepa.cloud.Cloud;
import edepa.cloud.CloudUsers;
import edepa.custom.CustomFragment;
import edepa.modelview.R;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class AdminsFragment extends CustomFragment {

    @BindView(R.id.admins_recycler)
    RecyclerView adminsRecycler;

    private AdminsAdapter adminsAdapter;

    @Override
    public int getResource() {
        return R.layout.admins_screen;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarText(R.string.text_admins);

        adminsAdapter = new AdminsAdapter();
        Cloud.getInstance()
                .getReference(Cloud.USERS)
                .addChildEventListener(adminsAdapter);

        Context context = getNavigationActivity();
        DividerItemDecoration decoration =
                new DividerItemDecoration(context, VERTICAL);
        decoration.setDrawable(context
                .getResources().getDrawable(R.drawable.util_decorator));
        adminsRecycler.addItemDecoration(decoration);
        adminsRecycler.setAdapter(adminsAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Cloud.getInstance()
                .getReference(Cloud.USERS)
                .removeEventListener(adminsAdapter);
    }

}
