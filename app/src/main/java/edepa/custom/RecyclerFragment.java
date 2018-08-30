package edepa.custom;

import android.view.View;
import android.os.Bundle;
import android.os.Parcelable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import edepa.app.CustomFragment;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * Este fragmento soluciona el problema que sufre tanto
 * la sección de noticias como el chat. Cuando se voltea
 * la pantalla se colocaba nuevamente al primer item, cuando
 * el comportamiento deseado es que el mismo item siga
 * visible cuando se retrocede a la sección anterior o se
 * voltea la pantalla
 */
public abstract class RecyclerFragment extends CustomFragment {

    /**
     * Guarda el estado que tenía el RecyclerView
     * antes de quedar en pausa
     */
    protected Bundle pausedState;

    /**
     * RecyclerView del cuál se deb guardar el estado
     * para recordar cual era el último item en pantalla
     * antes de girarla
     * @return RecyclerView
     */
    protected abstract RecyclerView getRecyclerView();

    /**
     * Adaptador que propociona los datos del modelo
     * al RecyclerView
     * @return RecyclerAdapter
     */
    protected abstract RecyclerAdapter getViewAdapter();

    /**
     * Al colocarse en pausa guarda la posición
     * del scroll
     * @see #onPausedViewStateRestored()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void savePausedInstanceState(){
        pausedState = new Bundle();
        Parcelable scrollPosition = getRecyclerView()
                .getLayoutManager().onSaveInstanceState();
        pausedState.putParcelable("scrollPosition", scrollPosition);
    }

    /**
     * Al volver de la pausa se restaura el scroll
     * a donde lo había dejado el usuario
     * @see #onPausedViewStateRestored()
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onPausedViewStateRestored(){
        if(pausedState != null) {
            Parcelable scrollPosition = pausedState
                    .getParcelable("scrollPosition");
            getRecyclerView()
                    .getLayoutManager()
                    .onRestoreInstanceState(scrollPosition);
        }
    }

    /**
     * Es similar a {@link #savePausedInstanceState()}
     * pero funciona únicamente al girar la pantalla
     * @param outState Donde se guarda la posición y el
     *                 item que estaba viendo el usuario
     * @see #onViewStateRestored(Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int position = ((LinearLayoutManager) getRecyclerView()
                .getLayoutManager()).findFirstVisibleItemPosition();

        View startView = getRecyclerView().getChildAt(0);

        int top = (startView == null) ? 0 :
                (startView.getTop() - getRecyclerView().getPaddingTop());

        outState.putInt("position", position);
        outState.putInt("top", top);
    }

    /**
     * Similar al {@link #onPausedViewStateRestored()} pero
     * funciona únicamente cuando se gira la pantalla
     * @param savedInstanceState Donde se guarda la posición y el
     *                           item que estaba viendo el usuario
     * @see #onSaveInstanceState(Bundle)
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt("position");
            int top = savedInstanceState.getInt("top");
            getViewAdapter().registerAdapterDataObserver(
            new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                ((LinearLayoutManager) getRecyclerView()
                        .getLayoutManager())
                        .scrollToPositionWithOffset(position, top);
            }});
        }
    }

}
