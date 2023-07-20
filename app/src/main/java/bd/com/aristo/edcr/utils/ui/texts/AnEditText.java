package bd.com.aristo.edcr.utils.ui.texts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import bd.com.aristo.edcr.utils.ui.fonts.FontCache;
import bd.com.aristo.edcr.utils.ui.fonts.FontsManager;

/**
 * Created by Tariqul.Islam on 6/20/17.
 */

public class AnEditText extends EditText {

    public AnEditText(Context context) {
        super(context);
    }

    public AnEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public AnEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }


    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(FontsManager.ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        switch (textStyle) {
            case Typeface.BOLD: // bold
                return FontCache.getTypeface(FontsManager.fontNameBold, context);

            case Typeface.ITALIC: // italic
                return FontCache.getTypeface(FontsManager.fontNameItalic, context);

//            case Typeface.BOLD_ITALIC: // bold italic
//                return FontCache.getTypeface("SourceSansPro-BoldItalic.ttf", context);

            case Typeface.NORMAL: // regular
            default:
                return FontCache.getTypeface(FontsManager.fontNameNormal, context);
        }
    }
}
