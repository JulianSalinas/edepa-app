package edepa.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import edepa.modelview.R;
import io.reactivex.annotations.Nullable;


public class DialogCustom extends DialogCool {

    public static final int INFO = 0;
    public static final int ERROR = 1;
    public static final int SUCCESS = 2;
    public static final int WARNING = 3;

    @Nullable
    @BindView(R.id.text_title)
    TextView textTitle;

    @Nullable
    @BindView(R.id.text_content)
    TextView textContent;

    @Nullable
    @BindView(R.id.text_accept)
    TextView textAccept;

    @Nullable
    @BindView(R.id.text_cancel)
    TextView textCancel;

    protected int status;
    protected String title;
    protected String content;
    protected boolean existsCancel;
    protected View.OnClickListener onAcceptClick;
    protected View.OnClickListener onCancelListener;

    @Override
    protected int getResource() {
        switch (status){
            case INFO: return R.layout.dialog_info;
            case ERROR: return R.layout.dialog_error;
            case SUCCESS: return R.layout.dialog_success;
            default: return R.layout.dialog_warning;
        }
    }

    private DialogCustom(Builder builder) {
        super(builder.inflater);
        this.title = builder.title;
        this.status = builder.status;
        this.content = builder.content;
        this.inflater = builder.inflater;
        this.existsCancel = builder.existsCancel;

        if(builder.onAcceptClick != null) {
            this.onAcceptClick = v -> {
                builder.onAcceptClick.onClick(textCancel);
                close();
            };
        }

        else this.onAcceptClick = null;
        this.onCancelListener = builder.onCancelListener;
    }

    @Override
    protected void onCreateDialog() {
        super.onCreateDialog();

        textTitle.setText(title);

        if (content != null)
             textContent.setText(content);
        else textContent.setVisibility(View.GONE);

        if(onAcceptClick != null)
             textAccept.setOnClickListener(onAcceptClick);
        else textAccept.setOnClickListener(v -> close());

        if (!existsCancel)
            textCancel.setVisibility(View.GONE);

        if(onCancelListener != null && existsCancel)
             textCancel.setOnClickListener(onCancelListener);

        else if (existsCancel)
            textCancel.setOnClickListener(v -> close());

    }

    public static final class Builder {

        private int status;
        private String title;
        private String content;
        private boolean existsCancel;
        private LayoutInflater inflater;
        private View.OnClickListener onAcceptClick;
        private View.OnClickListener onCancelListener;

        public Builder setInflater(LayoutInflater inflater) {
            this.inflater = inflater;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = inflater
                    .getContext()
                    .getResources()
                    .getString(title);
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setContent(int content) {
            this.content = inflater
                    .getContext()
                    .getResources()
                    .getString(content);
            return this;
        }

        public Builder setExistsCancel(boolean existsCancel) {
            this.existsCancel = existsCancel;
            return this;
        }

        public Builder setOnAcceptClick(View.OnClickListener onAcceptClick) {
            this.onAcceptClick = onAcceptClick;
            return this;
        }

        public Builder setOnCancelListener(View.OnClickListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public DialogCustom build() {
            return new DialogCustom(this);
        }

    }

}
