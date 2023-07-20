package bd.com.aristo.edcr.utils;

import android.content.Context;
import android.content.Intent;

import bd.com.aristo.edcr.utils.constants.StringConstants;

/**
 * Created by Tariqul.Islam on 5/25/17.
 */


public class RedirectUtils {

    public static void go(Context c, Class to, boolean isClearTop, String flag){
        Intent i = new Intent(c, to);
        i.putExtra("flag", flag);
        if(isClearTop) {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        c.startActivity(i);
    }


    public static void goNew(Context c, Class to){
        Intent i = new Intent(c, to);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }

    public static void goAndBack(Context c, Class to){
        Intent i = new Intent(c, to);
        i.putExtra(StringConstants.PARENT, c.getClass());
        c.startActivity(i);
    }

}
