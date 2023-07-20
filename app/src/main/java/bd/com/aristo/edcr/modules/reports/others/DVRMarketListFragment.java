package bd.com.aristo.edcr.modules.reports.others;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.modules.dvr.model.IMarketDoctorsModel;
import bd.com.aristo.edcr.modules.dvr.model.MarketDotModel;
import bd.com.aristo.edcr.modules.reports.model.DVRGroup;
import bd.com.aristo.edcr.modules.reports.model.DVRMarket;
import bd.com.aristo.edcr.modules.reports.model.DVRMarketDoctor;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/15/17.
 */

public class DVRMarketListFragment extends Fragment{

    public static final String TAG = "DVREveningFragment";


    @Inject
    APIServices apiServices;
    @Inject
    UserModel userModel;
    @Inject
    Realm r;

    DateModel dateModel = DCRUtils.getToday();

    private CompositeDisposable mCompositeDisposable;

    @BindView(R.id.doctorList)
    RecyclerView dotList;


    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;
    long count;

    List<DVRGroup> dvrGroupList = new ArrayList<>();

    int[][] AMO;
    int[][] BMO;
    int[][] CMO;
    int[][] DMO;
    int[][] AEV;
    int[][] BEV;
    int[][] CEV;
    int[][] DEV;
    Map<Integer, String> index;


