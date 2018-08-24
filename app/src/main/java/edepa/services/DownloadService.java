package edepa.services;

import android.net.Uri;
import android.content.Context;
import android.app.DownloadManager;

import edepa.minilibs.OnlineHelper;
import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;


public class DownloadService {

    private Context context;
    private DownloadManager downloadManager;
    private DownloadListener downloadListener;

    public interface DownloadListener {
        void onDownloadOffline();
        void onDownloadStarted();
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public DownloadService(Context context) {
        this.context = context;
        this.downloadManager = (DownloadManager)
        context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void download(String fileUrl) {
        Uri uri = Uri.parse(fileUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (downloadManager != null && OnlineHelper.isOnline(context))
             startDownload(request);
        else if(downloadListener != null)
            downloadListener.onDownloadOffline();
    }

    private void startDownload(DownloadManager.Request request){
        downloadManager.enqueue(request);
        if(downloadListener != null)
            downloadListener.onDownloadStarted();
    }

}
