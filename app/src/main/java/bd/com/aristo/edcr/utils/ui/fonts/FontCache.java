package bd.com.aristo.edcr.utils.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Tariqul.Islam on 5/17/17.
 */

public class FontCache {


    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }

}
