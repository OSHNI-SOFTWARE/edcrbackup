package bd.com.aristo.edcr.modules.gwds;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.ColorInfoTDPGDialog;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.fcm.notification.FCMSendNotification;
import bd.com.aristo.edcr.listener.MonthChangedListener;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.response.ResponseMaster;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.db.DateModel;
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

import static bd.com.aristo.edcr.utils.DateTimeUtils.MONTH_NAME;

/**
 * Created by monir.sobuj on 6/11/17.
 */

public class GWDSGiftFragment extends Fragment {

    private static final String TAG = "GWDSGiftFragment";
    List<ProductModel> giftModelList = new ArrayList<>();
    @BindView(R.id.gwdsList)
    RecyclerView gwdsList;
    @BindView(R.id.btnPrevMonth)
    ATextView btnPrevMonth;
    @BindView(R.id.txtCurrentMonth)
    ATextView txtCurrentMonth;
    @BindView(R.id.btnNextMonth)
    ATextView btnNextMonth;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    public MonthChangedListener monthChangedListener;


    DateModel currentDateModel, dateModel, nextDateModel, prevDateModel;
    public UserModel userModel;

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }
    int month, year;
    public GWDSGiftFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_product_tab, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();
        setHasOptionsMenu(true);
        setupMonth();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gwds, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setupMonth() {
        try {
            month = getArguments().getInt("month", 0);
            year = getArguments().getInt("year", 0);

            setDateModel(month, year);
            if(monthChangedListener != null) {
                monthChangedListener.onMonthChange(dateModel.getMonth(), dateModel.getYear());
            }else {
                ((Activity) context).onBackPressed();
            }
        }catch (NullPointerException e){
            ((Activity) context).onBackPressed();
        }

        txtCurrentMonth.setText(DateTimeUtils.getMonthForInt( dateModel.getMonth()));
        btnPrevMonth.setText(DateTimeUtils.getMonthForInt( prevDateModel.getMonth()));
        btnNextMonth.setText(DateTimeUtils.getMonthForInt( nextDateModel.getMonth()));
        txtCurrentMonth.setEnabled(false);
        setupList();

    }

    public void setMonthBtn(ATextView btnActiveMonth, ATextView btnPrevMonth, ATextView btnNextMonth) {
        if(monthChangedListener != null) {
            monthChangedListener.onMonthChange(dateModel.getMonth(), dateModel.getYear());
        }else {
            ((Activity) context).finish();
        }
        btnNextMonth.setEnabled(true);
        btnNextMonth.setTextColor(getResources().getColor(R.color.color2));

        btnPrevMonth.setEnabled(true);
        btnPrevMonth.setTextColor(getResources().getColor(R.color.color2));

        btnActiveMonth.setEnabled(false);
        btnActiveMonth.setTextColor(getResources().getColor(R.color.red));

        setupList();
    }



    private void setupList() {
        giftModelList = new ArrayList<>();
        giftModelList = GWDSUtils.getGWDSProducts(r, dateModel.getYear(), dateModel.getMonth());
        ((Activity) context).setTitle("GWDS (" + giftModelList.size() + ")");
        refreshList();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void refreshList(){
        List<GWDSGiftModel> giftsModels = GWDSUtils.getGWDSGifts(r, dateModel.getMonth(), dateModel.getYear());
        FastItemAdapter<GWDSGiftModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(giftsModels);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<GWDSGiftModel>() {
            @Override
            public boolean onClick(View v, IAdapter<GWDSGiftModel> adapter, GWDSGiftModel item, int position) {

                GWDSUtilsModel gwdsUtilsModel = new GWDSUtilsModel(item.getCode(), item.getName(), item.getPlanned(), position);
                Fragment fragment = new DoctorsFragment();
                Bundle b = new Bundle();
                b.putSerializable("gwdsUtils", gwdsUtilsModel);
                b.putSerializable("dateModel", dateModel);
                fragment.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("gwds_doctors").commit();
                return false;
            }
        });


        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        gwdsList.setLayoutManager(layoutManager);
        gwdsList.setAdapter(fastAdapter);
    }


    @OnClick(R.id.llSecond)
    void upload(){
        List<GWDServerModel> productWithDoctosList = GWDSUtils.getProductWithDoctorsList(giftModelList, r, dateModel.getMonth(), dateModel.getYear());
        Log.e("productWithDoctosList", productWithDoctosList.size()+"");

        if (productWithDoctosList.size()>0){
            Gson gson = new GsonBuilder().create();
            JsonArray jsonArray = gson.toJsonTree(productWithDoctosList).getAsJsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("MasterList", jsonArray);

            displayGWDSUploadAlert(jsonObject.toString());
        }else{
            displayGWDSErrorAlert();
        }
    }

    @OnClick(R.id.llInfo)
    public void onClickInfo(){
        DialogFragment dialogFragment = new ColorInfoTDPGDialog("GWDS");
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "color_info");
    }

    public void displayGWDSErrorAlert(){
        String msg = "No GWDS found to upload.";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void displayGWDSUploadAlert(final String jsonString){
        String msg = "You are trying to upload GWDS! Would you like to continue ?";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                uploadGWDS(jsonString);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void uploadGWDS(String jsonString){
        MyLog.show(TAG,"GWDS Json String:"+jsonString);
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        if(GWDSUtils.isApproved(r, dateModel.getMonth(), dateModel.getYear())){
            mCompositeDisposable.add(apiServices.postChangeGWDS(
                    userModel.getUserId(),
                    String.valueOf(dateModel.getYear()),
                    DateTimeUtils.getMonthNumber(dateModel.getMonth()),
                    jsonString)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseMaster<GWDServerModel>>() {
                        @Override
                        public void onComplete() {
                            Log.e(TAG, "OnComplete: Gift upload done!");
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseMaster<GWDServerModel> value) {
                            if (value!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                                ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                                GWDSUtils.updateGWDSAfterUpload(giftModelList, r, dateModel.getMonth(), dateModel.getYear());
                            }else{
                                ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            }

                        }
                    }));
        } else {
            mCompositeDisposable.add(apiServices.postGWDS(
                    userModel.getUserId(),
                    String.valueOf(dateModel.getYear()),
                    DateTimeUtils.getMonthNumber(dateModel.getMonth()),
                    jsonString)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseMaster<GWDServerModel>>() {
                        @Override
                        public void onComplete() {
                            Log.e(TAG, "OnComplete: product with doctors upload");
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseMaster<GWDServerModel> value) {
                            if (value!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                                ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                                GWDSUtils.updateGWDSAfterUpload(giftModelList, r, dateModel.getMonth(), dateModel.getYear());
                                new FCMSendNotification(userModel.getMarket(), "GWDS", MONTH_NAME[month-1], userModel.getUserId(), month, year);
                            }else{
                                ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            }
                        }
                    }));
        }

    }

    @OnClick(R.id.llFourth)
    void download(){
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();

        Log.e("downloadGWDS","MonthNumber:"+ DateTimeUtils.getMonthNumber(dateModel.getMonth())+" Year:"+String.valueOf(dateModel.getYear()));
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.getGWDS(
                userModel.getUserId(),
                String.valueOf(dateModel.getYear()),
                DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseMaster<GWDServerModel>>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "OnComplete: gift with doctors download");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.shortToast(StringConstants.DOWNLOAD_FAIL_MSG);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseMaster<GWDServerModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase("True")) {
                            ToastUtils.shortToast(StringConstants.DOWNLOAD_SUCCESS_MSG);
                            Log.e("Data","Size:"+value.getDataModelList().size());
                            GWDSUtils.updateGWDS(value.getDataModelList(),r, dateModel.getMonth(), dateModel.getYear());
                            refreshList();

                        }else{
                            ToastUtils.shortToast("No data found!");
                        }
                    }
                }));
    }
    @OnClick(R.id.btnPrevMonth)
    void clickOnPreviousMonth() {
        dateModel = prevDateModel;
        setMonthBtn(btnPrevMonth, btnNextMonth, txtCurrentMonth);
    }

    @OnClick(R.id.txtCurrentMonth)
    void clickOnCurrentMonth() {
        dateModel = currentDateModel;
        setMonthBtn(txtCurrentMonth, btnNextMonth, btnPrevMonth);
    }

    @OnClick(R.id.btnNextMonth)
    void clickOnNextMonth() {
        dateModel = nextDateModel;
        setMonthBtn(btnNextMonth, txtCurrentMonth, btnPrevMonth);
    }

    public void setDateModel(int month, int year) {
        dateModel = new DateModel(1,month,  year, 1, 1);
        currentDateModel = new DateModel(1,month,  year, 1, 1);
        prevDateModel = new DateModel(1,month,  year, 1, 1);
        nextDateModel = new DateModel(1,month,  year, 1, 1);

        if(month == 12){
            nextDateModel.setMonth(1);
            nextDateModel.setYear(year + 1);
            prevDateModel.setMonth(11);
        } else if(dateModel.getMonth() == 1){
            prevDateModel.setMonth(12);
            prevDateModel.setYear(year - 1);
            nextDateModel.setMonth(2);
        } else {
            prevDateModel.setMonth(month - 1);
            nextDateModel.setMonth(month + 1);
        }
    }

}
