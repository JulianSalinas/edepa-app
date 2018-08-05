package imagisoft.modelview.interfaces;

public interface IPageSubject {

    long getDate();
    IPageListener getPageListener();
    void setPageListener(IPageListener listener);

}
