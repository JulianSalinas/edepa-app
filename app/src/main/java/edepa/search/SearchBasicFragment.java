package edepa.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import edepa.app.ActivityNavig;
import edepa.app.CustomFragment;
import edepa.minilibs.SmoothLayout;
import edepa.modelview.R;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public abstract class SearchBasicFragment extends CustomFragment
        implements MaterialSearchView.OnQueryTextListener {

    public abstract void search(String query);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getNavigationActivity().getSearchView().setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.isEmpty())
            returnToSearchByPanelFragment();
        else search(newText);
        return false;
    }

    public void returnToSearchByPanelFragment(){
        ActivityNavig activity = getNavigationActivity();

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, new SearchByPanelFragment())
                .commit();

        // activity.openSearchByPanel();
    }

    public void setupRecycler(RecyclerView recycler, RecyclerView.Adapter adapter){
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(new SmoothLayout(getActivity()));
        recycler.addItemDecoration(createDecoration());
        recycler.setNestedScrollingEnabled(false);
    }

    public DividerItemDecoration createDecoration(){
        return getContext() == null ? null : createDecoration(getContext());
    }

    public DividerItemDecoration createDecoration(Context context){
        DividerItemDecoration item = new DividerItemDecoration(context, VERTICAL);
        int resource = R.drawable.util_decorator;
        item.setDrawable(getResources().getDrawable(resource));
        return item;
    }

}
