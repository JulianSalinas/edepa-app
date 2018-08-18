package edepa.minilibs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class OnlineHelper {

    /**
     * Revisa si existe conexión a internet
     * @return True si el teléfono está conectado
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
        context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm == null ? null : cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

}
