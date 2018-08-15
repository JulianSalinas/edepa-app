package edepa.custom;

import android.view.View;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import butterknife.ButterKnife;


public abstract class DialogCool {

    protected AlertDialog dialog;
    protected LayoutInflater inflater;
    protected AlertDialog.Builder builder;

    public void show(){
        onCreateDialog();
        dialog.show();
    }

    public void close(){
        dialog.dismiss();
    }

    protected abstract int getResource();

    public DialogCool(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    protected void onCreateDialog(){
        View view = inflater.inflate(getResource(), null);
        ButterKnife.bind(this, view);
        builder = new AlertDialog.Builder(inflater.getContext());
        builder.setView(view);
        dialog = builder.create();
    }

}
