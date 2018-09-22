package edepa.info;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import edepa.cloud.CloudPlaces;
import edepa.modelview.R;


public class InfoRestaurantsFragment extends InfoPlacesFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CloudPlaces cloud = new CloudPlaces();
        cloud.setCallbacks(this);
        cloud.connectRestaurants();
    }

    @Override
    public RecyclerView.Adapter instantiateAdapter() {
        return new RestaurantsAdapter();
    }

    public class RestaurantsAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return places.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RestaurantHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.info_restaurant, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RestaurantHolder restaurantHolder = (RestaurantHolder) holder;
            restaurantHolder.bind(places.get(holder.getAdapterPosition()));
        }

    }

}
