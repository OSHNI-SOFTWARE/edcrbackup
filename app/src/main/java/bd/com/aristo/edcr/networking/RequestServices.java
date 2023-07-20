package bd.com.aristo.edcr.networking;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bd.com.aristo.edcr.listener.CheckMarketListener;
import bd.com.aristo.edcr.listener.DateValidationListener;
import bd.com.aristo.edcr.listener.JobsListener;
import bd.com.aristo.edcr.models.db.DANatureModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.NotificationModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.db.TokenModel;
import bd.com.aristo.edcr.models.response.LoginResponse;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.response.ResponseMaster;
import bd.com.aristo.edcr.models.db.TARateModel;
import bd.com.aristo.edcr.models.response.ResponseTADA;
import bd.com.aristo.edcr.models.response.VersionResponse;
import bd.com.aristo.edcr.modules.dcr.DCRSendModel;
import bd.com.aristo.edcr.modules.dcr.DCRSenderListener;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelServer;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModelServer;
import bd.com.aristo.edcr.modules.dcr.dcr.model.IPlanExeModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRListListener;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRListener;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRProductModel;
import bd.com.aristo.edcr.modules.dvr.DVRUtils;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.modules.dvr.model.GetDVR;
import bd.com.aristo.edcr.modules.dvr.model.GetDVRDoctor;
import bd.com.aristo.edcr.modules.gwds.GWDSUtils;
import bd.com.aristo.edcr.modules.gwds.GWDServerModel;
import bd.com.aristo.edcr.modules.pwds.PWDSUtils;
import bd.com.aristo.edcr.modules.pwds.PWDServerModel;
import bd.com.aristo.edcr.modules.reports.model.AbsentReport;
import bd.com.aristo.edcr.modules.reports.model.DDListener;
import bd.com.aristo.edcr.modules.reports.model.DoctorDCRResponse;
import bd.com.aristo.edcr.modules.reports.model.IDOTExecution;
import bd.com.aristo.edcr.modules.reports.ss.SSListener;
import bd.com.aristo.edcr.modules.reports.ss.model.ItemStatementModel;
import bd.com.aristo.edcr.modules.reports.ss.model.NewDoctorDCR;
import bd.com.aristo.edcr.modules.reports.ss.model.SSResponse;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementModel;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementResponse;
import bd.com.aristo.edcr.modules.tp.model.PlaceModel;
import bd.com.aristo.edcr.modules.tp.model.TPModel;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import static bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils.getToday;

/**
 * Created by monir.sobuj on 9/26/2018.
 */

public class RequestServices {

    String TAG = RequestServices.class.getSimpleName();
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    String[][] dcrSubType = {{"Regular", "Intern"}, {"New DOC", "Existing", "Intern",  "Other"}};
    String[] dcrType = {"DOT DCR", "New DCR"};
    public String[] days;

    public LoadingDialog loadingDialog;
    StringBuilder errorMsg, statusMsg;
    Context context;
    APIServices apiServices;
    DateModel dateModel;
    String userId;
    String marketCode;
    Realm r;
    boolean isDCRSuccess = false, isNewDCRSuccess = false, isNewDCRListSuccess = false;

    public void setErrorMsg(String msg){
        if(TextUtils.isEmpty(errorMsg)){
            errorMsg.append(msg);
        } else {
            errorMsg.append(", "+msg);
        }
    }

    public void setStatusMsg(String msg){
        if(TextUtils.isEmpty(statusMsg)){
            statusMsg.append(msg);
        } else {
            statusMsg.append(", "+msg);
        }
    }

