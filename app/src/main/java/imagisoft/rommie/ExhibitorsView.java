package imagisoft.rommie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.views.AvatarView;
import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleEvent;

/**
 * Muestra la lista de expositores del congreso
 */
public class ExhibitorsView extends Fragment{

    /**
     * Es la capa donde se coloca cada uno de los expositores
     */
    private RecyclerView recyclerView;

    /**
     * Boton para ir atrás que está en el header de la lista
     */
    private ImageView backView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.exhibitors_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Personas de prueba para mostrar en la aplicación
        ArrayList<ExhibitorsView.ExhibitorItem> items = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            try { items.add(getTestingObject()); }
            catch (Exception e) { e.printStackTrace();}
        }

        setupRecyclerView(items);
    }

    @Override
    public void onDestroy() {
        ActivityMain activity = (ActivityMain) getActivity();
        activity.showActionBar();
        super.onDestroy();
    }

    /**
     * Se configura la capa que contiene las actividades (copiado de internet)
     */
    public void setupRecyclerView(ArrayList<ExhibitorItem> items){
        recyclerView = getView().findViewById(R.id.exhibitors_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new SmoothLayout(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ExhibitorsViewAdapter(items));
    }

    public class ExhibitorItem extends Exhibitor {

        ArrayList<ScheduleEvent> events;

        ExhibitorItem(String completeName, String personalTitle) {
            super(completeName, personalTitle);
            events = new ArrayList<>();
        }

    }

    // TODO: Borrar esto despues
    public ExhibitorItem getTestingObject() throws Exception{

        ExhibitorItem exhibitor = new ExhibitorItem(
                "Julian Salinas",
                "Instituto Tecnológico de Costa Rica");

        //  TODO: Consultar todas las actividades en las que participará el expositor
        // exhibitor.events = consultar eventos
        return exhibitor;

    }

    /**
     * Sirve para enlazar las funciones a una actividad en específico
     */
    public class ExhibitorsViewAdapter extends RecyclerView.Adapter<ExhibitorsViewAdapter.ExhibitorViewHolder> {

        /**
         * Objetos del modelo que serán adaptados visualmente
         */
        private ArrayList<ExhibitorsView.ExhibitorItem> items;

        private ExhibitorsViewAdapter(ArrayList<ExhibitorsView.ExhibitorItem> items){
            this.items = items;
        }

        /**
         * Requerida para saber la cantidad vistas que se tiene que crear
         */
        @Override
        public int getItemCount() {
            return items.size();
        }

        /**
         * No usar código en ésta función
         */
        @Override
        public ExhibitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exhibitors_item, null);
            return new ExhibitorsViewAdapter.ExhibitorViewHolder(view);
        }


        /**
         * Se enlazan los componentes y se agregan funciones a cada uno
         * @param position NO USAR, esta variable no tiene valor fijo. Usar holder.getAdapterPosition()
         */
        @Override
        public void onBindViewHolder(ExhibitorViewHolder holder, final int position) {

            ExhibitorItem item = items.get(holder.getAdapterPosition());

            // Rellana todos los espacios de la actividad
            holder.name.setText(item.getCompleteName());
            holder.title.setText(item.getPersonalTitle());

            // Coloca la primra letra del nombre como el avatar
            AvatarPlaceholder placeholder = new AvatarPlaceholder("A", 30);
            holder.avatar.setImageDrawable(placeholder);

            /*
             * Función ejecutada al presionar el botón "readmore" de una actividad
             * TODO: Pasar "item" a la "ScheduleDetail" para saber que información mostrar
             */
//            holder.readmore.setOnClickListener(v ->  {
//                ActivityMain activityMain = (ActivityMain) getActivity();
//                activityMain.switchFragment(new ScheduleDetail());
//            });

        }

        /**
         * Calse para enlzar cada uno de los componentes visuales de la actividad.
         * Es necesario que esta clase este anidada, asi que, no mover!
         */
        class ExhibitorViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView title;
            TextView readmore;
            AvatarView avatar;

            ExhibitorViewHolder(View view) {
                super(view);
                this.name = view.findViewById(R.id.exhibitor_item_name);
                this.title= view.findViewById(R.id.exhibitor_item_title);
//                this.readmore = view.findViewById(R.id.exhibitor_item_readmore);
                this.avatar = view.findViewById(R.id.exhibitor_item_avatar);
            }

        }

    }

}
