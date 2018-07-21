package imagisoft.modelview;

import android.util.Log;
import android.os.Bundle;


public abstract class EventsFragmentWithDate extends EventsFragment {

    protected long date;

    public long getDate() {
        return date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("date"))
            date = args.getLong("date");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("date", date);
        Log.i("EventsFragmentWithDate", "::onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            date = savedInstanceState.getLong("date");
            Log.i("EventsFragmentWithDate", "::onViewStateRestored");
        }
    }

}
