package imagisoft.rommie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import imagisoft.edepa.Exhibitor;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.util.DateConverter;

public class ScheduleView extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<ScheduleItem> items = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            try {
                items.add(getTestingObject());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setupRecyclerView(items);

    }

    public void setupRecyclerView(ArrayList<ScheduleItem> items){
        recyclerView = getView().findViewById(R.id.schedule_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ScheduleViewAdapter(items));
    }

    public class ScheduleItem extends ScheduleEvent {

        public ScheduleItem(Long id, Long start, Long end, String eventype, String header, String brief) {
            super(id, start, end, eventype, header, brief);
        }

    }

    public ScheduleItem getTestingObject() throws Exception{

        Exhibitor first = new Exhibitor("Julian Salinas", "Instituto Tecnológico de Costa Rica");
        Exhibitor second = new Exhibitor("Brandon Dinarte", "Instituto Tecnológico de Costa Rica");

        ScheduleItem event = new ScheduleItem(
                123L,
                DateConverter.stringToLong("12/12/18 11:00 am"),
                DateConverter.stringToLong("12/12/18 2:30 pm"),
                "Conferencia",
                "Nombre lo suficientemente largo para cubrir dos líneas",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ullamcorper aliquet dictum. Maecenas in imperdiet dui"
        );

        event.addExhibitor(first);
        event.addExhibitor(second);

        return event;

    }

    public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.ScheduleViewHolder> {

        private ArrayList<ScheduleItem> items;

        public ScheduleViewAdapter(ArrayList<ScheduleItem> items){
            this.items = items;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, null);
            return new ScheduleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ScheduleViewHolder holder, final int position) {
            ScheduleItem item = items.get(position);

            String range =
                    getResources().getString(R.string.text_from) + " " +
                    DateConverter.extractTime(item.getStart()) + " " +
                    getResources().getString(R.string.text_to) + " " +
                    DateConverter.extractTime(item.getEnd());

            holder.time.setText(range);
            holder.header.setText(item.getHeader());
            holder.eventype.setText(item.getEventype());
            holder.readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityMain activityMain = (ActivityMain) getActivity();
                    activityMain.showStatusMessage("Evento #" + String.valueOf(position));
                }
            });
        }

        class ScheduleViewHolder extends RecyclerView.ViewHolder {

            TextView time;
            TextView header;
            TextView eventype;
            TextView readmore;

            public ScheduleViewHolder(View view) {
                super(view);
                this.time = view.findViewById(R.id.schedule_item_time);
                this.header = view.findViewById(R.id.schedule_item_header);
                this.eventype = view.findViewById(R.id.schedule_item_eventype);
                this.readmore = view.findViewById(R.id.shedule_item_readmore);
            }

        }

    }

}