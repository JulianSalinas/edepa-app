package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import mehdi.sakout.aboutpage.Element;
import mehdi.sakout.aboutpage.AboutPage;

public class AboutView extends MainActivityFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        super.onCreateView(inflater, container, bundle);
        
        Element versionElement = new Element();
        versionElement.setTitle(getResources().getString(R.string.text_version));
        setToolbarText(R.string.nav_about);

        return new AboutPage(getNavigation())
                .isRTL(false)
                .addItem(versionElement)
                .setImage(R.drawable.ic_edepa)
                .addGroup(getResources().getString(R.string.text_connect_with_us))
                .addEmail("july12sali@gmail.com", "Julian Salinas")
                .addEmail("bdinarte1996@gmail.com", "Brandon Dinarte")
                .setDescription(getResources().getString(R.string.text_about_description))
                .create();
    }

}
