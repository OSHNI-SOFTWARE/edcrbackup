package bd.com.aristo.edcr.modules.gwds;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

    List<DoctorsModel> doctorsModels;



    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    @BindView(R.id.llNext)
    LinearLayout llNext;
    @BindView(R.id.llPrev)
    LinearLayout llPrev;
    List<GWDSGiftModel> giftsModels;// = GWDSUtils.getGwdsGifts(giftModelList, r);

    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;

    public UserModel userModel;
    public DateModel dateModel;
    public DateModel currentDateModel;
    public GWDSUtilsModel gwdsUtilsModel;
    List<TempGWDS> tempGWDSList = new ArrayList<>();
    boolean isChanged = false;
    boolean isApproved = false;

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
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();
        gwdsUtilsModel = (GWDSUtilsModel) getArguments().getSerializable("gwdsUtils");
        dateModel = (DateModel) getArguments().getSerializable("dateModel");
        currentDateModel = DCRUtils.getToday();
        isApproved = getApprovalStatus();
        setupList();
        setTitle();
        return rootView;
    }

    public void setTitle(){
        ((Activity) context).setTitle("GWDS for "+gwdsUtilsModel.getName()+"("+gwdsUtilsModel.getPlannedCount()+")");
    }

    @OnClick(R.id.llSecond)
    void clickOnSaveOption(){
        if(isChanged) {
            getEligiblePolicy(DateTimeUtils.getMonthYear(dateModel.getMonth(), dateModel.getYear()));
        } else {
            ToastUtils.shortToast("No Change Found!");
        }
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


    public void refreshList(){
        isChanged = false;
        giftsModels = GWDSUtils.getGWDSGifts(r, dateModel.getMonth(), dateModel.getYear());
        if(gwdsUtilsModel.getPosition() == 0){
            llPrev.setVisibility(View.INVISIBLE);
        } else if(gwdsUtilsModel.getPosition() == giftsModels.size() - 1){
            llNext.setVisibility(View.INVISIBLE);
        }
        List<GWDSDoctorsModel> gwdsDoctors = GWDSUtils.getGWDSDoctors(doctorsModels, r,
                gwdsUtilsModel.getCode(), dateModel.getMonth(), dateModel.getYear());

       final FastItemAdapter<GWDSDoctorsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(unique(gwdsDoctors));
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<GWDSDoctorsModel>() {
            @Override
            public boolean onClick(View v, IAdapter<GWDSDoctorsModel> adapter, final GWDSDoctorsModel item, int position) {

                if (!isPreviousMonth()) {  //&&dateModel.getDay() < 8   for
                    isChanged = true;
                    if(item.isGwds()){
                        removeFromTemp(item.getId());
                        item.setGwds(false);
                        gwdsUtilsModel.setPlannedCount(gwdsUtilsModel.getPlannedCount() - 1);
                    } else {
                        addToTemp(item.getId());
                        item.setGwds(true);
                        gwdsUtilsModel.setPlannedCount(gwdsUtilsModel.getPlannedCount() + 1);
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


        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<GWDSDoctorsModel>() {
            @Override
            public boolean filter(GWDSDoctorsModel item, CharSequence constraint) {
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
                RealmResults<GWDSModel> pwdsModelList = r.where(GWDSModel.class)
                        .equalTo(GWDSModel.COL_GIFT_ID, gwdsUtilsModel.getCode())
                        .equalTo(GWDSModel.COL_YEAR, dateModel.getYear())
                        .equalTo(GWDSModel.COL_MONTH, dateModel.getMonth())
                        .findAll();
                if(pwdsModelList != null && pwdsModelList.size() > 0){
                    pwdsModelList.deleteAllFromRealm();
                }
                long prevId = 0;
                if (r.where(GWDSModel.class).max(GWDSModel.COL_ID) != null) {
                    prevId = r.where(GWDSModel.class).max(GWDSModel.COL_ID).intValue() + 1;
                    //Log.e(TAG, "3 value prevId:" + prevId);
                }
                for(TempGWDS tempPWDS:tempGWDSList){
                    GWDSModel pwdsModel = new GWDSModel();
                    pwdsModel.setApproved(tempPWDS.isApproved());
                    pwdsModel.setDoctorID(tempPWDS.getDoctorID());
                    pwdsModel.setMonth(tempPWDS.getMonth());
                    pwdsModel.setGiftID(tempPWDS.getProductID());
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

    public void removeFromTemp(String docId){
        TempGWDS removeGWDS = new TempGWDS();
        for(TempGWDS tempGWDS:tempGWDSList){
            if(tempGWDS.getDoctorID().equalsIgnoreCase(docId)){
                removeGWDS = tempGWDS;
                break;
            }
        }
        tempGWDSList.remove(removeGWDS);
    }

    public void addToTemp(String docId){
        TempGWDS tempPWDS = new TempGWDS();
        tempPWDS.setApproved(false);
        tempPWDS.setDeleted(false);
        tempPWDS.setDoctorID(docId);
        tempPWDS.setMonth(dateModel.getMonth());
        tempPWDS.setProductID(gwdsUtilsModel.getCode());
        tempPWDS.setYear(dateModel.getYear());
        tempGWDSList.add(tempPWDS);
    }
    @Override
    public void onPause() {
        super.onPause();
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
        int pos = gwdsUtilsModel.getPosition() + 1;
        gwdsUtilsModel.setPosition(pos);
        if(gwdsUtilsModel.getPosition() == 0){
            llPrev.setVisibility(View.INVISIBLE);
        } else if(gwdsUtilsModel.getPosition() == giftsModels.size() - 1){
            llNext.setVisibility(View.INVISIBLE);
        } else if(pos >= 0 && pos < giftsModels.size() - 1){
            llPrev.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        }


        if(pos >= 0 && pos < giftsModels.size()) {
            GWDSGiftModel item = giftsModels.get(pos);
            gwdsUtilsModel.setCode(item.getCode());
            gwdsUtilsModel.setName(item.getName());
            gwdsUtilsModel.setPlannedCount(item.getPlanned());
            refreshList();
            setTitle();
        }
    }

    public void goToPrev(){
        int pos = gwdsUtilsModel.getPosition() - 1;
        gwdsUtilsModel.setPosition(pos);
        if(gwdsUtilsModel.getPosition() == 0){
            llPrev.setVisibility(View.INVISIBLE);
        } else if(gwdsUtilsModel.getPosition() == giftsModels.size() - 1){
            llNext.setVisibility(View.INVISIBLE);
        } else if(pos >= 0 && pos < giftsModels.size() - 1){
            llPrev.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        }


        if(pos >= 0 && pos < giftsModels.size()) {
            GWDSGiftModel item = giftsModels.get(pos);
            gwdsUtilsModel.setCode(item.getCode());
            gwdsUtilsModel.setName(item.getName());
            gwdsUtilsModel.setPlannedCount(item.getPlanned());
            refreshList();
            setTitle();
        }
    }



    private List<GWDSDoctorsModel> unique(List<GWDSDoctorsModel> list) {
        List<GWDSDoctorsModel> uniqueList = new ArrayList<>();
        Set<GWDSDoctorsModel> uniqueSet = new HashSet<>();
        for (GWDSDoctorsModel obj : list) {
            if (uniqueSet.add(obj)) {
                uniqueList.add(obj);
            }
        }

        tempGWDSList.clear();

        for(GWDSDoctorsModel gwdsDoctorsModel:uniqueList){
            if(gwdsDoctorsModel.isGwds()){
                GWDSModel pwds = r.where(GWDSModel.class)
                        .equalTo(GWDSModel.COL_DOCTOR_ID, gwdsDoctorsModel.getId())
                        .equalTo(GWDSModel.COL_GIFT_ID, gwdsUtilsModel.getCode())
                        .equalTo(GWDSModel.COL_YEAR, dateModel.getYear())
                        .equalTo(GWDSModel.COL_MONTH, dateModel.getMonth())
                        .findFirst();
                if(pwds != null){
                    TempGWDS tempGWDS = new TempGWDS();
                    tempGWDS.setApproved(pwds.isApproved());
                    tempGWDS.setDoctorID(pwds.getDoctorID());
                    tempGWDS.setMonth(pwds.getMonth());
                    tempGWDS.setProductID(pwds.getGiftID());
                    tempGWDS.setYear(pwds.getYear());
                    tempGWDSList.add(tempGWDS);
                }
            }
        }
        return uniqueList;
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
        alert.setMessage("Doctor selection for "+gwdsUtilsModel.getName()+" is changed!!" +
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

    public boolean getApprovalStatus(){
        GWDSModel pwdsModel = r.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_MONTH, dateModel.getMonth())
                .equalTo(GWDSModel.COL_YEAR, dateModel.getYear())
                .equalTo(GWDSModel.COL_IS_APPROVED, true)
                .findFirst();
        if(null != pwdsModel){
            return true;
        }
        return false;
    }

    public void getEligiblePolicy(String monthYear) {
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        compositeDisposable.add(apiServices.isEligible("GWDS", monthYear)
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
