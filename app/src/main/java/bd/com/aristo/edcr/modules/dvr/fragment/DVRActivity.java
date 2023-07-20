package bd.com.aristo.edcr.modules.dvr.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.fcm.notification.FCMSendNotification;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dvr.DVRUtils;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForSendMaster;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.modules.dvr.model.DayShift;
import bd.com.aristo.edcr.modules.dvr.model.GetDVR;
import bd.com.aristo.edcr.modules.dvr.model.GetDVRDoctor;
import bd.com.aristo.edcr.modules.dvr.model.IDoctorsModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
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
import io.realm.RealmConfiguration;

import static bd.com.aristo.edcr.utils.DateTimeUtils.MONTH_NAME;

public class DVRActivity extends AppCompatActivity {

    private final String TAG = DVRActivity.class.getSimpleName();

    DVRActivity context;


    @Inject
    Realm realm;
    @Inject
    APIServices apiServices;
    public UserModel userModel;
    public Calendar calCurrent, calPrev, calNext;
    int month, year;
    @BindView(R.id.txtPrevMonth)
    ATextView txtPrevMonth;
    @BindView(R.id.txtCurrentMonth)
    ATextView txtCurrentMonth;
    @BindView(R.id.txtNextMonth)
    ATextView txtNextMonth;
    @BindView(R.id.rvDoctors)
    RecyclerView rvDoctors;
    @BindView(R.id.llSync)
    LinearLayout llSync;
    FastItemAdapter<IDoctorsModel> fastItemAdapter;
    private CompositeDisposable mCompositeDisposable;
    boolean isCurrentMonth;
    private SearchView searchView;
    int totalDot = 0;
    boolean isReviewSuccess = false;
    //BackgroundTask backgroundTask;
    public static boolean IS_CHANGED = true;
    public boolean[] dvrApprovalStatus = new boolean[32];

    public static int[] M_DVR_COUNT;
    public static int[] E_DVR_COUNT;

    List<IDoctorsModel> iDoctorsModelList = new ArrayList<>();
    public void getUserInfo(){
        userModel = realm.where(UserModel.class).findFirst();
    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, DVRActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.activity_dvr);
        ButterKnife.bind(this);
        IS_CHANGED = true;
        context = this;
        getUserInfo();
        setupMonth();

        txtCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calCurrent.get(Calendar.YEAR);
                month = calCurrent.get(Calendar.MONTH) + 1;
                txtNextMonth.setEnabled(true);
                txtNextMonth.setTextColor(getResources().getColor(R.color.green));
                isCurrentMonth = true;
                txtPrevMonth.setEnabled(true);
                txtPrevMonth.setTextColor(getResources().getColor(R.color.green));
                txtCurrentMonth.setEnabled(false);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.red));

                //backgroundTask = new BackgroundTask();
                //backgroundTask.execute();
                getDoctors();
            }
        });
        txtNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calNext.get(Calendar.YEAR);
                month = calNext.get(Calendar.MONTH) + 1;
                txtNextMonth.setEnabled(false);
                txtNextMonth.setTextColor(getResources().getColor(R.color.red));
                isCurrentMonth = false;
                txtPrevMonth.setEnabled(true);
                txtPrevMonth.setTextColor(getResources().getColor(R.color.green));
                txtCurrentMonth.setEnabled(true);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.green));

                //backgroundTask = new BackgroundTask();
                //backgroundTask.execute();
                getDoctors();
            }
        });
        txtPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calPrev.get(Calendar.YEAR);
                month = calPrev.get(Calendar.MONTH) + 1;
                isCurrentMonth = false;
                txtNextMonth.setEnabled(true);
                txtNextMonth.setTextColor(getResources().getColor(R.color.green));
                txtPrevMonth.setEnabled(false);
                txtPrevMonth.setTextColor(getResources().getColor(R.color.red));
                txtCurrentMonth.setEnabled(true);
                txtCurrentMonth.setTextColor(getResources().getColor(R.color.green));

                //backgroundTask = new BackgroundTask();
                //backgroundTask.execute();
                getDoctors();
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        if(IS_CHANGED) {
            //backgroundTask = new BackgroundTask();
            //backgroundTask.execute();
            getDoctors();
        }
        IS_CHANGED = false;
    }

    public void setupMonth() {
        calCurrent = Calendar.getInstance();
        month = calCurrent.get(Calendar.MONTH);
        year = calCurrent.get(Calendar.YEAR);
        calPrev = (Calendar) calCurrent.clone();
        calNext = (Calendar) calCurrent.clone();
        if (calCurrent.get(Calendar.MONTH) == 11) {
            calPrev.set(year, month - 1, 1);
            calNext.set(year + 1, 0, 1);
        } else if (calCurrent.get(Calendar.MONTH) == 0) {
            calPrev.set(year - 1, 11, 1);
            calNext.set(year, month + 1, 1);
        } else {
            calPrev.set(year, month - 1, 1);
            calNext.set(year, month + 1, 1);
        }
        txtCurrentMonth.setText(DateTimeUtils.getMonthForInt((int) calCurrent.get(Calendar.MONTH) + 1));
        txtPrevMonth.setText(DateTimeUtils.getMonthForInt((int) calPrev.get(Calendar.MONTH) + 1));
        txtNextMonth.setText(DateTimeUtils.getMonthForInt((int) calNext.get(Calendar.MONTH) + 1));
        txtCurrentMonth.setEnabled(false);
        year = calCurrent.get(Calendar.YEAR);
        month = calCurrent.get(Calendar.MONTH) + 1;
        isCurrentMonth = true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dvr_calendar, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fastItemAdapter.getItemAdapter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                fastItemAdapter.getItemAdapter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if (id == R.id.action_dvr_list){
            Intent intent = new Intent( this, DVRSummaryActivity.class);
            intent.putExtra("MONTH", month);
            intent.putExtra("YEAR", year);
            intent.putExtra("IS_CURRENT", isCurrentMonth);
            startActivity(intent);
        }
        return true;
    }

    public void setAdapter(){
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iDoctorsModelList);
        fastItemAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDoctors.setLayoutManager(layoutManager);
        rvDoctors.setAdapter(fastItemAdapter);
        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDoctorsModel>() {
            @Override
            public boolean onClick(View v, IAdapter<IDoctorsModel> adapter, IDoctorsModel item, int position) {
                /*DVRSelectionActivity.start(context, context,
                        month, year, item.getId(), isCurrentMonth, position);*/
                Intent intent = new Intent(getApplicationContext(), DVRSelectionActivity.class);
                intent.putExtra("MONTH", month);
                intent.putExtra("POS", position);
                intent.putExtra("YEAR", year);
                intent.putExtra("ID", item.getId());
                intent.putExtra("IS_CURRENT", isCurrentMonth);
                //intent.putExtra("LISTENER", dvrSaveListener);
                startActivityForResult(intent, 100);
                return false;
            }
        });
        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDoctorsModel>() {
            @Override
            public boolean filter(IDoctorsModel item, CharSequence constraint) {
                boolean retVal = true;
                for(DayShift dayShift:item.getDayShiftList()){
                    if(String.valueOf(dayShift.getDay()).equalsIgnoreCase(constraint.toString())){
                        retVal = false;
                    }
                }
                return !(item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) && retVal ;
            }
        });
    }

    public void getDoctors() {
        LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait..");
        loadingDialog.show();
        M_DVR_COUNT = new int[32];
        E_DVR_COUNT = new int[32];
        for(int i = 1; i < 32; i++){
            dvrApprovalStatus[i] = true;
            List<DVRForServer> dvrForServers = realm.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_YEAR, year)
                    .equalTo(DVRForServer.COL_MONTH, month)
                    .equalTo(DVRForServer.COL_DAY, i)
                    .findAll();
            String idM = year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(i)+0;
            String idE = year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(i)+1;
            List<DVRDoctorRealm> dvrDoctorRealmMList = realm.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, Long.valueOf(idM))
                    .findAll();
            List<DVRDoctorRealm> dvrDoctorRealmEList = realm.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, Long.valueOf(idE))
                    .findAll();
            if(dvrDoctorRealmEList != null){
                E_DVR_COUNT[i] = dvrDoctorRealmEList.size();
            } else {
                E_DVR_COUNT[i] = 0;
            }
            if(dvrDoctorRealmMList != null){
                M_DVR_COUNT[i] = dvrDoctorRealmMList.size();
            } else {
                M_DVR_COUNT[i] = 0;
            }
            if(dvrForServers != null && dvrForServers.size() > 0){
                dvrApprovalStatus[i] = dvrForServers.get(0).isApproved();
            }
        }
        totalDot = 0;
        iDoctorsModelList = new ArrayList<>();
        List<DoctorsModel> doctorsModelList = new ArrayList<>();
        doctorsModelList.addAll(realm.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .distinct(DoctorsModel.COL_ID)
                .sort(DoctorsModel.COL_NAME)
                .findAll());
        long i = 0;
        for (DoctorsModel dm : doctorsModelList) {
            IDoctorsModel dvrIDoctor = new IDoctorsModel();
            dvrIDoctor.setId(dm.getId());
            dvrIDoctor.setDayShiftList(getDayShitForDoctor(dm.getId(), realm));
            dvrIDoctor.withIdentifier( (++i));
            dvrIDoctor.setName(dm.getName());
            dvrIDoctor.setSpecial(dm.getSpecial());
            dvrIDoctor.setDegree(dm.getDegree());
            dvrIDoctor.setmLoc(dm.getMorningLoc());
            dvrIDoctor.seteLoc(dm.getEveningLoc());
            dvrIDoctor.setClicked(false);
            iDoctorsModelList.add(dvrIDoctor);
            totalDot += dvrIDoctor.getDayShiftList().size();
        }

        loadingDialog.dismiss();
        setTitle("DVR("+totalDot+")");
        setAdapter();

    }

    @OnClick({R.id.llUpload, R.id.llInfo, R.id.llSync})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llUpload:
                if(DVRUtils.isApproved(realm, month, year)){
                    displayDVRUploadAlertAfterApproval();
                } else {
                    displayDVRUploadAlert();
                }
                break;
            case R.id.llInfo:
                DialogFragment dialogFragment = new ColorInfoDialog();
                dialogFragment.show(getSupportFragmentManager(), "color_info");
                break;
            case R.id.llSync:
                displayDVRDownLoadAlert();
                break;
        }
    }

    /*@Override
    public void onSave(int pos, String docId) {
        iDoctorsModelList.get(pos).setDayShiftList(getDayShitForDoctor( docId, realm));
        fastItemAdapter.notifyDataSetChanged();
        setTitle("DVR("+monthlyDOTCount()+")");
    }*/

    public class BackgroundTask extends AsyncTask<Void, Void, Void> {
        LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait..");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            App.getComponent().inject(this);

            loadingDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RealmConfiguration config = new RealmConfiguration
                    .Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm r = Realm.getInstance(config);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingDialog.dismiss();
            setTitle("DVR("+totalDot+")");
            setAdapter();
        }
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
                dayShift.setApproved(dvrApprovalStatus[i]);
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
                dayShift.setApproved(dvrApprovalStatus[i]);
                dayShift.setWeekDay(formatDate(i));
                dayShiftList.add(dayShift);
            }
            }
        return dayShiftList;
    }

    public void displayDVRDownLoadAlert(){
        String msg = "Your are trying to sync DVR! All DVRs of this month may be changed. Do you want to continue?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                getDVRFromServer();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void getDVRFromServer(){
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getDVR(userModel.getUserId(),
                String.valueOf(year),
                DateTimeUtils.getMonthNumber(month ))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<GetDVR>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        MyLog.show(TAG,"DVR Error:"+e.toString());
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                    }

                    @Override
                    public void onNext(final ResponseDetail<GetDVR> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            ToastUtils.shortToast(StringConstants.SYNC_SUCCESS_MSG);
                            MyLog.show("DVRCalendar", value.getDataModelList().size() +"");
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    DVRUtils.deletePreviousMonthlyDVR(realm, month, year);
                                    for(GetDVR getDVR : value.getDataModelList()){
                                        saveDVRFromServer(getDVR, realm);
                                    }
                                }
                            });
                            //backgroundTask = new BackgroundTask();
                            //backgroundTask.execute();
                            getDoctors();


                        }else{
                            ToastUtils.shortToast(StringConstants.NO_DATA_FOUND_MSG);
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCompositeDisposable != null)
            mCompositeDisposable.clear();

    }

    public void displayDVRUploadAlert(){
        String msg = "Your are trying to upload DVR! Would you like to continue?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                uploadDVR();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void displayDVRUploadAlertAfterApproval(){
        String msg = "Your are trying to upload DVR after TP change. You will not be able to make any changes after upload!! Would you like to continue?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                uploadApprovedDVR();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }



    public void uploadDVR(){
        ToastUtils.shortToast(StringConstants.UPLOADING_MSG);
        List<DVRForSendMaster> dvrForSendList = DVRUtils.getDVRForSend(realm, month, year);
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(dvrForSendList).getAsJsonArray();
        if (dvrForSendList.size() < 1){
            ToastUtils.shortToast(StringConstants.BLANK_UPLOAD_MSG);
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("MasterList", jsonArray);
        mCompositeDisposable = new CompositeDisposable();
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.postDVRBody(userModel.getUserId(),
                String.valueOf(year),
                DateTimeUtils.getMonthNumber(month),
                jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onNext(ResponseDetail<String> value) {
                        if (value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                            ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                            new FCMSendNotification(userModel.getMarket(), "DVR", MONTH_NAME[month-1], userModel.getUserId(), month, year);
                        }else{
                            ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        MyLog.show("onError","Server exception:"+e.toString());
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                    }
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();

                    }
                }));

    }

    public void uploadApprovedDVR(){
        ToastUtils.shortToast(StringConstants.UPLOADING_MSG);
        List<DVRForSendMaster> dvrForSendList = DVRUtils.getDVRForSend(realm, month, year);
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(dvrForSendList).getAsJsonArray();
        if (dvrForSendList.size() < 1){
            ToastUtils.shortToast(StringConstants.BLANK_UPLOAD_MSG);
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("MasterList", jsonArray);
        mCompositeDisposable = new CompositeDisposable();
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.postReviewDVRBody(userModel.getUserId(),
                String.valueOf(year),
                DateTimeUtils.getMonthNumber(month),
                jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onNext(ResponseDetail<String> value) {
                        if (value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                            ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                            isReviewSuccess = true;
                        }else{
                            ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                        if(isReviewSuccess){
                            getDVRFromServer();
                        }

                    }
                }));
    }

    public void saveDVRFromServer(GetDVR getDVR, Realm r){
        long dvrId = getDVRLocalID(getDVR);
        DVRForServer dvrForServer = new DVRForServer();
        dvrForServer.setId(dvrId);
        dvrForServer.setServerId(getDVR.getServerId());
        dvrForServer.setDay(Integer.parseInt(getDVR.getDay()));
        dvrForServer.setInitialize(true);
        dvrForServer.setMonth(Integer.parseInt(getDVR.getMonthName()));
        dvrForServer.setMorning(getDVR.getShift().equalsIgnoreCase(StringConstants.MORNING));
        dvrForServer.setApproved(getDVR.getStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED));
        dvrForServer.setYear(Integer.parseInt(getDVR.getYear()));
        r.insertOrUpdate(dvrForServer);
        setDVRDoctors(r, getDVR, dvrId);
    }

    public long getDVRLocalID(GetDVR getDVR){
        String id = getDVR.getYear()+getDVR.getMonthName()+getDVR.getDay()+(getDVR.getShift().equalsIgnoreCase(StringConstants.MORNING)? "0":"1");
        return Long.parseLong(id);
    }

    public String formatDate(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return DateTimeUtils.WEEK_DAY_1[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public boolean isDoctorExist(Realm realm, String docId){
        DoctorsModel dm = realm.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_ID, docId)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .findFirst();
        return (dm != null);
    }

    public void setDVRDoctors(Realm r, GetDVR getDVR, long dvrId){
        List<String> dcrOnly = new ArrayList<>();
        List<String> dvrOnly = new ArrayList<>();
        List<String> both = new ArrayList<>();
        String date = getDVR.getDay()+"-"+getDVR.getMonthName()+"-"+getDVR.getYear();
        List<GetDVRDoctor> dvrDoctors = new ArrayList<>(getDVR.getDvrDoctorList());
        List<DCRModel> dcrModelList = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_CREATE_DATE, date)
                .equalTo(DCRModel.COL_SHIFT, getDVR.getShift())
                .equalTo(DCRModel.COL_IS_SENT, true)
                .findAll();

        for(GetDVRDoctor dvrDoctor:dvrDoctors){
            dvrOnly.add(dvrDoctor.getDoctorID());
        }

        if(dcrModelList != null && dcrModelList.size() > 0){
            for(DCRModel dcrModel:dcrModelList){
               dcrOnly.add(dcrModel.getdID());
            }
        }
        for(String dvr:dvrOnly){
            boolean isInDCR = false;
            for(String dcr:dcrOnly){
                if(dvr.equalsIgnoreCase(dcr)){
                    isInDCR = true;
                }
            }
            if(isDoctorExist(r, dvr)){
                DVRDoctorRealm dvrDoctorRealm = new DVRDoctorRealm();
                dvrDoctorRealm.setDvrLocalId(dvrId);
                dvrDoctorRealm.setDoctorID(dvr);
                if(isInDCR){
                    dcrOnly.remove(dvr);
                    dvrDoctorRealm.setEditable(false);
                } else {
                    dvrDoctorRealm.setEditable(true);
                }
                r.insertOrUpdate(dvrDoctorRealm);
            }

        }
        //insert now
        for(String dvr:dcrOnly){
            if (isDoctorExist(r, dvr)){
                DVRDoctorRealm dvrDoctorRealm = new DVRDoctorRealm();
                dvrDoctorRealm.setDvrLocalId(dvrId);
                dvrDoctorRealm.setDoctorID(dvr);
                dvrDoctorRealm.setEditable(false);
                r.insertOrUpdate(dvrDoctorRealm);
            } else {
                Log.d(TAG, "Doctor ID "+ dvr);
            }
        }

    }

    public int monthlyDOTCount(){
        int count = 0;
        for(IDoctorsModel iDoctorsModel:iDoctorsModelList){
            count += iDoctorsModel.getDayShiftList().size();
        }
        return count;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == RESULT_OK && data != null){
                int pos = data.getIntExtra("POSITION", -1);
                String docId = data.getStringExtra("DOCTOR_ID");
                if(pos != -1) {
                    iDoctorsModelList.get(pos).setDayShiftList(getDayShitForDoctor(docId, realm));
                    fastItemAdapter.notifyDataSetChanged();
                    setTitle("DVR(" + monthlyDOTCount() + ")");
                }
            }
        }
    }
}