    public void compareDate(Context context, APIServices apiServices, final long changeTPMillis, final DateValidationListener listener){

        mCompositeDisposable.add(apiServices.getServerMillis() //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                    @Override
                    public void onNext(String value) {
                        String regx = "[0-9]+";
                        if (!TextUtils.isEmpty(value) && value.matches(regx)) {
                            long serverMillis = Long.valueOf(value);
                            if(changeTPMillis > serverMillis){
                                listener.valid();
                            } else {
                                listener.invalid();
                            }
                        }else{
                            listener.onError();
                        }
                    }
                }));
    }

    public void checkTodayForDCR(Context context, APIServices apiServices, final DateValidationListener listener){

        mCompositeDisposable.add(apiServices.getServerMillis()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                    @Override
                    public void onNext(String value) {
                        String regx = "[0-9]+";
                        if (!TextUtils.isEmpty(value) && value.matches(regx)) {
                            long serverMillis = Long.parseLong(value);
                            if(DateTimeUtils.checkToday(serverMillis)){
                                listener.valid();
                            } else {
                                listener.invalid();
                            }
                        }else{
                            listener.onError();
                        }
                    }
                }));
    }

    public void getAppVersion(APIServices apiServices, final ResponseListener<VersionResponse> listener) {
        mCompositeDisposable.add(apiServices.getAppVersion("MPO") //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<VersionResponse>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed();
                    }

                    @Override
                    public void onNext(VersionResponse value) {
                        if (value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            listener.onSuccess(value);
                        } else {
                            listener.onFailed();
                        }
                    }
                }));
    }



    public void loginRequest(Context context, APIServices apiServices, String userId, String password, String deviceToken, final ResponseListener<LoginResponse> listener){
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.login(userId, password) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<LoginResponse>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }
                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        listener.onFailed();
                    }
                    @Override
                    public void onNext(LoginResponse value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            listener.onSuccess(value);
                        }else{
                            listener.onFailed();
                        }
                    }
                }));
    }

    public void changePasswordRequest(Context context, APIServices apiServices, String userId, String oldPassword, String newPassword, String deviceToken, final ResponseListener<LoginResponse> listener){
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();

        mCompositeDisposable.add(apiServices.changePassword(userId,oldPassword,newPassword, deviceToken)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<LoginResponse>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "OnComplete login");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError login: "+e.toString());
                        loadingDialog.dismiss();
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);

                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        loadingDialog.dismiss();
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            //displayAlert("Success", " Password Changed Successfully!");
                            listener.onSuccess(value);

                        }else{
                            ToastUtils.shortToast(StringConstants.PASSWORD_CHANGE_FAIL_MSG);
                            listener.onFailed();
                        }
                    }
                }));

    }

    public void syncMaster(Context context, APIServices apiServices, String marketCode, String userId, Realm r){
        errorMsg  = new StringBuilder();
        statusMsg  = new StringBuilder();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        this.context = context;
        this.apiServices = apiServices;
        this.userId = userId;
        this.marketCode = marketCode;
        this.r = r;
        dateModel = getToday();
        getTADA();
        //getDCRFromServer();
    }


    // get other info
    private void getTADA(){
        mCompositeDisposable.add(apiServices.getDANature(userId)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseTADA>() {
                    @Override
                    public void onComplete() {
                        getProduct();
                    }
                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("DA & TA");
                        getProduct();
                    }

                    @Override
                    public void onNext(final ResponseTADA value) {
                        if (value.getStatus()!=null && value.getStatus().equals("True")) {
                            try{
                                r.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm r) {
                                        if(r.where(DANatureModel.class).findAll() != null)
                                            r.where(DANatureModel.class).findAll().deleteAllFromRealm();
                                        //insert
                                        for(DANatureModel daNatureModel:value.getDaNatureModels()){
                                            daNatureModel.setEffectDate(daNatureModel.toDate(daNatureModel.getEffectFrom()));
                                            r.insertOrUpdate(daNatureModel);
                                        }

                                        if(r.where(TARateModel.class).findAll() != null)
                                            r.where(TARateModel.class).findAll().deleteAllFromRealm();

                                        //insert
                                        for(TARateModel taRateModel:value.getTaRateModels()){
                                            taRateModel.setEffectDate(taRateModel.toDate(taRateModel.getEffectFrom()));
                                            r.insertOrUpdate(taRateModel);
                                        }
                                    }
                                });
                            }catch (Exception e){
                                setStatusMsg("DA & TA");
                            }
                        }
                    }
                }));
    }

    private void getProduct(){
        mCompositeDisposable.add(apiServices.getProducts(userId)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<ProductModel>>() {
                    @Override
                    public void onComplete() {
                      getDoctors();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("products");
                        SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, false);
                        getDoctors();
                    }

                    @Override
                    public void onNext(final ResponseDetail<ProductModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equals("True")) {
                            SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, true);

                            try {
                                r.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm r) {
                                        //delete existing product
                                        r.where(ProductModel.class)
                                                .findAll()
                                                .deleteAllFromRealm();
                                        r.insertOrUpdate(value.getDataModelList());
                                    }
                                });

                            }catch (Exception e){
                                setErrorMsg("products");
                            }

                        } else {
                            SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, false);
                        }
                    }
                }));
    }

    private void getDoctors(){
        mCompositeDisposable.add(apiServices.getDoctors(userId, marketCode)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<DoctorsModel>>() {
                    @Override
                    public void onComplete() {
                        r.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                insertIntern(realm);
                            }
                        });
                        getDCRFromServer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("doctors");
                        getDCRFromServer();


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
                                setStatusMsg("doctors");

                            }
                        }
                    }
                }));
    }

    private void getDCRFromServer(){
        mCompositeDisposable.add(apiServices.getDCRFromServer(userId,
                DateTimeUtils.getMonthYear(dateModel.getMonth(), dateModel.getYear()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseMaster<DCRModelServer>>() {
                    @Override
                    public void onComplete() {
                        displayAlertForMaster();
                        getToken(apiServices, r, userId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_DCR_SYNCED_SUCCESS, false);
                        setErrorMsg("DCR");
                        displayAlertForMaster();
                        getToken(apiServices, r, userId);
                    }

                    @Override
                    public void onNext(final ResponseMaster<DCRModelServer> value) {
                        if (value.getStatus()!=null && value.getStatus().equals("True")) {

                            try{
                                r.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        for(DCRModelServer dcrModelServer:value.getDataModelList()) {
                                            if (dcrModelServer.getDcrType().equalsIgnoreCase("Dcr")) {
                                                if (!isExistDCR(r, dcrModelServer.getdID(), dcrModelServer.getCreateDate(), dcrModelServer.getShift())) {
                                                    DCRModel dcrModel = new DCRModel();
                                                    dcrModel.setId(getDCRId(r));
                                                    dcrModel.setCreateDate(dcrModelServer.getCreateDate());
                                                    dcrModel.setdID(dcrModelServer.getdID());
                                                    dcrModel.setSendDate(dcrModelServer.getCreateDate());
                                                    dcrModel.setShift(dcrModelServer.getShift());
                                                    dcrModel.setMonth(dateModel.getMonth());
                                                    dcrModel.setYear(dateModel.getYear());
                                                    dcrModel.setNew(false);
                                                    dcrModel.setSent(true);
                                                    dcrModel.setAccompanyID(dcrModelServer.getAccompanyID());
                                                    dcrModel.setRemarks(dcrModelServer.getRemarks());
                                                    dcrModel.setStatus(dcrModelServer.getStatus());
                                                    dcrModel.setStatusCause(dcrModelServer.getStatusCause());
                                                    r.insertOrUpdate(dcrModel);
                                                    for(DCRProductModelServer dcrProductModelServer:dcrModelServer.getProductModelServerList()){
                                                        DCRProductModel dcrProductModel = new DCRProductModel();
                                                        dcrProductModel.setId(getProductId(r));
                                                        dcrProductModel.setDcrID(dcrModel.getId());
                                                        dcrProductModel.setPlanned(true);
                                                        dcrProductModel.setProductID(dcrProductModelServer.getProductID());
                                                        dcrProductModel.setType(getType(dcrProductModelServer.getType()));
                                                        dcrProductModel.setQuantity(dcrProductModelServer.getQuantity());
                                                        r.insertOrUpdate(dcrProductModel);
                                                    }

                                                }

                                            } else {
                                                if (!isExistNewDCR(r, dcrModelServer.getdID(), dcrModelServer.getCreateDate(), dcrModelServer.getShift())) {
                                                    NewDCRModel newDCRModel = new NewDCRModel();
                                                    newDCRModel.setId(getNewDCRId(r));
                                                    newDCRModel.setDate(dcrModelServer.getCreateDate());
                                                    newDCRModel.setDoctorID(dcrModelServer.getdID());
                                                    newDCRModel.setDoctorName(getNewDCRDoctor(dcrModelServer.getdID()));
                                                    newDCRModel.setShift(dcrModelServer.getShift());
                                                    newDCRModel.setAccompanyId(dcrModelServer.getAccompanyID());
                                                    newDCRModel.setRemarks(dcrModelServer.getRemarks());
                                                    newDCRModel.setOption(getNewDCROption(dcrModelServer.getdID()));
                                                    newDCRModel.setSynced(true);
                                                    r.insertOrUpdate(newDCRModel);
                                                    for(DCRProductModelServer dcrProductModelServer:dcrModelServer.getProductModelServerList()){
                                                        NewDCRProductModel newDCRProductModel = new NewDCRProductModel();
                                                        newDCRProductModel.setId(getProductId(r));
                                                        newDCRProductModel.setNewDcrId(newDCRModel.getId());
                                                        newDCRProductModel.setProductID(dcrProductModelServer.getProductID());
                                                        newDCRProductModel.setFlag(getType(dcrProductModelServer.getType()));
                                                        newDCRProductModel.setCount(dcrProductModelServer.getQuantity());
                                                        r.insertOrUpdate(newDCRProductModel);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                });

                            }catch (Exception e){
                                ToastUtils.longToast("ERROR!!");
                                setStatusMsg("DCR");
                            }
                        }
                    }
                }));
    }

    




    public void syncTransaction(Context context, APIServices apiServices, DateModel dateModel, String userId, Realm r){
        errorMsg  = new StringBuilder();
        statusMsg  = new StringBuilder();
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");

        this.context = context;
        this.apiServices = apiServices;
        this.dateModel = dateModel;
        this.userId = userId;
        this.r = r;
        displayConfirmationPopup();
    }

    private void displayConfirmationPopup(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(StringConstants.TP_DVR_PWDS_GWDS_SYNCED_WITH_CHANGES);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

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

    private void getTpFromServer(){
        loadingDialog.show();
        TPServerModel tpServerModel = r.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_MONTH, dateModel.getMonth())
                .equalTo(TPServerModel.COL_YEAR, dateModel.getYear())
                .findFirst();
        if(null == tpServerModel){
            refreshDays();
        }

        mCompositeDisposable.add(apiServices.getTP(userId,
                String.valueOf(dateModel.getYear()),
                DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<TPModel>>() {
                    @Override
                    public void onNext(final ResponseDetail<TPModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm r) {
                                    if (value.getDataModelList() != null && value.getDataModelList().size() > 0) {
                                        for (TPModel tpListModel : value.getDataModelList()) {
                                            long tpId;
                                            TPServerModel tpServerModel = r.where(TPServerModel.class)
                                                    .equalTo(TPServerModel.COL_LOCAL_ID, Long.valueOf(tpListModel.getAnDetailSL()))
                                                    .findFirst();
                                            if (tpServerModel != null) {
                                                tpId = tpServerModel.getLocalId();
                                                RealmResults<TPPlaceRealmModel> tpPlaceRealmModels = r.where(TPPlaceRealmModel.class)
                                                        .equalTo(TPPlaceRealmModel.COL_TP_ID, tpId)
                                                        .findAll();
                                                if (tpPlaceRealmModels != null && tpPlaceRealmModels.size() > 0) {
                                                    tpPlaceRealmModels.deleteAllFromRealm();
                                                }
                                                tpServerModel.setServerId(tpListModel.getDetailSL());
                                                tpServerModel.setContactPlace(tpListModel.getMeetingPlace());
                                                tpServerModel.setDay(getDayWithoutZero(tpListModel.getDayNumber()));
                                                tpServerModel.setnDA(tpListModel.getAllowanceNature());
                                                tpServerModel.setReportTime(tpListModel.getTime());
                                                tpServerModel.setShiftType(tpListModel.getShiftType());
                                                tpServerModel.setShift(tpListModel.getShift());
                                                tpServerModel.setUploaded(true);
                                                if (tpListModel.getApprovalStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED)) {
                                                    tpServerModel.setApproved(true);
                                                } else {
                                                    tpServerModel.setApproved(false);
                                                }

                                                if (tpListModel.getChangeStatus().equalsIgnoreCase(StringConstants.YES)) {
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
                                                if (tpServerModel.getShiftType().equalsIgnoreCase(StringConstants.WORKING)) {
                                                    for (PlaceModel place : tpListModel.getSubDetailList()) {
                                                        Number currentIdNum = r.where(TPPlaceRealmModel.class).max(TPPlaceRealmModel.COL_ID);
                                                        int nextId;
                                                        if (currentIdNum == null) {
                                                            nextId = 1;
                                                        } else {
                                                            nextId = currentIdNum.intValue() + 1;
                                                        }
                                                        TPPlaceRealmModel tpPlaceRealmModel = new TPPlaceRealmModel();
                                                        tpPlaceRealmModel.setId(nextId);
                                                        tpPlaceRealmModel.setShift(tpListModel.getShift());
                                                        tpPlaceRealmModel.setCode(place.getInstitutionCode());
                                                        tpPlaceRealmModel.setTpId(tpId);
                                                        //Realm insert
                                                        r.insertOrUpdate(tpPlaceRealmModel);
                                                    }
                                                }
                                                r.insertOrUpdate(tpServerModel);
                                            } else {
                                                setStatusMsg("TP");
                                            }
                                        }
                                    } else {
                                            setStatusMsg("TP");
                                    }
                                }
                            });

                        } else{
                            setStatusMsg("TP");
                        }
                    }

                    @Override
                    public void onComplete() {
                        getDVRFromServer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("TP");
                        getDVRFromServer();
                    }

                }));

    }

    private void getDVRFromServer(){
        mCompositeDisposable.add(apiServices.getDVR(userId,
                String.valueOf(dateModel.getYear()),
                DateTimeUtils.getMonthNumber(dateModel.getMonth() ))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<GetDVR>>() {
                    @Override
                    public void onNext(final ResponseDetail<GetDVR> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            r.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    DVRUtils.deletePreviousMonthlyDVR(realm, dateModel.getMonth(), dateModel.getYear());
                                    if (value.getDataModelList() != null && value.getDataModelList().size() > 0) {
                                        for (GetDVR getDVR : value.getDataModelList()) {
                                            saveDVRFromServer(getDVR, realm);
                                        }
                                    } else {
                                        setStatusMsg("DVR");
                                    }
                                }
                            });

                        }else{
                            setStatusMsg("DVR");
                        }
                    }

                    @Override
                    public void onComplete() {
                        getPWDSFromServer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("DVR");
                        getPWDSFromServer();
                    }

                }));
    }


    private void getPWDSFromServer() {
        mCompositeDisposable.add(apiServices.getPWDS(
                userId,
                String.valueOf(dateModel.getYear()),
                DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseMaster<PWDServerModel>>() {
                    @Override
                    public void onNext(ResponseMaster<PWDServerModel> value) {
                        if (value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            PWDSUtils.updatePWDS(value.getDataModelList(), r, dateModel.getMonth(), dateModel.getYear());
                        } else {
                            setStatusMsg("PWDS");
                        }
                    }

                    @Override
                    public void onComplete() {
                        getGWDSFromServer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("PWDS");
                        getGWDSFromServer();
                    }

                }));
    }

    private void getGWDSFromServer(){
        mCompositeDisposable.add(apiServices.getGWDS(
                userId,
                String.valueOf(dateModel.getYear()),
                DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseMaster<GWDServerModel>>() {
                    @Override
                    public void onNext(ResponseMaster<GWDServerModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase("True")) {
                            GWDSUtils.updateGWDS(value.getDataModelList(),r, dateModel.getMonth(), dateModel.getYear());
                        } else{
                            setStatusMsg("GWDS");
                        }
                    }

                    @Override
                    public void onComplete() {
                        displayAlertForSync();
                        //getDCRFromServer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMsg("GWDS");
                        displayAlertForSync();
                        //getDCRFromServer();
                    }

                }));
    }


    private void displayAlertForMaster() {
        loadingDialog.dismiss();
        String title = "";
        String msg = "";
        if(TextUtils.isEmpty(errorMsg)){
            title = "Success";
            SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_SYNCED_SUCCESS, false);
            if(TextUtils.isEmpty(statusMsg)){
                msg = "Sync successfully done";
            } else {
                msg = statusMsg.toString() + " not found.";
            }

        } else {
            SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_SYNCED_SUCCESS, true);
            title = "Failed";
            if(TextUtils.isEmpty(statusMsg)){
                msg = "Sync failed - "+errorMsg.toString();
            } else {
                msg = "Sync failed - "+errorMsg.toString()+", "+statusMsg.toString();
            }

        }
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private  void displayAlertForSync() {
        loadingDialog.dismiss();
        String title = "";
        String msg = "";
        if(TextUtils.isEmpty(errorMsg)){
            title = "Success";
            if(TextUtils.isEmpty(statusMsg)){
                msg = "Sync successfully done";
            } else {
                msg = statusMsg.toString() + " not found.";
            }

        } else {
            title = "Failed";
            if(TextUtils.isEmpty(statusMsg)){
                msg = "Sync failed - "+errorMsg.toString();
            } else {
                msg = "Sync failed - "+errorMsg.toString()+", "+statusMsg.toString();
            }

        }
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private String getDayWithoutZero(String day){
        if(day.startsWith("0")){
            return day.substring(1);
        } else
            return day;
    }

    private void refreshDays() {
        days                                            = new String[35];
        for(int i = 0; i < 35; i++){
            days[i] = "";
        }
        Calendar cal = Calendar.getInstance();
        cal.set(dateModel.getYear(), dateModel.getMonth() - 1, dateModel.getDay());
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);


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

        for(int i = 0; i < 35; i++){
            if(!days[i].equals("")){
                initializeTP(i, days[i], true, getTpId(days[i], true));
                initializeTP(i, days[i], false, getTpId(days[i], false));
            }
        }

    }

    private long getTpId(String day, boolean isMorning){
        //id with year,month,day,0/1(shift)
        return Long.valueOf(dateModel.getYear()+DateTimeUtils.getMonthNumber(dateModel.getMonth())+day+ (isMorning?"0":"1"));
    }

    //initialize shift tp
    private void initializeTP(final int cell, final String day, final boolean isMorning, final long id){
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
                tpServerModel.setYear(dateModel.getYear());
                tpServerModel.setMonth(dateModel.getMonth());
                tpServerModel.setApproved(false);
                tpServerModel.setChanged(true);
                r.insertOrUpdate(tpServerModel);
            }
        });


    }


    public void getPlanVsExe(Context context, APIServices apiServices, String userId, String monthYear, final ResponseListener<IPlanExeModel> listener){
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.getPlanVsExe(userId, monthYear) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<IPlanExeModel>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }
                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        listener.onFailed();
                    }
                    @Override
                    public void onNext(ResponseDetail<IPlanExeModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            listener.onSuccess(value.getDataModelList());
                        }else{
                            listener.onFailed();
                        }
                    }
                }));
    }

    private long getDCRId(Realm r){
        Number currentIdNum = r.where(DCRModel.class).max(DCRModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    private long getNewDCRId(Realm r){
        Number currentIdNum = r.where(NewDCRModel.class).max(NewDCRModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    private long getProductId(Realm r){
        Number currentIdNum = r.where(DCRProductModel.class).max(DCRProductModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    private boolean isExistDCR(Realm r, String docId, String date, String shift){
        DCRModel dcrModel = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_SEND_DATE, date)
                .equalTo(DCRModel.COL_DID, docId)
                .equalTo(DCRModel.COL_SHIFT, shift)
                .findFirst();
        return dcrModel != null;
    }

    private boolean isExistNewDCR(Realm r, String docId, String date, String shift){
        NewDCRModel dcrModel = r.where(NewDCRModel.class)
                .equalTo(NewDCRModel.COL_DATE, date)
                .equalTo(NewDCRModel.COL_DOCTOR_ID, docId)
                .equalTo(NewDCRModel.COL_SHIFT, shift)
                .findFirst();
        if(dcrModel != null){
            return true;
        }
        return false;
    }

    private int getType(String code){
        if(code.equalsIgnoreCase("Sm")){
            return 1;
        } else if(code.equalsIgnoreCase("Sl")){
            return 0;
        } else return 2;

    }


    public void getNotifications(Context context, final Realm r, APIServices apiServices, String userId, final ResponseListener<NotificationModel> listener){

        mCompositeDisposable.add(apiServices.getNotification(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseDetail<NotificationModel>>() {
                    @Override
                    public void onNext(final ResponseDetail<NotificationModel> response) {
                        if (response.getStatus() != null && response.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            if (response.getDataModelList().size() > 0){
                                r.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        for(NotificationModel notificationModel:response.getDataModelList()){
                                            NotificationModel notificationModel1 = realm.where(NotificationModel.class)
                                                    .equalTo("nID", notificationModel.getnID())
                                                    .findFirst();
                                            if(notificationModel1 != null){
                                                notificationModel.setRead(notificationModel1.isRead());
                                            }
                                            if(isLessThanTenDays(notificationModel.getDateTime())){
                                                realm.insertOrUpdate(notificationModel);
                                            } else if(notificationModel1 != null){
                                                notificationModel1.deleteFromRealm();
                                            }

                                        }


                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        listener.onFailed();

                    }

                    @Override
                    public void onComplete() {
                        listener.onSuccess();
                    }
                }));

    }

    public void postNotification(APIServices apiServices, String userId, String operationType, String title, String msg, int year, int month, String locCode){

        mCompositeDisposable.add(apiServices.postNotification(userId, operationType, title, msg, String.valueOf(year), DateTimeUtils.getMonthNumber(month), locCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onNext(final ResponseDetail<String> response) {
                        if (response.getStatus() != null && response.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            Log.d(TAG, "onNext: post notification success");
                            } else {
                            Log.d(TAG, "onNext: post notification failed");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onNext: post notification failed");


                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }

    public boolean isLessThanTenDays(String notificationModel){
        long cMilis = System.currentTimeMillis() - 11 * 24 * 60 * 60 * 1000;
        Date cDate = new Date(cMilis);
        Date date1 = new Date();
        try {
            date1=new SimpleDateFormat("dd-MM-yyyy").parse(notificationModel.substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.after(cDate);

    }

    public void saveDVRFromServer(GetDVR getDVR, Realm r){
        long dvrId = getDVRLocalID(getDVR);
        DVRForServer dvrForServer = new DVRForServer();
        dvrForServer.setId(dvrId);
        dvrForServer.setServerId(getDVR.getServerId());
        dvrForServer.setDay(Integer.valueOf(getDVR.getDay()));
        dvrForServer.setInitialize(true);
        dvrForServer.setMonth(Integer.valueOf(getDVR.getMonthName()));
        dvrForServer.setMorning(getDVR.getShift().equalsIgnoreCase(StringConstants.MORNING));
        dvrForServer.setApproved(getDVR.getStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED));
        dvrForServer.setYear(Integer.valueOf(getDVR.getYear()));
        r.insertOrUpdate(dvrForServer);
        setDVRDoctors(r, getDVR, dvrId);
    }

    public boolean isDoctorExist(Realm realm, String docId, int year, int month){
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
        int month = Integer.parseInt(getDVR.getMonthName());
        int year = Integer.parseInt(getDVR.getYear());
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
            if(isDoctorExist(r, dvr, year, month)){
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
            if (isDoctorExist(r, dvr, year, month)){
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

public long getDVRLocalID(GetDVR getDVR){

        String id = getDVR.getYear()+getDVR.getMonthName()+getDVR.getDay()+(getDVR.getShift().equalsIgnoreCase(StringConstants.MORNING)? "0":"1");
        return Long.valueOf(id);
    }


    public void getProductAfterDCR(final Context context, APIServices apiServices, String userId,
                                   final Realm r){
        final DateModel dateModel1 = getToday();
        mCompositeDisposable.add(apiServices.getProductsBeforeDCR(userId, DateTimeUtils.getMonthYear(dateModel1.getMonth(), dateModel1.getYear()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<ProductModel>>() {
                    @Override
                    public void onComplete() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, false);
                    }
                    @Override
                    public void onNext(final ResponseDetail<ProductModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equals("True")) {
                            SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, true);
                            try {
                                r.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.where(ProductModel.class)
                                                .equalTo(ProductModel.COL_YEAR, dateModel1.getYear())
                                                .equalTo(ProductModel.COL_MONTH, dateModel1.getMonth())
                                                .findAll()
                                                .deleteAllFromRealm();
                                        realm.insertOrUpdate(value.getDataModelList());
                                    }
                                });
                            }catch (Exception e){
                                Log.d(TAG, "onNext: "+e);
                            }
                        } else {
                            SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, false);
                        }
                    }
                }));
    }

    public void syncProductJob(final Context context, APIServices apiServices, String userId,
                               final Realm r, final JobsListener listener, final JobParameters params){

        if(!SharedPrefsUtils.getBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, false)) {
            final DateModel dateModel1 = getToday();
            mCompositeDisposable.add(apiServices.getProductsBeforeDCR(userId, DateTimeUtils.getMonthYear(dateModel1.getMonth(), dateModel1.getYear()))
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDetail<ProductModel>>() {
                        @Override
                        public void onComplete() {
                            listener.onFinish(params);
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onFinish(params);
                        }

                        @Override
                        public void onNext(final ResponseDetail<ProductModel> value) {
                            if (value.getStatus() != null && value.getStatus().equals("True")) {
                                try {
                                    r.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.where(ProductModel.class)
                                                    .equalTo(ProductModel.COL_YEAR, dateModel1.getYear())
                                                    .equalTo(ProductModel.COL_MONTH, dateModel1.getMonth())
                                                    .findAll()
                                                    .deleteAllFromRealm();
                                            realm.insertOrUpdate(value.getDataModelList());
                                            SharedPrefsUtils.setBooleanPreference(context, StringConstants.IS_PRODUCT_SYNCED, true);
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.d(TAG, "onNext: " + e);
                                }
                            }
                        }
                    }));
        } else {
            listener.onFinish(params);
        }
    }


    public void insertIntern(Realm realm1){
        int cMonth, pMonth, nMonth, cYear, pYear, nYear;
        cMonth = dateModel.getMonth();
        cYear = dateModel.getYear();
        if(cMonth == 12){
            nMonth = 1;
            nYear = dateModel.getYear() + 1;
        } else {
            nMonth = cMonth + 1;
            nYear = dateModel.getYear();
        }

        if(cMonth == 1){
            pMonth = 12;
            pYear = dateModel.getYear() - 1;
        } else {
            pMonth = cMonth - 1;
            pYear = dateModel.getYear();
        }


        insertInternIndividual(realm1, "OTHER", cMonth, cYear);
        insertInternIndividual(realm1, "OTHER", nMonth, nYear);
        insertInternIndividual(realm1, "OTHER", pMonth, pYear);


        insertInternIndividual(realm1, "DUTY", cMonth, cYear);
        insertInternIndividual(realm1, "DUTY", nMonth, nYear);
        insertInternIndividual(realm1, "DUTY", pMonth, pYear);


        insertInternIndividual(realm1, "DMF", cMonth, cYear);
        insertInternIndividual(realm1, "DMF", nMonth, nYear);
        insertInternIndividual(realm1, "DMF", pMonth, pYear);

        insertInternIndividual(realm1, "CARD", cMonth, cYear);
        insertInternIndividual(realm1, "CARD", nMonth, nYear);
        insertInternIndividual(realm1, "CARD", pMonth, pYear);

        insertInternIndividual(realm1, "PEAD", cMonth, cYear);
        insertInternIndividual(realm1, "PEAD", nMonth, nYear);
        insertInternIndividual(realm1, "PEAD", pMonth, pYear);

        insertInternIndividual(realm1, "SURG", cMonth, cYear);
        insertInternIndividual(realm1, "SURG", nMonth, nYear);
        insertInternIndividual(realm1,  "SURG", pMonth, pYear);

        insertInternIndividual(realm1, "GYN", cMonth, cYear);
        insertInternIndividual(realm1, "GYN", nMonth, nYear);
        insertInternIndividual(realm1, "GYN", pMonth, pYear);

        insertInternIndividual(realm1, "MED", cMonth, cYear);
        insertInternIndividual(realm1, "MED", nMonth, nYear);
        insertInternIndividual(realm1, "MED", pMonth, pYear);


    }

    private void insertInternIndividual(Realm r1, String id, int month, int year){

        DoctorsModel dm = new DoctorsModel();
        dm.setEveningLoc("Intern");
        dm.setMorningLoc("Intern");
        dm.setAddress("");
        dm.setDegree("");
        dm.setId("I-" + id + "-" + userId);
        dm.setMonth(month);
        dm.setYear(year);
        dm.setName("Intern "+id);
        dm.setSpecial(id);
        r1.insertOrUpdate(dm);

    }

    private int getNewDCROption(String docID){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(docID);
        if(docID.contains("I-")){
            return 2;
        } else if(docID.contains("other")){
            return 3;
        }

        if(matcher.find()){
            return 1;
        } else {
            return 0;
        }
    }

    private String getNewDCRDoctor(String docID){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(docID);
        if(docID.contains("intern")){
            return "Intern";
        } else if(docID.contains("others")){
            return "Other";
        }

        if(matcher.find()){
            DoctorsModel dm = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, docID).findFirst();
            if(dm != null){
                return dm.getName();
            } else {
                return docID;
            }
        } else {
            return docID;
        }
    }
//46686 //[{"ProductCode":"SmR6400000128","Quantity":"0"},{"ProductCode":"SmR6400000004","Quantity":"0"},{"ProductCode":"PPM","Quantity":"1"}]
    public void uploadDCR(Context context, final DCRSendModel dcrSendModel, APIServices apiServices, final Realm r, final DCRSenderListener listener){
        isDCRSuccess = false;
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(dcrSendModel.getSampleList()).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("DetailList", jsonArray);
        loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.postDemoDCR(
                dcrSendModel.getUserId(),
                TextUtils.isEmpty(dcrSendModel.getAccompanyIds())?"":dcrSendModel.getAccompanyIds(),
                dcrSendModel.getCreateDate(),
                dcrSendModel.getShift(),
                dcrSendModel.getDoctorId(),
                dcrSendModel.getStatus(),
                dcrSendModel.getStatusCause(),
                dcrSendModel.getRemarks(),
                dcrType[0],
                dcrSubType[0][dcrSendModel.getDcrSubType()],
                jsonObject.toString())
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                        if(isDCRSuccess){
                            listener.onSuccess(dcrSendModel.getRemarks());
                        } else {
                            listener.onError(dcrSendModel.getRemarks());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        listener.onError(dcrSendModel.getRemarks());

                    }

                    @Override
                    public void onNext(ResponseDetail<String> value) {
                        if(value != null && value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            isDCRSuccess = true;
                        } else
                        {
                            isDCRSuccess = false;
                        }
                    }
                }));

    }


    public void  uploadNewDcr(Context context, final DCRSendModel dcrSendModel, APIServices apiServices, final Realm r, final NewDCRListener listener){
        isNewDCRSuccess = false;
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(dcrSendModel.getSampleList()).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("DetailList", jsonArray);
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.postRawDCR(
                dcrSendModel.getUserId(),
                TextUtils.isEmpty(dcrSendModel.getAccompanyIds()) ? "" : dcrSendModel.getAccompanyIds(),
                dcrSendModel.getCreateDate(),
                dcrSendModel.getShift(),
                dcrSendModel.getDoctorId(),  // docId, new doc name, "I-type-userID", "other".
                dcrSendModel.getRemarks(),
                dcrSendModel.getWard(),  // degree for existing, degree for new, ward for intern
                dcrSendModel.getTeamLeader(), //only for intern names
                dcrSendModel.getContactNo(), // address for existing and new, contact no for intern
                dcrSendModel.getTeamVolume(), // "0" for existing and new, team volume for intern
                dcrType[1],
                dcrSubType[1][dcrSendModel.getDcrSubType()],
                jsonObject.toString())
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                        if(isNewDCRSuccess){
                            listener.onSuccess();
                        } else {
                            listener.onFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDetail<String> value) {
                        if (value.getStatus() != null && !TextUtils.isEmpty(value.getStatus()) && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            isNewDCRSuccess = true;
                        }

                    }
                }));
    }

    public void  uploadNewDcrFromList(Context context, final DCRSendModel dcrSendModel, APIServices apiServices, final Realm r, final NewDCRListListener listener, final NewDCRModel newDCRModel){
        isNewDCRListSuccess = false;
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(dcrSendModel.getSampleList()).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("DetailList", jsonArray);
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.postRawDCR(
                dcrSendModel.getUserId(),
                TextUtils.isEmpty(dcrSendModel.getAccompanyIds()) ? "" : dcrSendModel.getAccompanyIds(),
                dcrSendModel.getCreateDate(),
                dcrSendModel.getShift(),
                dcrSendModel.getDoctorId(),  // docId, new doc name, "intern".
                dcrSendModel.getRemarks(),
                dcrSendModel.getWard(),  // degree for existing, degree for new, ward for intern
                dcrSendModel.getTeamLeader(), //only for intern names
                dcrSendModel.getContactNo(), // address for existing and new, contact no for intern
                dcrSendModel.getTeamVolume(), // "0" for existing and new, team volume for intern
                dcrType[1],
                dcrSubType[1][dcrSendModel.getDcrSubType()],
                jsonObject.toString())
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                        if(isNewDCRListSuccess){
                            listener.onSuccess(dcrSendModel.getRemarks(), newDCRModel);
                        } else {
                            listener.onFailed(dcrSendModel.getRemarks());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed(dcrSendModel.getRemarks());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDetail<String> value) {
                        if (value.getStatus() != null && !TextUtils.isEmpty(value.getStatus()) && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            isNewDCRListSuccess = true;
                        }
                    }
                }));
    }


    public void getToken(APIServices apiServices, Realm r, String userId){

        mCompositeDisposable.add(apiServices.getToken(userId, "MPO")
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<TokenModel>>() {
                    @Override
                    public void onComplete() {
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(ResponseDetail<TokenModel> value) {
                        if(value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                            r.executeTransaction(realm -> {
                                RealmResults<TokenModel> tokenModels = r.where(TokenModel.class).findAll();
                                if(tokenModels != null){
                                    tokenModels.deleteAllFromRealm();
                                }
                                for(TokenModel tokenModel:value.getDataModelList()){
                                    if(!TextUtils.isEmpty(tokenModel.getToken())){
                                        r.insertOrUpdate(tokenModel);
                                    }
                                }
                            });

                        }
                    }
                }));
    }

    public void getSampleStatement(Context context, APIServices apiServices, String userId, int month, int year1, SSListener listener) {
        SSResponse ssResponse = SSResponse.getInstance();
        if(ssResponse.itemStatementModelList != null)
            ssResponse.itemStatementModelList.clear();
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        String year = String.valueOf(year1);
        String monthNumber = DateTimeUtils.getMonthNumber(month);

        Observable obs1 = apiServices.getItemStatement(userId, year, monthNumber, StringConstants.GIFT_ITEM).subscribeOn(Schedulers.newThread());
        Observable obs2 = apiServices.getItemStatement(userId, year, monthNumber, StringConstants.SAMPLE_ITEM).subscribeOn(Schedulers.newThread());
        Observable obs3 = apiServices.getItemStatement(userId, year, monthNumber, StringConstants.SELECTED_ITEM).subscribeOn(Schedulers.newThread());

        mCompositeDisposable.add((Disposable) Observable.zip(obs1, obs2, obs3, mergeData())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SSResponse>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        loadingDialog.dismiss();
                        listener.onError();

                    }

                    @Override
                    public void onNext(SSResponse ssResponse) {
                        if (null != ssResponse) {
                            listener.getSS(ssResponse);
                        } else {
                            ToastUtils.longToast("Try again!!");
                            listener.onError();
                        }
                        loadingDialog.dismiss();
                    }
                }));
    }


    private Function3<ResponseDetail<ItemStatementModel>, ResponseDetail<ItemStatementModel>, ResponseDetail<ItemStatementModel>, SSResponse> mergeData() {
        return new Function3<ResponseDetail<ItemStatementModel>, ResponseDetail<ItemStatementModel>, ResponseDetail<ItemStatementModel>, SSResponse>() {
            @Override
            public SSResponse apply(ResponseDetail<ItemStatementModel> giftList, ResponseDetail<ItemStatementModel> sampleList, ResponseDetail<ItemStatementModel> selectedList) throws Exception {
                SSResponse ssResponse = SSResponse.getInstance();
                if (giftList != null && giftList.getStatus() != null && giftList.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS) && giftList.getDataModelList() != null && giftList.getDataModelList().size() > 0)
                    ssResponse.addModels(giftList.getDataModelList());
                if (sampleList != null && sampleList.getStatus() != null && sampleList.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS) && sampleList.getDataModelList() != null && sampleList.getDataModelList().size() > 0)
                    ssResponse.addModels(sampleList.getDataModelList());
                if (selectedList != null && selectedList.getStatus() != null && selectedList.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS) && selectedList.getDataModelList() != null && selectedList.getDataModelList().size() > 0)
                    ssResponse.addModels(selectedList.getDataModelList());
                return ssResponse;
            }
        };

    }
    boolean failedStatus;
    public void getDetailSampleStatement(Context context, APIServices apiServices, String userId, int month, int year1, SSListener listener){
        failedStatus = true;
        SampleStatementResponse ssResponse = SampleStatementResponse.getInstance();
        if(ssResponse.sampleStatementModelList != null)
            ssResponse.sampleStatementModelList.clear();
        String year = String.valueOf(year1);
        String monthNumber = DateTimeUtils.getMonthNumber(month);
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.getSampleStatement(userId, year, monthNumber)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<SampleStatementModel>>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                        if(failedStatus){
                           ToastUtils.longToast("Sync failed!! Try Again");
                           listener.onError();
                        } else {
                            listener.getSamples(ssResponse);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }
                    @Override
                    public void onNext(final ResponseDetail<SampleStatementModel> value) {
                        if (value.getStatus()!=null && value.getStatus().equals("True")) {
                            if(value.getDataModelList() != null && value.getDataModelList().size() > 0){
                                ssResponse.addModels(value.getDataModelList());
                                failedStatus = false;
                            }
                        }
                    }
                }));
    }


    public void getDoctorDCRReport(Context context, APIServices apiServices, String userId, int day, int month, int year1, DDListener listener) {

        String endDate = DateTimeUtils.getMonthNumber(day) + "-"
                + DateTimeUtils.getMonthNumber(month) + "-"
                + year1;
        String startDate = "01" + "-"
                + DateTimeUtils.getMonthNumber(month) + "-"
                + year1;
        DoctorDCRResponse ddResponse = DoctorDCRResponse.getInstance();
        ddResponse.setAbsentReportList(new ArrayList<>());
        ddResponse.setIdotExecutionList(new ArrayList<>());
        ddResponse.setNewDoctorDCRList(new ArrayList<>());
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        String year = String.valueOf(year1);
        String monthNumber = DateTimeUtils.getMonthNumber(month);

        Observable obs1 = apiServices.getDCRAbsentReport(userId, "MPO", startDate, endDate).subscribeOn(Schedulers.newThread());
        Observable obs2 = apiServices.getDOTReport(userId, year, monthNumber).subscribeOn(Schedulers.newThread());
        Observable obs3 = apiServices.getNewDoctorDCR(userId, year, monthNumber).subscribeOn(Schedulers.newThread());

        mCompositeDisposable.add((Disposable) Observable.zip(obs1, obs2, obs3, mergeDCRData())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<DoctorDCRResponse>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        loadingDialog.dismiss();
                        listener.onError();
                    }

                    @Override
                    public void onNext(DoctorDCRResponse ddResponse) {
                        if (null != ddResponse) {
                            listener.getSS(ddResponse);
                        } else {
                            ToastUtils.longToast("Try again!!");
                            listener.onError();
                        }
                        loadingDialog.dismiss();
                    }
                }));
    }


    private Function3<ResponseDetail<AbsentReport>, ResponseDetail<IDOTExecution>, ResponseDetail<NewDoctorDCR>, DoctorDCRResponse> mergeDCRData() {
        return (absentList, dotList, newDoctorDCRList) -> {
            DoctorDCRResponse ddResponse = DoctorDCRResponse.getInstance();
            if (absentList != null && absentList.getStatus() != null && absentList.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS) && absentList.getDataModelList() != null && absentList.getDataModelList().size() > 0)
                ddResponse.setAbsentReportList(absentList.getDataModelList());
            if (dotList != null && dotList.getStatus() != null && dotList.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS) && dotList.getDataModelList() != null && dotList.getDataModelList().size() > 0)
                ddResponse.setIdotExecutionList(dotList.getDataModelList());
            if (newDoctorDCRList != null && newDoctorDCRList.getStatus() != null && newDoctorDCRList.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS) && newDoctorDCRList.getDataModelList() != null && newDoctorDCRList.getDataModelList().size() > 0)
                ddResponse.setNewDoctorDCRList(newDoctorDCRList.getDataModelList());
            return ddResponse;
        };

    }

    //CheckUserMarket
    public void checkUserMarket(Context context, APIServices apiServices, String empCode, String marketCode, String userID, String token, CheckMarketListener listener) {
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable.add(apiServices.checkEmpMarket(empCode, userID, marketCode, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean s) {
                        listener.onCheckMarket(s);
                    }

                    @Override
                    public void onComplete() {
                        SharedPrefsUtils.setStringPreference(context, StringConstants.PREF_CHECKED_MARKET_DATE, DateTimeUtils.getToday(DateTimeUtils.FORMAT9));
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        listener.onError(e.getMessage());
                    }
                }));
    }






}
