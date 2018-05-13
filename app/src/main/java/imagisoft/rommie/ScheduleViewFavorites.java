package imagisoft.rommie;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import imagisoft.edepa.ScheduleBlock;

public class ScheduleViewFavorites extends ScheduleView {

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleViewFavorites newInstance(List<ScheduleBlock> events) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("events", new ArrayList<>(events));

        ScheduleViewFavorites fragment = new ScheduleViewFavorites();
        fragment.setArguments(args);

        return fragment;
    }

}