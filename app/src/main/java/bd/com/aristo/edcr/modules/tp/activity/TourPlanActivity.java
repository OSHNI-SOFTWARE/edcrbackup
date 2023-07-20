package bd.com.aristo.edcr.modules.tp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.listener.DateValidationListener;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dvr.DVRUtils;
import bd.com.aristo.edcr.modules.tp.TourPlanUtils;
import bd.com.aristo.edcr.modules.tp.fragment.TPCalendarFragment;
import bd.com.aristo.edcr.modules.tp.fragment.TPEveningFragment;
import bd.com.aristo.edcr.modules.tp.fragment.TPMorningFragment;
import bd.com.aristo.edcr.modules.tp.model.AddressModel;
import bd.com.aristo.edcr.models.Day;
import bd.com.aristo.edcr.modules.tp.model.ITPPlacesModel;
import bd.com.aristo.edcr.modules.tp.model.TPCopyEveningModel;
import bd.com.aristo.edcr.modules.tp.model.TPCopyMorningModel;
import bd.com.aristo.edcr.modules.tp.model.TPEveningModel;
import bd.com.aristo.edcr.modules.tp.model.TPMorningModel;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.collections.AViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TourPlanActivity extends AppCompatActivity implements DateValidationListener {

    private final String TAG = TourPlanActivity.class.getSimpleName();
    public boolean isChanged = false;
    public boolean isApproved = false;
    private TourPlanActivity activity = this;
    public Day day;
    TPMorningModel tpMorningModel;
    TPEveningModel tpEveningModel;
    List<String> copyFromDateList;
    ArrayAdapter<String> copyFromListAdapter;
    @Inject
    Realm r;
    @BindView(R.id.tp_view_pager)
    AViewPager aViewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.llSecond)
    LinearLayout llSave;
    @BindView(R.id.llFourth)
    LinearLayout llSaveNext;
    @BindView(R.id.llPrev)
    LinearLayout llPrev;
    @BindView(R.id.llNext)
    LinearLayout llNext;
    UserModel userModel;

    @Inject
    RequestServices requestServices;
    @Inject
    APIServices apiServices;

    public static void start(Activity context, Day day){
        Intent intent = new Intent(context, TourPlanActivity.class);
        intent.putExtra("DAY", day);
        context.startActivity(intent);
    }

    public void setUserInfo(){
        if(r.where(UserModel.class).findFirst() != null){
            userModel = r.where(UserModel.class).findFirst();
        } else {
            ToastUtils.longToast("User info deleted. please logout and login again!!");
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.activity_tour_plan);
        ButterKnife.bind(this);
        //getServerTime();
        setUserInfo();
        setTitle(getString(R.string.tour_plan));
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        if(getIntent() != null){
            day = (Day) getIntent().getSerializableExtra("DAY");
        }
        if(day != null) {
            setStatus(day);
            firstDate(false);
            lastDate(true);
            setDateText();
            setupTabs();
        } else {
            ToastUtils.longToast("Date not found. Try again!!");
            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        copyFromDateList = TourPlanUtils.getSavedTpDayList(r, day.getMonth(), day.getYear());
    }


    private void setupTabs() {

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),activity);

        aViewpager.setAdapter(mSectionsPagerAdapter);
        aViewpager.setPagingEnabled(true);
        tabs.setupWithViewPager(aViewpager);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }
    }


    //--------------------------------Bottom Button Start------------------------------------
    @OnClick(R.id.llPrev)
    public void goToPrevDate(){
        int date = day.getDay();
        int cell = day.getCell();
        day.setDay(date - 1);
        day.setCell(--cell);
        day.setCopyDate(date - 1);
        setStatus(day);
        firstDate(true);
    }

    @OnClick(R.id.llSecond)
    public void saveOnly() {
        if (isRequiredFieldDataFound()){
            saveTPToRealm();
            ToastUtils.shortToast(StringConstants.SAVED_SUCCESS_MSG);
            copyFromDateList.clear();
            copyFromDateList = TourPlanUtils.getSavedTpDayList(r, day.getMonth(), day.getYear());
            copyFromListAdapter.notifyDataSetChanged();
            finish();
        }
    }

    @OnClick(R.id.llHome)
    void goHome(){
        finish();
    }

    @OnClick(R.id.llFourth)
    public void saveAndNext(){
        if (isRequiredFieldDataFound()){
            saveTPToRealm();
            ToastUtils.shortToast(StringConstants.SAVED_SUCCESS_MSG);
            copyFromDateList.clear();
            copyFromDateList = TourPlanUtils.getSavedTpDayList(r, day.getMonth(), day.getYear());
            copyFromMenuOption(cpySpinner);
            goToNextDate();
        }
    }

    @OnClick(R.id.llNext)
    public void goToNextDate(){
        int date = day.getDay();
        int cell = day.getCell();
        day.setDay(date + 1);
        day.setCell(cell + 1);
        day.setCopyDate(date + 1);
        setStatus(day);
        lastDate(true);
    }

    //----------------------------Bottom Button End-----------------------
    private void saveTPToRealm() {
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TPPlaceRealmModel> deleteTpPlaceRealmModels = r.where(TPPlaceRealmModel.class)
                        .equalTo(TPPlaceRealmModel.COL_TP_ID, tpMorningModel.getId())
                        .findAll();
                deleteTpPlaceRealmModels.deleteAllFromRealm();

                long placeId = TourPlanUtils.getPrevIdForPlace(r);
                RealmList<TPPlaceRealmModel> tpPlaceRealmModelList = new RealmList<>();


                TPServerModel tpServerModel = new TPServerModel();
                //tpServerModel.setWorkPlaceList(tpPlaceRealmModelList);
                tpServerModel.setLocalId(tpMorningModel.getId());
                tpServerModel.setServerId(tpMorningModel.getServerId());
                tpServerModel.setDay(String.valueOf(day.getDay()));
                tpServerModel.setYear(day.getYear());
                tpServerModel.setMonth(day.getMonth());
                tpServerModel.setcCell(String.valueOf(day.getCell()));
                tpServerModel.setContactPlace(StringUtils.getAndFormAmp(tpMorningModel.getContactAddress()));
                tpServerModel.setReportTime(tpMorningModel.getrTime());
                tpServerModel.setShift(StringConstants.MORNING);
                tpServerModel.setShiftType(tpMorningModel.getShiftType());
                tpServerModel.setnDA(tpMorningModel.getNda());
                tpServerModel.setChanged(true);
                tpServerModel.setApproved(false);
                tpServerModel.setUploaded(false);
                if(!tpMorningModel.getShiftType().equalsIgnoreCase(StringConstants.LEAVE) ||
                        !tpMorningModel.getShiftType().equalsIgnoreCase(StringConstants.MEETING)) {
                    for (ITPPlacesModel placeModel : tpMorningModel.getPlaceList()) {
                        TPPlaceRealmModel tpPlaceRealmModel = new TPPlaceRealmModel();
                        tpPlaceRealmModel.setShift(placeModel.getShift());
                        tpPlaceRealmModel.setCode(placeModel.getCode());
                        tpPlaceRealmModel.setId(++placeId);
                        tpPlaceRealmModel.setTpId(tpMorningModel.getId());
                        tpPlaceRealmModelList.add(tpPlaceRealmModel);
                        //insert to db
                        r.insertOrUpdate(tpPlaceRealmModel);
                    }
                }
                r.insertOrUpdate(tpServerModel);

                //Evening TP
                RealmResults<TPPlaceRealmModel> deleteTpEveningPlaceRealmModels = r.where(TPPlaceRealmModel.class)
                        .equalTo(TPPlaceRealmModel.COL_TP_ID, tpEveningModel.getId())
                        .findAll();
                deleteTpEveningPlaceRealmModels.deleteAllFromRealm();

                long eveningPlaceId = TourPlanUtils.getPrevIdForPlace(r);
                RealmList<TPPlaceRealmModel> tpEveningPlaceRealmModelList = new RealmList<>();

                TPServerModel tpEveningServerModel = new TPServerModel();
                //tpServerModel.setWorkPlaceList(tpPlaceRealmModelList);
                tpEveningServerModel.setLocalId(tpEveningModel.getId());
                tpEveningServerModel.setServerId(tpEveningModel.getServerId());
                tpEveningServerModel.setDay(String.valueOf(day.getDay()));
                tpEveningServerModel.setYear(day.getYear());
                tpEveningServerModel.setMonth(day.getMonth());
                tpEveningServerModel.setcCell(String.valueOf(day.getCell()));
                tpEveningServerModel.setContactPlace(StringUtils.getAndFormAmp(tpEveningModel.getContactAddress()));
                tpEveningServerModel.setReportTime(tpEveningModel.getrTime());
                tpEveningServerModel.setShift(StringConstants.EVENING);
                tpEveningServerModel.setShiftType(tpEveningModel.getShiftType());
                tpEveningServerModel.setnDA(tpEveningModel.getNda());
                tpEveningServerModel.setChanged(true);
                tpEveningServerModel.setApproved(false);
                tpServerModel.setUploaded(false);

                if(!tpEveningModel.getShiftType().equalsIgnoreCase(StringConstants.LEAVE) ||
                        !tpEveningModel.getShiftType().equalsIgnoreCase(StringConstants.MEETING)) {
                    for(ITPPlacesModel placeModel:tpEveningModel.getPlaceList()){
                        TPPlaceRealmModel tpPlaceRealmModel = new TPPlaceRealmModel();
                        tpPlaceRealmModel.setShift(placeModel.getShift());
                        tpPlaceRealmModel.setCode(placeModel.getCode());
                        tpPlaceRealmModel.setId(++eveningPlaceId);
                        tpPlaceRealmModel.setTpId(tpEveningModel.getId());
                        tpEveningPlaceRealmModelList.add(tpPlaceRealmModel);
                        r.insertOrUpdate(tpPlaceRealmModel);
                    }
                }
                r.insertOrUpdate(tpEveningServerModel);

                //save to suggestion list
                AddressModel morningAddressModel =r.where(AddressModel.class).equalTo("address",tpMorningModel.getContactAddress()).findFirst();
                AddressModel eveningAddressModel =r.where(AddressModel.class).equalTo("address",tpEveningModel.getContactAddress()).findFirst();
                if (morningAddressModel==null){
                    r.insert(new AddressModel(tpMorningModel.getContactAddress()));
                }
                if (eveningAddressModel==null){
                    r.insert(new AddressModel(tpEveningModel.getContactAddress()));
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if(id == R.id.action_change){
            if(ConnectionUtils.isNetworkConnected(this)){
                long dateMillis = getTPMillis();
                requestServices.compareDate(this, apiServices, dateMillis, this);
            } else {
                ToastUtils.longToast("No Internet!! Please turn on mobile data or wifi.");
            }

        }
        return true;
    }

    public void displayConfirmationPopup(final Activity activity){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        // alert.setTitle(getString(R.string.logout));
        alert.setMessage(StringConstants.TP_CHANGE_ALERT);
        alert.setPositiveButton("Yes", (dialog, whichButton) -> {
            invalidateOptionsMenu();
            llSave.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });

        alert.setNegativeButton("No",
                (dialog, whichButton) -> dialog.dismiss());
        alert.show();
    }

    public void displayPendingDVRPopup(final Activity activity){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        // alert.setTitle(getString(R.string.logout));
        alert.setMessage(StringConstants.TP_CHANGE_PENDING_DVR_ALERT);
        alert.setPositiveButton("Ok", (dialog, whichButton) -> dialog.dismiss());
        alert.show();
    }

    public long getTPMillis(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(day.getYear(), day.getMonth()-1, day.getDay(), 23, 59, 30);
        return calendar.getTimeInMillis();
    }

    @Override
    public void valid() {
        if(DVRUtils.isApproved(r, day.getMonth(), day.getYear())) {
            displayConfirmationPopup(this);
        } else {
            displayPendingDVRPopup(this);
        }
    }

    @Override
    public void invalid() {
        ToastUtils.displayAlert(activity, "Sorry!! Back Date TP Change Not Allowed!");
        //displayConfirmationPopup(this); //TODO::
    }

    @Override
    public void onError() {
        ToastUtils.displayAlert(activity, "Server Error!! Try Again.");
    }


    //Pager adapter

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private String tabTitles[] = new String[] { "Morning", "Evening" };
        private int[] imageResId = { R.drawable.ic_mini_morning_inverted, R.drawable.ic_mini_evening_inverted };
        Context context;
        SectionsPagerAdapter(FragmentManager fm,Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TPMorningFragment.newInstance(day);
                case 1:
                    return TPEveningFragment.newInstance(day);
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.tp_custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textViewTabTitle);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) v.findViewById(R.id.imageViewTab);
            img.setImageResource(imageResId[position]);
            return v;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tourplan, menu);
        MenuItem item = menu.findItem(R.id.action_copy_from);
        cpySpinner = (Spinner) item.getActionView();
        copyFromMenuOption(cpySpinner);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //invalidateOptionsMenu();
        Calendar calendar = Calendar.getInstance();
        calendar.set(day.getYear(), day.getMonth()-1, day.getDay());

        if(isApproved && !isChanged){
            menu.findItem(R.id.action_change).setVisible(true);
        } else {
            menu.findItem(R.id.action_change).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private Spinner cpySpinner;
    private void copyFromMenuOption(final Spinner spinner) {
        copyFromListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, copyFromDateList);
        copyFromListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(copyFromListAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    ToastUtils.shortToast("Copied from "+copyFromDateList.get(i)+" "
                            + DateTimeUtils.getMonthForInt(day.getMonth()));
                    spinner.setSelection(0);
                    day.setCopyDate(Integer.valueOf(copyFromDateList.get(i)));
                    EventBus.getDefault().post(day);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //Field validation
    public boolean isRequiredFieldDataFound(){
        if(tpMorningModel != null && tpEveningModel != null){
            if (TextUtils.isEmpty(tpMorningModel.getContactAddress())){
                ToastUtils.shortToast("Fill up the morning contact address ");
                aViewpager.setCurrentItem(0);
                return false;
            }else if(!tpMorningModel.getShiftType().equalsIgnoreCase("Leave") &&  tpMorningModel.getNda().equalsIgnoreCase("Nature Of DA")){
                ToastUtils.shortToast("Select morning nature of DA");
                aViewpager.setCurrentItem(0);
                return false;
            }else if(tpMorningModel.getShiftType().equalsIgnoreCase(StringConstants.WORKING) && tpMorningModel.getPlaceList().size()<1){
                ToastUtils.shortToast("You must add morning place");
                aViewpager.setCurrentItem(0);
                return false;
            }else if (TextUtils.isEmpty(tpEveningModel.getContactAddress())){
                ToastUtils.shortToast("Fill up the evening contact address ");
                aViewpager.setCurrentItem(1);
                return false;
            }else if(!tpEveningModel.getShiftType().equalsIgnoreCase("Leave") &&  tpEveningModel.getNda().equalsIgnoreCase("Nature Of DA")){
                ToastUtils.shortToast("Select evening nature of DA");
                aViewpager.setCurrentItem(1);
                return false;
            }else if(tpEveningModel.getShiftType().equalsIgnoreCase(StringConstants.WORKING) && tpEveningModel.getPlaceList().size()<1){
                ToastUtils.shortToast("You must add evening place");
                aViewpager.setCurrentItem(1);
                return false;
            }
        } else {
            ToastUtils.shortToast("Error!! No TP Found.");
            aViewpager.setCurrentItem(1);
            return false;
        }

        return true;
    }



    //**************************Event**************************
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void tpMorningChangeEvent(TPMorningModel tpMorningModel){
        this.tpMorningModel = tpMorningModel;
        MyLog.show(TAG, tpMorningModel.getContactAddress());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void tpEveningChangeEvent(TPEveningModel tpEveningModel){
        this.tpEveningModel = tpEveningModel;
    }


    public void eventFire(TPCopyMorningModel tpCopyMorningModel1){
        EventBus.getDefault().post(tpCopyMorningModel1);
    }

    public void eventFire(TPCopyEveningModel tpCopyEveningModel1){
        EventBus.getDefault().post(tpCopyEveningModel1);
    }


    //************************End event************************


    public List<TPPlaceRealmModel> getWorkPlaceList(long id){
        return r.where(TPPlaceRealmModel.class)
                .equalTo(TPPlaceRealmModel.COL_TP_ID, id)
                .findAll();
    }

    public void setStatus(Day day){
        TPServerModel tpModelM = r.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_DAY, String.valueOf(day.getCopyDate()))
                .equalTo(TPServerModel.COL_MONTH, day.getMonth())
                .equalTo(TPServerModel.COL_YEAR, day.getYear())
                .equalTo(TPServerModel.COL_SHIFT, StringConstants.MORNING).findFirst();
        if(tpModelM != null){
            isChanged = tpModelM.isChanged();
            isApproved = tpModelM.isApproved();
        }
    }

    public void firstDate(boolean isPrev){
        llSave.setVisibility(View.VISIBLE);
        if(isPrev){
            if(TPCalendarFragment.IS_APPROVED){
                llSaveNext.setVisibility(View.GONE);
            } else {
                llSaveNext.setVisibility(View.VISIBLE);
            }
            llNext.setVisibility(View.VISIBLE);
            if(day.getCell() == -1){
                day.setCell(34);
            }
            setDateText();
            EventBus.getDefault().post(day);
            aViewpager.setCurrentItem(0);
        }


        if(day.getDay() == 1){
            llPrev.setVisibility(View.GONE);
        } else {
            llPrev.setVisibility(View.VISIBLE);
        }
        if(isApproved && !isChanged){
            llSave.setVisibility(View.GONE);
            llSaveNext.setVisibility(View.GONE);
        }

    }

    public void lastDate(boolean isNext){
        llSave.setVisibility(View.VISIBLE);
        if(isNext){
            llPrev.setVisibility(View.VISIBLE);
            if(day.getCell() == 35){
                day.setCell(0);
            }
            setDateText();
            EventBus.getDefault().post(day);
            aViewpager.setCurrentItem(0);
        }

        if(day.getDay() == day.getLastDay()){
            llSaveNext.setVisibility(View.GONE);
            llNext.setVisibility(View.GONE);
        } else {
            if(TPCalendarFragment.IS_APPROVED){
                llSaveNext.setVisibility(View.GONE);
            } else {
                llSaveNext.setVisibility(View.VISIBLE);
            }
            llNext.setVisibility(View.VISIBLE);
        }
        if(isApproved && !isChanged){
            llSave.setVisibility(View.GONE);
            llSaveNext.setVisibility(View.GONE);
        }
    }

    public void setDateText(){
        if (day!=null){
            setTitle("TP-"+formatDate());
        }
        invalidateOptionsMenu();
    }


    public String formatDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(day.getYear(), day.getMonth() - 1, day.getDay());
        Date date1 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EE, d MMM");
        String hireDate = sdf.format(date1);
        return hireDate;
    }



}
