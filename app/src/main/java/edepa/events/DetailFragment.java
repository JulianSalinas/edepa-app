package edepa.events;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.activity.MainFragment;
import edepa.misc.DateConverter;
import edepa.misc.WallpaperGenerator;
import edepa.model.Cloud;
import edepa.model.Person;
import edepa.model.Preferences;
import edepa.model.ScheduleEvent;
import edepa.modelview.R;
import edepa.people.PersonViewHolder;
import edepa.settings.SettingsLanguage;

import static edepa.settings.SettingsLanguage.ENGLISH;
import static edepa.settings.SettingsLanguage.SPANISH;


public class DetailFragment extends MainFragment implements ValueEventListener {

    private static final String SAVED_EVENT_KEY = "event_state";

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout toolbarContainer;

    @BindView(R.id.toolbar_image)
    KenBurnsView toolbarImage;

    @BindView(R.id.button_back_view)
    View buttonBackView;

    @BindView(R.id.event_detail_location)
    TextView eventDetailLocation;

    @BindView(R.id.event_detail_date_range)
    TextView eventDetailDateRange;

    @BindView(R.id.favorites_amount_view)
    TextView favoritesAmountView;

    @BindView(R.id.event_title)
    TextView eventTitle;

    @BindView(R.id.event_abstract_container)
    LinearLayout eventAbstractContainer;

    @BindView(R.id.event_detail_abstract)
    TextView eventDetailAbstract;

    @BindView(R.id.favorite_button)
    FloatingActionButton favoriteButton;

    @BindView(R.id.event_people_drop_view)
    View eventPeopleDropView;

    @BindView(R.id.people_flip_view)
    EasyFlipView peopleFlipView;

    @BindView(R.id.people_container)
    LinearLayout peopleContainer;

    @BindView(R.id.people_front)
    LinearLayout peopleFront;

    @BindView(R.id.event_download_filename)
    TextView eventDownloadFilename;

    @BindView(R.id.event_download_size)
    TextView eventDownloadSize;

    @BindView(R.id.event_download_view)
    View eventDownloadView;

    @BindView(R.id.event_agenda_view)
    View eventAgendaView;

    @BindView(R.id.event_agenda_date_rage)
    TextView eventAgendaDateRange;

    /**
     * Evento que se coloca en pantalla
     */
    private ScheduleEvent event;

    public ScheduleEvent getEvent() {
        return event;
    }

