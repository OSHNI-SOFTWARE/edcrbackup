package bd.com.aristo.edcr;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.Calendar;

import javax.inject.Inject;

import bd.com.aristo.edcr.listener.MonthChangedListener;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dss.DSSCalendarFragment;
import bd.com.aristo.edcr.modules.gwds.GWDSGiftFragment;
import bd.com.aristo.edcr.modules.gwds.GWDSListFragment;
import bd.com.aristo.edcr.modules.pwds.PWDSListFragment;
import bd.com.aristo.edcr.modules.pwds.PWDSProductsFragment;
import bd.com.aristo.edcr.modules.reports.others.DVRSummaryFragment;
import bd.com.aristo.edcr.modules.reports.others.DCRDoctorListFragment;
import bd.com.aristo.edcr.modules.tp.activity.TPListActivity;
import bd.com.aristo.edcr.modules.tp.fragment.TPCalendarFragment;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.modules.wp.fragment.WPCalendarFragment;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HostActivity extends AppCompatActivity implements MonthChangedListener {

    @Inject
    Realm r;

    String flag;
    Resources res;
    String title;
    Activity activity;

    int month, year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        res =  getResources();
        activity = this;
        Intent intent = getIntent();
        getToday();
        flag = intent.getStringExtra("flag");
        //nextFlag = flag;

        MyLog.show("HostActivity","OnCreate called");

        Log.e("HostActivity", flag);
        setFragmentAndTitle();

    }



    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        if (id == R.id.action_list){
            TPListActivity.start(this, month, year);
        }

        if (id == R.id.action_pwds_list){
            PWDSListFragment fragment = new PWDSListFragment();
            Bundle b = new Bundle();
            b.putInt("month", month);
            b.putInt("year", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("pwds_list").commit();
        }
        if (id == R.id.action_gwds_list){
            GWDSListFragment fragment = new GWDSListFragment();
            Bundle b = new Bundle();
            b.putInt("month", month);
            b.putInt("year", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("gwds_list").commit();
        }


        if (id == R.id.action_dvr_list){
            DVRSummaryFragment fragment = new DVRSummaryFragment();
            Bundle b = new Bundle();
            b.putInt("month", month);
            b.putInt("year", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("dvr_list").commit();
        }

        if (id == R.id.action_dvr_doc){
            DCRDoctorListFragment fragment = new DCRDoctorListFragment();
            Bundle b = new Bundle();
            b.putInt("MONTH", month);
            b.putInt("YEAR", year);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("dvr_list").commit();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if(WPUtils.IS_CHANGED){
                ToastUtils.displayConfirmationPopupForWorkPlan(activity);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            finish();
        }
    }


    public void getToday(){
        Calendar cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH)+1;
        year = cal.get(Calendar.YEAR);
    }

    @Override
    public void onMonthChange(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public void setFragmentAndTitle() {

        Fragment fragment;
        Bundle b = new Bundle();
        getToday();
        switch (flag){
            case "tp":
                title = "Tour Plan";
                fragment = new TPCalendarFragment();
                ((TPCalendarFragment) fragment).monthChangedListener = this;
                break;
            case "pwds":
                title = "P W D S";
                fragment = new PWDSProductsFragment();
                ((PWDSProductsFragment) fragment).monthChangedListener = this;
                break;
            case "gwds":
                title = "G W D S";
                fragment = new GWDSGiftFragment();
                ((GWDSGiftFragment) fragment).monthChangedListener = this;
                break;
            case "wp":
                title = "Work Plan";
                fragment = new WPCalendarFragment();
                ((WPCalendarFragment) fragment).monthChangedListener = this;
                break;
            case "dss":
                title = "D S S";
                fragment = new DSSCalendarFragment();
                break;
            default:
                title = "D S S";
                fragment = new DSSCalendarFragment();
                break;
        }

        setTitle(title);
        b.putInt("month", month);
        b.putInt("year", year);
        b.putSerializable(StringConstants.DATE_MODEL, DCRUtils.getToday());
        fragment.setArguments(b);
        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++){
            getSupportFragmentManager().popBackStack();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("host").commit();

    }
}
