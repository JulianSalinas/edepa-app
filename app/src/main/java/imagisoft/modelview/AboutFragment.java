package imagisoft.modelview;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.res.Resources;

import mehdi.sakout.aboutpage.Element;
import mehdi.sakout.aboutpage.AboutPage;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class AboutFragment extends ActivityFragment {

    /**
     * No usa un recurso como layout, pues usa una librería.
     * Por eso se colocar solo la descripción que se muestra
     */
    @Override
    public void setupResource() {
        this.resource = R.string.text_about_description;
    }

    /**
     * Coloca el título
     * Oculta las vistas que no necesita
     */
    @Override
    public void setupActivityView() {
        setToolbarText(R.string.nav_about);
        setToolbarVisibility(VISIBLE);
        setTabLayoutVisibility(GONE);
    }

    /**
     * Usa una librería, por lo que no se debe llamar a la función
     * super.onCreateView. Solo se retorna la vista que crea la librería.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        Resources resources = getResources();
        Element versionElement = new Element();
        versionElement.setTitle(resources.getString(R.string.text_version));

        return new AboutPage(activity)
                .isRTL(false)
                .addItem(versionElement)
                .setImage(R.drawable.ic_edepa)
                .addGroup(resources.getString(R.string.text_connect_with_us))
                .addEmail("july12sali@gmail.com", "Julian Salinas")
                .addEmail("bdinarte1996@gmail.com", "Brandon Dinarte")
                .setDescription(resources.getString(resource))
                .create();
    }

}
