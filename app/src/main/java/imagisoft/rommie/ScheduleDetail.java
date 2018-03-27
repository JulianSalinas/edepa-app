package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

// TODO: Corresponde a los detalles de una actividad, falta enlazar todos las opciones

public class ScheduleDetail extends MainViewFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from schedule_detail.xmlxml
        View view = inflater.inflate(R.layout.schedule_detail, container, false);
        return view;
    }

}