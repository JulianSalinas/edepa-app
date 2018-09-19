package edepa.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.cloud.CloudEvents;
import edepa.custom.CustomFragment;
import edepa.minilibs.ReadMoreOption;
import edepa.minilibs.TimeConverter;

import edepa.model.Event;
import edepa.modelview.R;
import edepa.services.DownloadService;
import edepa.settings.SettingsLanguage;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static edepa.event.EventFragment.SAVED_EVENT_KEY;
import static edepa.settings.SettingsLanguage.ENGLISH;
import static edepa.settings.SettingsLanguage.SPANISH;


public class EventContent extends EventHostFragment
        implements ValueEventListener, DownloadService.DownloadListener  {

    private static final int MAX_ABSTRACT_LINES = 5;

    @BindView(R.id.event_agenda_date_rage)
    TextView eventAgendaDateRange;

    @BindView(R.id.event_abstract_container)
    View eventAbstractContainer;

    @BindView(R.id.event_detail_abstract)
    TextView eventDetailAbstract;

    @BindView(R.id.event_see_map_view)
    View eventSeeMapView;

    @BindView(R.id.event_download_view)
    View eventDownloadView;

    @BindView(R.id.event_item_people)
    View eventPeopleView;

    @BindView(R.id.event_title)
    TextView eventTitle;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.event_screen_content;
    }

    /**
     * Se obtiene una nueva instancia del fragmento
     * @return EventFragment
     */
    public static EventContent newInstance(Event event) {
        EventContent fragment = new EventContent();
        Bundle args = new Bundle();
        args.putParcelable(SAVED_EVENT_KEY, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateEventView();
    }

    @OnClick(R.id.event_comments_view)
    public void openComments(){
        Fragment parent = getParentFragment();
        if (parent != null && parent instanceof EventFragment){
            EventFragment eventFragment = (EventFragment) parent;
            eventFragment.openComments();
        }
    }

    @OnClick(R.id.event_calendar_view)
    public void openCalendar(){
        super.openCalendar();
    }

    @OnClick(R.id.event_see_map_view)
    public void openMinimap(){
        super.openMinimap();
    }

    /**
     * Enlaza los componentes visuales con la información
     * del evento
     */
    private void updateEventView(){

        bindTitle();
        bindAbstract();
        bindDateRange();

        EventHolderPeople holderPeople = new EventHolderPeople(eventPeopleView);
        holderPeople.bind(event);

        EventHolderDownload holderDownload = new EventHolderDownload(eventDownloadView);
        holderDownload.bind(this, event);

    }

    /**
     * Coloca el título del evento
     */
    private void bindTitle(){
        eventTitle.setText(event.getTitle());
    }

    /**
     * Coloca el horario del evento en dos componentes, uno
     * debajo de la ubicación y otro en el cuadro de
     * "Agregar al calendario"
     */
    private void bindDateRange(){
        String block = TimeConverter
                .getBlockString(getNavigationActivity(), event.getStart(), event.getEnd());
        eventAgendaDateRange.setText(block);
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

    @Override
    public void onDownloadOffline() {
        showStatusMessage(R.string.text_download_error);
    }

    @Override
    public void onDownloadStarted() {
        showStatusMessage(R.string.text_download_started);
    }

    /**
     * {@inheritDoc}
     * Se obtiene el evento de la base de datos para sincronizar
     * la información
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Event event = dataSnapshot.getValue(Event.class);
        updateEvent(event);
        updateEventView();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i(toString(), databaseError.getMessage());
    }

}