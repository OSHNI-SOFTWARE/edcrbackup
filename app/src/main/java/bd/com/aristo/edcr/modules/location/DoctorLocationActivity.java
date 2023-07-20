package bd.com.aristo.edcr.modules.location;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class DoctorLocationActivity extends AppCompatActivity implements DoctorLocationListener{

    public static final String TAG = DoctorLocationActivity.class.getSimpleName();

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @Inject
    LoadingDialog loadingDialog;
    @BindView(R.id.btnUpload)
    Button btnUpload;
    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;
    @BindView(R.id.doctorList)
    RecyclerView rvDoctor;


    public UserModel userModel;
    public DateModel dateModel;
    public DateModel dateNextModel;

    boolean isCurrentMonth = false;

    List<String> morningLocation  = new ArrayList<>();
    List<String> eveningLocation  = new ArrayList<>();

    List<IDoctorLocation> iDoctorLocationListIsLocated = new ArrayList<>();
    List<IDoctorLocation> iDoctorLocationListNotLocated = new ArrayList<>();
    List<IDoctorLocation> iDoctorLocationList = new ArrayList<>();
    FastItemAdapter<IDoctorLocation> fastAdapter;
    int pos;
    private CompositeDisposable mCompositeDisposableForSend;
    @Inject
    RequestServices requestServices;

    public void getUserInfo() {
        userModel = r.where(UserModel.class).findFirst();
        dateModel = DCRUtils.getToday();
        //setDateModel(isCurrentMonth);

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, DoctorLocationActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_location);
        App.getComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ButterKnife.bind(this);
        getUserInfo();
        //monthChange();
    }

    private void monthChange(){
        iDoctorLocationList = new ArrayList<>();
        iDoctorLocationListNotLocated = new ArrayList<>();
        iDoctorLocationListIsLocated = new ArrayList<>();
        setDoctors();
        fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(iDoctorLocationList);
        fastAdapter.setHasStableIds(true);
        fastAdapter.withSelectable(true);
        rvDoctor.setLayoutManager(new LinearLayoutManager(this));
        rvDoctor.setAdapter(fastAdapter);
        rvDoctor.setHasFixedSize(true);
        fastAdapter.withItemEvent(new ClickEventHook<IDoctorLocation>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IDoctorLocation.ViewHolder) {
                    return ((IDoctorLocation.ViewHolder) viewHolder).llMorning;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IDoctorLocation> fastAdapter1, final IDoctorLocation item) {
                //if(!isCurrentMonth) {
                    pos = position;
                    DialogFragment dialogFragment = new EditDoctorLocationDialog(DoctorLocationActivity.this, item, true, dateNextModel.getMonth(), dateNextModel.getYear());
                    dialogFragment.show(getSupportFragmentManager(), "doctor_location");
               // } else {
              //      ToastUtils.longToast("Unable to update!!");
              //  }
            }
        });

        fastAdapter.withItemEvent(new ClickEventHook<IDoctorLocation>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IDoctorLocation.ViewHolder) {
                    return ((IDoctorLocation.ViewHolder) viewHolder).llEvening;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IDoctorLocation> fastAdapter1, final IDoctorLocation item) {
                //if(!isCurrentMonth) {
                    pos = position;
                    DialogFragment dialogFragment = new EditDoctorLocationDialog(DoctorLocationActivity.this, item, false, dateNextModel.getMonth(), dateNextModel.getYear());
                    dialogFragment.show(getSupportFragmentManager(), "doctor_location");
                //} else {
                    //ToastUtils.longToast("Unable to update!!");
                //}
            }
        });

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDoctorLocation>() {
            @Override
            public boolean filter(IDoctorLocation item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

        etFilterDoctor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fastAdapter.getItemAdapter().filter(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setDoctors(){
        List<DoctorsModel> doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, dateNextModel.getMonth())
                .equalTo(DoctorsModel.COL_YEAR, dateNextModel.getYear())
                .findAll();
        if(doctorsModels != null && doctorsModels.size() > 0){
            for(DoctorsModel dm:doctorsModels){
                if(dm.getId().equals(dm.getId().toLowerCase())) {
                    IDoctorLocation iDoctorLocation = new IDoctorLocation();
                    iDoctorLocation.setmLoc(dm.getMorningLoc());
                    iDoctorLocation.seteLoc(dm.getEveningLoc());
                    iDoctorLocation.setAddress(dm.getAddress());
                    iDoctorLocation.setDegree(dm.getDegree());
                    iDoctorLocation.setId(dm.getId());
                    iDoctorLocation.setName(dm.getName());
                    iDoctorLocation.setSaved(true);
                    iDoctorLocation.setSpecial(dm.getSpecial());
                    iDoctorLocation.setSynced(true);
                    if (!TextUtils.isEmpty(dm.getMorningLoc())) {
                        morningLocation.add(dm.getMorningLoc());
                    }
                    if (!TextUtils.isEmpty(dm.getEveningLoc())) {
                        eveningLocation.add(dm.getEveningLoc());
                    }
                    if (TextUtils.isEmpty(dm.getMorningLoc()) && TextUtils.isEmpty(dm.getEveningLoc())) {
                        iDoctorLocationListNotLocated.add(iDoctorLocation);
                        iDoctorLocation.setSaved(false);
                    } else {
                        iDoctorLocationListIsLocated.add(iDoctorLocation);
                    }
                }
            }
            iDoctorLocationList.addAll(iDoctorLocationListNotLocated);
            iDoctorLocationList.addAll(iDoctorLocationListIsLocated);
        }

        Set<String> locSet = new HashSet<>(morningLocation);
        morningLocation.clear();
        morningLocation.addAll(locSet);
        Set<String> locSetE = new HashSet<>(eveningLocation);
        eveningLocation.clear();
        eveningLocation.addAll(locSetE);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if(id == R.id.action_list){
            LocationWiseDoctorActivity.start(this, dateNextModel);
        }

        if(id == R.id.action_current){
            isCurrentMonth = !isCurrentMonth;
            invalidateOptionsMenu();

            //LocationWiseDoctorActivity.start(this);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(isCurrentMonth){
            menu.findItem(R.id.action_current).setTitle(DateTimeUtils.getMonthForInt(dateNextModel.getMonth()));
        } else {
            menu.findItem(R.id.action_current).setTitle(DateTimeUtils.getMonthForInt(dateModel.getMonth()));
        }
        setDateModel(isCurrentMonth);
        monthChange();
        setTitle(String.format(getResources().getString(R.string.doctor_location), DateTimeUtils.getMonthForInt(dateNextModel.getMonth())));

        return super.onPrepareOptionsMenu(menu);
    }




    @Override
    public void onSave(final IDoctorLocation iDoctorLocation, final boolean isMorning) {

        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                iDoctorLocationList.get(pos).setSaved(true);
                DoctorsModel dm = r.where(DoctorsModel.class)
                        .equalTo(DoctorsModel.COL_YEAR, dateNextModel.getYear())
                        .equalTo(DoctorsModel.COL_MONTH, dateNextModel.getMonth())
                        .equalTo(DoctorsModel.COL_ID, iDoctorLocation.getId())
                        .findFirst();
                if(dm != null) {
                    if (isMorning) {
                        dm.setMorningLoc(iDoctorLocation.getmLoc());
                    } else {
                        dm.setEveningLoc(iDoctorLocation.geteLoc());
                    }
                }

                fastAdapter.notifyAdapterDataSetChanged();
            }
        });

    }


    @OnClick(R.id.btnUpload)
    void uploadDoctorLocation(){

        //if(!isCurrentMonth) {
            if (ConnectionUtils.isNetworkConnected(this)) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                List<DoctorsModel> doctorsModels = r.where(DoctorsModel.class)
                        .equalTo(DoctorsModel.COL_MONTH, dateNextModel.getMonth())
                        .equalTo(DoctorsModel.COL_YEAR, dateNextModel.getYear())
                        .findAll();
                List<DoctorsModel> doctorsModelsNotAssign = r.where(DoctorsModel.class)
                        .equalTo(DoctorsModel.COL_MONTH, dateNextModel.getMonth())
                        .equalTo(DoctorsModel.COL_YEAR, dateNextModel.getYear())
                        .equalTo(DoctorsModel.COL_EVENING_LOC, "")
                        .equalTo(DoctorsModel.COL_MORNING_LOC, "")
                        .findAll();
                long doctorCount = iDoctorLocationList == null ? 0 : iDoctorLocationList.size();
                final long locCount = doctorCount - (doctorsModelsNotAssign == null ? 0 : doctorsModelsNotAssign.size());
                String nameId = "total doctors <span style=\"color:#FF9E02;\">" + doctorCount + "</span> <br><br>"
                        + "location assigned doctors <span style=\"color:#01991f;\">" + locCount + "</span> <br><br>"
                        + "do you want to upload?";
                alert.setMessage(Html.fromHtml(nameId));
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if(locCount > 0){
                            uploadToServer();
                        }

                    }
                });

                alert.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                alert.show();
            } else {
                ToastUtils.longToast("No Internet!!");
            }
        /*} else {
            ToastUtils.longToast("Unable to upload!!");
        }*/
        }

    private void uploadToServer() {
        JSONArray jsonArray = new JSONArray();
        for(IDoctorLocation iDoctorLocation:iDoctorLocationList) {
            try {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("DoctorID", iDoctorLocation.getId());
                jsonObject1.put("MarketCode", userModel.getTeritoryId());
                jsonObject1.put("Year", String.valueOf(dateNextModel.getYear()));
                jsonObject1.put("MonthNumber", DateTimeUtils.getMonthNumber(dateNextModel.getMonth()));
                jsonObject1.put("MorningLocation", iDoctorLocation.getmLoc());
                jsonObject1.put("EveningLocation", iDoctorLocation.geteLoc());
                jsonArray.put(jsonObject1);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("DetailList", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        final LoadingDialog loadingDialog = LoadingDialog.newInstance(this, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposableForSend = new CompositeDisposable();
        Log.d(TAG, "uploadToServer: " + jsonObject.toString());

        mCompositeDisposableForSend.add(apiServices.postDoctor(
                jsonObject.toString())
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG,"onComplete");
                        //loadingDialog.dismiss();
                        getDoctors();
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
                        }else if(value.getMessage() != null){
                            //ToastUtils.shortToast(value.getMessage());
                            showAlert(value.getMessage());
                        }
                    }
                }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    public void setDateModel(boolean isCurrent) {
        if(isCurrent){
            dateNextModel = dateModel;
        } else {
            dateNextModel = new DateModel(dateModel.getDay(),dateModel.getMonth(),  dateModel.getYear(), 0, 0);
            if(dateModel.getMonth() == 12){
                dateNextModel.setMonth(1);
                dateNextModel.setYear(dateModel.getYear() + 1);
            } else if(dateModel.getMonth() == 1){
                dateNextModel.setMonth(dateModel.getMonth() + 1);
            } else {
                dateNextModel.setMonth(dateModel.getMonth() + 1);
            }
        }

    }

    public void showAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void getDoctors(){
        mCompositeDisposableForSend.add(apiServices.getDoctors(userModel.getUserId(), "")
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<DoctorsModel>>() {
                    @Override
                    public void onComplete() {
                        r.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                requestServices.insertIntern(realm);
                            }
                        });
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(final ResponseDetail<DoctorsModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equals("True")) {
                            try {
                                r.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm r) {
                                        //clear old place data
                                        r.where(DoctorsModel.class)
                                                .findAll().deleteAllFromRealm();
                                        //insert
                                        r.insertOrUpdate(value.getDataModelList());

                                        MyLog.show(TAG, "onNext: doctor Sync");
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }
                    }
                }));
    }



}
