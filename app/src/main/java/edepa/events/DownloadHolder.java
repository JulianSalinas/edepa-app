package edepa.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edepa.minilibs.RegexSearcher;
import edepa.modelview.R;
import edepa.services.DownloadService;


public class DownloadHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.event_download_size)
    TextView eventDownloadSize;

    @BindView(R.id.event_download_filename)
    TextView eventDownloadFilename;

    private String fileUrl;
    private Context context;
    private DownloadService downloadService;

    public DownloadHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        this.downloadService = new DownloadService(context);
    }

    public DownloadHolder setListener(DownloadService.DownloadListener listener){
        downloadService.setDownloadListener(listener);
        return this;
    }

    public DownloadHolder setFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
        eventDownloadFilename.setText(RegexSearcher.findFilenameFromUrl(fileUrl));
        eventDownloadSize.setText(context.getString(R.string.text_download_file));
        return this;
    }

    @OnClick(R.id.event_download_view)
    public void download(){
        downloadService.download(fileUrl);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
