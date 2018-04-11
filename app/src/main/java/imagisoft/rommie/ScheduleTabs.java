package imagisoft.rommie;

import imagisoft.edepa.FavoriteList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ViewSwitcher;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;

/**
 * Contiene los tabs de cronograma, agenda y en curso
 */
public class ScheduleTabs extends MainViewFragment implements TabLayout.OnTabSelectedListener {

    /*
     * Usadas para saber cual tab se debe colocar al crearse la vista
     */
    public static final int SCHEDULE_TAB = 0;
    public static final int FAVORITES_TAB = 1;
    public static final int ONGOING_TAB = 2;

    /**
     * TabLayout con sus tres vistas principales
     */
    private TabLayout tabLayout;
    private Fragment [] tabOptions;

    /**
     * Contiene el número de tab que se está mostrando
     */
    private int currentTab;

    /**
     * Variable usada para colocar los tabs en la appbar
     */
    protected ViewSwitcher switcher;

    /**
     * Contructor po defector, coloca la vista en el tab del cronograma
     */
    public static ScheduleTabs newInstance() {

        ScheduleTabs fragment = new ScheduleTabs();
        fragment.currentTab = 0;
        return fragment;

    }

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan el # tab que se desea ver
     */
    public static ScheduleTabs newInstance(int currentTab) {

        ScheduleTabs fragment = new ScheduleTabs();
        fragment.currentTab = currentTab;
        return fragment;

    }

    /**
     * Se inicializan las variables no gráficas
     */
    @Override
    public void onCreate(@Nullable Bundle bundle) {

        super.onCreate(bundle);
        currentTab = 0;
        tabOptions = new Fragment[3];

    }

    /**
     * Se crea la vista que contiene el tabLayout
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle bundle) {

        return inflater.inflate(R.layout.schedule_tabs_view, container, false);

    }

    /**
     * Se agrega los tabs al toolbar de la actividad y se navaga
     * hacia el última tab seleccionado
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        assert getActivity() != null;

        // Se agrega fucionalidad a los tabs
        tabLayout = getActivity().findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);

        // Cuando la vista se crea se colocan los tabs en la appbar
        switcher = getActivity().findViewById(R.id.toolbar_switcher);
        switcher.setMeasureAllChildren(false);
        switcher.showNext();

        // Configuración inicial que se muestra al crear la vista
        navigateToPosition(currentTab);

    }

    /**
     * Al cambiar a otra sección se deben quitar los tabs
     * de la appbar
     */
    @Override
    public void onDestroyView() {

        super.onDestroyView();
        switcher.showNext();

    }

    /**
     * Cambia el contenido según el tab que se presione
     * @param fragment que contiene el nuevo contenido (actividades o eventos )
     */
    public void switchFragment(Fragment fragment){

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        transaction.replace(R.id.tabs_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
     * Evento que dispara la función switch framgent
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        int position = tab.getPosition();
        if(position != currentTab)
            navigateToPosition(position);

    }

    /**
     * Según el tab que escoge el usuario, se instancia la vista
     * (instanciación perezosa) y se coloca en pantalla
     * @param position: Número de tab de izq a der
     */
    private void navigateToPosition(int position) {

        currentTab = position;
        if(tabOptions[position] == null)
            tabOptions[position] = createTabFragment(position);
        switchFragment(tabOptions[position]);

    }

    /**
     * Sirve de apoyo a la función navigateToPosition para realizar
     * la instanciación
     * @param tabId: Alguno de los atributos estáticos definidos
     */
    public Fragment createTabFragment(int tabId){ switch (tabId){

        case SCHEDULE_TAB:
            return new SchedulePagerFragment();

        case FAVORITES_TAB:
            return ScheduleView.newInstance(FavoriteList.getInstance().getEvents());

        case ONGOING_TAB:
            return ScheduleView.newInstance(FavoriteList.getInstance().getEvents());

        default:
            return ScheduleView.newInstance(FavoriteList.getInstance().getEvents());

    }}

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Se requiere sobrescribir
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Se requiere sobrescribir
    }

}
