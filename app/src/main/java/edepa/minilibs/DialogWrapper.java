package edepa.minilibs;

import android.content.Context;
import android.view.View;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import butterknife.ButterKnife;


public abstract class DialogWrapper {

    protected Context context;
    protected AlertDialog dialog;
    protected AlertDialog.Builder builder;

    public void show(){
        onCreateDialog();
        dialog.show();
    }

    public void close(){
        dialog.dismiss();
    }

    protected abstract int getResource();

    public DialogWrapper(Context context) {
        this.context = context;
    }

    protected void onCreateDialog(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getResource(), null);
        ButterKnife.bind(this, view);
        builder = new AlertDialog.Builder(inflater.getContext());
        builder.setView(view);
        dialog = builder.create();
    }

}
