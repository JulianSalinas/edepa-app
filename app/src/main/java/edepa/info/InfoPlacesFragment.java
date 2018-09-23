package edepa.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.cloud.CloudPlaces;
import edepa.custom.BaseFragment;
import edepa.minilibs.ViewUtils;
import edepa.model.Place;
import edepa.modelview.R;


public abstract class InfoPlacesFragment extends BaseFragment implements CloudPlaces.Callbacks{

    @BindView(R.id.places_recycler)
    RecyclerView recycler;

    protected List<Place> places;
    protected RecyclerView.Adapter adapter;

    @Override
    public int getResource() {
        return R.layout.info_places;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        places = new ArrayList<>();
        adapter = instantiateAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.setupDividedRecyclerView(getActivity(), recycler, adapter);
        connectData();
    }

    @Override
    public void addLocation(Place place) {
        int index = places.indexOf(place);
        if(index == -1){
            places.add(place);
            adapter.notifyItemInserted(places.size() + 1);
        }
    }

    @Override
    public void changeLocation(Place place) {
        int index = places.indexOf(place);
        if(index != -1){
            places.set(index, place);
            adapter.notifyItemChanged(index);
        }
    }

    @Override
    public void removeLocation(Place place) {
        int index = places.indexOf(place);
        if(index != -1){
            places.remove(index);
            adapter.notifyItemRemoved(index);
        }
    }

    public abstract void connectData();

    public abstract RecyclerView.Adapter instantiateAdapter();

}
