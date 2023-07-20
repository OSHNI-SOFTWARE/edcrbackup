package bd.com.aristo.edcr.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Tariqul.Islam on 3/5/17.
 */

public class SystemUtils {

    public static double getBatteryLevel(Context context) {
        int level = 0, scale = 0;
        try {
            Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }catch (Exception e){
            e.printStackTrace();
        }

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0;
        }

        return ((double) level / (double) scale) * 100.0f;
    }

    public static void log(String log){
        Log.i("SystemUtils", log);
    }

    public static Drawable getDrawableFromID(Context context, int id){
        return context.getResources().getDrawable(id);
    }

    public static int getColorFromID(Context context, int id){
        return context.getResources().getColor(id);
    }

    public static void hideSoftKeyboard(View view, Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
