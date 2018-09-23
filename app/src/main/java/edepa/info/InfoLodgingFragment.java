package edepa.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import edepa.cloud.CloudPlaces;
import edepa.modelview.R;


public class InfoLodgingFragment extends InfoPlacesFragment {

    @Override
    public void connectData() {
        CloudPlaces cloud = new CloudPlaces();
        cloud.setCallbacks(this);
        cloud.connectLodging();
    }

    @Override
    public RecyclerView.Adapter instantiateAdapter() {
        return new LodgingAdapter();
    }

    public class LodgingAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return places.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LodgingHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.info_lodging, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LodgingHolder lodgingHolder = (LodgingHolder) holder;
            lodgingHolder.bind(places.get(holder.getAdapterPosition()));
        }

    }

}
