package bd.com.aristo.edcr.modules.dvr.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.modules.dvr.DVRSaveListener;
import bd.com.aristo.edcr.modules.dvr.model.DVRCalender;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.modules.dvr.model.DayShift;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static bd.com.aristo.edcr.modules.dvr.fragment.DVRActivity.E_DVR_COUNT;
import static bd.com.aristo.edcr.modules.dvr.fragment.DVRActivity.M_DVR_COUNT;

public class DVRSelectionActivity extends AppCompatActivity {

    private final String TAG = DVRSelectionActivity.class.getSimpleName();
    DVRSelectionActivity context;
    @BindView(R.id.rvCalender)
    RecyclerView rvCalender;
    @BindView(R.id.txtName)
    ATextView txtName;
    @BindView(R.id.txtMonth)
    ATextView txtMonth;
    @Inject
    Realm realm;
    int month, year, dayOfMonth, lastDay, locStatus, pos; // locStatus: -1 - no Place, 0 - Morning,1 - Evening, 2 - Both.
    boolean isCurrent;
    private List<DayShift> dayShiftList = new ArrayList<>();
    public String[] days;
    public Calendar cal;
    private FastAdapter<DVRCalender> mFastAdapter;
    ItemAdapter<DVRCalender> itemAdapter;
    boolean[] mornings = new boolean[32];
    boolean[] morningsFixed = new boolean[32];
    boolean[] evenings = new boolean[32];
    boolean[] eveningsFixed = new boolean[32];
    public int[] DVR_STATUS = new int[32]; // 0 - No, 1 - Morning,  2 - Evening, 3 - Both.
    public boolean[] DVR_APPROVED_STATUS = new boolean[32];
    String id, name;
    public static boolean isFromList = false;

    public static void start(Activity context, DVRSaveListener dvrSaveListener,
                             int month, int year, String id, boolean isCurrent, int pos) {
        //listener = dvrSaveListener;
        Intent intent = new Intent(context, DVRSelectionActivity.class);
        intent.putExtra("MONTH", month);
        intent.putExtra("POS", pos);
        intent.putExtra("YEAR", year);
        intent.putExtra("ID", id);
        intent.putExtra("IS_CURRENT", isCurrent);
        context.startActivity(intent);
    }

    public static void startFromList(Context context, int month, int year, String id, boolean isCurrent, int pos) {
        isFromList = true;
        Intent intent = new Intent(context, DVRSelectionActivity.class);
        intent.putExtra("MONTH", month);
        intent.putExtra("POS", pos);
        intent.putExtra("YEAR", year);
        intent.putExtra("ID", id);
        intent.putExtra("IS_CURRENT", isCurrent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.fragment_dialog_dvr_calendar);
        ButterKnife.bind(this);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        month = getIntent().getIntExtra("MONTH", 1);
        year = getIntent().getIntExtra("YEAR", 2020);
        pos = getIntent().getIntExtra("POS", -1);
        id = getIntent().getStringExtra("ID");
        isCurrent = getIntent().getBooleanExtra("IS_CURRENT", true);
        setDVRStatus();
        setStatusAndName();
        dayShiftList = getDayShitForDoctor(id, realm);
        setMorningEvening();
        days = new String[35];
        String doctor = name + " [" + id + "]";
        txtName.setText(doctor);
        String monthName = DateTimeUtils.MONTH_NAME_SHORT[month - 1];
        txtMonth.setText(monthName);
        setCalendar();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setDVRStatus(){
        for(int i = 1; i < 32; i++){
            DVR_STATUS[i] = 0;
            DVR_APPROVED_STATUS[i] = true;
            List<DVRForServer> dvrForServers = realm.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_YEAR, year)
                    .equalTo(DVRForServer.COL_MONTH, month)
                    .equalTo(DVRForServer.COL_DAY, i)
                    .findAll();
            if(dvrForServers != null && dvrForServers.size() > 0){
                DVR_APPROVED_STATUS[i] = dvrForServers.get(0).isApproved();
                if(dvrForServers.size() == 2){
                    DVR_STATUS[i] = 3;
                } else {
                    if(dvrForServers.get(0).isMorning()){
                        DVR_STATUS[i] = 1;
                    } else {
                        DVR_STATUS[i] = 2;
                    }
                }
            }
        }
    }

