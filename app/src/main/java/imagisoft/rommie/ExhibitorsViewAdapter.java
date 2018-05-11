package imagisoft.rommie;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.AvatarPlaceholder;
import agency.tango.android.avatarview.utils.StringUtils;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.edepa.Exhibitor;
import imagisoft.miscellaneous.SearchNormalizer;


public class ExhibitorsViewAdapter
        extends RecyclerView.Adapter<ExhibitorsViewAdapter.ExhibitorViewHolder> {

    /**
     * Objetos del modelo que serán adaptados visualmente
     */
    private List<Exhibitor> exhibitors;
    private List<Exhibitor> filteredExhibitors;

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
        this.filteredExhibitors = this.exhibitors;
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
        bindColor(item.getCompleteName(), holder);

        holder.exhibitorCardView.setOnClickListener(v -> exhibitorsView.switchFragment(
                ExhibitorDetail.newInstance(item, exhibitorsView.getExhibitorsEvents(item))));

    }

    /**
     * Coloca la informacíon básica de la persona
     */
    public void bindInformation(Exhibitor exhibitor, ExhibitorViewHolder holder){
        holder.nameTextView.setText(exhibitor.getCompleteName());
        holder.titleTextView.setText(exhibitor.getPersonalTitle());
    }

    /**
     * Coloca la primra letra del nombre como el exhibitorAvatarView y le pone color
     * con base a eso.
     */
    private void bindColor(String name, ExhibitorViewHolder holder){
        int color = convertStringToColor(name);
        AvatarPlaceholder avatar = new AvatarPlaceholder(name, 30);
        holder.exhibitorAvatarView.setImageDrawable(avatar);
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
     * Según una palabra de búsqueda se filtran todos los expositores
     * @param keyword: Palabra de búsqueda
     */
    public void filter(String keyword){

        keyword = SearchNormalizer.removeAccents(keyword.toLowerCase());

        filteredExhibitors = new ArrayList<>();
        for(Exhibitor exhibitor : exhibitors){

            String name = SearchNormalizer.removeAccents(exhibitor.getCompleteName().toLowerCase());
            String title = SearchNormalizer.removeAccents(exhibitor.getPersonalTitle().toLowerCase());

            if(name.contains(keyword) || title.contains(keyword))
                filteredExhibitors.add(exhibitor);

        }

        if (filteredExhibitors.isEmpty())
            filteredExhibitors = exhibitors;

        notifyDataSetChanged();

    }

    /**
     * Clase para enlazar cada uno de los componentes visuales
     */
    class ExhibitorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.line)
        View line;

        @BindView(R.id.name_text_view)
        TextView nameTextView;

        @BindView(R.id.title_text_view)
        TextView titleTextView;

        @BindView(R.id.exhibitor_card_view)
        CardView exhibitorCardView;

        @BindView(R.id.exhibitor_avatar_view)
        AvatarView exhibitorAvatarView;

        ExhibitorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}