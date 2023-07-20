package bd.com.aristo.edcr;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.databinding.ActivityHomeBinding;
import bd.com.aristo.edcr.listener.ClearAllDataListener;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.NotificationModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.activity.DCRActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.activity.DCRCalendarActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.modules.wp.model.WPModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.networking.ResponseListener;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.RealmUtils;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

import static bd.com.aristo.edcr.utils.DateTimeUtils.FORMAT3;
import static bd.com.aristo.edcr.utils.DateTimeUtils.FORMAT9;
import static bd.com.aristo.edcr.utils.DateTimeUtils.getToday;

public class HomeActivity extends AppCompatActivity implements ResponseListener<NotificationModel>, ClearAllDataListener {

    private String TAG = "HomeActivity";

    public static HomeActivity activity;
    private ActivityHomeBinding binding;
    TextView notificationCountTV;
    Context mContext;
    DateModel dateModel;
    @Inject
    RequestServices requestServices;
    @Inject
    Realm r;
    @Inject
    LoadingDialog loadingDialog;

    long notificationCount = 0;


    private CompositeDisposable mCompositeDisposable  = new CompositeDisposable();

    @Inject
    APIServices apiServices;

    public static String INTENT_SYNC_PARAM ="isNeedSync";
    public static String INTENT_SYNC_DCR_PARAM ="isDCRSynced";
    Boolean isNeedSync = false;
    final long productSyncMillis = 1800000;


    public UserModel userModel;

