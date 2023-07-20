package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.response.ResponseMaster;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.modules.reports.model.Uncover;
import bd.com.aristo.edcr.modules.reports.model.UncoverDetails;
import bd.com.aristo.edcr.modules.reports.model.UncoverDot;
import bd.com.aristo.edcr.modules.reports.model.UncoverRealm;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class UncoverDotFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    List<DoctorsModel> doctorsModels;
    @Inject
    Realm r;
    @Inject
    APIServices apiServices;
    private CompositeDisposable mCompositeDisposable;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    @BindView(R.id.cardBottom)
    CardView cardBottom;


    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;
    DateModel dateModel = DCRUtils.getToday();
    @Inject
    UserModel userModel;
    List<Uncover> uncoverList;

    Context context;

    public UncoverDotFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor, container, false);
        ButterKnife.bind(this, rootView);
        cardBottom.setVisibility(View.GONE);
        if (ConnectionUtils.isNetworkConnected(context)) {
            LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
            loadingDialog.show();
            getUncovered(loadingDialog);
        } else {
            ToastUtils.longToast("No Internet Connection!!");
        }

        return rootView;
    }


    public void setTitle(long count){
        ((Activity) context).setTitle("Uncovered Doctor List("+count+")");
    }

    public void getUncovered(final LoadingDialog loadingDialog){
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getUncovered(userModel.getUserId(),
                DateTimeUtils.getMonthNumber(dateModel.getMonth()),
                String.valueOf(dateModel.getYear()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseMaster<Uncover>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG, "onComplete doctors list sync");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(final ResponseMaster<Uncover> value) {
                        if (value!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){

                            if(value.getDataModelList() != null && value.getDataModelList().size() > 0){
                                uncoverList = value.getDataModelList();
                                setupList();
                            }
                        }else{

                        }
                    }
                }));
    }


    private void setupList() {

        if(uncoverList != null && uncoverList.size() > 0){
            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(UncoverRealm.class);
                    for(Uncover uncover:uncoverList){
                        for(UncoverDetails uncoverDetails:uncover.getDetailsList()){
                            UncoverRealm uncoverRealm = new UncoverRealm();
                            uncoverRealm.setDay(uncover.getDay());
                            uncoverRealm.setDoctorId(uncoverDetails.getDoctorId());
                            uncoverRealm.setStatus(getStatus(uncoverDetails.getStatus(), uncover.getDay()));
                            uncoverRealm.setStatusCause(uncoverDetails.getStatusCause());
                            realm.insertOrUpdate(uncoverRealm);
                        }
                    }
                }
            });

        }

        doctorsModels = r.where(DoctorsModel.class).sort(DoctorsModel.COL_NAME).findAll();
        setTitle(r.where(DoctorsModel.class).count());
        if(doctorsModels != null && doctorsModels.size() > 0) { //doctors list
            refreshList();
        }
    }

    public int getStatus(String status, int day){
        if(status.startsWith("U")){
            if(day > dateModel.getDay()){
                return 4;
            }
            return 0;
        } else if(status.startsWith("E")){
            return 1;
        } else if(status.startsWith("A")){
            return 2;
        } else if(status.startsWith("N")){
            return 3;
        } else if(status.startsWith("D")){
            return 4;
        } else {
            return 0;
        }

    }





    public void refreshList(){

        List<IUncoverModel> dvrIDoctors = new ArrayList<>();
        int i = 0;
        for(DoctorsModel dm: doctorsModels){
            IUncoverModel dvrIDoctor = new IUncoverModel();
            dvrIDoctor.setId(dm.getId());
            dvrIDoctor.withIdentifier(i);
            dvrIDoctor.setName(dm.getName());
            dvrIDoctor.setSpecial(dm.getSpecial());
            dvrIDoctor.setDegree(dm.getDegree());
            dvrIDoctor.setAddress(dm.getAddress());
            dvrIDoctor.setClicked(false);
            dvrIDoctors.add(dvrIDoctor);
            i++;
        }
       final FastItemAdapter<IUncoverModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dvrIDoctors);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IUncoverModel>() {
            @Override
            public boolean filter(IUncoverModel item, CharSequence constraint) {
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

    public List<UncoverDot> getDotList(String docId){
       List<UncoverDot> iDotModels = new ArrayList<>();
        List<UncoverRealm> uncoverRealms = r.where(UncoverRealm.class)
                .equalTo("doctorId", docId)
                .sort("day")
                .findAll();
        if(uncoverRealms != null && uncoverRealms.size() > 0){
            for(UncoverRealm uncoverRealm:uncoverRealms){
                UncoverDot iDotModel = new UncoverDot();
                iDotModel.setDay(uncoverRealm.getDay());
                iDotModel.setStatus(uncoverRealm.getStatus());
                iDotModels.add(iDotModel);
            }
        }

        return iDotModels;
    }

    public String getDotCount(String docId){
        List<DVRForServer> dvrRealmModelList = r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                .equalTo(DVRForServer.COL_YEAR, String.valueOf(dateModel.getYear()))
                .findAll();

        int dotCount = 0;
        for(DVRForServer dvrForServer:dvrRealmModelList){
            dotCount += r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, docId)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrForServer.getId())
                    .count();
        }

        return String.valueOf(dotCount);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
