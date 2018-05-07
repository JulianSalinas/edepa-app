package imagisoft.rommie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutView extends MainViewFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        Element versionElement = new Element();
        versionElement.setTitle(getResources().getString(R.string.text_version));

        toolbarText = getToolbar().getTitle();
        getToolbar().setTitle(getResources().getString(R.string.nav_about));

        return new AboutPage(getNavigation())
                .isRTL(false)
                .addItem(versionElement)
                .setImage(R.drawable.ic_edepa)
                .addGroup(getResources().getString(R.string.text_connect_with_us))
                .addEmail("july12sali@gmail.com", "Julian Salinas")
                .addEmail("bdinarte1996@gmail.com", "Brandon Dinarte")
                .addGitHub("JulianSalinas/Rommie")
                .setDescription(getResources().getString(R.string.text_about_description))
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getToolbar().setTitle(toolbarText);
    }

}
