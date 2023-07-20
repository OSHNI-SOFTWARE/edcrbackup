package bd.com.aristo.edcr.utils;

import android.util.Log;

/**
 * Created by altaf.sil on 12/12/17.
 */

public class MyLog {

    public static boolean showLog = true; //set false when go live

    public static void show(String title, String message){
        if(showLog){
            if (message == null) {
                Log.e(title, "EMPTY");
            }else{
                Log.e(title, message);
            }
        }

    }
}
