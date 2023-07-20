package bd.com.aristo.edcr.modules.reports;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.reports.others.DCRAccompanyFragment;
import bd.com.aristo.edcr.modules.reports.others.DCRMPOFragment;
import bd.com.aristo.edcr.modules.reports.others.DVRSummaryFragment;
import bd.com.aristo.edcr.modules.reports.others.DoctorCoverageFragment;
import bd.com.aristo.edcr.modules.reports.others.DoctorWiseDOTFragment;
import bd.com.aristo.edcr.modules.reports.others.DoctorWiseItemFragment;
import bd.com.aristo.edcr.modules.reports.others.NoDCRDoctorListFragment;
import bd.com.aristo.edcr.modules.reports.others.PWDSReportFragment;
import bd.com.aristo.edcr.modules.reports.others.PhysicalStockFragment;
import bd.com.aristo.edcr.modules.reports.others.UncoverDotFragment;
import bd.com.aristo.edcr.modules.reports.others.WorkPlanSummaryFragment;
import bd.com.aristo.edcr.modules.reports.ss.SampleStatementFragment;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.reports.ss.StatementSummaryDialogFragment;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.MyLog;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ReportActivity extends AppCompatActivity {

    @Inject
    Realm r;
    String flag;
    Resources res;
    String title;
    Activity activity;
    int month, year, day;

    public DateModel dateModel;
    public DateModel prevDateModel;
    boolean isCurrentMonth = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        res =  getResources();
        activity = this;
        Intent intent = getIntent();
        dateModel = DCRUtils.getToday();
        month = dateModel.getMonth();
        year = dateModel.getYear();
        day = dateModel.getDay();
        setPrevDateModel();
        flag = intent.getStringExtra("flag");
        MyLog.show("ReportActivity","OnCreate called");
        Log.e("HostActivity", flag);
        setFragmentAndTitle(flag);

    }


    public void setFragmentAndTitle(String flag){

        Fragment fragment = null;// = new DCRDoctorListFragment();
        switch (flag){
            case "DOCTOR_DCR":
                title = res.getString(R.string.title_report_fragment_doctor_list);
                fragment = new DoctorWiseDOTFragment();
                Bundle b1 = new Bundle();
                b1.putInt("month", month);
                b1.putInt("year", year);
                b1.putInt("day", day);
                fragment.setArguments(b1);
                break;

            case "DOCTOR_NO_DCR":
                title = res.getString(R.string.title_report_fragment_market_dvr);
                fragment = new NoDCRDoctorListFragment();
                Bundle nob = new Bundle();
                nob.putInt("month", month);
                nob.putInt("year", year);
                fragment.setArguments(nob);
                break;

            case "DVR_SUMMARY":
                title = res.getString(R.string.title_report_fragment_dvr_summary);
                fragment = new DVRSummaryFragment();
                Bundle b = new Bundle();
                b.putInt("month", month);
                b.putInt("year", year);
                fragment.setArguments(b);
                break;

            case "PWDS":
                title = res.getString(R.string.title_report_fragment_pwds);
                Bundle b2 = new Bundle();
                b2.putInt("month", month);
                b2.putInt("year", year);
                fragment = new PWDSReportFragment();
                fragment.setArguments(b2);
                break;
            case "WP":
                title = res.getString(R.string.title_report_fragment_work_plan);
                fragment = new WorkPlanSummaryFragment();
                break;

            case "UNCOVERED_DOCTORS":
                title = res.getString(R.string.title_report_fragment_uncovered_doctor);
                fragment = new UncoverDotFragment();
                break;

            case "DOCTOR_COVERAGE":
                title = res.getString(R.string.title_report_fragment_doctor_coverage);
                fragment = new DoctorCoverageFragment();
                Bundle bc = new Bundle();
                bc.putInt("month", month);
                bc.putInt("year", year);
                fragment.setArguments(bc);
                break;
            case "SAMPLE_STATEMENT":
                title = res.getString(R.string.title_report_fragment_sample_statement);
                fragment = new SampleStatementFragment();
                Bundle bs = new Bundle();
                bs.putInt("month", month);
                bs.putInt("year", year);
                fragment.setArguments(bs);
                break;
            case "DCR_SUMMARY":
                title = res.getString(R.string.title_report_fragment_dcr_summary);
                fragment = new DCRMPOFragment();
                Bundle bdcr = new Bundle();
                bdcr.putInt("month", month);
                bdcr.putInt("year", year);
                fragment.setArguments(bdcr);
                break;
            case "DOCTOR_WISE_ITEM":
                title = res.getString(R.string.title_report_fragment_doctor_wise_item);
                fragment = new DoctorWiseItemFragment();
                Bundle bdwi = new Bundle();
                bdwi.putInt("month", month);
                bdwi.putInt("year", year);
                fragment.setArguments(bdwi);
                break;
            case "ACCOMPANY":
                title = res.getString(R.string.title_report_fragment_accompany);
                fragment = new DCRAccompanyFragment();
                Bundle ba = new Bundle();
                ba.putInt("month", month);
                ba.putInt("year", year);
                fragment.setArguments(ba);
                break;
            case "PSC":
                title = res.getString(R.string.title_report_fragment_psc);
                fragment = new PhysicalStockFragment();
                Bundle bpsc = new Bundle();
                bpsc.putInt("month", month);
                bpsc.putInt("year", year);
                fragment.setArguments(bpsc);
                break;

            default:
                break;
        }

        setTitle(title);
        this.flag = flag;
        if(fragment != null) {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("report").commit();
        }

    }



    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if(id == R.id.action_month_ss){
            isCurrentMonth = !isCurrentMonth;
            setMonthYear(isCurrentMonth);
            monthChange(0);
            invalidateOptionsMenu();
        }

        if(id == R.id.action_month_coverage){
            isCurrentMonth = !isCurrentMonth;
            setMonthYear(isCurrentMonth);
            monthChange(1);
            invalidateOptionsMenu();
        }
        if(id == R.id.action_month_dd){
            isCurrentMonth = !isCurrentMonth;
            setMonthYear(isCurrentMonth);
            monthChange(2);
            invalidateOptionsMenu();
        }

        //action_month_accompany
        if(id == R.id.action_month_accompany){
            isCurrentMonth = !isCurrentMonth;
            setMonthYear(isCurrentMonth);
            monthChange(3);
            invalidateOptionsMenu();
        }

        if(id == R.id.action_month_psc){
            isCurrentMonth = !isCurrentMonth;
            setMonthYear(isCurrentMonth);
            monthChange(4);
            invalidateOptionsMenu();
        }
        if(id == R.id.action_month_dcr_sum){
            isCurrentMonth = !isCurrentMonth;
            setMonthYear(isCurrentMonth);
            monthChange(5);
            invalidateOptionsMenu();
        }

        if(id == R.id.action_summary){
            String monthName = "";
            if(isCurrentMonth){
                monthName = DateTimeUtils.MONTH_NAME[dateModel.getMonth() - 1];
            } else {
                monthName = DateTimeUtils.MONTH_NAME[prevDateModel.getMonth() - 1];
            }
            StatementSummaryDialogFragment newFragment = StatementSummaryDialogFragment.newInstance(monthName);
            newFragment.show(getSupportFragmentManager(), "summary_dialog");
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String title;
        if(menu != null && menu.findItem(R.id.action_month_ss) != null) {
            if (isCurrentMonth) {
                menu.findItem(R.id.action_month_ss).setTitle(DateTimeUtils.getMonthForInt(prevDateModel.getMonth()));
            } else {
                menu.findItem(R.id.action_month_ss).setTitle(DateTimeUtils.getMonthForInt(dateModel.getMonth()));
            }
            title = "Statement for " + DateTimeUtils.getMonthForInt(month);
            setTitle(title);
            return super.onPrepareOptionsMenu(menu);
        }

        if(menu != null && menu.findItem(R.id.action_month_coverage) != null) {
            if (isCurrentMonth) {
                menu.findItem(R.id.action_month_coverage).setTitle(DateTimeUtils.getMonthForInt(prevDateModel.getMonth()));
            } else {
                menu.findItem(R.id.action_month_coverage).setTitle(DateTimeUtils.getMonthForInt(dateModel.getMonth()));
            }
            title = "Coverage for " + DateTimeUtils.getMonthForInt(month);
            setTitle(title);
            return super.onPrepareOptionsMenu(menu);
        }

        if(menu != null && menu.findItem(R.id.action_month_dd) != null) {
            if (isCurrentMonth) {
                menu.findItem(R.id.action_month_dd).setTitle(DateTimeUtils.getMonthForInt(prevDateModel.getMonth()));
            } else {
                menu.findItem(R.id.action_month_dd).setTitle(DateTimeUtils.getMonthForInt(dateModel.getMonth()));
            }
            title = "Doctor List for " + DateTimeUtils.getMonthForInt(month);
            setTitle(title);
            return super.onPrepareOptionsMenu(menu);
        }

        if(menu != null && menu.findItem(R.id.action_month_accompany) != null) {
            if (isCurrentMonth) {
                menu.findItem(R.id.action_month_accompany).setTitle(DateTimeUtils.getMonthForInt(prevDateModel.getMonth()));
            } else {
                menu.findItem(R.id.action_month_accompany).setTitle(DateTimeUtils.getMonthForInt(dateModel.getMonth()));
            }
            title = "Accompany Info for " + DateTimeUtils.getMonthForInt(month);
            setTitle(title);
            return super.onPrepareOptionsMenu(menu);
        }

        if(menu != null && menu.findItem(R.id.action_month_psc) != null) {
            if (isCurrentMonth) {
                menu.findItem(R.id.action_month_psc).setTitle(DateTimeUtils.getMonthForInt(prevDateModel.getMonth()));
            } else {
                menu.findItem(R.id.action_month_psc).setTitle(DateTimeUtils.getMonthForInt(dateModel.getMonth()));
            }
            title = "PSC for " + DateTimeUtils.getMonthForInt(month);
            setTitle(title);
            return super.onPrepareOptionsMenu(menu);
        }

        if(menu != null && menu.findItem(R.id.action_month_dcr_sum) != null) {
            if (isCurrentMonth) {
                menu.findItem(R.id.action_month_dcr_sum).setTitle(DateTimeUtils.getMonthForInt(prevDateModel.getMonth()));
            } else {
                menu.findItem(R.id.action_month_dcr_sum).setTitle(DateTimeUtils.getMonthForInt(dateModel.getMonth()));
            }
            title = "DCR Summary for " + DateTimeUtils.getMonthForInt(month);
            setTitle(title);
            return super.onPrepareOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
            finish();
    }


    public void setMonthYear(boolean isCurrent) {
        if(!isCurrent){
            month = prevDateModel.getMonth();
            year = prevDateModel.getYear();
            day = prevDateModel.getLastDay();
        } else {
            month = dateModel.getMonth();
            year = dateModel.getYear();
            day = dateModel.getDay();
        }

    }

    public void monthChange(int type){
        Fragment fragment = null;
        switch (type){ //0 for statement, 1 for coverage, 2 for doctor list
            case 0:
                fragment = new SampleStatementFragment();
                break;
            case 1:
                fragment = new DoctorCoverageFragment();
                break;
            case 2:
                fragment = new DoctorWiseDOTFragment();
                break;
            case 3:
                fragment = new DCRAccompanyFragment();
                break;
            case 4:
                fragment = new PhysicalStockFragment();
                break;
            case 5:
                fragment = new DCRMPOFragment();
                break;
                default:
                    break;
        }
        Bundle bs = new Bundle();
        bs.putInt("month", month);
        bs.putInt("year", year);
        bs.putInt("day", day);
        fragment.setArguments(bs);
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("report").commit();
    }

    public void setPrevDateModel(){
        prevDateModel = new DateModel(dateModel.getDay(),dateModel.getMonth(),  dateModel.getYear(), 0, 0);
        if(dateModel.getMonth() == 1){
            prevDateModel.setMonth(12);
            prevDateModel.setYear(dateModel.getYear() - 1);
        } else {
            prevDateModel.setMonth(dateModel.getMonth() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCurrentMonth = true;
    }
}
