package imagisoft.rommie;

import com.firebase.ui.auth.AuthUI;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;

public class MainViewNavigation extends MainViewFirebase
        implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * Variables para poder reciclar los fragmentos
     */
    private Fragment chatView;
    private Fragment newsView;
    private Fragment configView;
    private Fragment scheduleTabs;
    private Fragment exhibitorsView;
    private Fragment informationView;

    /**
     * Atributos custom
     */
    private TextView currentSection;
    private FloatingActionLayout favoriteButton;

    /**
     * Sección de las vista que hay en el encabezado
     */
    public void bindNavigationViews(){
        View header = navigation.getHeaderView(0);
        favoriteButton = header.findViewById(R.id.favorite_button);
        currentSection = header.findViewById(R.id.current_section_view);
        favoriteButton.setOnClickListener(v -> onFavoriteButtonClick());
    }

    /**
     * Envía al usuario al tab donde están sus favoritos
     */
    @Override
    public void onFavoriteButtonClick() {

        int favTab = ScheduleTabs.FAVORITES_TAB;
        currentSection.setText(R.string.nav_favorites);

        if(scheduleTabs == null)
            scheduleTabs = ScheduleTabs.newInstance(favTab);
        else
            ((ScheduleTabs) scheduleTabs).setCurrentTab(favTab);

        switchFragment(scheduleTabs);
        ((ScheduleTabs) scheduleTabs).navigateToPosition(favTab);

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
            break;

        // Muestra el cronograma del congreso
        case R.id.nav_schedule:
            currentSection.setText(R.string.nav_schedule);
            if(scheduleTabs == null)
                scheduleTabs = ScheduleTabs.newInstance();
            else ((ScheduleTabs) scheduleTabs).navigateToPosition(ScheduleTabs.SCHEDULE_TAB);
            switchFragment(scheduleTabs);
            break;

        // Muestra la lista de expositores o ponentes
        case R.id.nav_people:
            currentSection.setText(R.string.nav_people);
            if(exhibitorsView == null)
                exhibitorsView = new ExhibitorsView();
            switchFragment(exhibitorsView);
            break;

        // Muestra la lista de expositores o ponentes
        case R.id.nav_chat:
            currentSection.setText(R.string.nav_chat);
            if(chatView == null)
                chatView = new ChatView();
            switchFragment(chatView);
            break;

        // Muestra la lista de expositores o ponentes
        case R.id.nav_news:
            currentSection.setText(R.string.nav_news);
            if(newsView == null)
                newsView = new NewsView();
            switchFragment(newsView);
            break;

        // Muestra la pantalla de administración
        case R.id.nav_manage:
            currentSection.setText(R.string.nav_settings);
            if(configView == null)
                configView = new ConfigView();
            switchFragment(configView);
            break;
        }

    }

}
