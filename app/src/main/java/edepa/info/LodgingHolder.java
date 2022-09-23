package edepa.info;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.app.MainActivity;
import edepa.app.NavigationActivity;
import edepa.custom.PhotoFragment;
import edepa.minilibs.RegexSearcher;
import edepa.model.Location;
import edepa.model.Place;
import edepa.modelview.R;


public class LodgingHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.place_name)
    TextView itemPlaceName;

    @BindView(R.id.place_web)
    TextView itemPlaceWeb;

    @BindView(R.id.place_phone)
    TextView itemPlacePhone;

    @BindView(R.id.place_description)
    TextView itemPlaceDescription;

    @BindView(R.id.place_facebook)
    TextView itemPlaceFacebook;

    @BindView(R.id.info_place_image)
    ImageView itemPlaceImage;

    @BindView(R.id.info_place_phone)
    View itemInfoPlacePhone;

    @BindView(R.id.info_place_web)
    View itemInfoPlaceWeb;

    @BindView(R.id.info_place_map)
    View itemInfoPlaceMap;

    @BindView(R.id.info_place_facebook)
    View itemInfoPlaceFacebook;

    protected Place place;

    @OnClick(R.id.place_call_button)
    public void openActionDial(){
        if (place.getPhone() != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + place.getPhone()));
            itemView.getContext().startActivity(intent);
        }
    }

    @OnClick(R.id.place_open_map)
    public void openMap(){
        Context context = itemView.getContext();
        if (context instanceof NavigationActivity){
            NavigationActivity activity = (NavigationActivity) context;
            String tag = "MAP:" + place.getKey();
            Fragment frag = new FullMapFragment();
            Bundle args = new Bundle();
            args.putString(FullMapFragment.TAG_KEY, place.getName());
            args.putParcelable(FullMapFragment.LOCATION_KEY, getRealLocation());
            frag.setArguments(args);
            activity.setFragmentOnScreen(frag, tag);
            registerMapLocation(getRealLocation());
        }
    }

    public Location getRealLocation(){
        String placeUrl = place.getLocation();
        Location location = new Location();
        location.setLat(RegexSearcher.findLatitude(placeUrl));
        location.setLng(RegexSearcher.findLongitude(placeUrl));
        return location;
    }

    public void registerMapLocation(Location location){
        double latitude = location.getLat();
        double longitude = location.getLng();
        String format = "map opened at latitude %2.8f and longitude %2.8f";
        String log = String.format(Locale.getDefault(), format, latitude, longitude);
        Log.i(getClass().getSimpleName(), log);
    }

    public LodgingHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Abre una imagen en un fragmento aparte
     */
    @OnClick(R.id.info_place_image)
    public void openImage(){
        Context context = itemView.getContext();
        Fragment imageFragment = PhotoFragment
                .newInstance(place.getName(), place.getImageUrl());
        if(context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            activity.setFragmentOnScreen(imageFragment, place.getKey());
        }
    }

    public void bind(Place place){

        this.place = place;
        itemPlaceName.setText(place.getName());

        int visibility = place.getDescription() != null ? VISIBLE : GONE;
        itemPlaceDescription.setVisibility(visibility);
        itemPlaceDescription.setText(place.getDescription());

        visibility = place.getWeb() != null ? VISIBLE : GONE;
        itemInfoPlaceWeb.setVisibility(visibility);
        if (itemInfoPlaceWeb.getVisibility() ==  VISIBLE) {
            itemPlaceWeb.setText(place.getWeb());
            Linkify.addLinks(itemPlaceWeb, Linkify.ALL);
        }

        visibility = place.getFacebookUrl() != null ? VISIBLE : GONE;
        itemInfoPlaceFacebook.setVisibility(visibility);
        if (itemInfoPlaceFacebook.getVisibility() ==  VISIBLE) {
            itemPlaceFacebook.setText(place.getFacebookUrl());
            Linkify.addLinks(itemPlaceFacebook, Linkify.ALL);
        }

        visibility = place.getPhone() != null ? VISIBLE : GONE;
        itemInfoPlacePhone.setVisibility(visibility);
        if (itemInfoPlacePhone.getVisibility() ==  VISIBLE) {
            itemPlacePhone.setText(place.getPhone());
            Linkify.addLinks(itemPlacePhone, Linkify.ALL);
        }

        visibility = place.getImageUrl() != null ? VISIBLE : GONE;
        itemPlaceImage.setVisibility(visibility);
        if (itemPlaceImage.getVisibility() ==  VISIBLE) {
            Context context = itemView.getContext().getApplicationContext();
            Glide.with(context)
                    .load(place.getImageUrl())
                    .apply(PhotoFragment.getRequestOptions(context))
                    .into(itemPlaceImage);
        }

        visibility = place.getLocation() != null ? VISIBLE : GONE;
        itemInfoPlaceMap.setVisibility(visibility);

    }

}
