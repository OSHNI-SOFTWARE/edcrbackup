package bd.com.aristo.edcr.utils.ui.texts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by Tariqul.Islam on 5/17/17.
 */

public class ACheckBox extends CheckBox {

    public boolean isClicked = false;

    public ACheckBox(Context context) {
            super(context);
        }

    public ACheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ACheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
