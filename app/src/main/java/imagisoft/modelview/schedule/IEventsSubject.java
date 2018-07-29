package imagisoft.modelview.schedule;

public interface IEventsSubject {

    long getDate();
    IEventsListener getListener();
    void setListener(IEventsListener listener);

}
