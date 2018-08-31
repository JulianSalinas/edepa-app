package edepa.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.minilibs.RegexSearcher;
import edepa.model.Event;
import edepa.modelview.R;
import edepa.services.DownloadService;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class EventHolderDownload extends RecyclerView.ViewHolder {

    @BindView(R.id.event_download_size)
    TextView eventDownloadSize;

    @BindView(R.id.event_download_filename)
    TextView eventDownloadFilename;

    private Event event;
    private Context context;
    private DownloadService downloadService;

    public void setListener(DownloadService.DownloadListener listener){
        downloadService.setDownloadListener(listener);
    }

    public EventHolderDownload(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        this.downloadService = new DownloadService(context);
    }

    public void bind(DownloadService.DownloadListener listener, Event event){
        this.event = event;
        boolean visible = event.getFileUrl() != null;
        itemView.setVisibility(visible ? VISIBLE : GONE);
        if (visible){
            setListener(listener);
            String filename = RegexSearcher.findFilenameFromUrl(event.getFileUrl());
            eventDownloadFilename.setText(filename);
            eventDownloadSize.setText(context.getString(R.string.text_download_file));
        }
    }

    @OnClick(R.id.event_download_view)
    public void download(){
        if (event.getFileUrl() != null)
        downloadService.download(event.getFileUrl());
    }

}