    int plan = 0,
            mPlan = 0,
            ePlan = 0,
            mDcr = 0,
            eDcr = 0,
            newDcr = 0,
            totalDcr = 0,
            mProgress = 0,
            eProgress = 0;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }


    public static void start(Activity context, boolean isNeedSync){
        Intent intent = new Intent(context,HomeActivity.class);
        intent.putExtra(INTENT_SYNC_PARAM,isNeedSync);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        App.getComponent().inject(this);
        activity = this;
        mContext = this;
        //test();
        //Firebase messaging service purpose
        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "all: Subscription success";
                if (!task.isSuccessful()) {
                    msg = "all: Subscription failed";
                }
                Log.d(TAG, msg);
                //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("mpo")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "mpo: Subscription success";
                        if (!task.isSuccessful()) {
                            msg = "mpo: Subscription failed";
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("test")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "mpo: Subscription success";
                        if (!task.isSuccessful()) {
                            msg = "mpo: Subscription failed";
                        }
                        Log.d(TAG, msg);
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        dateModel = DCRUtils.getToday();
        //int d = 50 / 0;
        //Get user info
        getUserInfo();
        setTitle("EDCR");
        binding.llContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()  {
                binding.llContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = binding.llContent.getWidth();
                int height;
                if(getSupportActionBar() != null) {
                    height = binding.llContent.getHeight() - getSupportActionBar().getHeight();
                } else {
                    height = binding.llContent.getHeight();
                }
                setMenuWH(height);
            }
        });
      //  showFrontPanel();
        this.registerReceiver(this.fcmMessageReceiver, new IntentFilter("EDCR_MESSAGING_EVENT"));
        //scheduleJob();
        setupBadge();
        binding.tvActionDCRSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DCRCalendarActivity.start(activity);
            }
        });

        binding.tvActionNewDCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DCRActivity.class);
                intent.putExtra("TYPE", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.tvActionEveningDCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DCRActivity.class);
                intent.putExtra("TYPE", 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.tvActionMorningDCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DCRActivity.class);
                intent.putExtra("TYPE", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void test() {
        RealmUtils.exportDatabase();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = MenuItemCompat.getActionView(menuItem);
        notificationCountTV = (TextView) actionView.findViewById(R.id.notification_badge);

        //setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_goto_menu) {
            // do something here
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.action_logout) {

            displayLogoutConfirmationPopup();

        }else if (id == R.id.action_settings){

            SettingsActivity.start(activity, this);

        }else if (id == R.id.action_notification){
            NotificationsActivity.start(activity);
        }



        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if(ConnectionUtils.isNetworkConnected(this)){
            //requestServices.getNotifications(this, r, apiServices, userModel.getUserId(), this);
        } else {
            ToastUtils.longToast(StringConstants.INTERNET_CONNECTION_ERROR);
            updateNotifications();
        }
    }



    public void updateNotifications(){
        notificationCount = r.where(NotificationModel.class).equalTo("isRead",false).count();

        MyLog.show("setupBadge", "Badge updated");

        if (notificationCountTV != null) {
            if (notificationCount == 0) {
                notificationCountTV.setVisibility(View.GONE);
                try {
                    //ShortcutBadger.removeCount(activity);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                notificationCountTV.setText(String.valueOf(Math.min(notificationCount, 99)));
                notificationCountTV.setVisibility(View.VISIBLE);
                //show the notification on app icon
                try {
                    //ShortcutBadger.applyCount(activity, (int) notificationCount);
                }catch (Exception e){
                    //ShortcutBadger.removeCount(activity);
                    e.printStackTrace();
                }

            }
        }
    }


    public void displayLogoutConfirmationPopup(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
       // alert.setTitle(getString(R.string.logout));
        alert.setMessage(StringConstants.LOGOUT_CONF_MSG);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 dialog.dismiss();
                r.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm r) {
                        UserModel userModel = r.where(UserModel.class).findFirst();

                        if(userModel!=null){
                            r.delete(UserModel.class);
                        }
                    }
                });
                finish();

            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }


    public void syncAppDB(){
        if (ConnectionUtils.isNetworkConnected(this)) {
            //requestServices.syncMaster(this, apiServices, userModel.getMarketCode(), userModel.getUserId(), r);
        } else {
            ToastUtils.shortToast(getString(R.string.check_internet));
        }
    }


    private BroadcastReceiver fcmMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            MyLog.show("BroadcastReceiver", "Called");
            //to receive service messages
            if( intent.getAction().equalsIgnoreCase("EDCR_MESSAGING_EVENT") ){
                //updating badge
                setupBadge();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        //setupBadge();
        setDisplayData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(fcmMessageReceiver);
        if (mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }

    }

    public void setDisplayData(){
        binding.tvTodayDate.setText(getToday(FORMAT3));
        //DVR Count
        mPlan = 0;
        ePlan = 0;
        mDcr = 0;
        eDcr = 0;

        //plan, mPlan, ePlan count
        List<WPModel> savedWPs = r.where(WPModel.class)
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        if(savedWPs != null && savedWPs.size() > 0) {
            List<WPModel> uniqueWPModels = WPUtils.uniqueWP(savedWPs);
            plan = uniqueWPModels.size();
            for(WPModel wpModel:uniqueWPModels){
                if(wpModel.isMorning()){
                    mPlan++;
                } else {
                    ePlan++;
                }
            }
        }
        //dcr, mDcr, eDcr count
        List<DCRModel> dcrModelsToday = DCRUtils.getDCRList(r, DateTimeUtils.getToday(FORMAT9));
        List<DCRModel> dcrModelsMonth = DCRUtils.getDCRListMonth(r, DCRUtils.getToday().getMonth(), DCRUtils.getToday().getYear());
        totalDcr = dcrModelsMonth.size();
        for(DCRModel dcrModel:dcrModelsToday){
            if(dcrModel.getShift().equalsIgnoreCase(StringConstants.MORNING)){
                mDcr++;
            } else {
                eDcr++;
            }
        }
        //newDcr count
        List<NewDCRModel> newDCRModels = DCRUtils.getNewDCRList(r, DateTimeUtils.getToday(FORMAT9));
        newDcr = newDCRModels.size();
        eProgress = (int)((float)eDcr/ (float) ePlan * 100);
        mProgress = (int)((float)mDcr/ (float) mPlan * 100);
        binding.progressBarED.setProgress(eProgress);
        binding.progressBarMD.setProgress(mProgress);
        String progressMD = mDcr+"/"+mPlan;
        binding.tvProgressMD.setText(progressMD);
        String progressED = eDcr+"/"+ePlan;
        binding.tvProgressED.setText(progressED);
        binding.tvDCRCount.setText(""+totalDcr);
        binding.tvNewDCRCount.setText(""+newDcr);
    }


    @Override
    public void onSuccess(List<NotificationModel> valueList) {

    }

    @Override
    public void onSuccess(NotificationModel value) {
    }

    @Override
    public void onSuccess() {
        isNeedSync = SharedPrefsUtils.getBooleanPreference(this, StringConstants.IS_SYNCED_SUCCESS, true);
        if(isNeedSync)
            syncAppDB();
        else
            //requestServices.getToken(apiServices, r, userModel.getUserId());
    }

    @Override
    public void onFailed() {
        isNeedSync = SharedPrefsUtils.getBooleanPreference(this, StringConstants.IS_SYNCED_SUCCESS, true);
        if(isNeedSync)
            syncAppDB();
        else
            //requestServices.getToken(apiServices, r, userModel.getUserId());
    }


    @Override
    public void onClearSuccess() {
        finish();
    }

    public void setMenuWH(int h){
        SharedPrefsUtils.setIntegerPreference(this, StringConstants.PREF_MENU_H, h/3 - 20);

        if(getSupportActionBar() != null) {
            h = h - getSupportActionBar().getHeight() - 90;
        }

        SharedPrefsUtils.setIntegerPreference(this, StringConstants.PREF_CALENDAR_ITEM_H, h/5);
        SharedPrefsUtils.setIntegerPreference(this, StringConstants.PREF_SUMMARY_GRID_ITEM_H, h/15);
    }
}
