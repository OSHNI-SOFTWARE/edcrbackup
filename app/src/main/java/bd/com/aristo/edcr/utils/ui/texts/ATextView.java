package bd.com.aristo.edcr.utils.ui.texts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import bd.com.aristo.edcr.utils.ui.fonts.FontCache;
import bd.com.aristo.edcr.utils.ui.fonts.FontsManager;

/**
 * Created by Tariqul.Islam on 5/17/17.
 */

public class ATextView extends TextView {

//    String fontNameNormal = "fonts/Raleway.ttf", fontNameBold = "fonts/RalewayBold.ttf", fontNameItalic = "fonts/RalewayItalic.ttf";
//    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";
        public ATextView(Context context) {
            super(context);
        }

    public ATextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public ATextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(FontsManager.ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */
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
