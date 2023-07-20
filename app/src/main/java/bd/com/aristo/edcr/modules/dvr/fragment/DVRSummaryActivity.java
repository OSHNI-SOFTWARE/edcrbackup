package bd.com.aristo.edcr.modules.dvr.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.modules.dvr.model.CalenderItem;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.IDVRDoctors;
import bd.com.aristo.edcr.modules.dvr.model.IDVRSummaryModel;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class DVRSummaryActivity extends AppCompatActivity implements OnDoctorClickListener{

    private final String TAG = DVRSummaryActivity.class.getSimpleName();

    DVRSummaryActivity context;
    List<IDVRSummaryModel> idvrSummaryModelList;




    @Inject
    Realm realm;

    int pos = 0;

    int month, year, lastDay, dayOfMonth;
    boolean isCurrentMonth;
    public String[] days = new String[35];
    DateModel dateModel;
    FastAdapter<CalenderItem> mFastAdapter;
    ItemAdapter<CalenderItem> itemAdapter;


    @BindView(R.id.doctorList)
    RecyclerView rvDoctors;
    @BindView(R.id.rvCalendar)
    RecyclerView rvCalendar;

    public Bundle savedInstanceState;


    public static void start(Activity context) {
        Intent intent = new Intent(context, DVRSummaryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.activity_dvr_summary);
        ButterKnife.bind(this);
        context = this;
        month = getIntent().getIntExtra("MONTH", 0);
        year = getIntent().getIntExtra("YEAR", 2020);
        isCurrentMonth = getIntent().getBooleanExtra("IS_CURRENT", true);
        lastDay = getLastDate(month, year);
        dateModel = new DateModel();
        dateModel.setMonth(month);
        dateModel.setYear(year);
        dateModel.setLastDay(lastDay);
        refreshDays();
        setTitle();

    }

    public void setTitle(){
        setTitle("DVR for "+DateTimeUtils.getMonthForInt(month));
    }



    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();

        }
        return true;
    }

    public void setAdapter(){
        final FastItemAdapter<IDVRSummaryModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(getDvrSummary());
        fastAdapter.withSelectable(false);
        fastAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvDoctors.setLayoutManager(layoutManager);
        rvDoctors.setAdapter(fastAdapter);
        rvDoctors.scrollToPosition(pos);
    }

    public List<IDVRSummaryModel> getDvrSummary(){
        idvrSummaryModelList = new ArrayList<>();
        TPServerModel morningTP = realm.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_DAY, String.valueOf(1))
                .equalTo(TPServerModel.COL_MONTH, month)
                .equalTo(TPServerModel.COL_YEAR, year)
                .equalTo(TPServerModel.COL_SHIFT, StringConstants.MORNING)
                .findFirst();
        TPServerModel eveningTP = realm.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_DAY, String.valueOf(1))
                .equalTo(TPServerModel.COL_MONTH, month)
                .equalTo(TPServerModel.COL_YEAR, year)
                .equalTo(TPServerModel.COL_SHIFT, StringConstants.EVENING)
                .findFirst();
        if(null == morningTP || null == eveningTP){
            displayAlert();
            return idvrSummaryModelList;
        }
        for (int i = 1; i< lastDay + 1; i++) {
            String morningCount = "Morning";
            String eveningCount = "Evening";
            List<IDVRDoctors> idvrDoctorsM = new ArrayList<>();
            List<IDVRDoctors> idvrDoctorsE = new ArrayList<>();
            StringBuilder morningLoc = new StringBuilder();
            StringBuilder eveningLoc = new StringBuilder();
            RealmResults<TPPlaceRealmModel> morningPlaces = realm.where(TPPlaceRealmModel.class)
                    .equalTo(TPPlaceRealmModel.COL_TP_ID, getTPId(i, true))
                    .findAll();
            RealmResults<TPPlaceRealmModel> eveningPlaces = realm.where(TPPlaceRealmModel.class)
                    .equalTo(TPPlaceRealmModel.COL_TP_ID, getTPId(i, false))
                    .findAll();
            if (morningPlaces != null && morningPlaces.size() > 0) {
                String prefix = "";
                for (TPPlaceRealmModel morningDoctor : morningPlaces) {
                    if (morningDoctor != null) {
                        morningLoc.append(prefix + morningDoctor.getCode());
                    }
                    prefix = "\n";
                }
            } else {
                morningLoc.append("No location");
            }

            if (eveningPlaces != null && eveningPlaces.size() > 0) {
                String prefix = "";
                for (TPPlaceRealmModel morningDoctor : eveningPlaces) {
                    if (morningDoctor != null) {
                        eveningLoc.append(prefix).append(morningDoctor.getCode());
                    }
                    prefix = "\n";
                }
            } else {
                eveningLoc.append("No location");
            }
            List<DVRDoctorRealm> morningDVRDoctors = realm.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, getDVRId(i, true)).findAll();
            long countM = realm.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, getDVRId(i, true)).count();
            morningCount = morningCount + "(" + countM + ")";
            if (morningDVRDoctors != null && morningDVRDoctors.size() > 0) {
                for (DVRDoctorRealm morningDoctor : morningDVRDoctors) {
                    IDVRDoctors idvrDoctors = new IDVRDoctors();
                    DoctorsModel doctorsModel = realm.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, morningDoctor.getDoctorID()).findFirst();
                    if (doctorsModel != null) {
                        idvrDoctors.setId(morningDoctor.getDoctorID());
                        idvrDoctors.setName(doctorsModel.getName());
                    } else {
                        idvrDoctors.setId(morningDoctor.getDoctorID());
                        idvrDoctors.setName("Name not Found");
                    }
                    idvrDoctorsM.add(idvrDoctors);
                }
            }

            List<DVRDoctorRealm> eveningDVRDoctors = realm.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, getDVRId(i, false)).findAll();
            long countE = realm.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, getDVRId(i, false)).count();
            eveningCount = eveningCount + "(" + countE + ")";
            if (eveningDVRDoctors != null && eveningDVRDoctors.size() > 0) {
                for (DVRDoctorRealm eveningDoctor : eveningDVRDoctors) {
                    IDVRDoctors idvrDoctors = new IDVRDoctors();
                    DoctorsModel doctorsModel = realm.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, eveningDoctor.getDoctorID()).findFirst();
                    if (doctorsModel != null) {
                        idvrDoctors.setId(eveningDoctor.getDoctorID());
                        idvrDoctors.setName(doctorsModel.getName());
                    } else {
                        idvrDoctors.setId(eveningDoctor.getDoctorID());
                        idvrDoctors.setName("Name not Found");
                    }
                    idvrDoctorsE.add(idvrDoctors);
                }
            }
            IDVRSummaryModel idvrSummaryModel = new IDVRSummaryModel();
            idvrSummaryModel.setMonth(month);
            idvrSummaryModel.setYear(year);
            idvrSummaryModel.setOnDoctorClickListener(this);
            idvrSummaryModel.setCurrentMonth(isCurrentMonth);
            idvrSummaryModel.setEveningCount(eveningCount);
            idvrSummaryModel.setMorningCount(morningCount);
            idvrSummaryModel.seteDVRDoctors(idvrDoctorsE);
            idvrSummaryModel.setMorningLoc(morningLoc.toString());
            idvrSummaryModel.setmDVRDoctors(idvrDoctorsM);
            idvrSummaryModel.setEveningLoc(eveningLoc.toString());
            idvrSummaryModel.setDay(i);
            idvrSummaryModel.setId(formatDate(i));
            idvrSummaryModel.withIdentifier(i);
            idvrSummaryModelList.add(idvrSummaryModel);
        }
        return idvrSummaryModelList;
    }

    private int getLastDate(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public void displayAlert(){
        String msg = "No TP Found. Please Create or Sync TP.";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                ((Activity) context).onBackPressed();
            }
        });
        alert.show();
    }
    public String formatDate(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        Date date1 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd");
        return sdf.format(date1);
    }

    public long getTPId(int day, boolean isMorning){
        String id = year+DateTimeUtils.getMonthNumber(month)+day+(isMorning? "0":"1");
        return Long.parseLong(id);
    }

    public long getDVRId(int day, boolean isMorning){
        String id = year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(day)+(isMorning? "0":"1");
        return Long.parseLong(id);
    }

    @Override
    public void onDoctorClick(int position) {
        pos = position;
    }

    boolean isSaved = false;
    public void refreshDays() {
        for(int i = 0; i < 35; i++){
            days[i] = "";
        }
        year = dateModel.getYear();
        month = dateModel.getMonth();
        lastDay = dateModel.getLastDay();
        Calendar cal = Calendar.getInstance();
        if(isCurrentMonth) {
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        }else {
            dayOfMonth = 0;
            cal.set(year, month-1, 1);
        }

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = (int) cal.get(Calendar.DAY_OF_WEEK);
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
        setCalendarAdapter();
    }

    public void setCalendarAdapter(){
        mFastAdapter = new FastAdapter<>();
        itemAdapter = new ItemAdapter<>();
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<CalenderItem>() {
            @Override
            public boolean onClick(View v, IAdapter<CalenderItem> adapter, CalenderItem item, int position) {
                if(!item.date.getText().equals("") && item.isSaved()){
                    int day = Integer.parseInt(item.date.getText().toString());
                    rvDoctors.scrollToPosition(getPosition(day));
                    String date = DateTimeUtils.getMonthNumber(Integer.parseInt(item.date.getText().toString()));
                    //startActivity(DCRDetailActivity.start(context, Integer.parseInt(date), month, year));
                }
                return false;
            }
        });


        rvCalendar.setLayoutManager(new GridLayoutManager(this,7));
        rvCalendar.setItemAnimator(new SlideLeftAlphaAnimator());
        rvCalendar.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<CalenderItem> items = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            CalenderItem item = new CalenderItem();
            item
                    .withIdentifier(100 + i);
            if(!days[i].equals("")) {
                if (String.valueOf(dayOfMonth).equals(days[i])) {
                    item.withIsCurrent(true);
                }
                item.withNameAndInfo(days[i], getDayWiseDVR(Integer.parseInt(days[i])));
                item.setSaved(isSaved);
            } else
                item.withName(days[i]);
            items.add(item);
        }
        itemAdapter.add(items);
        mFastAdapter.withSavedInstanceState(savedInstanceState);

    }

    public int getPosition(int day){
        for(int i = 0; i < idvrSummaryModelList.size(); i++){
            if(idvrSummaryModelList.get(i).getDay() == day){
                return i;
            }
        }
        return 0;
    }

    public String getDayWiseDVR(int day){
        isSaved = false;
        int mCount = 0, eCount = 0;
        long countM = realm.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, getDVRId(day, true)).count();
        long countE = realm.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, getDVRId(day, false)).count();
        mCount = (int) countM;
        eCount = (int) countE;
        if(mCount > 0 || eCount > 0){
            isSaved = true;
            return mCount + ", " + eCount +", " + (mCount+eCount);
        }
        return "";
    }
}
