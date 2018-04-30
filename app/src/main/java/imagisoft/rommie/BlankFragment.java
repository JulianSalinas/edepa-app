package imagisoft.rommie;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

public class BlankFragment extends MainViewFragment {

    private String description;

    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(String text){
        BlankFragment blankFragment = new BlankFragment();
        blankFragment.description = text;
        return blankFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.fragment_blank);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        descriptionTextView.setText(description);
    }

}
