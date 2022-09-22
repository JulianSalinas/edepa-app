package edepa.people;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.support.design.widget.TabLayout;
import androidx.core.app.DialogFragment;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentPagerAdapter;
import androidx.core.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mklimek.circleinitialsview.CircleInitialsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edepa.custom.BaseFragment;
import edepa.custom.DefaultDialog;
import edepa.schedule.ScheduleAdapter;
import edepa.minilibs.ColorConverter;
import edepa.minilibs.ColorGenerator;
import edepa.minilibs.SmoothLayout;
import edepa.model.Event;
import edepa.model.Person;
import edepa.modelview.R;

public class PersonFragment extends DefaultDialog {

    public static final String SAVED_PERSON_KEY = "person_key";

    @BindView(R.id.text_title)
    TextView titleText;

    @BindView(R.id.name_text)
    TextView nameText;

    @BindView(R.id.exhibitor_avatar_view)
    CircleInitialsView avatarView;

    @BindView(R.id.person_view_pager)
    ViewPager personViewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private Person person;

    @Override
    public int getResource() {
        return R.layout.person_detail;
    }

    /**
     * Se obtiene una nueva instancia del fragmento
     * @return EventFragment
     */
    public static PersonFragment newInstance(Person person) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putParcelable(SAVED_PERSON_KEY, person);
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(SAVED_PERSON_KEY))
            person = args.getParcelable(SAVED_PERSON_KEY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindPersonAvatar();
        nameText.setText(person.getCompleteName());
        titleText.setText(person.getPersonalTitle());

        boolean hasAbout = person.getAbout() != null;
        boolean hasEvents = person.getEventsList() != null && !person.getEventsList().isEmpty();

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return hasAbout && hasEvents ? 2 : 1;
            }

            @Override
            public Fragment getItem(int position) {
                if(hasAbout && !hasEvents){
                    return position == 0 ?
                            getPersonAboutFragment(person) :
                            getPersonEventsFragment(person);
                }
                else {
                    return position == 0 ?
                            getPersonEventsFragment(person):
                            getPersonAboutFragment(person);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(position == 0 ?
                        R.string.text_related_events : R.string.text_about);
            }

        };

        personViewPager.setAdapter(adapter);

        if (!hasAbout || !hasEvents) {
            tabLayout.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        tabLayout.setupWithViewPager(personViewPager);

    }

    public Fragment getPersonAboutFragment(Person person){
        Bundle args = new Bundle();
        args.putString(PersonAboutFragment.SAVED_TEXT_KEY, person.getAbout());
        Fragment fragment = new PersonAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment getPersonEventsFragment(Person person){
        Bundle args = new Bundle();
        ArrayList<Event> events = new ArrayList<>(person.getEventsList());
        args.putParcelableArrayList(PersonEventsFragment.SAVED_EVENTS_KEY, events);
        Fragment fragment = new PersonEventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_PERSON_KEY, person);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            person = savedInstanceState.getParcelable(SAVED_PERSON_KEY);
    }

    public void bindPersonAvatar(){
        String personName = person.getCompleteName();
        avatarView.setText(personName);
        avatarView.setTextColor(Color.WHITE);
        int color = getAvatarColor();
        avatarView.setBackgroundColor(color);
    }

    private int getAvatarColor(){
        String personName = person.getCompleteName();
        ColorGenerator colorGenerator = new ColorGenerator(getContext());
        int color = colorGenerator.getColor(personName);
        return ColorConverter.lighten(color);
    }

    public static class PersonAboutFragment extends BaseFragment {

        public static final String SAVED_TEXT_KEY = "text_key";

        @BindView(R.id.person_about_text)
        TextView aboutText;

        @Override
        public int getResource() {
            return R.layout.person_about;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Bundle args = getArguments();
            if(args != null && args.containsKey(SAVED_TEXT_KEY))
                aboutText.setText(args.getString(SAVED_TEXT_KEY));

        }


    }

    public static class PersonEventsFragment extends BaseFragment {

        public static final String SAVED_EVENTS_KEY = "events_key";

        @BindView(R.id.events_recycler)
        RecyclerView eventsRecycler;

        private List<Event> events;

        @Override
        public int getResource() {
            return R.layout.person_events;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            if(args != null && args.containsKey(SAVED_EVENTS_KEY))
                events = args.getParcelableArrayList(SAVED_EVENTS_KEY);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            ScheduleAdapter adapter = new ScheduleAdapter(events){
                @Override
                public int getItemViewType(int position) {
                    return SINGLE;
                }
            };

            eventsRecycler.setAdapter(adapter);
            eventsRecycler.setLayoutManager(new SmoothLayout(getContext()));
        }

    }

}
