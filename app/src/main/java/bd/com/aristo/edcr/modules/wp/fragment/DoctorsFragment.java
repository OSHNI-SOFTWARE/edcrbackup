package bd.com.aristo.edcr.modules.wp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.listener.DateValidationListener;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.InternModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.modules.wp.model.WPDoctorsModel;
import bd.com.aristo.edcr.modules.wp.model.WPForGet;
import bd.com.aristo.edcr.modules.wp.model.WPForSend;
import bd.com.aristo.edcr.modules.wp.model.WPModel;
import bd.com.aristo.edcr.modules.wp.model.WPModelForSend;
import bd.com.aristo.edcr.modules.wp.model.WPUtilsModel;
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

/**
 * Created by monir.sobuj on 6/11/17.
 */

public class DoctorsFragment extends Fragment implements DateValidationListener {

    private static final String TAG="WP:DoctorsFragment";

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;
    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;

    @BindView(R.id.llFourth)
    LinearLayout llFourth;
    @BindView(R.id.llSecond)
    LinearLayout llSecond;

    List<DoctorsModel> doctorsModels;
    public Fragment fragment;

    private CompositeDisposable mCompositeDisposable;

    List<WPModelForSend> wpModelForSendList;

    public UserModel userModel;
    public DateModel dateModel;
    @Inject
    RequestServices requestServices;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }


    public DoctorsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor_tab, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();
        if(getArguments() != null) {
            dateModel = (DateModel) getArguments().getSerializable(StringConstants.DATE_MODEL);
            MyLog.show(TAG, " " + dateModel.getDay());
            setupList();
            //((Activity) context).setTitle("Workplan: "+formatDate());
        } else {
            ((Activity) context).onBackPressed();
        }
        return rootView;
    }

    private void setupList() {

        doctorsModels = r.where(DoctorsModel.class).findAll();//distinct(DoctorsModel.COL_DID);

        Log.e(TAG,"doctors list size:"+doctorsModels.size());

        if(doctorsModels != null && doctorsModels.size() > 0) { //products available, load list
            refreshList();
        }else{ // no product is available, download now
            //downloadDoctors();
        }
    }



    public void refreshList(){
        List<WPDoctorsModel> wpDoctors = WPUtils.getWPDoctors(r, dateModel);
        ((Activity) context).setTitle("Work Plan "+formatDate()+"("+wpDoctors.size()+")");
        Log.e(TAG, "wpdoctors size:" + wpDoctors.size());
        final FastItemAdapter<WPDoctorsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(wpDoctors);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((v, adapter, item, position) -> {
            WPUtilsModel wpUtilsModel = new WPUtilsModel(item.getId(), item.getName(), item.getIns(), item.isMorning());
            if(item.getId().contains("I")) {
                InternModel internModel = r.where(InternModel.class).equalTo(InternModel.COL_ID, item.getId())
                        .equalTo(InternModel.COL_DATE, DateTimeUtils.getDayMonthYear(dateModel))
                        .equalTo(InternModel.COL_SHIFT, item.isMorning())
                        .findFirst();
                if(internModel != null){
                    goToInternWorkPlanPager(wpUtilsModel);
                } else {
                    r.executeTransaction(realm -> {
                        String wardName = item.getId().split("-")[1];
                        InternModel internModel1 = new InternModel();
                        internModel1.setInternId(item.getId());
                        internModel1.setDate(DateTimeUtils.getDayMonthYear(dateModel));
                        internModel1.setInstitute("");
                        internModel1.setMorning(item.isMorning());
                        internModel1.setUnit(wardName);
                        r.insertOrUpdate(internModel1);
                        Fragment fragment = new WPInternViewPager();
                        Bundle b = new Bundle();
                        b.putSerializable(StringConstants.WORK_PLAN_UTIL_MODEL, wpUtilsModel);
                        b.putSerializable(StringConstants.DATE_MODEL, dateModel);
                        fragment.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("intern_wp_pager").commit();
                    });
                }
            } else {
                goToWorkPlanPager(wpUtilsModel);
            }
            return false;
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);
        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<WPDoctorsModel>() {
            @Override
            public boolean filter(WPDoctorsModel item, CharSequence constraint) {
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

    public void goToWorkPlanPager(WPUtilsModel wpUtilsModel){


        fragment = new WPViewPager();
        Bundle b = new Bundle();
        b.putSerializable(StringConstants.WORK_PLAN_UTIL_MODEL, wpUtilsModel);
        b.putSerializable(StringConstants.DATE_MODEL, dateModel);
        fragment.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("wp_pager").commit();

    }

    public void goToInternWorkPlanPager(WPUtilsModel wpUtilsModel){


        fragment = new WPInternViewPager();
        Bundle b = new Bundle();
        b.putSerializable(StringConstants.WORK_PLAN_UTIL_MODEL, wpUtilsModel);
        b.putSerializable(StringConstants.DATE_MODEL, dateModel);
        fragment.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("intern_wp_pager").commit();

    }


    @OnClick(R.id.llSecond)
    void upload(){

        if(ConnectionUtils.isNetworkConnected(context)){
            long dateMillis = getTPMillis();
            requestServices.compareDate(context, apiServices, dateMillis, this);
        } else {
            ToastUtils.longToast("No Internet!! Please turn on mobile data or wifi.");
        }

    }


    public void displayWorkplanErrorAlert(){
        String msg = "No work plan found to upload.";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void displayWorkplanUploadAlert(final String jsonString){
        String msg = "You are trying to upload work plan! Would you like to continue ?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                uploadWorkPlan(jsonString);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void uploadWorkPlan(String jsonString){
        ToastUtils.shortToast(StringConstants.UPLOADING_MSG);

        String selectedDate = DateTimeUtils.getMonthNumber(dateModel.getDay()) +"-"
                +DateTimeUtils.getMonthNumber(dateModel.getMonth())+ "-"
                +dateModel.getYear();

        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        MyLog.show("Sent parameter:","UserID:"+userModel.getUserId()+"Selected DateTime:"+ selectedDate+" JsonString:"+jsonString);
        mCompositeDisposable.add(apiServices.postWorkPlanPlain( //postWorkPlan(
                userModel.getUserId(),
                selectedDate,
                jsonString)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "OnComplete: work plan upload");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDetail<String> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                            ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                            WPUtils.setWorkPlanAfterSend(r, dateModel);
                        }else{
                            ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                        }


                    }
                }));
    }

    @OnClick(R.id.llFourth)
    void sync(){

        ToastUtils.shortToast(StringConstants.SYNC_MSG);

        CompositeDisposable mCompositeDisposable = new CompositeDisposable();

        String selectedDate = DateTimeUtils.getMonthNumber(dateModel.getDay()) +"-"
                +DateTimeUtils.getMonthNumber(dateModel.getMonth())+ "-"
                +dateModel.getYear();

        MyLog.show("WPDF","Sync WP::Sent param-UserID:"+userModel.getUserId()+"selectedDate DateTime:"+ selectedDate);
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.getWorkPlan(
                userModel.getUserId(),
                selectedDate)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<WPForGet>>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "OnComplete: sync work plan done");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDetail<WPForGet> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                            ToastUtils.shortToast(StringConstants.SYNC_SUCCESS_MSG);
                            if(value.getDataModelList().size() > 0){
                                saveWPFromServer(value.getDataModelList());
                            }
                        }else{
                            ToastUtils.shortToast(StringConstants.NO_DATA_FOUND_MSG);
                        }


                    }
                }));

    }
    @OnClick(R.id.llHome)
    void clickOnHome(){
        ((Activity) context).finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }
    }


    public void saveWPFromServer(final List<WPForGet> wpForGetList){

        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(WPModel.class)
                        .equalTo(WPModel.COL_DAY, dateModel.getDay())
                        .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                        .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                        .findAll().deleteAllFromRealm();
                for (WPForGet wpForGet:wpForGetList){

                    MyLog.show("Product","Product Code:"+wpForGet.getProductCode());

                    if(isProductAndDoctorExist(wpForGet.getDoctorID(), wpForGet.getProductCode())) {
                        WPModel wpModel = new WPModel();
                        wpModel.setDay(dateModel.getDay());
                        wpModel.setMonth(dateModel.getMonth());
                        wpModel.setYear(dateModel.getYear());
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

                        r.insertOrUpdate(wpModel);
                    }

                }//end for product


                refreshList();

            }
        });


    }

    public String formatDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(dateModel.getYear(), dateModel.getMonth() - 1, dateModel.getDay());
        Date date1 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EE, d MMM");
        String hireDate = sdf.format(date1);
        return hireDate;
    }

    /*public boolean isEligible(){
        long currentMillis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_MONTH) == dateModel.getDay() && cal.get(Calendar.MONTH)+1 == dateModel.getMonth()){
            //Today 9:00 am
            cal.set(dateModel.getYear(), dateModel.getMonth() -1, dateModel.getDay(), 9, 0, 0);
            return (currentMillis < cal.getTimeInMillis());
        } else {
            cal.set(dateModel.getYear(), dateModel.getMonth() -1, dateModel.getDay());
            return (currentMillis < cal.getTimeInMillis());
        }
    }*/

    public boolean isProductAndDoctorExist(String docId, String productCode){
        DoctorsModel dm = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_ID, docId)
                .equalTo(DoctorsModel.COL_YEAR, dateModel.getYear())
                .equalTo(DoctorsModel.COL_MONTH, dateModel.getMonth())
                .findFirst();
        ProductModel pm = r.where(ProductModel.class)
                .equalTo(ProductModel.COL_CODE, productCode)
                .equalTo(ProductModel.COL_YEAR, dateModel.getYear())
                .equalTo(ProductModel.COL_MONTH, dateModel.getMonth())
                .findFirst();
        return (dm != null && pm != null);
    }

    @Override
    public void valid() {
        List<WPForSend> wpForSendList = WPUtils.getWorkPlanForSend(r, dateModel, userModel.getMarketCode());
        //wpModelForSendList = WPUtils.getWPForSend(r, dateModel);
        //MyLog.show("productWithDoctorsList", wpModelForSendList.size()+"");
        MyLog.show("workplan", wpForSendList.size()+"");

        //if (wpModelForSendList.size()>0){
        if (wpForSendList.size()>0){
            Gson gson = new GsonBuilder().create();
            JsonArray jsonArray = gson.toJsonTree(wpForSendList).getAsJsonArray();
            //JsonArray jsonArray = gson.toJsonTree(wpModelForSendList).getAsJsonArray();
            JsonObject jsonObject = new JsonObject();
            //jsonObject.add("MasterList", jsonArray);
            jsonObject.add("DetailList", jsonArray);

            displayWorkplanUploadAlert(jsonObject.toString());
        }else{
            displayWorkplanErrorAlert();
        }
    }

    @Override
    public void invalid() {
        ToastUtils.displayAlert(((AppCompatActivity) context), "Sorry!! You can not upload work plan in back date!");
    }

    @Override
    public void onError() {
        ToastUtils.displayAlert(((AppCompatActivity) context), "Server Error!! Try Again.");
    }

    public long getTPMillis(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(dateModel.getYear(), dateModel.getMonth()-1, dateModel.getDay(), 23, 0, 0);
        return calendar.getTimeInMillis();
    }
}
