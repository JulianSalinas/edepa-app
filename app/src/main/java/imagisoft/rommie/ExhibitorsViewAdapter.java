package imagisoft.rommie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import imagisoft.edepa.Exhibitor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.views.AvatarView;
import imagisoft.edepa.ScheduleBlock;
import imagisoft.edepa.ScheduleEvent;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class ExhibitorsViewAdapter extends RecyclerView.Adapter<ExhibitorsViewAdapter.ExhibitorViewHolder> {

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private ArrayList<Exhibitor> exhibitors;

    /**
     * Referencia al objeto que adapta
     */
    private ExhibitorsView exhibitorsView;

    /**
     * Contructor donde se agrega un listener a los eventos con el fin de tomar
     * los expositores de cada uno
     */
    public ExhibitorsViewAdapter(ExhibitorsView exhibitorsView){
        this.exhibitors = new ArrayList<>();
        this.exhibitorsView = exhibitorsView;

        exhibitorsView
                .getFirebase()
                .getScheduleReference()
                .addValueEventListener(new ExhibitorsViewAdapterValueEventListener());
    }

    /**
     * Requerida para saber la cantidad vistas que se tiene que crear
     */
    @Override
    public int getItemCount() {
        return exhibitors.size();
    }

    /**
     * No usar código en ésta función
     */
    @Override
    public ExhibitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exhibitors_item, parent, false);
        return new ExhibitorsViewAdapter.ExhibitorViewHolder(view);
    }


    /**
     * Se enlazan los componentes y se agregan funciones a cada uno
     * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
     */
    @Override
    public void onBindViewHolder(ExhibitorViewHolder holder, final int position) {

        Exhibitor item = exhibitors.get(holder.getAdapterPosition());

        // Rellana todos los espacios de la actividad
        holder.name.setText(item.getCompleteName());
        holder.title.setText(item.getPersonalTitle());

        // Coloca la primra letra del nombre como el avatar
        AvatarPlaceholder placeholder = new AvatarPlaceholder(item.getCompleteName(), 30);
        holder.avatar.setImageDrawable(placeholder);

    }

    /**
     * Calse para enlzar cada uno de los componentes visuales de la actividad.
     * Es necesario que esta clase este anidada, asi que, no mover!
     */
    class ExhibitorViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView title;
        AvatarView avatar;

        ExhibitorViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.exhibitor_item_name);
            this.title= view.findViewById(R.id.exhibitor_item_title);
            this.avatar = view.findViewById(R.id.exhibitor_item_avatar);
        }

    }

    /**
     * Clase que conecta las fechas del paginador con las extraídas del
     * cronograma
     */
    class ExhibitorsViewAdapterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            // Se extraen todos los eventos de firebase
            ArrayList<ScheduleEvent> scheduleEvents = new ArrayList<>();
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                scheduleEvents.add(postSnapshot.getValue(ScheduleEvent.class));

            // Por cada evento se extren los expositores
            for(ScheduleEvent event : scheduleEvents)
                exhibitors.addAll(event.getExhibitors());

            // Se ordenan alfabeticamente
            Collections.sort(exhibitors, (e1, e2) ->
                    e1.getCompleteName().compareTo(e2.getCompleteName()));

            notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // TODO: Manejar el error
        }

    }

}