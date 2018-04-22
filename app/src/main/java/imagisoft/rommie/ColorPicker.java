package imagisoft.rommie;

import android.content.Context;
import android.util.AttributeSet;

import com.kizitonwose.colorpreferencecompat.ColorPreferenceCompat;

public class ColorPicker extends ColorPreferenceCompat{

    public ColorPicker(Context context) {
        super(context);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onClick() {
        // Se requiere así por que se requiere que
    }

}