    final FastItemAdapter<IMarketDoctorsModel> fastAdapter = new FastItemAdapter<>();

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DVRMarketListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_dvr_markets_doctor, container, false);
        ButterKnife.bind(this, rootView);
        initializeMarketArray();

        if (ConnectionUtils.isNetworkConnected(context)) {
            LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
            loadingDialog.show();
            syncMarketDVRList(loadingDialog);
        } else {
            refreshList();
        }
        return rootView;
    }

    public void initializeMarketArray(){
        count = r.where(DoctorsModel.class).count() + 1;
        AMO = new int[(int)count][32];
        BMO = new int[(int)count][32];
        CMO = new int[(int)count][32];
        DMO = new int[(int)count][32];
        AEV = new int[(int)count][32];
        BEV = new int[(int)count][32];
        CEV = new int[(int)count][32];
        DEV = new int[(int)count][32];
        index = new HashMap<>();
        List<DoctorsModel> doctorsModelList = r.where(DoctorsModel.class).sort(DoctorsModel.COL_NAME).findAll();
        int k = 0;
        if(doctorsModelList != null && doctorsModelList.size() > 0){
            for(DoctorsModel doctorsModel:doctorsModelList){
                index.put(k, doctorsModel.getId());
                k++;
            }
            index.put(k, "Sys Doctor");
        }
        for(int i = 0; i < count; i++){
            for(int j = 0; j < 32; j++){
                AMO[i][j] = 0;
                BMO[i][j] = 0;
                CMO[i][j] = 0;
                DMO[i][j] = 0;
                AEV[i][j] = 0;
                BEV[i][j] = 0;
                CEV[i][j] = 0;
                DEV[i][j] = 0;
            }
        }

        List<MarketDVRModel> marketDVRModels = getOwnDVR();

        if(userModel.getUserId().endsWith("A")){
            populateMarketArray(1, marketDVRModels);
        } else if(userModel.getUserId().endsWith("B")){
            populateMarketArray(2, marketDVRModels);
        } else if(userModel.getUserId().endsWith("C")){
            populateMarketArray(3, marketDVRModels);
        } else if(userModel.getUserId().endsWith("D")){
            populateMarketArray(4, marketDVRModels);
        }
    }

    public void syncMarketDVRList(final LoadingDialog loadingDialog){
        mCompositeDisposable = new CompositeDisposable();
        /*mCompositeDisposable.add(apiServices.getMarketDVR(userModel.getUserId(),
                String.valueOf(dateModel.getYear()),
                DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseMaster<DVRGroup>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG, "onComplete doctors list sync");
                        loadingDialog.dismiss();
                        generateMarketDVR();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        refreshList();
                    }

                    @Override
                    public void onNext(final ResponseMaster<DVRGroup> value) {
                        if (value!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){

                            //ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                            dvrGroupList.addAll(value.getDataModelList());
                        }else{
                            //ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            refreshList();
                        }
                    }
                }));*/
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshList(){
        fastAdapter.add(getDVRMarketList());
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        dotList.setLayoutManager(layoutManager);
        dotList.setAdapter(fastAdapter);
        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IMarketDoctorsModel>() {
            @Override
            public boolean filter(IMarketDoctorsModel item, CharSequence constraint) {
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


    public List<MarketDVRModel> getOwnDVR(){
        List<MarketDVRModel> marketDVRModels = new ArrayList<>();

        for (int i = 1; i< dateModel.getLastDay() + 1; i++) {
            String[] morning = null;
            String[] evening = null;

            DVRForServer morningDVR = r.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_DAY, i)
                    .equalTo(DVRForServer.COL_MONTH, DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                    .equalTo(DVRForServer.COL_YEAR, String.valueOf(dateModel.getYear()))
                    .equalTo(DVRForServer.COL_SHIFT, StringConstants.MORNING)
                    .findFirst();
            DVRForServer eveningDVR = r.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_DAY, i)
                    .equalTo(DVRForServer.COL_MONTH, DateTimeUtils.getMonthNumber(dateModel.getMonth()))
                    .equalTo(DVRForServer.COL_YEAR, String.valueOf(dateModel.getYear()))
                    .equalTo(DVRForServer.COL_SHIFT, StringConstants.EVENING)
                    .findFirst();
            if (morningDVR != null) {

                List<DVRDoctorRealm> morningDVRDoctors = r.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, morningDVR.getId()).findAll();
                if (morningDVRDoctors != null && morningDVRDoctors.size() > 0) {
                    morning = new String[morningDVRDoctors.size()];
                    int k = 0;
                    for (DVRDoctorRealm morningDoctor : morningDVRDoctors) {
                        morning[k] = morningDoctor.getDoctorID();
                        k++;
                    }
                }
            }


            if (eveningDVR != null) {
                List<DVRDoctorRealm> eveningDVRDoctors = r.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, eveningDVR.getId()).findAll();

                if (eveningDVRDoctors != null && eveningDVRDoctors.size() > 0) {
                    evening = new String[eveningDVRDoctors.size()];
                    int k = 0;
                    for (DVRDoctorRealm eveningDoctor : eveningDVRDoctors) {
                        evening[k] = eveningDoctor.getDoctorID();
                        k++;
                    }
                }


            }




            MarketDVRModel marketDVRModel = new MarketDVRModel();
            marketDVRModel.setDay(i);
            marketDVRModel.setMorning(morning);
            marketDVRModel.setEvening(evening);
            marketDVRModels.add(marketDVRModel);
        }

        return marketDVRModels;
    }

    public List<MarketDVRModel> getMarketDVR(List<DVRMarket> dvrMarketList){
        List<MarketDVRModel> marketDVRModels = new ArrayList<>();
        for (int i = 1; i< dateModel.getLastDay() + 1; i++) {
            MarketDVRModel marketDVRModel = new MarketDVRModel();
            marketDVRModel.setDay(i);
            String[] morning = null;
            String[] evening = null;
            for (DVRMarket dvrMarket : dvrMarketList) {
                if(Integer.valueOf(dvrMarket.getDay()) == i) {
                    if(dvrMarket.getShift().equalsIgnoreCase(StringConstants.MORNING)){
                        int k = 0;
                        morning = new String[dvrMarket.getMarketDoctorList().size()];
                        for(DVRMarketDoctor dvrMarketDoctor:dvrMarket.getMarketDoctorList()){
                            morning[k] = dvrMarketDoctor.getId();
                            k++;
                        }
                    }
                    if(dvrMarket.getShift().equalsIgnoreCase(StringConstants.EVENING)){
                        int k = 0;
                        evening = new String[dvrMarket.getMarketDoctorList().size()];
                        for(DVRMarketDoctor dvrMarketDoctor:dvrMarket.getMarketDoctorList()){
                            evening[k] = dvrMarketDoctor.getId();
                            k++;
                        }
                    }
                }
            }
            marketDVRModel.setMorning(morning);
            marketDVRModel.setEvening(evening);
            marketDVRModels.add(marketDVRModel);
        }
        return marketDVRModels;
    }

    public void generateMarketDVR(){
        if(dvrGroupList != null && dvrGroupList.size() > 0){
            for(DVRGroup dvrGroup:dvrGroupList){
                if(dvrGroup.getGroup().endsWith("A")){
                    populateMarketArray(1, getMarketDVR(dvrGroup.getDvrMarketList()));
                } else if(dvrGroup.getGroup().endsWith("B")){
                    populateMarketArray(2, getMarketDVR(dvrGroup.getDvrMarketList()));
                } else if(dvrGroup.getGroup().endsWith("C")){
                    populateMarketArray(3, getMarketDVR(dvrGroup.getDvrMarketList()));
                } else if(dvrGroup.getGroup().endsWith("D")){
                    populateMarketArray(4, getMarketDVR(dvrGroup.getDvrMarketList()));
                }
            }
        }

        refreshList();

    }

    public void populateMarketArray(int market, List<MarketDVRModel> marketDVRModels){

        switch (market){
            case 1:
                for(MarketDVRModel marketDVRModel: marketDVRModels){
                    if(marketDVRModel.getMorning() != null) {
                        for (String docId : marketDVRModel.getMorning()) {
                            AMO[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                    if(marketDVRModel.getEvening() != null) {
                        for (String docId : marketDVRModel.getEvening()) {
                            AEV[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                }
                break;
            case 2:
                for(MarketDVRModel marketDVRModel: marketDVRModels){
                    if(marketDVRModel.getMorning() != null) {
                        for (String docId : marketDVRModel.getMorning()) {
                            BMO[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                    if(marketDVRModel.getEvening() != null) {
                        for (String docId : marketDVRModel.getEvening()) {
                            BEV[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                }
                break;
            case 3:
                for(MarketDVRModel marketDVRModel: marketDVRModels){
                    if(marketDVRModel.getMorning() != null) {
                        for (String docId : marketDVRModel.getMorning()) {
                            CMO[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                    if(marketDVRModel.getEvening() != null) {
                        for (String docId : marketDVRModel.getEvening()) {
                            CEV[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                }
                break;
            case 4:
                for(MarketDVRModel marketDVRModel: marketDVRModels){
                    if(marketDVRModel.getMorning() != null) {
                        for (String docId : marketDVRModel.getMorning()) {
                            DMO[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                    if(marketDVRModel.getEvening() != null) {
                        for (String docId : marketDVRModel.getEvening()) {
                            DEV[getKeyFromValue(index, docId)][marketDVRModel.getDay()] = 1;
                        }
                    }
                }
                break;
                default:
                    break;
        }

    }

    public int getKeyFromValue(Map hm, String value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return (int) o;
            }
        }
        return (int) count-1;
    }

    public List<IMarketDoctorsModel> getDVRMarketList(){
        List<IMarketDoctorsModel> marketDoctorsModelList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            DoctorsModel doctorsModel = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, index.get(i)).findFirst();
            IMarketDoctorsModel marketDoctorsModel = new IMarketDoctorsModel();
            marketDoctorsModel.setId(index.get(i));
            marketDoctorsModel.setName(doctorsModel != null ? doctorsModel.getName():"Unknown");
            marketDoctorsModel.setgA(getACount(i));
            marketDoctorsModel.setgB(getBCount(i));
            marketDoctorsModel.setgC(getCCount(i));
            marketDoctorsModel.setgD(getDCount(i));
            marketDoctorsModel.setMarketDotModels(getMarketDots(i));
            marketDoctorsModelList.add(marketDoctorsModel);
        }


        return marketDoctorsModelList;
    }

    public String getACount(int i){
        int count = 0;
        for(int j = 1; j < 32; j++){
            if(AMO[i][j] == 1){
                count++;
            }
            if(AEV[i][j] == 1){
                count++;
            }
        }
        return String.valueOf(count);
    }

    public String getBCount(int i){
        int count = 0;
        for(int j = 1; j < 32; j++){
            if(BMO[i][j] == 1){
                count++;
            }
            if(BEV[i][j] == 1){
                count++;
            }
        }
        return String.valueOf(count);
    }

    public String getCCount(int i){
        int count = 0;
        for(int j = 1; j < 32; j++){
            if(CMO[i][j] == 1){
                count++;
            }
            if(CEV[i][j] == 1){
                count++;
            }
        }
        return String.valueOf(count);
    }
    public String getDCount(int i){
        int count = 0;
        for(int j = 1; j < 32; j++){
            if(DMO[i][j] == 1){
                count++;
            }
            if(DEV[i][j] == 1){
                count++;
            }
        }
        return String.valueOf(count);
    }

    public List<MarketDotModel> getMarketDots(int i){
        List<MarketDotModel> marketDotModels = new ArrayList<>();
        for(int j = 1; j < 32; j++){
            MarketDotModel marketDotModel = new MarketDotModel();
            marketDotModel.setDay(String.valueOf(j));

            if(DMO[i][j] == 1){
                marketDotModel.setD("M");
            }
            if(DEV[i][j] == 1){
                marketDotModel.setD("E");
            }
            if(DMO[i][j] == 0 && DEV[i][j] == 0){
                marketDotModel.setD(".");
            }
            if(CMO[i][j] == 1){
                marketDotModel.setC("M");
            }
            if(CEV[i][j] == 1){
                marketDotModel.setC("E");
            }
            if(CMO[i][j] == 0 && CEV[i][j] == 0){
                marketDotModel.setC(".");
            }
            if(BMO[i][j] == 1){
                marketDotModel.setB("M");
            }
            if(BEV[i][j] == 1){
                marketDotModel.setB("E");
            }
            if(BMO[i][j] == 0 && BEV[i][j] == 0){
                marketDotModel.setB(".");
            }
            if(AMO[i][j] == 1){
                marketDotModel.setA("M");
            }
            if(AEV[i][j] == 1){
                marketDotModel.setA("E");
            }
            if(AMO[i][j] == 0 && AEV[i][j] == 0){
                marketDotModel.setA(".");
            }
            marketDotModels.add(marketDotModel);
        }
        return marketDotModels;
    }

}
