package imagisoft.modelview.views;

import android.os.Bundle;

import imagisoft.misc.DateConverter;
import imagisoft.modelview.schedule.IEventsListener;
import imagisoft.modelview.schedule.IEventsSubject;

public class DateFragment extends BlankFragment implements IEventsSubject {

    private long date;

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public IEventsListener getListener() {
        return null;
    }

    private IEventsListener listener;

    @Override
    public void setListener(IEventsListener listener) {
        this.listener = listener;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("date")) {
            this.date = args.getLong("date");
            descriptionTextView.setText(DateConverter.extractDate(date));
        }
    }

}
