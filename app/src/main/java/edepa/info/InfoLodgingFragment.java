package edepa.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.app.SimpleFragment;
import edepa.cloud.CloudLodging;
import edepa.minilibs.SmoothLayout;
import edepa.model.Lodging;
import edepa.modelview.R;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class InfoLodgingFragment extends SimpleFragment implements CloudLodging.Callbacks{

    @BindView(R.id.lodging_recycler)
    RecyclerView lodgingRecycler;

    private List<Lodging> lodgings;

    private LodgingAdapter lodgingAdapter;

    @Override
    public int getResource() {
        return R.layout.info_lodging;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CloudLodging cloud = new CloudLodging();
        cloud.setCallbacks(this);
        cloud.connect();

        lodgings = new ArrayList<>();
        lodgingAdapter = new LodgingAdapter();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lodgingRecycler.setAdapter(lodgingAdapter);
        lodgingRecycler.setLayoutManager(new SmoothLayout(getContext()));
        lodgingRecycler.addItemDecoration(getDecoration());
    }

    public DividerItemDecoration getDecoration(){
        if (getContext() != null) {
            DividerItemDecoration decoration =
                    new DividerItemDecoration(getContext(), VERTICAL);
            decoration.setDrawable(
                    getResources().getDrawable(R.drawable.util_decorator));
            return decoration;
        }
        return null;
    }

    @Override
    public void addLocation(Lodging lodging) {
        int index = lodgings.indexOf(lodging);
        if(index == -1){
            lodgings.add(lodging);
            lodgingAdapter.notifyItemInserted(lodgings.size() + 1);
        }
    }

    @Override
    public void changeLocation(Lodging lodging) {
        int index = lodgings.indexOf(lodging);
        if(index != -1){
            lodgings.set(index, lodging);
            lodgingAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void removeLocation(Lodging lodging) {
        int index = lodgings.indexOf(lodging);
        if(index != -1){
            lodgings.remove(index);
            lodgingAdapter.notifyItemRemoved(index);
        }
    }

    public class LodgingAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return lodgings.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LodgingHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lodging_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LodgingHolder lodgingHolder = (LodgingHolder) holder;
            lodgingHolder.bind(lodgings.get(holder.getAdapterPosition()));
        }

    }

}
