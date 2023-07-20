package bd.com.aristo.edcr.utils.ui.texts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

/**
 * Created by Tariqul.Islam on 5/17/17.
 */

public class ARadioGroup extends RadioGroup {

    public boolean isClicked = false;

    public ARadioGroup(Context context) {
            super(context);
        }

    public ARadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
