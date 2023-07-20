package bd.com.aristo.edcr.modules.dcr.dcr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.CalenderModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.IPlanExeModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.networking.ResponseListener;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.MainMenuConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DCRCalendarActivity extends AppCompatActivity implements ResponseListener<IPlanExeModel> {

    //saveAndNext our FastAdapter
    private FastAdapter<CalenderModel> mFastAdapter;

    List<IPlanExeModel> planExeListSummary = new ArrayList<>();

    public String[] days = new String[35];
    public Calendar calCurrent, calPrevious;
    int dayOfMonth, month, year, lastDay;


    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @Inject
    RequestServices requestServices;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tvTitle)
    ATextView tvTitle;

    Context context;

    boolean isCurrentMonth = true;

    UserModel userModel;
    public void setUserModel(){
        userModel = r.where(UserModel.class).findFirst();
        setupMonth();
    }
    public Bundle savedInstanceState;
    ItemAdapter<CalenderModel> itemAdapter;
    public int[] isWorkPlanned = new int[35];

    public static void start(Activity context){
        Intent i = new Intent(context, DCRCalendarActivity.class);
        context.startActivity(i);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.fragment_calendar_dcr);
        ButterKnife.bind(this);
        context = this;
        MainMenuConstants.getInstance().setActivityWH(this);
        setTitle("DCR Calendar");
        setUserModel();
        setupCalendar(isCurrentMonth);

    }

    public void setupMonth(){
        calCurrent = Calendar.getInstance();
        int m = calCurrent.get(Calendar.MONTH);
        int y = calCurrent.get(Calendar.YEAR);
        calPrevious = Calendar.getInstance();
        if(m == 0){
            calPrevious.set(y-1, 11, 1);
        } else {
            calPrevious.set(y, m-1, 1);
        }

    }

    public  void setupCalendar(boolean isCurrent){


        if(isCurrent){
            refreshDays(calCurrent,  true);
        } else {
            refreshDays(calPrevious, false);
        }

        requestServices.getPlanVsExe(this,
                apiServices,
                userModel.getUserId(),
                DateTimeUtils.getMonthYear(month,year),
                this);

    }

    public void refreshDays(Calendar cal, boolean isCurrentMonth) {
        int firstDay;
        tvTitle.setText("DCR Calendar of " + DateTimeUtils.MONTH_NAME[cal.get(Calendar.MONTH)]);
        for(int i = 0; i < 35; i++){
            days[i] = "";
        }
        if(isCurrentMonth){
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            dayOfMonth = 0;
        }

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        firstDay = (int) cal.get(Calendar.DAY_OF_WEEK);

        int realFirstDay = (firstDay) % 7;

        // populate days
        int dayNumber = 1;
        for (int i = realFirstDay; i < days.length; i++) {
            days[i] = "" + dayNumber;

            dayNumber++;
            if((lastDay) < dayNumber){
                break;
            }
        }
        if (realFirstDay + lastDay == 36) {
            days[0] = "" + dayNumber;
        } else if (realFirstDay + lastDay == 37) {
            days[0] = "" + dayNumber;
            days[1] = "" + (dayNumber + 1);
        }
    }

    public void setAdapter(final List<IPlanExeModel> planExeList){

        mFastAdapter = new FastAdapter<>();
        itemAdapter = new ItemAdapter<>();
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<CalenderModel>() {
            @Override
            public boolean onClick(View v, IAdapter<CalenderModel> adapter, CalenderModel item, int position) {
                if(!item.date.getText().equals("")){
                    String date = DateTimeUtils.getDayMonthYear(Integer.valueOf(item.date.getText().toString()), month, year);
                    List<IPlanExeModel> planExes = new ArrayList<>();
                    for(IPlanExeModel planExe:planExeList){
                        if(planExe.getDate().equalsIgnoreCase(date)){
                            planExes.add(planExe);
                        }
                    }
                    if(planExes.size() > 0) {
                        startActivity(DCRDetailViewPagerActivity.start(context, planExes, date));
                    } else {
                        ToastUtils.longToast("No DCR Found!!");
                    }
                }
                return false;
            }
        });


        rv.setLayoutManager(new GridLayoutManager(this,7));
        rv.setItemAnimator(new SlideLeftAlphaAnimator());
        rv.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<CalenderModel> items = new ArrayList<>();
        isWorkPlanned           = getDCRDayStatus(planExeList);

        for (int i = 0; i < 35; i++) {
            CalenderModel item = new CalenderModel();
            item
                    .withName(days[i])
                    .withIdentifier(100 + i);
            if(!days[i].equals("")) {
                if (String.valueOf(dayOfMonth).equals(days[i])) {
                    item.withIsCurrent(true);
                }
                if (isWorkPlanned[Integer.valueOf(days[i])] > 0) {
                    item.setSaved(true);
                }
            }
            items.add(item);
        }
        itemAdapter.add(items);
        mFastAdapter.withSavedInstanceState(savedInstanceState);

    }


    public int[] getDCRDayStatus(List<IPlanExeModel> planExeList){

        int[] planCount = new int[35];
        for(IPlanExeModel planExe:planExeList){
            planCount[Integer.valueOf(planExe.getDate().split("-")[0])] = 1;
        }
        return planCount;
    }

    @Override
    public void onSuccess(List<IPlanExeModel> valueList) {
        planExeListSummary.size();
        planExeListSummary = new ArrayList<>();
        planExeListSummary.addAll(valueList);
        setAdapter(valueList);
    }

    @Override
    public void onSuccess(IPlanExeModel value) {
        planExeListSummary.size();
        planExeListSummary = new ArrayList<>();
    }
    @Override
    public void onSuccess() {
        planExeListSummary.size();
        planExeListSummary = new ArrayList<>();
    }

    @Override
    public void onFailed() {
        planExeListSummary.size();
        planExeListSummary = new ArrayList<>();
        setAdapter(new ArrayList<IPlanExeModel>());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if(id == R.id.action_dcr_list){
            DCRSummaryListActivity.start(this, planExeListSummary, month, year);
            //ToastUtils.longToast("Coming soon...");
        }
        if(id == R.id.month_change){
            isCurrentMonth = !isCurrentMonth;
            setupCalendar(isCurrentMonth);
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dcr_summary, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu != null && menu.findItem(R.id.month_change) != null) {
            if (isCurrentMonth) {
                menu.findItem(R.id.month_change).setTitle(DateTimeUtils.getMonthForInt(calPrevious.get(Calendar.MONTH) + 1));
            } else {
                menu.findItem(R.id.month_change).setTitle(DateTimeUtils.getMonthForInt(calCurrent.get(Calendar.MONTH) + 1));
            }
            return super.onPrepareOptionsMenu(menu);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCurrentMonth = true;
    }
}
