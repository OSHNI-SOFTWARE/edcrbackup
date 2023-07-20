package bd.com.aristo.edcr.modules.wp.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import bd.com.aristo.edcr.listener.MonthChangedListener;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.modules.wp.model.CalenderModel;
import bd.com.aristo.edcr.modules.wp.model.WPForGet;
import bd.com.aristo.edcr.modules.wp.model.WPModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.MainMenuConstants;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by Tariqul.Islam on 6/5/17.
 */

public class WPCalendarFragment extends Fragment {

    final String TAG = WPCalendarFragment.class.getSimpleName();

    //saveAndNext our FastAdapter
    private FastAdapter<CalenderModel> mFastAdapter;



    public String[] days;
    public Calendar calCurrent, calPrev, calNext;
    ATextView txtCurrentMonth;
    int dayOfMonth, month, year, lastDay;
    ATextView btnNextMonth, btnPrevMonth;

    public int[] plannedCount = new int[35];
    public int[] dvrCount = new int[35];

    @Inject
    Realm r;
    @BindView(R.id.rv)
    RecyclerView rv;
    public Bundle savedInstanceState;
    ItemAdapter<CalenderModel> itemAdapter;
    UserModel userModel;
    @Inject
    APIServices apiServices;
    LoadingDialog loadingDialog;
    AlertDialog.Builder alert;

