package edepa.schedule;

public interface IPageListener {
    void onPageChanged(long pageDate);
    void onPageRemoved(long pageDate);
}
