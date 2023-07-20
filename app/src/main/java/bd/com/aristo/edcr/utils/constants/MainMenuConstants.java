package bd.com.aristo.edcr.utils.constants;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.TempData;

/**
 * Created by Tariqul.Islam on 5/18/17.
 */

public class MainMenuConstants {


    private final String[] menuTexts = new String[]{
            "Doctor Info",
            "Tour Plan",
            "DVR",
            "PWDS",
            "GWDS",
            "Work Plan",
            "DSS",
            "Others",
            "Bill"
    };

    private final String[] reportMenuTexts = new String[]{
            "Home",
            "Sample Statement",
            "Doctor Coverage",
            "Doctor DCR Report",
            "No DCR Yet",
            "DCR Summary",
            "Accompany Info",
            "Physical Stock Check"
    };

    private final int[] menuIcons = new int[]{
            R.drawable.ic_doctor_list,
            R.drawable.ic_tour_plan,
            R.drawable.ic_dvr,
            R.drawable.ic_pwds,
            R.drawable.ic_gwds,
            R.drawable.ic_work_plan,
            R.drawable.ic_day_sample_summery,
            R.drawable.ic_sample_statement,
            R.drawable.ic_bill_statement
    };
    private final int[] reportMenuIcons = new int[]{
            R.drawable.ic_home,
            R.drawable.ic_sample_statement,
            R.drawable.ic_coverage,
            R.drawable.ic_dvr,
            R.drawable.ic_uncovered,
            R.drawable.ic_dcr,
            R.drawable.ic_doctor_list,
            R.drawable.ic_bill_statement
    };
    private Point wh = null;


    private static MainMenuConstants mainMenuConstants = null;
    private MainMenuConstants(){}

    public static MainMenuConstants getInstance(){
        if(mainMenuConstants == null){
            mainMenuConstants = new MainMenuConstants();
        }
        return mainMenuConstants;
    }

    public int[] getMenuIcons(){
        return menuIcons;
    }

    public String[] getMenuTexts(){
        return menuTexts;
    }


    public int[] getReportMenuIcons(){
        return reportMenuIcons;
    }

    public String[] getReportMenuTexts(){
        return reportMenuTexts;
    }

//    public Point getActivityWH(){
//        return wh;
//    }

    public void setActivityWH(Activity activity){
        if(!TempData.IS_WH_CALCULATED){
            Rect rect = new Rect();
            Window win = activity.getWindow();  // Get the Window
            win.getDecorView().getWindowVisibleDisplayFrame(rect);
            // Get the height of Status Bar
            int statusBarHeight = rect.top;
            // Get the height occupied by the decoration contents
            int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
            // Calculate titleBarHeight by deducting statusBarHeight from contentViewTop
            int titleBarHeight = contentViewTop - statusBarHeight;
            // By now we got the height of titleBar & statusBar
            // Now lets get the screen size
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenHeight = metrics.heightPixels;
            int screenWidth = metrics.widthPixels;

//                // Now calculate the height that our layout can be set
//                // If you know that your application doesn't have statusBar added, then don't add here also. Same applies to application bar also
            int layoutHeight = screenHeight - (titleBarHeight + statusBarHeight);
            int calenderHeight = layoutHeight - (int) Math.ceil(2f * (titleBarHeight));
            int menuHeight = layoutHeight - (int) Math.ceil(3f * titleBarHeight);

            TempData.ACTIVITY_WH  = new Point(screenWidth, layoutHeight);
            TempData.CALENDER_WH = new Point((int) Math.ceil(screenWidth / 7f), layoutHeight);
            TempData.DOT_WH = new Point((int) Math.ceil(screenWidth / 31f), layoutHeight);
            TempData.MAIN_MENU_WH = new Point((int) Math.ceil(screenWidth / 3f), (int) Math.ceil(menuHeight / 3f));
            TempData.REPORT_MAIN_MENU_WH = new Point((int) Math.ceil(screenWidth / 3f), (int) Math.ceil(menuHeight / 3f));
            TempData.IS_WH_CALCULATED = true;
        }
    }

}
