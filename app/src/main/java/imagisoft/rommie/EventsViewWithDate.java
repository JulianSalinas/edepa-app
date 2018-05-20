package imagisoft.rommie;

import android.os.Bundle;
import android.util.Log;

public abstract class EventsViewWithDate extends EventsView {

    protected String date;

    public String getDate() {
        return date;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if(args != null && args.containsKey("date")) {
            date = args.getString("date");
            Log.i(getTag(), "dateReceived");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("date", date);
        Log.i(getTag(), "dateSaved");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            date = savedInstanceState.getString("date");
            Log.i(getTag(), "dateLoaded");
        }
    }

}
