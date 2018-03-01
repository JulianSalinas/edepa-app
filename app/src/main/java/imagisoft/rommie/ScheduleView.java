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
        for(int i = 0; i < 4; i++)
            items.add(new ScheduleItem());

        setupRecyclerView(items);

    }

    public void setupRecyclerView(ArrayList<ScheduleItem> items){
        recyclerView = getView().findViewById(R.id.schedule_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ScheduleViewAdapter(items));
    }

    public class ScheduleItem {

        private String datetime;
        private String description;

        public String getDatetime() {
            return datetime;
        }

        public String getDescription() {
            return description;
        }

        public ScheduleItem(){
            datetime = "18/12/20 5:00pm";
            description = "Descripcion del evento";
        }

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
        public void onBindViewHolder(ScheduleViewHolder holder, int position) {
            ScheduleItem item = items.get(position);
            holder.datetime.setText(item.getDatetime());
            holder.description.setText(item.getDescription());
        }

        class ScheduleViewHolder extends RecyclerView.ViewHolder {

            TextView datetime;
            TextView description;

            public ScheduleViewHolder(View view) {
                super(view);
                this.datetime = view.findViewById(R.id.schedule_item_datetime);
                this.description = view.findViewById(R.id.schedule_item_description);
            }

        }

    }

}