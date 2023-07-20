package bd.com.aristo.edcr.modules.tp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.ColorInfoTDPGDialog;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.fcm.notification.FCMSendNotification;
import bd.com.aristo.edcr.listener.MonthChangedListener;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.tp.activity.TourPlanActivity;
import bd.com.aristo.edcr.modules.tp.TourPlanUtils;
import bd.com.aristo.edcr.modules.tp.model.CalenderModel;
import bd.com.aristo.edcr.models.Day;
import bd.com.aristo.edcr.modules.tp.model.PlaceModel;
import bd.com.aristo.edcr.modules.tp.model.TPModel;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.MainMenuConstants;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import static bd.com.aristo.edcr.utils.DateTimeUtils.MONTH_NAME;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class TPCalendarFragment extends Fragment {

    public static final String TAG = "TPCalendar";
    public static boolean IS_APPROVED = false;
    public static boolean IS_ALL_APPROVED = true;
    public int[] dayStatusTP = new int[35];

    //saveAndNext our FastAdapter
    private FastAdapter<CalenderModel> mFastAdapter;


    public String[] days;
    public java.util.Calendar calCurrent, calPrev, calNext;
    ATextView txtCurrentMonth;
    int dayOfMonth, month, year, lastDay;
    ATextView btnNextMonth, btnPrevMonth;

    public List<String> listOfSavedDate;
    public List<String> listOfSavedDateStatus;

    private CompositeDisposable mCompositeDisposableForGet;
    private CompositeDisposable mCompositeDisposableForSend;

    public MonthChangedListener monthChangedListener;



    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.llSecond)
    public LinearLayout llSecond;
    @BindView(R.id.llInfo)
    public LinearLayout llInfo;
    @BindView(R.id.llFourth)
    public LinearLayout llFourth;
    @BindView(R.id.txtSecond)
    public ATextView txtSecond;
    @BindView(R.id.txtFourth)
    public ATextView txtFourth;
    @BindView(R.id.ivSecond)
    public ImageView ivSecond;
    @BindView(R.id.ivFourth)
    public ImageView ivFourth;
    public Bundle savedInstanceState;
    ItemAdapter<CalenderModel> itemAdapter;
    DialogFragment dialogFragment;

    public UserModel userModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }
    @Inject
    RequestServices requestServices;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public TPCalendarFragment() {
        listOfSavedDateStatus                           = new ArrayList<>();
        listOfSavedDate                                 = new ArrayList<>();
        days                                            = new String[35];
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_tp_calendar, container, false);
        ButterKnife.bind(this, rootView);

        getUserInfo();
        setHasOptionsMenu(true);
        this.savedInstanceState                         = savedInstanceState;
        txtCurrentMonth = (ATextView) rootView.findViewById(R.id.txtCurrentMonth);
        btnNextMonth = (ATextView) rootView.findViewById(R.id.btnNextMonth);
        btnPrevMonth = (ATextView) rootView.findViewById(R.id.btnPrevMonth);
        setupMonth();
        setupCalendar();
        llSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalDayOfMonth = lastDay;
                int totalDoneTp = getNoTpOfCurrentMonth(r)/2;
                if (totalDayOfMonth != totalDoneTp ){
                    displayTpUploadErrorAlert(totalDayOfMonth-totalDoneTp);
                }else if(totalDayOfMonth != totalDoneTp) {
                    displayPartialTpUploadAlert(totalDayOfMonth-totalDoneTp);
                }else {
                    if(IS_ALL_APPROVED){
                        displayTpUploadApprovedAlert();
                    } else {
                        if(IS_APPROVED){
                            displayTpChangeUploadAlert();
                        } else {
                            displayTpUploadAlert();
                        }
                    }


                }
            }
        });

        llFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLocallyChanged()){
                    displayConfirmationPopup(((Activity) context));
                } else {
                    ToastUtils.longToast(StringConstants.SYNC_MSG);
                    getTpFromServer();
                }
            }
        });

        llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment = new ColorInfoTDPGDialog("TP");
                dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "color_info");
            }
        });

        ((Activity) context).setTitle(getString(R.string.title_activity_calendar, "Tour Plan"));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainMenuConstants.getInstance().setActivityWH(((Activity) context));
        setAdapter();
    }

    public  void setupCalendar(){
        refreshDays(calCurrent, true);
        txtCurrentMonth.setText(DateTimeUtils.getMonthForInt((int)calCurrent.get(Calendar.MONTH) + 1));
        btnPrevMonth.setText(DateTimeUtils.getMonthForInt((int)calPrev.get(Calendar.MONTH) + 1));
        btnNextMonth.setText(DateTimeUtils.getMonthForInt((int)calNext.get(Calendar.MONTH) + 1));
        txtCurrentMonth.setEnabled(false);
        txtCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupMonth();
                refreshDays(calCurrent, true);
                setAdapter();
                btnNextMonth.setEnabled(true);
                btnNextMonth.setTextColor(getResources().getColor(R.color.color2));

                btnPrevMonth.setEnabled(true);
                btnPrevMonth.setTextColor(getResources().getColor(R.color.color2));

                txtCurrentMonth.setEnabled(false);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.red));


            }
        });
        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupMonth();
                refreshDays(calNext, false);
                setAdapter();
                btnNextMonth.setEnabled(false);
                btnNextMonth.setTextColor(getResources().getColor(R.color.red));

                btnPrevMonth.setEnabled(true);
                btnPrevMonth.setTextColor(getResources().getColor(R.color.color2));

                txtCurrentMonth.setEnabled(true);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.color2));

            }
        });
        btnPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupMonth();
                refreshDays(calPrev, false);
                setAdapter();
                btnNextMonth.setEnabled(true);
                btnNextMonth.setTextColor(getResources().getColor(R.color.color2));

                btnPrevMonth.setEnabled(false);
                btnPrevMonth.setTextColor(getResources().getColor(R.color.red));

                txtCurrentMonth.setEnabled(true);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.color2));

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundel
        outState = mFastAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    public void setupMonth(){
        calCurrent = Calendar.getInstance();
        int month = calCurrent.get(Calendar.MONTH);
        int year = calCurrent.get(Calendar.YEAR);
        calPrev = (Calendar) calCurrent.clone();
        calNext = (Calendar) calCurrent.clone();
        if(calCurrent.get(Calendar.MONTH) == 11){
            calPrev.set(year, month-1, 1);
            calNext.set(year+1, 0, 1);
        } else if(calCurrent.get(Calendar.MONTH) == 0){
            calPrev.set(year-1, 11, 1);
            calNext.set(year, month+1, 1);
        } else {
            calPrev.set(year, month-1, 1);
            calNext.set(year, month+1, 1);
        }

    }



    public void refreshDays(Calendar cal, boolean isCurrentMonth) {
        for(int i = 0; i < 35; i++){
            days[i] = "";
        }
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        if(monthChangedListener != null) {
            monthChangedListener.onMonthChange(month, year);
        }else {
            ((Activity) context).finish();
        }
        lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(isCurrentMonth){
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            dayOfMonth = 0;
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
    }

    public void setAdapter(){
        mFastAdapter = new FastAdapter<>();
        itemAdapter = new ItemAdapter<>();
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<CalenderModel>() {
            @Override
            public boolean onClick(View v, IAdapter<CalenderModel> adapter, CalenderModel item, int position) {
                if(!item.date.getText().equals("")){
                    Day date = new Day();
                    date.setCell(position);
                    date.setDay(Integer.valueOf(item.date.getText().toString()));
                    date.setMonth(month);
                    date.setYear(year);
                    date.setLastDay(lastDay);
                    date.setCopyDate(Integer.valueOf(item.date.getText().toString()));
                    TourPlanActivity.start(((AppCompatActivity) context), date);
                }
                return false;
            }
        });


        rv.setLayoutManager(new GridLayoutManager(context,7));
        rv.setItemAnimator(new SlideLeftAlphaAnimator());
        rv.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<CalenderModel> items = new ArrayList<>();
        getTourPlansWithStatus(r);
        for (int i = 0; i < 35; i++) {
            CalenderModel item = new CalenderModel();
            item
                    .withName(days[i])
                    .withIdentifier(100 + i)
                    .setSaved(dayStatusTP[i] == 1);
                    item.setSynced(dayStatusTP[i] == 3);
                    item.setChanged(dayStatusTP[i] == 4);
                    item.setSent(dayStatusTP[i] == 2);
            if(String.valueOf(dayOfMonth).equals(days[i])){
                item.withIsCurrent(true);
            }
            items.add(item);
        }
        //initialize monthly tp
        if(r.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_YEAR, year)
                .equalTo(TPServerModel.COL_MONTH, month)
                .findAll().size() <= 0){
            for(int i = 0; i < 35; i++){
                if(!days[i].equals("")){
                    initializeTP(i, days[i], true, getTpId(days[i], true));
                    initializeTP(i, days[i], false, getTpId(days[i], false));
                }
            }


        }
        itemAdapter.add(items);
        mFastAdapter.withSavedInstanceState(savedInstanceState);

    }


    public long getTpId(String day, boolean isMorning){
        //id with year,month,day,0/1(shift)
        return Long.parseLong(year+DateTimeUtils.getMonthNumber(month)+day+ (isMorning?"0":"1"));
    }
    //initialize shift tp
    public void initializeTP(final int cell, final String day, final boolean isMorning, final long id){
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TPServerModel tpServerModel = new TPServerModel();
                tpServerModel.setLocalId(id);
                tpServerModel.setServerId("0");
                tpServerModel.setDay(day);
                tpServerModel.setContactPlace("");
                tpServerModel.setReportTime("00:00");
                tpServerModel.setShiftType("Working");
                tpServerModel.setShift(isMorning?StringConstants.MORNING:StringConstants.EVENING);
                tpServerModel.setnDA("HQ");
                tpServerModel.setcCell(String.valueOf(cell));
                tpServerModel.setYear(year);
                tpServerModel.setMonth(month);
                tpServerModel.setApproved(false);
                tpServerModel.setUploaded(false);
                tpServerModel.setChanged(true);
                tpServerModel.setChangedFromServer(false);
                r.insertOrUpdate(tpServerModel);
            }
        });


    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tp,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void displayTpUploadErrorAlert(int count){
        String msg = count + " day TP not done yet! Complete the full month TP then try again.";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void displayTpUploadApprovedAlert(){
        String msg = "No Pending TP!!";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void displayTpUploadAlert(){
        String msg = "Your are trying to upload full month TP! Would you like to continue ?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                sendTpToServer();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void displayPartialTpUploadAlert(int count){
        String msg = "Your are trying to upload partial monthly TP ("+count+")! Would you like to continue ?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                sendTpToServer();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void displayTpChangeUploadAlert(){
        String msg = "Your are trying to upload Changed TP's! Would you like to continue ?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                sendTpToServer();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    private void sendTpToServer(){

        ToastUtils.shortToast(StringConstants.UPLOADING_MSG);
        String jsonString = TourPlanUtils.getTPListForSend(r, month, year);

        MyLog.show("JsonString","UserID:"+userModel.getUserId()+
                " Month: "+DateTimeUtils.getMonthNumber(month)+" Json String:"+jsonString);
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposableForSend = new CompositeDisposable();
        if(IS_APPROVED){
            mCompositeDisposableForSend.add(apiServices.sendChangedTPBody("",
                    String.valueOf(year),
                    DateTimeUtils.getMonthNumber(month),
                    jsonString)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                        @Override
                        public void onComplete() {
                            MyLog.show(TAG,"onComplete");
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            MyLog.show(e.getLocalizedMessage(), e.getMessage());
                            ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDetail<String> value) {
                            if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                                ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                                TourPlanUtils.updateTPAfterSend(r, month, year);
                                //new FCMSendNotification(userModel.getMarket(), "Change TP", MONTH_NAME[month-1], userModel.getUserId(), month, year);
                            }else{
                                ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            }
                        }
                    }));

        } else {
            mCompositeDisposableForSend.add(apiServices.sendTPBody("userModel.getUserId()",
                    String.valueOf(year),
                    DateTimeUtils.getMonthNumber(month),
                    jsonString)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                        @Override
                        public void onComplete() {
                            MyLog.show(TAG,"onComplete");
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            MyLog.show(e.getLocalizedMessage(), e.getMessage());
                            ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDetail<String> value) {
                            if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                                ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                                TourPlanUtils.updateTPAfterSend(r, month, year);
                                //new FCMSendNotification(userModel.getMarket(), "Tour Plan", MONTH_NAME[month-1], userModel.getUserId(), month, year);
                            }else{
                                ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            }
                        }
                    }));

        }

    }

    private void getTpFromServer(){
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();

        mCompositeDisposableForGet = new CompositeDisposable();

        mCompositeDisposableForGet.add(apiServices.getTP("userModel.getUserId()",
                String.valueOf(year),
                DateTimeUtils.getMonthNumber(month))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<TPModel>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(final ResponseDetail<TPModel> value) {

                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            ToastUtils.shortToast(StringConstants.SYNC_SUCCESS_MSG);
                            Log.e("TPCalendar", value.getDataModelList().size() +"");
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm r) {
                                    for (TPModel tpListModel : value.getDataModelList()) {
                                        long tpId;
                                        TPServerModel tpServerModel = r.where(TPServerModel.class)
                                                .equalTo(TPServerModel.COL_LOCAL_ID, Long.valueOf(tpListModel.getAnDetailSL()))
                                                .findFirst();
                                        tpId = tpServerModel.getLocalId();
                                        RealmResults<TPPlaceRealmModel> tpPlaceRealmModels = r.where(TPPlaceRealmModel.class)
                                                .equalTo(TPPlaceRealmModel.COL_TP_ID, tpId)
                                                .findAll();
                                        if(tpPlaceRealmModels != null && tpPlaceRealmModels.size() > 0) {
                                            tpPlaceRealmModels.deleteAllFromRealm();
                                        }

                                        MyLog.show("TPID: ", ""+tpId);

                                        tpServerModel.setServerId(tpListModel.getDetailSL());
                                        tpServerModel.setContactPlace(tpListModel.getMeetingPlace());
                                        tpServerModel.setDay(getDayWithoutZero(tpListModel.getDayNumber()));
                                        tpServerModel.setnDA(tpListModel.getAllowanceNature());
                                        tpServerModel.setReportTime(tpListModel.getTime());
                                        tpServerModel.setShiftType(tpListModel.getShiftType());
                                        tpServerModel.setShift(tpListModel.getShift());
                                        tpServerModel.setUploaded(true);
                                        if(tpListModel.getApprovalStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED)){
                                            tpServerModel.setApproved(true);
                                        } else {
                                            tpServerModel.setApproved(false);
                                        }

                                        if(tpListModel.getChangeStatus().equalsIgnoreCase(StringConstants.YES)){
                                            tpServerModel.setChanged(true);
                                        } else {
                                            tpServerModel.setChanged(false);
                                        }

                                        if(tpListModel.getChangeStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED) || tpListModel.getChangeStatus().equalsIgnoreCase(StringConstants.YES)){
                                            tpServerModel.setChangedFromServer(true);
                                        } else {
                                            tpServerModel.setChangedFromServer(false);
                                        }

                                        //Realm insert
                                        if(tpServerModel.getShiftType().equalsIgnoreCase(StringConstants.WORKING)){
                                            for (PlaceModel place : tpListModel.getSubDetailList()) {
                                                Number currentIdNum = r.where(TPPlaceRealmModel.class).max(TPPlaceRealmModel.COL_ID);
                                                int nextId;
                                                if(currentIdNum == null) {
                                                    nextId = 1;
                                                } else {
                                                    nextId = currentIdNum.intValue() + 1;
                                                }
                                                TPPlaceRealmModel tpPlaceRealmModel = new TPPlaceRealmModel();
                                                tpPlaceRealmModel.setId(nextId);
                                                tpPlaceRealmModel.setShift(tpListModel.getShift());
                                                tpPlaceRealmModel.setCode(place.getInstitutionCode());
                                                tpPlaceRealmModel.setTpId(tpId);

                                                MyLog.show("OnNext","Place:"+place.getInstitutionCode());

                                                //Realm insert
                                                r.insertOrUpdate(tpPlaceRealmModel);

                                            }
                                        }

                                        r.insertOrUpdate(tpServerModel);
                                    }
                                    setAdapter();
                                }
                            });

                        } else{
                            ToastUtils.shortToast(StringConstants.NO_DATA_FOUND_MSG);
                        }
                    }
                }));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCompositeDisposableForSend != null)
            mCompositeDisposableForSend.clear();
        if(mCompositeDisposableForGet != null)
            mCompositeDisposableForGet.clear();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void displayConfirmationPopup(final Activity activity){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(StringConstants.TP_SYNCED_WITH_CHANGES);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ToastUtils.longToast(StringConstants.SYNC_MSG);
                getTpFromServer();

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

    public boolean isLocallyChanged(){

        boolean returnValue = false;
        RealmResults<TPServerModel> tpServerModels = r. where(TPServerModel.class)
                .equalTo(TPServerModel.COL_YEAR, year)
                .equalTo(TPServerModel.COL_MONTH, month)
                .findAll();
        for (TPServerModel tpServerModel:tpServerModels){
            if(tpServerModel.isChanged()){
                returnValue = true;
            }
        }
        return returnValue;
    }

    public void getTourPlansWithStatus(Realm realm){
        IS_APPROVED = false;
        IS_ALL_APPROVED = true;
        dayStatusTP = new int[35];
        List<TPServerModel> savedTPs                          = realm
                .where(TPServerModel.class)
                .equalTo(TPServerModel.COL_YEAR, year)
                .equalTo(TPServerModel.COL_MONTH, month)
                .notEqualTo(TPServerModel.COL_CONTACT, "")
                .findAll();
        for (TPServerModel tp : savedTPs){
            dayStatusTP[Integer.parseInt(tp.getcCell())] =  1;
            if(tp.isUploaded()){
                dayStatusTP[Integer.parseInt(tp.getcCell())] =  2;
            }
            if(tp.isApproved()){
                IS_APPROVED = true;
                dayStatusTP[Integer.parseInt(tp.getcCell())] =  3;
            } else {
                IS_ALL_APPROVED = false;
            }

            if(tp.isChangedFromServer()){
                if(!tp.isChanged())
                    dayStatusTP[Integer.parseInt(tp.getcCell())] =  4;
            }

        }
    }

    public int getNoTpOfCurrentMonth(Realm realm){
        List<TPServerModel> tpList = realm.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_YEAR, year)
                .equalTo(TPServerModel.COL_MONTH, month)
                .notEqualTo(TPServerModel.COL_CONTACT, "")
                .findAll();
        MyLog.show("TPCalendar", "TPCount: "+tpList.size());
        return tpList.size();
    }

    public String getDayWithoutZero(String day){
        if(day.startsWith("0")){
            return day.substring(1);
        } else
            return day;
    }
}
