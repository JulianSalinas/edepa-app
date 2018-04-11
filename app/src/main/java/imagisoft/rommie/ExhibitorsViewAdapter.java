package imagisoft.rommie;

import java.util.List;
import imagisoft.edepa.Exhibitor;

import android.view.View;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.utils.StringUtils;

public class ExhibitorsViewAdapter
        extends RecyclerView.Adapter<ExhibitorsViewAdapter.ExhibitorViewHolder> {

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private List<Exhibitor> exhibitors;

    /**
     * Es un fragmento permite obtener los eventos de un expositor
     */
    private ExhibitorsViewFragment exhibitorsView;

    /**
     * Se colocan los expositores
     */
    public ExhibitorsViewAdapter(ExhibitorsViewFragment exhibitorsView){

        this.exhibitorsView = exhibitorsView;
        this.exhibitors = exhibitorsView.getExhibitors();

    }

    /**
     * Requerida para saber la cantidad vistas que se tienen que crear
     */
    @Override
    public int getItemCount() {
        return exhibitors.size();
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

        Exhibitor item = exhibitors.get(holder.getAdapterPosition());

        bindInformation(item, holder);
        bindColor(item.getCompleteName(), holder);

        holder.exhibitor.setOnClickListener(v -> exhibitorsView.switchFragment(
                ExhibitorDetail.newInstance(item, exhibitorsView.getExhibitorsEvents(item))));

    }

    /**
     * Coloca la informacíon básica de la persona
     */
    public void bindInformation(Exhibitor exhibitor, ExhibitorViewHolder holder){

        holder.name.setText(exhibitor.getCompleteName());
        holder.title.setText(exhibitor.getPersonalTitle());

    }

    /**
     * Coloca la primra letra del nombre como el avatar y le pone color
     * con base a eso.
     */
    private void bindColor(String name, ExhibitorViewHolder holder){

        int color = convertStringToColor(name);
        AvatarPlaceholder avatar = new AvatarPlaceholder(name, 30);
        holder.avatar.setImageDrawable(avatar);
        holder.line.setBackgroundColor(color);

    }

    /**
     * A partir de un string retorna un color.
     * Usada para colorear los avatares y decoraciones del mismo
     */
    private int convertStringToColor(String text) {

        String color = StringUtils.isNullOrEmpty(text) ? "#3F51B5" :
                String.format("#FF%06X", (0xFFFFFF & text.hashCode()));
        return Color.parseColor(color);

    }

    /**
     * Clase para enlazar cada uno de los componentes visuales
     */
    class ExhibitorViewHolder extends RecyclerView.ViewHolder {

        View line;
        TextView name;
        TextView title;
        AvatarView avatar;
        CardView exhibitor;

        ExhibitorViewHolder(View view) {

            super(view);
            this.line = view.findViewById(R.id.exhibitor_item_line);
            this.name = view.findViewById(R.id.exhibitor_item_name);
            this.title= view.findViewById(R.id.exhibitor_item_title);
            this.avatar = view.findViewById(R.id.exhibitor_item_avatar);
            this.exhibitor = view.findViewById(R.id.exhibitors_item);

        }

    }

}