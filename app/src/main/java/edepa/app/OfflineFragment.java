package edepa.app;

import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.modelview.R;

public class OfflineFragment extends SimpleFragment {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public int getResource() {
        return R.layout.offline_view;
    }

    @OnClick(R.id.retry_button)
    public void retryConnection(View button){
        if (getContext() instanceof SignInActivity) {

            SignInActivity activity = (SignInActivity) getContext();
//            Intent intent = new Intent(getActivity(), SignInActivity.class);
//
//            if (activity.getIntent() != null) {
//                intent.setAction(activity.getIntent().getAction());
//                intent.setType(activity.getIntent().getType());
//            }
//
//            if (activity.getIntent().getExtras() != null) {
//                intent.putExtras(activity.getIntent().getExtras());
//            }
//
//            // startActivity(intent);

            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(activity::recreate, 3000);

        }
    }

}
