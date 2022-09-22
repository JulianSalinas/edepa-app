package edepa.event;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.cloud.CloudFavorites;
import edepa.minilibs.BitmapSave;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.TimeConverter;
import edepa.custom.WallpaperGenerator;
import edepa.model.Event;
import edepa.model.EventType;

import edepa.model.Preferences;
import edepa.modelview.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class EventFragment extends EventHostFragment
        implements ValueEventListener, BitmapSave.SaveListener {

    @BindView(R.id.toolbar_image)
    KenBurnsView toolbarImage;

    @BindView(R.id.event_detail_location)
    TextView eventDetailLocation;

    @BindView(R.id.event_detail_type)
    TextView eventDetailType;

    @BindView(R.id.event_detail_date_range)
    TextView eventDetailDateRange;

    @BindView(R.id.event_detail_favorites)
    TextView eventDetailFavorites;

    @BindView(R.id.event_favorite_button)
    FloatingActionButton favoriteButton;

    @BindView(R.id.event_save_image_button)
    ImageView buttonSaveImage;

    @BindView(R.id.event_emphasis_view)
    View emphasisView;

    @BindView(R.id.button_comments)
    View buttonComments;

    @BindView(R.id.event_view_pager)
    ViewPager eventViewPager;

    @BindView(R.id.button_back_image_icon)
    ImageView buttonBack;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.event_screen;
    }

    /**
     * Se obtiene una nueva instancia del fragmento
     * @return EventFragment
     */
    public static EventFragment newInstance(Event event) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putParcelable(SAVED_EVENT_KEY, event);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarVisibility(GONE);
        bindCurrentEvent();
        bindFavoriteIcon();
        eventViewPager.setAdapter(new EventPagerAdapter());
    }

    @Override
    public void onResume() {
        super.onResume();
        CloudFavorites
                .getSingleFavoriteQuery(event.getKey())
                .addValueEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        CloudFavorites
                .getSingleFavoriteQuery(event.getKey())
                .removeEventListener(this);
    }

    @OnClick(R.id.button_back_view)
    public void onBackPressed(){
        getNavigationActivity().onBackPressed();
    }

    @OnClick(R.id.button_see_map)
    public void openMinimap(){
        super.openMinimap();
    }

    @OnClick(R.id.button_calendar)
    public void openCalendar(){
        super.openCalendar();
    }

    @OnClick(R.id.button_comments)
    public void openComments(){
        boolean hasAdapter = eventViewPager.getAdapter() != null;
        if (hasAdapter && eventViewPager.getAdapter().getCount() > 1){
            eventViewPager.setCurrentItem(1);
        }
    }

    /**
     * Actualiza el evento (des) marcado cuando se
     * presiona el boton {@link #favoriteButton}
     */
    @OnClick(R.id.event_favorite_button)
    public void updateFavorite(){
        CloudFavorites.updateFavorite(event);
    }

    @OnClick(R.id.button_save_image)
    public void buttonSaveImageClick(){
        new DialogFancy.Builder()
                .setContext(getNavigationActivity())
                .setStatus(DialogFancy.INFO)
                .setTitle(R.string.text_save_image)
                .setContent(R.string.text_save_image_content)
                .setExistsCancel(true)
                .setOnAcceptClick(v -> saveImage())
                .build().show();
    }

    public void saveImage(){
        if(toolbarImage.getDrawable() instanceof BitmapDrawable) {
            BitmapSave bitmapSave = new BitmapSave(this);
            BitmapDrawable draw = (BitmapDrawable) toolbarImage.getDrawable();
            bitmapSave.execute(draw.getBitmap());
        }
    }

    @Override
    public void onSaveComplete(Uri uri) {
        showStatusMessage(R.string.text_save_complete);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    /**
     * Enlaza los componentes visuales con la información
     * del evento
     */
    private void bindCurrentEvent(){

        bindLocation();
        bindToolbarImage();
        bindType();
        bindDateRange();
        bindFavorites();

        Context context = getNavigationActivity();
        String key = Preferences.COMMENTS_KEY;
        boolean visible = Preferences.getBooleanPreference(context, key);
        buttonComments.setVisibility(visible ? VISIBLE : GONE);
    }

    private void bindType(){
        EventType type = event.getEventype();
        int color = getResources().getColor(type.getColorResource());
        eventDetailType.setText(type.toString());
        eventDetailType.setBackgroundColor(color);
        emphasisView.setBackgroundColor(color);
    }

    /**
     * Coloca la ubicación por ejemplo "Centro de las Artes"
     * encima de la imagen
     */
    private void bindLocation(){
        String location = event.getLocation();
        eventDetailLocation.setText(location);
    }

    /**
     * Coloca el horario del evento en dos componentes, uno
     * debajo de la ubicación y otro en el cuadro de
     * "Agregar al calendario"
     */
    private void bindDateRange(){
        String date = TimeConverter.extractDate(event.getStart());
        String block = TimeConverter.getBlockString(getNavigationActivity(),
                event.getStart(), event.getEnd());
        String dateRange = String.format("%s %s", date, block);
        dateRange = dateRange.toLowerCase();
        eventDetailDateRange.setText(dateRange);
    }

    /**
     * Enlaza el botón de favoritos y el texto
     * para saber la cantidad de personas que lo han marcado
     * como favoritos
     */
    private void bindFavorites(){
        int amount = event.getFavorites();
        eventDetailFavorites.setText(String.valueOf(amount));
    }

    /**
     * Actualiza el accent de icono cuando el
     * usuario (des) marca el evento
     */
    private void bindFavoriteIcon(){
        favoriteButton.setColorFilter(
                getResources().getColor(event.isFavorite() ?
                R.color.material_amber: R.color.app_primary_font));
    }

    private void bindToolbarImage() {

        String imageUrl = event.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()){

            Glide.with(getNavigationActivity().getApplicationContext())
                    .load(imageUrl)
                    .into(toolbarImage);

            int fontColor = ContextCompat
                    .getColor(activity, R.color.app_white_font);

            ColorStateList fontColorList = ContextCompat
                    .getColorStateList(activity, R.color.app_white_font);

            buttonBack.setBackgroundTintList(fontColorList);
            buttonSaveImage.setBackgroundTintList(fontColorList);

            eventDetailLocation.setTextColor(fontColor);
            eventDetailDateRange.setTextColor(fontColor);

            buttonSaveImage.setVisibility(GONE);
        }

        else {
            WallpaperGenerator gen = new WallpaperGenerator(getNavigationActivity());

            int resource = gen.parseText(event.getLocation());

            // No se encontró imagen y se coloca el fondo por defecto
            if (resource == WallpaperGenerator.NO_IMAGE_FOUND) {
                resource = R.drawable.pattern_white;
            }

            Drawable wallpaper = getResources().getDrawable(resource);
            toolbarImage.setImageDrawable(wallpaper);

            int fontColor = ContextCompat.getColor(
                    activity, resource == R.drawable.pattern_white ?
                            R.color.app_primary_font : R.color.app_white_font);

            ColorStateList fontColorList = ContextCompat.getColorStateList(
                    activity, resource == R.drawable.pattern_white ?
                            R.color.app_primary_font : R.color.app_white_font);

            buttonBack.setBackgroundTintList(fontColorList);
            buttonSaveImage.setBackgroundTintList(fontColorList);

            eventDetailLocation.setTextColor(fontColor);
            eventDetailDateRange.setTextColor(fontColor);

            boolean canBeSaved = wallpaper instanceof BitmapDrawable
                    && resource != R.drawable.pattern_white;
            buttonSaveImage.setVisibility(canBeSaved ? VISIBLE : GONE);
        }
    }

    /**
     * {@inheritDoc}
     * Se obtiene el evento de la base de datos para sincronizar
     * la información
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Object data = dataSnapshot.getValue();
        if (data == null || data instanceof Long) {
            event.setFavorite(dataSnapshot.getValue() != null);
            bindFavoriteIcon();
        }
        else {
            Event newValue = dataSnapshot.getValue(Event.class);
            updateEvent(newValue);
            bindCurrentEvent();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

    public class EventPagerAdapter extends FragmentPagerAdapter {

        public EventPagerAdapter() {
            super(EventFragment.this.getChildFragmentManager());
        }

        @Override
        public int getCount() {
            return Preferences.getBooleanPreference(
                    EventFragment.this.getNavigationActivity(),
                    Preferences.COMMENTS_KEY) ? 2 : 1;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return EventContent.newInstance(event);
            else return EventComments.newInstance(event);
        }

    }

}