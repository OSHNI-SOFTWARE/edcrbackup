package bd.com.aristo.edcr.utils;

import android.content.Context;

/**
 * Created by Tariqul.Islam on 6/7/17.
 */

public class StringUtils {


    public static long hashString64Bit(CharSequence str) {
        long result = 0xcbf29ce484222325L;
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            result ^= str.charAt(i);
            result *= 0x100000001b3L;
        }
        return result;
    }


    public static String getVersionName(Context context){
        try {
            return
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static int getVersionCode(Context context){
        try {
            return
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 99;
    }

    public static String getAndFormAmp(String s){
        String[] rtnString = s.split("&");// s.replace("&", " and ");
        StringBuilder string = new StringBuilder();
        string.append(rtnString[0]);
        for(int i = 1; i < rtnString.length; i++){
            string.append(" and "+rtnString[i]);
        }
        String str = string.toString();
        String[] rtnString1 = str.split("'");
        StringBuilder string1 = new StringBuilder();
        string1.append(rtnString1[0]);
        for(int i = 1; i < rtnString1.length; i++){
            string1.append(" "+rtnString1[i]);
        }
        return string1.toString();
    }


}
