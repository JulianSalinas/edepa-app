package imagisoft.rommie;

import android.os.Bundle;
import android.view.View;

public class BlankFragmentTabbed extends BlankFragment {

    /**
     * Crea una nueva instancia del fragmento
     * Guarda y restaura el mensaje en caso de que la pantalla se gire
     * @param description: Mensaje que se debe mostrar en el fragmento
     * @return BlankFragment
     */
    public static BlankFragmentTabbed newInstance(String description){

        Bundle args = new Bundle();
        args.putString("msg", description);

        BlankFragmentTabbed fragment = new BlankFragmentTabbed();
        fragment.setArguments(args);

        return fragment;

    }

    /**
     * Se coloca el mensaje en la vista
     * @param bundle: No es necesario
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setToolbarVisibility(View.VISIBLE);
        setTabLayoutVisibility(View.VISIBLE);
    }

}
