package edepa.event;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.minilibs.DialogFancy;
import edepa.minilibs.RegexSearcher;
import edepa.model.Event;
import edepa.modelview.R;
import edepa.services.DownloadService;


public class EventHolderDownload extends RecyclerView.ViewHolder {

    @BindView(R.id.event_download_filename)
    TextView eventDownloadFilename;

    @BindView(R.id.event_download_description)
    TextView eventDownloadDescription;

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
            eventDownloadDescription.setText(context.getString(R.string.text_download_file));
        }
    }

    @OnClick(R.id.event_download_view)
    public void download(){

        String filename = RegexSearcher.findFilenameFromUrl(event.getFileUrl());
        String content = String.format("%s %s ?",
                context.getString(R.string.text_download_permission), filename);

        new DialogFancy.Builder()
                .setContext(itemView.getContext())
                .setStatus(DialogFancy.INFO)
                .setTitle(R.string.text_download_file)
                .setContent(content)
                .setExistsCancel(true)
                .setOnAcceptClick(v -> {
                    if (event.getFileUrl() != null)
                        downloadService.download(event.getFileUrl());
                }).build().show();
    }

}
