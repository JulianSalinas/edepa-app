package imagisoft.rommie;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;

public class CustomLayout extends LinearLayoutManager{

    /**
     * Determina que tan rápido se mueve el scroll
     */
    private static final float MILLISECONDS_PER_INCH = 50f;

    /**
     * Necesario para saber a que vista se le agrega el efecto
     */
    private Context context;

    CustomLayout(Context context) {
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

        LinearSmoothScroller scroller = new LinearSmoothScroller(context) {

            @Override
            public PointF computeScrollVectorForPosition(int target) {
                return CustomLayout.this.computeScrollVectorForPosition(target);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
            }

        };


        /*
         * Se podríua ajustar para que el screoll no inicie en el primer elemento
         */
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);

    }

}