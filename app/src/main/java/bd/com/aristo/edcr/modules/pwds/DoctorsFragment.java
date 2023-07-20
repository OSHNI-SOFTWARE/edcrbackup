package bd.com.aristo.edcr.modules.pwds;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class DoctorsFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    List<DoctorsModel> doctorsModels;
    @Inject
    Realm r;

    @Inject
    APIServices apiServices;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    @BindView(R.id.cardBottom)
    CardView cardBottom;

    @BindView(R.id.llNext)
    LinearLayout llNext;
    @BindView(R.id.llPrev)
    LinearLayout llPrev;

    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;
    Context context;

    public UserModel userModel;
    public DateModel dateModel;
    public DateModel currentDateModel;
    boolean isPrevMonth = false;
    public PWDSUtilsModel pwdsUtilsModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    List<PWDSProductsModel> pwdsProducts;
    List<TempPWDS> tempPWDSList = new ArrayList<>();
    boolean isChanged = false;
    boolean isApproved = false;

    public DoctorsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor, container, false);
        ButterKnife.bind(this, rootView);
        pwdsUtilsModel = (PWDSUtilsModel) getArguments().getSerializable("pwdsUtils");
        dateModel = (DateModel) getArguments().getSerializable("dateModel");
        currentDateModel = DCRUtils.getToday();

        getUserInfo();
        isPrevMonth = isPreviousMonth();
        isApproved = getApprovalStatus();

        setupList();
        setTitle();

        return rootView;
    }


    public void setTitle(){
        //isApproved = getApprovalStatus();
        ((Activity) context).setTitle( "PWDS for "+ pwdsUtilsModel.getName() + "("+pwdsUtilsModel.getPlannedCount() + ")" );
    }


    private void setupList() {

        doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, dateModel.getMonth())
                .equalTo(DoctorsModel.COL_YEAR, dateModel.getYear())
                .findAll();
        if(doctorsModels != null && doctorsModels.size() > 0) { //products available, load list
            refreshList();
        }
    }


    @OnClick(R.id.llSecond)
    void clickOnSaveOption(){
        if(isChanged) {

            getEligiblePolicy(DateTimeUtils.getMonthYear(dateModel.getMonth(), dateModel.getYear()));
        } else {
            ToastUtils.shortToast("No Change Found!");
        }
    }




    private List<PWDSDoctorsModel> unique(List<PWDSDoctorsModel> list) {


        List<PWDSDoctorsModel> uniqueList = new ArrayList<>();
        Set<PWDSDoctorsModel> uniqueSet = new HashSet<>();
        for (PWDSDoctorsModel obj : list) {
            if (uniqueSet.add(obj)) {
                uniqueList.add(obj);
            }
        }
        tempPWDSList.clear();

        for(PWDSDoctorsModel pwdsDoctorsModel:uniqueList){
            if(pwdsDoctorsModel.isPwds()){
                PWDSModel pwds = r.where(PWDSModel.class)
                        .equalTo(PWDSModel.COL_DOCTOR_ID, pwdsDoctorsModel.getId())
                        .equalTo(PWDSModel.COL_PRODUCT_ID, pwdsUtilsModel.getCode())
                        .equalTo(PWDSModel.COL_YEAR, dateModel.getYear())
                        .equalTo(PWDSModel.COL_MONTH, dateModel.getMonth())
                        .findFirst();
                if(pwds != null){
                    TempPWDS tempPWDS = new TempPWDS();
                    tempPWDS.setApproved(pwds.isApproved());

                    tempPWDS.setDoctorID(pwds.getDoctorID());
                    tempPWDS.setMonth(pwds.getMonth());
                    tempPWDS.setProductID(pwds.getProductID());
                    tempPWDS.setYear(pwds.getYear());
                    tempPWDSList.add(tempPWDS);
                }
            }
        }
        return uniqueList;
    }
    public void refreshList(){
        isChanged = false;
        pwdsProducts = PWDSUtils.getPwdsProducts(r, dateModel.getMonth(), dateModel.getYear());
        if(pwdsUtilsModel.getPosition() == 0){
            llPrev.setVisibility(View.INVISIBLE);
        } else if(pwdsUtilsModel.getPosition() == pwdsProducts.size() - 1){
            llNext.setVisibility(View.INVISIBLE);
        }
        List<PWDSDoctorsModel> pwdsDoctors = PWDSUtils.getPwdsDoctors(doctorsModels, r, pwdsUtilsModel.getCode(), dateModel.getMonth(), dateModel.getYear());
        final FastItemAdapter<PWDSDoctorsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(unique(pwdsDoctors));
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<PWDSDoctorsModel>() {
            @Override
            public boolean onClick(View v, IAdapter<PWDSDoctorsModel> adapter, final PWDSDoctorsModel item, int position) {
                if (!isPreviousMonth()) {  //&&dateModel.getDay() < 8   for
                    isChanged = true;
                    if(item.isPwds()){
                        removeFromTemp(item.getId());
                        item.setPwds(false);
                        pwdsUtilsModel.setPlannedCount(pwdsUtilsModel.getPlannedCount() - 1);
                    } else {
                        addToTemp(item.getId());
                        item.setPwds(true);
                        pwdsUtilsModel.setPlannedCount(pwdsUtilsModel.getPlannedCount() + 1);
                    }
                    setTitle();
                    adapter.getFastAdapter().notifyDataSetChanged();
                }
                    return false;
                }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<PWDSDoctorsModel>() {
            @Override
            public boolean filter(PWDSDoctorsModel item, CharSequence constraint) {
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

    public void saveDoctorsSelection(final boolean isFromNext){
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                RealmResults<PWDSModel> pwdsModelList = r.where(PWDSModel.class)
                        .equalTo(PWDSModel.COL_PRODUCT_ID, pwdsUtilsModel.getCode())
                        .equalTo(PWDSModel.COL_YEAR, dateModel.getYear())
                        .equalTo(PWDSModel.COL_MONTH, dateModel.getMonth())
                        .findAll();
                if(pwdsModelList != null && pwdsModelList.size() > 0){
                    pwdsModelList.deleteAllFromRealm();
                }
                long prevId = 0;
                if (r.where(PWDSModel.class).max(PWDSModel.COL_ID) != null) {
                    prevId = r.where(PWDSModel.class).max(PWDSModel.COL_ID).intValue() + 1;
                    Log.e(TAG, "3 value prevId:" + prevId);
                }
                for(TempPWDS tempPWDS:tempPWDSList){
                    PWDSModel pwdsModel = new PWDSModel();
                    pwdsModel.setApproved(tempPWDS.isApproved());
                    pwdsModel.setDoctorID(tempPWDS.getDoctorID());
                    pwdsModel.setMonth(tempPWDS.getMonth());
                    pwdsModel.setProductID(tempPWDS.getProductID());
                    pwdsModel.setYear(tempPWDS.getYear());
                    pwdsModel.setId(prevId);
                    pwdsModel.setUploaded(false);
                    r.insertOrUpdate(pwdsModel);
                    prevId++;
                }
                if(!isFromNext){
                    ((Activity) context).onBackPressed();
                }
            }
        });
    }

    public void onCancel(){
    }

    @Override
    public void onPause() {
        super.onPause();
        onCancel();
    }

    public void removeFromTemp(String docId){
        TempPWDS removePWDS = new TempPWDS();
        for(TempPWDS tempPWDS:tempPWDSList){
            if(tempPWDS.getDoctorID().equalsIgnoreCase(docId)){
                removePWDS = tempPWDS;
                break;
            }
        }
        tempPWDSList.remove(removePWDS);
    }

    public void addToTemp(String docId){
        TempPWDS tempPWDS = new TempPWDS();
        tempPWDS.setApproved(false);
        tempPWDS.setDeleted(false);
        tempPWDS.setDoctorID(docId);
        tempPWDS.setMonth(dateModel.getMonth());
        tempPWDS.setProductID(pwdsUtilsModel.getCode());
        tempPWDS.setYear(dateModel.getYear());
        tempPWDSList.add(tempPWDS);
    }


    @OnClick(R.id.llPrev)
    void clickOnPrev(){
        if(isChanged && !isApproved){
            showChangeAlert("previous product", false);
        } else {
            goToPrev();
        }


    }

    @OnClick(R.id.llNext)
    void clickOnNext(){
        if(isChanged && !isApproved){
            showChangeAlert("next product", true);
        } else {
            goToNext();
        }

    }

    public void goToNext(){
        int pos = pwdsUtilsModel.getPosition() + 1;
        pwdsUtilsModel.setPosition(pos);
        if(pwdsUtilsModel.getPosition() == 0){
            llPrev.setVisibility(View.INVISIBLE);
        } else if(pwdsUtilsModel.getPosition() == pwdsProducts.size() - 1){
            llNext.setVisibility(View.INVISIBLE);
        } else if(pos >= 0 && pos < pwdsProducts.size() - 1){
            llPrev.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        }


        if(pos >= 0 && pos < pwdsProducts.size()) {
            PWDSProductsModel item = pwdsProducts.get(pos);
            pwdsUtilsModel.setCode(item.getCode());
            pwdsUtilsModel.setName(item.getName());
            pwdsUtilsModel.setPlannedCount(item.getPlanned());
            refreshList();
            setTitle();
        }
    }

    public void goToPrev(){
        int pos = pwdsUtilsModel.getPosition() - 1;//--TempData.SELECTED_OR_GIFT_POS;
        pwdsUtilsModel.setPosition(pos);
        if(pwdsUtilsModel.getPosition() == 0){
            llPrev.setVisibility(View.INVISIBLE);
        } else if(pwdsUtilsModel.getPosition() == pwdsProducts.size() - 1){
            llNext.setVisibility(View.INVISIBLE);
        } else if(pos >= 0 && pos < pwdsProducts.size() - 1){
            llPrev.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        }


        if(pos >= 0 && pos < pwdsProducts.size()) {
            PWDSProductsModel item = pwdsProducts.get(pos);
            pwdsUtilsModel.setCode(item.getCode());
            pwdsUtilsModel.setName(item.getName());
            pwdsUtilsModel.setPlannedCount(item.getPlanned());
            refreshList();
            setTitle();
        }
    }

    public boolean isPreviousMonth(){
        Calendar current = Calendar.getInstance();
        current.set(currentDateModel.getYear(), currentDateModel.getMonth(), 1);
        Calendar another = Calendar.getInstance();
        another.set(dateModel.getYear(), dateModel.getMonth(), 1);
        return another.compareTo(current) < 0;
    }

    public void showChangeAlert(String next, final boolean isNext){

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Doctors Selection");
        alert.setMessage("Doctor selection for "+pwdsUtilsModel.getName()+" is changed!!" +
                "\nDo you want to go "+next+" without saving changes?");
        alert.setCancelable(false);
        alert.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

                if(isNext){
                    goToNext();
                } else {
                    goToPrev();
                }


            }
        });

        alert.setNegativeButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        saveDoctorsSelection(true);
                        if(isNext){
                            goToNext();
                        } else {
                            goToPrev();
                        }
                    }
                });
        alert.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public boolean getApprovalStatus(){
        PWDSModel pwdsModel = r.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_MONTH, dateModel.getMonth())
                .equalTo(PWDSModel.COL_YEAR, dateModel.getYear())
                .equalTo(PWDSModel.COL_IS_APPROVED, true)
                .findFirst();
        if(null != pwdsModel){
            return true;
        }
        return false;
    }

    public void getEligiblePolicy(String monthYear) {
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        compositeDisposable.add(apiServices.isEligible("PWDS", monthYear)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean s) {
                        if(s){
                            saveDoctorsSelection(false);
                        } else {
                            ToastUtils.shortToast("Unable to save. Date over!!");
                        }
                    }

                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ToastUtils.longToast("Try again!!");
                    }
                }));
    }
}
