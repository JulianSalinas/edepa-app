package imagisoft.rommie;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.mklimek.circleinitialsview.CircleInitialsView;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.ColorGenerator;
import imagisoft.miscellaneous.SearchNormalizer;


public class ExhibitorsAdapter extends RecyclerView.Adapter
        <ExhibitorsAdapter.ExhibitorViewHolder> implements ChildEventListener{

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private List<Exhibitor> exhibitors;

    /**
     * Referencia a los expositores resultados de la búsqueda
     */
    private List<Exhibitor> filteredExhibitors;

    protected LinkedMultiValueMap<Exhibitor, ScheduleEvent> eventsByExhibitor;

    /**
     * Es un fragmento permite obtener los eventos de un expositor
     */
    private ExhibitorsFragment exhibitorsView;

    /**
     * Se colocan los expositores
     */
    public ExhibitorsAdapter(ExhibitorsFragment exhibitorsView){
        this.exhibitorsView = exhibitorsView;
        this.exhibitors = new ArrayList<>();
        this.eventsByExhibitor = new LinkedMultiValueMap<>();
        this.filteredExhibitors = this.exhibitors;
        setupListener();
    }

    protected void setupListener(){
        exhibitorsView.activity
                .getScheduleReference()
                .addChildEventListener(this);
    }

    /**
     * Requerida para saber la cantidad vistas que se tienen que crear
     */
    @Override
    public int getItemCount() {
        return filteredExhibitors.size();
    }

    /**
     * Crear la vista para un solo expositor
     */
    @Override
    public ExhibitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exhibitors_item, parent, false);
        return new ExhibitorViewHolder(view);
    }

    /**
     * Se enlazan los componentes y se agregan funciones a cada uno
     * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(ExhibitorViewHolder holder, final int position) {

        Exhibitor item = filteredExhibitors.get(holder.getAdapterPosition());

        bindInformation(item, holder);
        String name = item.getCompleteName();
        holder.exhibitorAvatarView.setText(name);
        holder.exhibitorAvatarView.setTextColor(Color.WHITE);

        holder.exhibitorAvatarView
                .setBackgroundColor(ColorGenerator.MATERIAL.getColor(name));

        holder.exhibitorCardView.setOnClickListener(v -> exhibitorsView.switchFragment(
                ExhibitorDetail.newInstance(item, eventsByExhibitor.get(item))));

    }

    /**
     * Coloca la informacíon básica de la persona
     */
    public void bindInformation(Exhibitor exhibitor, ExhibitorViewHolder holder){
        holder.nameTextView.setText(exhibitor.getCompleteName());
        holder.titleTextView.setText(exhibitor.getPersonalTitle());
    }

    /**
     * Según una palabra de búsqueda se filtran todos los expositores
     * @param keyword: Palabra de búsqueda
     */
    public void filter(String keyword){

        keyword = SearchNormalizer.normalize(keyword);

        filteredExhibitors = new ArrayList<>();
        for(Exhibitor exhibitor : exhibitors){

            String name = SearchNormalizer.normalize(exhibitor.getCompleteName());
            String title = SearchNormalizer.normalize(exhibitor.getPersonalTitle());

            if(name.contains(keyword) || title.contains(keyword))
                filteredExhibitors.add(exhibitor);

        }

        // Si ninguno coincide con el criterio de búequeda ex mejor
        // mostrar a todos en vez de no mostrar alguno 
        if (filteredExhibitors.isEmpty())
            filteredExhibitors = exhibitors;

        notifyDataSetChanged();

    }

    /**
     * Clase para enlazar cada uno de los componentes visuales
     */
    class ExhibitorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_text_view)
        TextView nameTextView;

        @BindView(R.id.title_text_view)
        TextView titleTextView;

        @BindView(R.id.exhibitor_card_view)
        CardView exhibitorCardView;

        @BindView(R.id.exhibitor_avatar_view)
        CircleInitialsView exhibitorAvatarView;

        ExhibitorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null && event.getExhibitors() != null){
            for(Exhibitor exhibitor: event.getExhibitors()){
                if(!exhibitors.contains(exhibitor)){
                    int index = Collections.binarySearch(exhibitors, exhibitor);
                    exhibitors.add(-index-1, exhibitor);
                    notifyItemInserted(-index-1);
                }
                eventsByExhibitor.add(exhibitor, event);
                Log.i("Agreg::", exhibitor.getCompleteName() + " " + event.getTitle());
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null && event.getExhibitors() != null){
            for(Exhibitor exhibitor: event.getExhibitors()){
                if(exhibitors.contains(exhibitor)){
                    int index = exhibitors.indexOf(exhibitor);
                    exhibitors.set(index, exhibitor);
                    notifyItemChanged(index);
                }
                else {
                    int index = Collections.binarySearch(exhibitors, exhibitor);
                    exhibitors.add(-index-1, exhibitor);
                    notifyItemInserted(-index-1);
                }
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ScheduleEvent event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null && event.getExhibitors() != null){
            for(Exhibitor exhibitor: event.getExhibitors()){
                if(exhibitors.contains(exhibitor)){
                    int index = exhibitors.indexOf(exhibitor);
                    exhibitors.remove(index);
                    notifyItemRemoved(index);
                }
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}