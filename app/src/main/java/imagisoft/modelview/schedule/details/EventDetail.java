package imagisoft.modelview.schedule.details;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import imagisoft.misc.DateConverter;
import imagisoft.model.Cloud;
import imagisoft.model.ScheduleEvent;
import imagisoft.modelview.R;
import imagisoft.modelview.activity.MainFragment;

public class EventDetail extends MainFragment
        implements AppBarLayout.OnOffsetChangedListener, ValueEventListener{

    /**
     * Para manejar el efecto parallax
     */
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;

    DatabaseReference eventReference;

    private int statusBarColor;

    @BindView(R.id.favorite_button)
    FloatingActionButton favoriteButton;

    @BindView(R.id.title_text_view)
    TextView titleTV;

    @BindView(R.id.eventype_text_view)
    TextView eventypeTV;

    @BindView(R.id.favorites_amount_text_view)
    TextView favoritesAmountTV;

    @BindView(R.id.location_text_view)
    TextView locationTV;

    @BindView(R.id.time_descripcion_text_view)
    TextView timeDescriptionTV;

    @BindView(R.id.brief_text_view)
    TextView briefTV;

    @BindView(R.id.collapsing_toolbar_layout)
    View collapsingToolbarLayout;

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    public ScheduleEvent getEvent(){
        return event;
    }

    private String eventKey;

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static EventDetail newInstance(String eventKey) {
        EventDetail fragment = new EventDetail();
        Bundle args = new Bundle();
        args.putString("eventKey", eventKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getResource() {
        return R.layout.schedule_item_detail;
    }

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Bundle args = getArguments();
        if(args != null)
            eventKey = args.getString("eventKey");


    }

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        bindActivity();

        eventReference = Cloud.getInstance()
                .getReference(Cloud.SCHEDULE).child(eventKey);

        eventReference.addValueEventListener(this);


        mAppBarLayout.addOnOffsetChangedListener(this);

//        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

//        if(FavoriteList.getInstance().contains(event))
//            favoriteButton.setImageResource(R.drawable.star_on);
//        else
//            favoriteButton.setImageResource(R.drawable.star_off);


        favoriteButton.setOnClickListener(v -> {
//            if(FavoriteList.getInstance().contains(event)) {
                // FavoriteList.getInstance().removeEvent(event);
                // favoriteButton.setImageResource(R.drawable.star_off);
//            }
//            else {
                // FavoriteList.getInstance().addEvent(event);
                // favoriteButton.setImageResource(R.drawable.star_on);
//            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventReference.removeEventListener(this);
    }

    private void bindActivity() {
        View view = getView();
        if(view != null) {
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
            mTitle = (TextView) view.findViewById(R.id.main_textview_title);
            mTitleContainer = (LinearLayout) view.findViewById(R.id.title_linear_layout);
            mAppBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
            mToolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
//                mToolbar.setVisibility(View.VISIBLE);
                favoriteButton.setVisibility(View.INVISIBLE);
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
//                mToolbar.setVisibility(View.GONE);
                favoriteButton.setVisibility(View.VISIBLE);
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    protected String getDateAsString(ScheduleEvent event){
        return  activity.getResources().getString(R.string.text_from) + " " +
                DateConverter.extractTime(event.getStart()) + " " +
                activity.getResources().getString(R.string.text_to) + " " +
                DateConverter.extractTime(event.getEnd());
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.event = dataSnapshot.getValue(ScheduleEvent.class);
        if(event != null){
            this.event.setKey(dataSnapshot.getKey());
            titleTV.setText(event.getTitle());
            eventypeTV.setText(event.getEventype().toString());
            mTitle.setText(event.getEventype().toString());
            favoritesAmountTV.setText(String.valueOf(event.getFavoritesAmount()));
            locationTV.setText(event.getLocation());
            timeDescriptionTV.setText(getDateAsString(event));
            briefTV.setText(event.getBriefSpanish());

            int color = getResources().getColor(event.getEventype().getColor());
//            setStatusBarColor(ColorConverter.darken(color, 12));

            mToolbar.setBackgroundColor(color);
            collapsingToolbarLayout.setBackgroundColor(color);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i("EventDetail::", databaseError.getMessage());
    }

}