package imagisoft.rommie;

import com.firebase.ui.auth.AuthUI;
import android.support.design.widget.NavigationView;
import android.view.View;

public class MainViewNavigation extends MainViewFirebase
        implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * Variables para poder reciclar los fragmentos
     */
    private ChatView chatView;
    private NewsView newsView;
    private ConfigView configView;
    private ScheduleTabs scheduleTabs;
    private ExhibitorsView exhibitorsView;
    private InformationView informationView;

    /**
     * Método utilizado al escoger una opción del menú de navegación
     * @param id del elemento del menú de navegación seleccionado
     */
    @Override
    public void navigateById(int id){ switch (id){

        // Cierra la aplicación
        case R.id.nav_exit:
            finishAffinity(); break;

        // Cierra la aplicación y la sesión
        case R.id.nav_exit_and_signout:
            AuthUI.getInstance().signOut(this);
            finishAffinity();
            break;

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
                scheduleTabs = new ScheduleTabs();
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
