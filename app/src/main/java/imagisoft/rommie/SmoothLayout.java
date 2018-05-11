package imagisoft.rommie;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;


public class SmoothLayout extends LinearLayoutManager {

    /**
     * Necesario para saber a que vista se le agrega el efecto
     */
    private Context context;

    /**
     * Determina que tan rápido se mueve el scroll
     */
    private final float MILLISECONDS_PER_INCH = 50f;

    /**
     * Constructor del layout
     * @param context Actvidad o fragmento donde se ejecuta
     */
    public SmoothLayout(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Coloca el scrooll en la vista. No es necesario llamar esta función en otro
     * lugar dentro del código, la vista la usa de forma automática
     */
    @Override
    public void smoothScrollToPosition(RecyclerView view,
                                       RecyclerView.State state, final int position) {

        // Scroll que se mueve acorde a la velocidad aplicada
        LinearSmoothScroller scroller = new LinearSmoothScroller(context) {

            /*
             * Esto sirve para mover la vista hasta el último elemento agregado.
             * Por ejemplo, el chat o las noticias donde la cantidad de elementos se actuliza
             */
            @Override
            public PointF computeScrollVectorForPosition(int target) {
                return SmoothLayout.this.computeScrollVectorForPosition(target);
            }

            /**
             * Calcula la velocidad del scroll según las dimensiones de la pantalla
             */
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
            }

        };

        // Inicia con la animación
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);

    }

}