    Context context;
    public MonthChangedListener monthChangedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    public WPCalendarFragment() {

        days                                            = new String[35];
    }

    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_wp_calendar, container, false);
        ButterKnife.bind(this, rootView);

        this.savedInstanceState                         = savedInstanceState;
        MainMenuConstants.getInstance().setActivityWH(((Activity) context));
        loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        alert = new AlertDialog.Builder(context);
        txtCurrentMonth = (ATextView) rootView.findViewById(R.id.txtCurrentMonth);
        btnNextMonth = (ATextView) rootView.findViewById(R.id.btnNextMonth);
        btnPrevMonth = (ATextView) rootView.findViewById(R.id.btnPrevMonth);
        getUserInfo();
        setupMonth();
        setupCalendar();
        setAdapter();

        ((Activity) context).setTitle("Calendar for workplan");

        return rootView;
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
                btnNextMonth.setTextColor(getResources().getColor(R.color.green));

                btnPrevMonth.setEnabled(true);
                btnPrevMonth.setTextColor(getResources().getColor(R.color.green));

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
                btnPrevMonth.setTextColor(getResources().getColor(R.color.green));

                txtCurrentMonth.setEnabled(true);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.green));
            }
        });
        btnPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupMonth();
                refreshDays(calPrev, false);
                setAdapter();
                btnNextMonth.setEnabled(true);
                btnNextMonth.setTextColor(getResources().getColor(R.color.green));

                btnPrevMonth.setEnabled(false);
                btnPrevMonth.setTextColor(getResources().getColor(R.color.red));

                txtCurrentMonth.setEnabled(true);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.green));
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
        } else dayOfMonth = 0;

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
                if(!item.date.getText().equals("") && item.isDvrSaved()){
                    DateModel dateModel = new DateModel(Integer.valueOf(item.date.getText().toString()), month, year, 0, lastDay);
                    Fragment fragment = new DoctorsFragment();
                    Bundle b = new Bundle();
                    b.putSerializable(StringConstants.DATE_MODEL, dateModel);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("wp_doctors").commit();
                }
                return false;
            }
        });


        rv.setLayoutManager(new GridLayoutManager(context,7));
        rv.setItemAnimator(new SlideLeftAlphaAnimator());
        rv.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<CalenderModel> items = new ArrayList<>();

        dvrCount               = WPUtils.getSavedDVRDayList(r, month, year);
        plannedCount           = WPUtils.getSavedWPDayList(r, month, year);

        for (int i = 0; i < 35; i++) {
            CalenderModel item = new CalenderModel();
            item
                    .withName(days[i])
                    .withIdentifier(100 + i);
            if(!days[i].equals("")) {
                int progress = 0;
                if (plannedCount[Integer.valueOf(days[i])] > 0) {
                    item.setSaved(true);
                }
                if (dvrCount[Integer.valueOf(days[i])] > 0) {
                    item.setDvrSaved(true);
                    int planned = plannedCount[Integer.valueOf(days[i])];
                    int dvr = dvrCount[Integer.valueOf(days[i])];
                    float divide = (float)planned/(float) dvr;
                    divide = divide * 100;
                    progress = (int) divide;
                }



                item.setProgress(progress);
            }
            if(String.valueOf(dayOfMonth).equals(days[i])){
                item.withIsCurrent(true);
            }

            items.add(item);
        }
        itemAdapter.add(items);
        mFastAdapter.withSavedInstanceState(savedInstanceState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    //llFourth
    @OnClick(R.id.llFourth)
    void clickOnSync(){

        TPServerModel tpModel = r.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_MONTH, month)
                .equalTo(TPServerModel.COL_YEAR, year)
                .findFirst();
        if(tpModel != null && tpModel.isApproved()){
            wpSyncedAlert();
        } else {
            tpSyncedAlert();
        }

    }

    @OnClick(R.id.llInfo)
    void onClickInfo(){
        DialogFragment dialogFragment = new ColorInfoWPDialog();
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "color_info");
    }

    public void tpSyncedAlert(){
        String msg = "Please TP Synced first!!";
        alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void wpSyncedAlert(){
        String msg = "All saved work plan will be deleted after sync!\n Do you want to sync?";
        alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                syncWPMonthly();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void syncWPMonthly(){
        try {
            ToastUtils.shortToast(StringConstants.SYNC_MSG);

            CompositeDisposable mCompositeDisposable = new CompositeDisposable();

            String selectedDate = DateTimeUtils.getMonthNumber(month) + "-" + year;

            MyLog.show("WPDF", "Sync WP::Sent param-UserID:" + userModel.getUserId() + "selectedDate DateTime:" + selectedDate);

            loadingDialog.show();
            mCompositeDisposable.add(apiServices.getMonthlyWorkPlan(
                    userModel.getUserId(),
                    selectedDate)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDetail<WPForGet>>() {
                        @Override
                        public void onComplete() {
                            Log.e(TAG, "OnComplete: sync work plan done");
                            dismissLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                            dismissLoading();
                        }

                        @Override
                        public void onNext(ResponseDetail<WPForGet> value) {
                            if (value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                                ToastUtils.shortToast(StringConstants.SYNC_SUCCESS_MSG);
                                if (value.getDataModelList().size() > 0) {
                                    saveWPFromServer(value.getDataModelList());

                                } else {
                                    //TODO
                                }
                            } else {
                                ToastUtils.shortToast(StringConstants.NO_DATA_FOUND_MSG);
                            }


                        }
                    }));
        }catch (IllegalStateException e){
            Log.d(TAG, "syncWPMonthly: "+e);
        }

    }


    public void saveWPFromServer(final List<WPForGet> wpForGetList){

        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(WPModel.class)
                        .equalTo(WPModel.COL_MONTH, month)
                        .equalTo(WPModel.COL_YEAR, year)
                        .findAll().deleteAllFromRealm();
                for (WPForGet wpForGet:wpForGetList) {
                    if (isProductAndDoctorExist(wpForGet.getDoctorID(), wpForGet.getProductCode())) {
                        MyLog.show("Doctors", "Doctor ID:" + wpForGet.getDoctorID() + " Product Qty:" + wpForGet.getQuantity());
                        WPModel wpModel = new WPModel();
                        wpModel.setDay(DateTimeUtils.getMonthOrDayWithoutZero(wpForGet.getDate()));
                        wpModel.setMonth(month);
                        wpModel.setYear(year);
                        wpModel.setDoctorID(wpForGet.getDoctorID());
                        wpModel.setInstCode(wpForGet.getInstiCode());
                        wpModel.setProductID(wpForGet.getProductCode());
                        wpModel.setCount(Integer.parseInt(wpForGet.getQuantity()));
                        wpModel.setName(wpForGet.getProductName());
                        wpModel.setUploaded(true);
                        wpModel.setMorning(wpForGet.getShiftName().equalsIgnoreCase(StringConstants.MORNING));

                        if (wpForGet.getItemType().equalsIgnoreCase(StringConstants.SAMPLE_ITEM)) {
                            wpModel.setFlag(1);


                        } else if (wpForGet.getItemType().equalsIgnoreCase(StringConstants.SELECTED_ITEM)) {
                            wpModel.setFlag(0);
                        } else if (wpForGet.getItemType().equalsIgnoreCase(StringConstants.GIFT_ITEM)) {
                            //gift item
                            wpModel.setFlag(2);

                        }
                        realm.insertOrUpdate(wpModel);

                    }//end for product
                }


                setAdapter();

            }
        });


    }

    private void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    public boolean isProductAndDoctorExist(String docId, String productCode){
        DoctorsModel dm = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_ID, docId)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .findFirst();
        ProductModel pm = r.where(ProductModel.class)
                .equalTo(ProductModel.COL_CODE, productCode)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_MONTH, month)
                .findFirst();
        return (dm != null && pm != null);
    }
}
