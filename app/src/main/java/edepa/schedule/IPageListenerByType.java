package edepa.schedule;

import edepa.model.EventType;

public interface IPageListenerByType {
    void onPageChanged(EventType type);
    void onPageRemoved(EventType type);
}