    public void setStatusAndName(){
        DoctorsModel dm = realm.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, id)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .findFirst();
        if(dm != null){
            name = dm.getName();
             locStatus = -1;
            if(!TextUtils.isEmpty(dm.getMorningLoc()) && !TextUtils.isEmpty(dm.getEveningLoc())){
                locStatus = 2;
            } else if(!TextUtils.isEmpty(dm.getMorningLoc())){
                locStatus = 0;
            } else if(!TextUtils.isEmpty(dm.getEveningLoc())){
                locStatus = 1;
            }
        } else {
            finish();
        }
    }


    public void setCalendar() {
        cal = Calendar.getInstance();
        if (isCurrent) {
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            dayOfMonth = 0;
            cal.set(year, month - 1, 1);
        }

        for (int i = 0; i < 35; i++) {
            days[i] = "";
        }
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = (int) cal.get(Calendar.DAY_OF_WEEK);
        int realFirstDay = (firstDay) % 7;
        // populate days
        int dayNumber = 1;
        for (int i = realFirstDay; i < days.length; i++) {
            days[i] = "" + dayNumber;

            dayNumber++;
            if ((lastDay) < dayNumber) {
                break;
            }
        }
        if (realFirstDay + lastDay == 36) {
            days[0] = "" + dayNumber;
        } else if (realFirstDay + lastDay == 37) {
            days[0] = "" + dayNumber;
            days[1] = "" + (dayNumber + 1);
        }

        setAdapter();

    }

    public void setAdapter(){
        mFastAdapter = new FastAdapter<>();

        itemAdapter = new ItemAdapter<>();
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withItemEvent(new ClickEventHook<DVRCalender>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof DVRCalender.ViewHolder) {
                    return ((DVRCalender.ViewHolder) viewHolder).txtMorning;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<DVRCalender> fastAdapter, DVRCalender item) {
                if (item.isMEditable() && item.isClickable()) {
                    int i = Integer.parseInt(item.date.toString());
                    if(!morningsFixed[i]) {
                        if (mornings[i]) {
                            if (M_DVR_COUNT[i] > 1) {
                                mornings[i] = !mornings[i];
                                M_DVR_COUNT[i]--;
                            } else {
                                ToastUtils.displayAlert(context, String.format(getString(R.string.alert_dvr_zero), "morning", i));
                            }
                        } else {
                            mornings[i] = !mornings[i];
                            M_DVR_COUNT[i]++;
                        }
                        setAdapter();
                    } else {
                        ToastUtils.displayAlert(context, String.format(getString(R.string.alert_dcr_dot)));
                    }
                }
            }
        });

        mFastAdapter.withItemEvent(new ClickEventHook<DVRCalender>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof DVRCalender.ViewHolder) {
                    return ((DVRCalender.ViewHolder) viewHolder).txtEvening;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<DVRCalender> fastAdapter, DVRCalender item) {
                if (item.isEEditable() && item.isClickable()) {
                    int i = Integer.parseInt(item.date.toString());
                    if(!eveningsFixed[i]) {
                        if (evenings[i]) {
                            if (E_DVR_COUNT[i] > 1) {
                                evenings[i] = !evenings[i];
                                E_DVR_COUNT[i]--;
                            } else {
                                ToastUtils.displayAlert(context, String.format(getString(R.string.alert_dvr_zero), "evening", i));
                            }
                        } else {
                            evenings[i] = !evenings[i];
                            E_DVR_COUNT[i]++;
                        }

                        setAdapter();
                    } else {
                        ToastUtils.displayAlert(context, String.format(getString(R.string.alert_dcr_dot)));
                    }
                }
            }
        });
        rvCalender.setLayoutManager(new GridLayoutManager(context, 7));
        rvCalender.setItemAnimator(new SlideLeftAlphaAnimator());
        rvCalender.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<DVRCalender> items = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            DVRCalender item = new DVRCalender();
            item
                    .withName(days[i])
                    .withIdentifier(100 + i);
            if(!TextUtils.isEmpty(days[i])) {
                int j = Integer.valueOf(days[i]);
                item.setEvening(evenings[j]);
                item.setMorning(mornings[j]);
                item.setClickable(!DVR_APPROVED_STATUS[j]);
                if (DVR_STATUS[j] == 3) { //
                    if (locStatus == 2) {
                        item.setEEditable(true);
                        item.setMEditable(true);
                    } else if (locStatus == 1) {
                        item.setEEditable(true);
                        item.setMEditable(false);
                    } else if (locStatus == 0) {
                        item.setEEditable(false);
                        item.setMEditable(true);
                    }
                } else if (DVR_STATUS[j] == 2) {
                    if (locStatus == 1 || locStatus == 2) {
                        item.setEEditable(true);
                        item.setMEditable(false);
                    }

                } else if (DVR_STATUS[j] == 1) {
                    if (locStatus == 0 || locStatus == 2) {
                        item.setMEditable(true);
                        item.setEEditable(false);
                    }
                } else {
                    item.setEEditable(false);
                    item.setMEditable(false);
                }
            }


            if (String.valueOf(dayOfMonth).equals(days[i])) {
                item.withIsCurrent(true);
            }
            items.add(item);
        }

        itemAdapter.add(items);
    }

    public void setMorningEvening() {
        for (DayShift dayShift : dayShiftList) {
            if (dayShift.isMorning()) {
                mornings[dayShift.getDay()] = true;
                morningsFixed[dayShift.getDay()] = dayShift.isFixed();
            } else {
                evenings[dayShift.getDay()] = true;
                eveningsFixed[dayShift.getDay()] = dayShift.isFixed();
            }
        }
    }

    @OnClick({R.id.btnCancel, R.id.btnSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnSave:
                saveDVR();
                break;
        }
    }

    public void saveDVR(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                for(int i = 1; i < 32; i++) {
                    String idM = year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(i)+0;
                    String idE = year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(i)+1;
                    DVRDoctorRealm dvrDoctorRealmM = realm.where(DVRDoctorRealm.class)
                            .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, id)
                            .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, Long.valueOf(idM))
                            .findFirst();
                    if(dvrDoctorRealmM != null){
                        dvrDoctorRealmM.deleteFromRealm();
                    }
                    DVRDoctorRealm dvrDoctorRealmE = realm.where(DVRDoctorRealm.class)
                            .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, id)
                            .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, Long.valueOf(idE))
                            .findFirst();
                    if(dvrDoctorRealmE != null){
                        dvrDoctorRealmE.deleteFromRealm();
                    }

                    if(mornings[i]){
                        DVRDoctorRealm dvrDoctorRealm = new DVRDoctorRealm();
                        dvrDoctorRealm.setDoctorID(id);
                        dvrDoctorRealm.setDvrLocalId(Long.valueOf(idM));
                        dvrDoctorRealm.setEditable(!morningsFixed[i]);
                        r.insertOrUpdate(dvrDoctorRealm);
                    }

                    if(evenings[i]){
                        DVRDoctorRealm dvrDoctorRealm = new DVRDoctorRealm();
                        dvrDoctorRealm.setDoctorID(id);
                        dvrDoctorRealm.setDvrLocalId(Long.valueOf(idE));
                        dvrDoctorRealm.setEditable(!eveningsFixed[i]);
                        r.insertOrUpdate(dvrDoctorRealm);
                    }

                }
                if(pos != -20 && !isFromList){
                    DVRActivity.IS_CHANGED = false;
                    Intent intent = new Intent();
                    intent.putExtra("POSITION", pos);
                    intent.putExtra("DOCTOR_ID", id);
                    setResult(RESULT_OK, intent);
                } else {
                    DVRActivity.IS_CHANGED = true;
                }

                onBackPressed();


            }
        });
    }

    public List<DayShift> getDayShitForDoctor(String docID, Realm realm){
        List<DayShift> dayShiftList = new ArrayList<>();
        for(int i = 1; i < 32; i++) {
            String idM = year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(i)+0;
            String idE = year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(i)+1;
            DVRDoctorRealm dvrDoctorRealmM = realm.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, docID)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, Long.valueOf(idM))
                    .findFirst();
            if(dvrDoctorRealmM != null){
                DayShift dayShift = new DayShift();
                dayShift.setDay(i);
                dayShift.setWeekDay(formatDate(i));
                dayShift.setMorning(true);
                dayShift.setFixed(!dvrDoctorRealmM.isEditable());
                dayShiftList.add(dayShift);
            }
            DVRDoctorRealm dvrDoctorRealmE = realm.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, docID)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, Long.valueOf(idE))
                    .findFirst();
            if(dvrDoctorRealmE != null){
                DayShift dayShift = new DayShift();
                dayShift.setDay(i);
                dayShift.setMorning(false);
                dayShift.setWeekDay(formatDate(i));
                dayShift.setFixed(!dvrDoctorRealmE.isEditable());
                dayShiftList.add(dayShift);
            }

        }
        return dayShiftList;
    }

    public String formatDate(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return DateTimeUtils.WEEK_DAY_1[cal.get(Calendar.DAY_OF_WEEK)];
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
