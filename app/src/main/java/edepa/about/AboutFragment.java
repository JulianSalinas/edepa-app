package edepa.about;

import edepa.modelview.R;
import edepa.activity.MainFragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.res.Resources;

import mehdi.sakout.aboutpage.Element;
import mehdi.sakout.aboutpage.AboutPage;


public class AboutFragment extends MainFragment {

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResource() {
        return R.string.text_about_description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarText(R.string.nav_about);
        setToolbarVisibility(View.VISIBLE);
        setStatusBarColorRes(R.color.app_primary_dark);
    }

    /**
     * Usa una librería, por lo que no se debe llamar a la función
     * super.onCreateView. Solo se retorna la vista que crea la librería
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

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
                .setDescription(resources.getString(getResource()))
                .create();
    }

}
