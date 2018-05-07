package imagisoft.rommie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;


public class MainActivityNavigation extends MainActivityFirebase
        implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * Variables para poder reciclar los fragmentos
     */
    private Fragment chatView;
    private Fragment newsView;
    private Fragment aboutView;
    private Fragment configView;
    private Fragment themePicker;
    private Fragment scheduleTabs;
    private Fragment exhibitorsView;
    private Fragment informationView;

    /**
     * Atributos custom
     */
    private TextView currentMenu;
    private TextView currentSection;
    private FloatingActionLayout favoriteButton;

    boolean isScheduleView;

    public View getMenuView(){
        return navigation.getHeaderView(0);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        isScheduleView = true;

        toolbar.setTitle(R.string.app_name);
        scheduleTabs = ScheduleTabs.newInstance(ScheduleTabs.SCHEDULE_TAB);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, scheduleTabs);
        transaction.commitAllowingStateLoss();

    }

    /**
     * Sección de las vista que hay en el encabezado
     */
    public void bindNavigationViews(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        View header = navigation.getHeaderView(0);
        favoriteButton = header.findViewById(R.id.favorite_button);
        currentMenu = header.findViewById(R.id.current_menu_view);
        currentSection = header.findViewById(R.id.current_section_view);
        favoriteButton.setOnClickListener(v -> onFavoriteButtonClick());

//        int menuColor = prefs.getInt(APP_HEADER_COLOR.toString(), APP_HEADER_COLOR.getColor());
//        currentMenu.setTextColor(menuColor);
//        currentSection.setTextColor(menuColor);
//
//        int fabColor = prefs.getInt(APP_ACCENT.toString(), APP_ACCENT.getColor());
//        favoriteButton.setFabColor(fabColor);

    }

    /**
     * Envía al usuario al tab donde están sus favoritos
     */
    @Override
    public void onFavoriteButtonClick() {

        int favTab = ScheduleTabs.FAVORITES_TAB;
        currentSection.setText(R.string.nav_favorites);

        if(scheduleTabs == null) {
            scheduleTabs = ScheduleTabs.newInstance(favTab);
        }
        else {

            ((ScheduleTabs) scheduleTabs).setCurrentTab(favTab);

            if (isScheduleView)
                ((ScheduleTabs) scheduleTabs).navigateToPosition(favTab);

        }
        switchFragment(scheduleTabs);

    }

    /**
     * Método utilizado al escoger una opción del menú de navegación
     * @param id del elemento del menú de navegación seleccionado
     */
    @Override
    public void navigateById(int id){ switch (id){

        // Cierra la aplicación
        case R.id.nav_exit:
            finishAndRemoveTask();
            System.exit(0);

        // Cierra la aplicación y la sesión
        case R.id.nav_exit_and_signout:
            AuthUI.getInstance().signOut(this);
            finishAndRemoveTask();
            System.exit(0);

        // Muestra la información general del congreso
        case R.id.nav_infomation:
            currentSection.setText(R.string.nav_info);
            if(informationView == null)
                informationView = new InformationView();
            switchFragment(informationView);
            isScheduleView = false;
            break;

        // Muestra el cronograma del congreso
        case R.id.nav_schedule:
            currentSection.setText(R.string.nav_schedule);
            toolbar.setTitle(R.string.app_name);

            if(scheduleTabs == null)
                scheduleTabs = ScheduleTabs.newInstance(ScheduleTabs.SCHEDULE_TAB);

            else {

                ((ScheduleTabs) scheduleTabs).setCurrentTab(ScheduleTabs.SCHEDULE_TAB);

                if (isScheduleView)
                    ((ScheduleTabs) scheduleTabs).navigateToPosition(ScheduleTabs.SCHEDULE_TAB);

            }

            switchFragment(scheduleTabs);
            isScheduleView = true;
            break;

        // Muestra la lista de expositores o ponentes
        case R.id.nav_people:
            currentSection.setText(R.string.nav_people);
            if(exhibitorsView == null)
                exhibitorsView = new ExhibitorsView();
            switchFragment(exhibitorsView);
            isScheduleView = false;
            break;

        // Muestra la lista de expositores o ponentes
        case R.id.nav_chat:
            currentSection.setText(R.string.nav_chat);
            if(chatView == null)
                chatView = new ChatView();
            switchFragment(chatView);
            isScheduleView = false;
            break;

        // Muestra la lista de expositores o ponentes
        case R.id.nav_news:
            currentSection.setText(R.string.nav_news);
            if(newsView == null)
                newsView = new NewsView();
            switchFragment(newsView);
            isScheduleView = false;
            break;

        // Muestra la pantalla de administración
        case R.id.nav_manage:
            currentSection.setText(R.string.nav_settings);
            if(configView == null)
                configView = new ConfigView();
            switchFragment(configView);
            isScheduleView = false;
            break;

        // Muestra la pantalla acerca de
        case R.id.nav_pallete:
            currentSection.setText(R.string.nav_palette);
            if(themePicker == null)
                themePicker = ThemePicker.newInstance(this);
            switchFragment(themePicker);
            isScheduleView = false;
            break;

        // Muestra la pantalla acerca de
        case R.id.nav_about:
            currentSection.setText(R.string.nav_about);
            if(aboutView == null)
                aboutView = new AboutView();
            switchFragment(aboutView);
            isScheduleView = false;
            break;

        }

    }

}
