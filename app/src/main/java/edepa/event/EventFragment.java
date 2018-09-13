package edepa.event;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.custom.CustomFragment;
import edepa.cloud.CloudEvents;
import edepa.cloud.CloudFavorites;
import edepa.minilibs.BitmapSave;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.ReadMoreOption;
import edepa.minilibs.TimeConverter;
import edepa.custom.WallpaperGenerator;
import edepa.model.Event;
import edepa.model.EventType;

import edepa.modelview.R;
import edepa.services.DownloadService;
import edepa.settings.SettingsLanguage;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static edepa.settings.SettingsLanguage.ENGLISH;
import static edepa.settings.SettingsLanguage.SPANISH;


public class EventFragment extends CustomFragment
        implements ValueEventListener, DownloadService.DownloadListener, BitmapSave.SaveListener {

    private static final int MAX_ABSTRACT_LINES = 5;
    private static final String SAVED_EVENT_KEY = "event_state";

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar_image)
    KenBurnsView toolbarImage;

    @BindView(R.id.event_detail_location)
    TextView eventDetailLocation;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout toolbarContainer;

    @BindView(R.id.event_title)
    TextView eventTitle;

    @BindView(R.id.event_detail_type)
    TextView eventDetailType;

    @BindView(R.id.event_detail_date_range)
    TextView eventDetailDateRange;

    @BindView(R.id.event_agenda_date_rage)
    TextView eventAgendaDateRange;

    @BindView(R.id.event_abstract_container)
    View eventAbstractContainer;

    @BindView(R.id.event_detail_abstract)
    TextView eventDetailAbstract;

    @BindView(R.id.favorites_amount_view)
    TextView favoritesAmountView;

    @BindView(R.id.event_favorite_button)
    FloatingActionButton favoriteButton;

    @BindView(R.id.event_see_map_view)
    View eventSeeMapView;

    @BindView(R.id.event_download_view)
    View eventDownloadView;

    @BindView(R.id.event_calendar_view)
    View eventCalendarView;

    @BindView(R.id.event_item_people)
    View eventPeopleView;

    @BindView(R.id.button_save_image)
    View buttonSaveImage;

    /**
     * Evento que se coloca en pantalla
     */
    private Event event;

    public Event getEvent() {
        return event;
    }

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
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (args != null && args.containsKey(SAVED_EVENT_KEY))
            event = args.getParcelable(SAVED_EVENT_KEY);
    }

    /**
     * Guarda el evento que fue pasado como argumento
     * @param outState: Bundle donde se guarda el evento
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_EVENT_KEY, event);
    }

    /**
     * Se carga el evento que había antes de girar la pantalla
     * @param savedInstanceState: Bundle donde se carga el evento
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            event = savedInstanceState.getParcelable(SAVED_EVENT_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateEventView();
        updateFavoriteIcon();
        setToolbarVisibility(GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        CloudEvents.getSingleEventQuery(event.getKey()).addValueEventListener(this);
        CloudFavorites.getSingleFavoriteQuery(event.getKey()).addValueEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        CloudEvents.getSingleEventQuery(event.getKey()).removeEventListener(this);
        CloudFavorites.getSingleFavoriteQuery(event.getKey()).removeEventListener(this);
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
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void updateEvent(Event newValue){
        if (newValue != null) {
            event.setEnd(newValue.getEnd());
            event.setDate(newValue.getDate());
            event.setStart(newValue.getStart());
            event.setTitle(newValue.getTitle());
            event.setLocation(newValue.getLocation());
            event.setEventype(newValue.getEventype());
            event.setBriefSpanish(newValue.getBriefSpanish());
            event.setBriefEnglish(newValue.getBriefEnglish());
            event.setFavorites(newValue.getFavorites());
            updateEventView();
        }
    }

    /**
     * Enlaza los componentes visuales con la información
     * del evento
     */
    private void updateEventView(){

        if (!isTheSameLocation()) {
            bindToolbarImage();
            bindLocation();
        }

        bindType();
        bindTitle();
        bindAbstract();

        bindDateRange();
        bindFavorites();

        EventHolderSeeMap holderSeeMap = new EventHolderSeeMap(eventSeeMapView);
        holderSeeMap.bind(getContext());

        EventHolderCalendar holderCalendar = new EventHolderCalendar(eventCalendarView);
        holderCalendar.bind(event);

        EventHolderPeople holderPeople = new EventHolderPeople(eventPeopleView);
        holderPeople.bind(event);

        EventHolderDownload holderDownload = new EventHolderDownload(eventDownloadView);
        holderDownload.bind(this, event);

    }

    @OnClick(R.id.button_back_view)
    public void onBackPressedd(){
        getNavigationActivity().onBackPressed();
    }

    private boolean isTheSameLocation(){
        return eventDetailLocation.getText().toString().equals(event.getLocation());
    }

    private void bindType(){
        EventType type = event.getEventype();
        eventDetailType.setText(type.toString());
        eventDetailType.setBackgroundResource(type.getColorResource());
    }

    private void bindToolbarImage() {
        WallpaperGenerator gen = new WallpaperGenerator(getNavigationActivity());
        Drawable wallpaper = gen.getWallpaper(event);
        Glide.with(this)
                .load(wallpaper)
                .into(toolbarImage);
        boolean canBeSaved = wallpaper instanceof BitmapDrawable;
        buttonSaveImage.setVisibility(canBeSaved ? VISIBLE : GONE);
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
        eventAgendaDateRange.setText(block);
    }

    /**
     * Enlaza el botón de favoritos y el texto
     * para saber la cantidad de personas que lo han marcado
     * como favoritos
     */
    private void bindFavorites(){
        int amount = event.getFavorites();
        favoritesAmountView.setText(String.valueOf(amount));
    }

    /**
     * Actualiza el accent de icono cuando el
     * usuario (des) marca el evento
     */
    private void updateFavoriteIcon(){
        favoriteButton.setColorFilter(
        getResources().getColor(event.isFavorite() ?
                R.color.material_amber: R.color.app_primary_font));
    }

    /**
     * Coloca el título del evento
     */
    private void bindTitle(){
        eventTitle.setText(event.getTitle());
    }

    @Override
    public void onDownloadOffline() {
        showStatusMessage(R.string.text_download_error);
    }

    @Override
    public void onDownloadStarted() {
        showStatusMessage(R.string.text_download_started);
    }

    /**
     * Coloca el abstract del evento dependiendo si la aplicación
     * está en ingles o en español. Si no existe el abstrac entonces
     * se remueve esa vista de la interfaz
     */
    private void bindAbstract(){

        if (event.getBriefSpanish() == null && event.getBriefEnglish() == null) {
            eventDetailAbstract.setVisibility(GONE);
            eventAbstractContainer.setVisibility(GONE);
        }
        else {
            eventDetailAbstract.setVisibility(VISIBLE);
            eventAbstractContainer.setVisibility(VISIBLE);
            ReadMoreOption readMoreOption = new ReadMoreOption
                    .Builder(getNavigationActivity())
                    .textLength(MAX_ABSTRACT_LINES, ReadMoreOption.TYPE_LINE)
                    .moreLabel(R.string.text_show_more)
                    .lessLabel(R.string.text_show_less)
                    .moreLabelColorRes(R.color.app_accent)
                    .lessLabelColorRes(R.color.app_accent)
                    .labelUnderLine(true)
                    .expandAnimation(true)
                    .build();
            readMoreOption.addReadMoreTo(eventDetailAbstract, getAbstract(event));
            eventDetailAbstract.setText(getAbstract(event));
        }

    }

    public String getAbstract(Event event){
        String lang = SettingsLanguage.getCurrentLanguage(getContext());
        return lang.equals(SPANISH) && event.getBriefSpanish() != null ?
                event.getBriefSpanish():
                lang.equals(ENGLISH) && event.getBriefEnglish() != null ?
                event.getBriefEnglish():
                lang.equals(SPANISH) && event.getBriefEnglish() != null ?
                event.getBriefEnglish():
                event.getBriefSpanish();
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
            updateFavoriteIcon();
        }
        else {
            Event newValue = dataSnapshot.getValue(Event.class);
            updateEvent(newValue);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

}