package edepa.search;

import android.content.Context;
import android.os.Bundle;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import edepa.custom.CustomFragment;
import edepa.minilibs.SmoothLayout;
import edepa.modelview.R;
import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SearchBasicFragment extends CustomFragment
        implements MaterialSearchView.OnQueryTextListener {

    private MaterialSearchView searchView;

    public abstract void clear();

    public abstract void search(String query);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView = getNavigationActivity().getSearchView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchView.setOnQueryTextListener(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.isEmpty())
            clear();
        else search(newText);
        return true;
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