    private List<Person> people;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.event_detail;
    }

    /**
     * Se obtiene una nueva instancia del fragmento
     * @return DetailFragment
     */
    public static DetailFragment newInstance(ScheduleEvent event) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(SAVED_EVENT_KEY, event);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Obtiene la úbicación del evento en la base de datos
     * @return Query de Firebase
     */
    public Query getEventReference() {
        return Cloud.getInstance()
                .getReference(Cloud.SCHEDULE)
                .child(event.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        people = new ArrayList<>();
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
        setToolbarVisibility(View.GONE);
        updateEvent();
        updateFavoriteIcon();
    }

    private ValueEventListener favoriteListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            event.setFavorite(dataSnapshot.getValue() != null);
            updateFavoriteIcon();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(DetailFragment.this.toString(), databaseError.getMessage());
        }

    };

    @Override
    public void onResume() {
        super.onResume();

        getEventReference().addValueEventListener(this);

        String uid = Cloud.getInstance().getAuth().getUid();

        if (uid != null) {
            Query query = FavoriteHandler.getFavoriteReference(uid, event.getKey());
            query.addValueEventListener(favoriteListener);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        getEventReference().removeEventListener(this);

        String uid = Cloud.getInstance().getAuth().getUid();

        if (uid != null) {
            Query query = FavoriteHandler.getFavoriteReference(uid, event.getKey());
            query.removeEventListener(favoriteListener);
        }

    }

    /**
     * {@inheritDoc}
     * Se obtiene el evento de la base de datos para sincronizar
     * la información
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ScheduleEvent after = dataSnapshot.getValue(ScheduleEvent.class);
        if (after != null) {
            event.setEnd(after.getEnd());
            event.setDate(after.getDate());
            event.setStart(after.getStart());
            event.setTitle(after.getTitle());
            event.setLocation(after.getLocation());
            event.setEventype(after.getEventype());
            event.setBriefSpanish(after.getBriefSpanish());
            event.setBriefEnglish(after.getBriefEnglish());
            event.setFavoritesAmount(after.getFavoritesAmount());
            updateEvent();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i("DetailFragment::", databaseError.getMessage());
    }

    /**
     * Enlaza los componentes visuales con la información
     * del evento
     */
    private void updateEvent(){

        bindTitle();
        bindAbstract();
        bindToolbarImage();

        bindPeople();
        bindLocation();
        bindDateRange();
        bindFavorites();

        buttonBackView.setOnClickListener(v -> getMainActivity().onBackPressed());
    }

    private void bindToolbarImage() {
        WallpaperGenerator gen = new WallpaperGenerator(getMainActivity());
        Drawable wallpaper = gen.getWallpaper(event.getLocation());
        toolbarImage.setImageDrawable(wallpaper);
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
        String block = DateConverter.getBlockString(getMainActivity(),
                        event.getStart(), event.getEnd());
        eventDetailDateRange.setText(block);
        eventAgendaDateRange.setText(block);
    }

    /**
     * Enlaza el botón de favoritos y el texto
     * para saber la cantidad de personas que lo han marcado
     * como favoritos
     */
    private void bindFavorites(){
        int amount = event.getFavoritesAmount();
        favoritesAmountView.setText(String.valueOf(amount));
        favoriteButton.setOnClickListener(v -> updateFavorite());
    }

    /**
     * Actualiza el evento (des) marcado cuando se
     * presiona el boton {@link #favoriteButton}
     */
    private void updateFavorite(){
        FavoriteHandler.updateFavorite(event);
    }

    /**
     * Actualiza el color de icono cuando el
     * usuario (des) marca el evento
     */
    private void updateFavoriteIcon(){
        favoriteButton.setColorFilter(
        getResources().getColor(event.isFavorite() ?
                R.color.material_yellow: R.color.app_primary_font));
    }

    /**
     * Coloca el título del evento
     */
    private void bindTitle(){
        eventTitle.setText(event.getTitle());
    }

    /**
     * Coloca el abstract del evento dependiendo si la aplicación
     * está en ingles o en español. Si no existe el abstrac entonces
     * se remueve esa vista de la interfaz
     */
    private void bindAbstract(){

        String lang = SettingsLanguage.getCurrentLanguage(getContext());

        if (event.getBriefSpanish() == null && event.getBriefEnglish() == null) {
            eventDetailAbstract.setVisibility(View.GONE);
            eventAbstractContainer.setVisibility(View.GONE);
        }

        if (lang.equals(SPANISH) && event.getBriefSpanish() != null)
            eventDetailAbstract.setText(event.getBriefSpanish());

        else if (lang.equals(ENGLISH) && event.getBriefEnglish() != null)
            eventDetailAbstract.setText(event.getBriefEnglish());

        else if (lang.equals(SPANISH) && event.getBriefEnglish() != null)
            eventDetailAbstract.setText(event.getBriefEnglish());

        else eventDetailAbstract.setText(event.getBriefSpanish());

    }

    private ValueEventListener personListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Person person = dataSnapshot.getValue(Person.class);
            if (person != null & !people.contains(person)) {
                people.add(person);
                addPersonView(person);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(toString(), databaseError.getMessage());
        }

    };

    private void bindPeople(){

        Preferences prefs = Preferences.getInstance();
        boolean available = prefs.getBooleanPreference(
                getMainActivity(), Preferences.PEOPLE_AVAILABLE_KEY);

        available = available &&
                event.getPeople() != null &&
                event.getPeople().size() > 0;

        if(!available) {
            peopleFlipView.setVisibility(View.GONE);
            peopleContainer.setVisibility(View.GONE);
            peopleFront.setVisibility(View.GONE);
        }

        else for (String personKey : event.getPeople()) {
            Cloud.getInstance()
                .getReference(Cloud.PEOPLE)
                .child(personKey)
                .addListenerForSingleValueEvent(personListener);
        }

        peopleFlipView.setFlipOnTouch(false);
        peopleFlipView.setOnClickListener(v -> peopleFlipView.flipTheView());

    }

    private void addPersonView(Person person) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.people_item, null);
        View line = getLayoutInflater().inflate(R.layout.custom_line, null);
        new PersonViewHolder(view).bind(person);
        peopleContainer.addView(view, getPersonLayoutParams());
        peopleContainer.addView(line, getLineLayoutParams());
    }

    private LinearLayout.LayoutParams getLineLayoutParams() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
    }

    private LinearLayout.LayoutParams getPersonLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Resources res = getResources();
        params.setMarginStart((int) res.getDimension(R.dimen.space_default));
        params.setMarginEnd((int) res.getDimension(R.dimen.space_default));
        return params;
    }

}