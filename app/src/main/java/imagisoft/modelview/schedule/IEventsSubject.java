package imagisoft.modelview.schedule;

public interface IEventsSubject {

    long getDate();
    void setListener(IEventsListener listener);

}
