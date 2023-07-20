package bd.com.aristo.edcr.utils.ui.buttons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import bd.com.aristo.edcr.utils.ui.fonts.FontCache;

import static bd.com.aristo.edcr.utils.ui.fonts.FontsManager.ANDROID_SCHEMA;
import static bd.com.aristo.edcr.utils.ui.fonts.FontsManager.fontNameBold;

/**
 * Created by Tariqul.Islam on 5/30/17.
 */

public class AButton extends Button {

    public AButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public AButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    public void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        return FontCache.getTypeface(fontNameBold, context);
    }

}
