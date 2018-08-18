package edepa.notices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import butterknife.BindView;
import butterknife.OnClick;
import edepa.cloud.CloudAdmin;
import edepa.cloud.CloudNotices;
import edepa.modelview.R;
import edepa.custom.RecyclerAdapter;
import edepa.custom.RecyclerFragment;
import edepa.minilibs.SmoothLayout;


public class NoticesFragment extends RecyclerFragment
        implements CloudAdmin.AdminPermissionListener {

    private CloudAdmin admin;

    public boolean isAdmin() {
        return admin != null && admin.isAdmin();
    }

    /**
     * Es donde se colocan cada uno de los mensajes
     * de forma VISUAL
     */
    @BindView(R.id.news_rv)
    RecyclerView noticesRecycler;

    @Override
    protected RecyclerView getRecyclerView() {
        return noticesRecycler;
    }

    @BindView(R.id.publish_button)
    FloatingActionButton publishButton;

    /**
     * Contiene todos los posts de las noticias y ejecuta
     * los evento de inserción, deleción y modificación
     */
    protected NoticesAdapter noticesAdapter;

    @Override
    protected RecyclerAdapter getViewAdapter() {
        return noticesAdapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.layout.news_view;
    }

    /**
     * Carga los mensajes
     */
    protected CloudNotices cloudNotices;

    /**
     * {@inheritDoc}
     * Se configura el contenedor de mensajes {@link #noticesRecycler}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noticesAdapter = new NoticesAdapter(getNavigationActivity());
        cloudNotices = new CloudNotices();
        cloudNotices.setCallbacks(noticesAdapter);
        cloudNotices.connect();
        admin = new CloudAdmin();
        admin.setAdminPermissionListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cloudNotices.disconnect();
    }

    /**
     * {@inheritDoc}
     * Se prepara el adaptador para poder recibir nuevas vistas de noticias.
     * Si el adaptador ya había sido colocado no es necesario crearlo otra vez
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customizeActivity();
        admin.requestAdminPermission();
        noticesRecycler.setHasFixedSize(true);
        noticesRecycler.setAdapter(noticesAdapter);
        noticesRecycler.setItemAnimator(new DefaultItemAnimator());
        noticesRecycler.setLayoutManager(new SmoothLayout(getActivity()));

        noticesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && publishButton.getVisibility() == View.VISIBLE)
                    publishButton.hide();
                else if (dy < 0 && publishButton.getVisibility() != View.VISIBLE) {
                    publishButton.show();
                }
            }
        });

    }

    public void customizeActivity(){
        setToolbarText(R.string.nav_news);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);
    }

    @OnClick(R.id.publish_button)
    public void openNewsEditor(){
        String tag = "NEWS_EDITOR";
        NoticeEditor frag = (NoticeEditor) getNavigationActivity()
                .getSupportFragmentManager().findFragmentByTag(tag);
        setFragmentOnScreen(frag != null ? frag: new NoticeEditor(), tag);
    }

    @Override
    public void onPermissionGranted() {
        publishButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPermissionDenied() {
        publishButton.setVisibility(View.GONE);
    }

}
