package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.res.Resources;

import mehdi.sakout.aboutpage.Element;
import mehdi.sakout.aboutpage.AboutPage;


public class AboutView extends MainActivityFragment {

    /**
     * Usa una librería, por lo que no se debe llamar a la función
     * super.onCreateVie. Solo se retorna la vista que crea la librería.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        setToolbarText(R.string.nav_about);
        setToolbarVisibility(View.VISIBLE);
        setTabLayoutVisibility(View.GONE);

        Resources res = getResources();
        Element versionElement = new Element();
        versionElement.setTitle(res.getString(R.string.text_version));

        return new AboutPage(activity)
                .isRTL(false)
                .addItem(versionElement)
                .setImage(R.drawable.ic_edepa)
                .addGroup(res.getString(R.string.text_connect_with_us))
                .addEmail("july12sali@gmail.com", "Julian Salinas")
                .addEmail("bdinarte1996@gmail.com", "Brandon Dinarte")
                .setDescription(res.getString(R.string.text_about_description))
                .create();
    }

}
