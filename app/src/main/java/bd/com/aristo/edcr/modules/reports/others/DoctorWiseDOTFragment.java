package bd.com.aristo.edcr.modules.reports.others;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.reports.model.AbsentReport;
import bd.com.aristo.edcr.modules.reports.model.DDListener;
import bd.com.aristo.edcr.modules.reports.model.DoctorDCRResponse;
import bd.com.aristo.edcr.modules.reports.model.DoctorWiseItemModel;
import bd.com.aristo.edcr.modules.reports.model.IDOTExecution;
import bd.com.aristo.edcr.modules.reports.model.IDotModel;
import bd.com.aristo.edcr.modules.reports.ss.model.NewDoctorDCR;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/15/17.
 */

public class DoctorWiseDOTFragment extends Fragment implements DDListener {

    public static final String TAG = "DoctorWiseItemFragment";

    @Inject
    APIServices apiServices;
    int day, month, year;
    private CompositeDisposable mCompositeDisposable;
    @BindView(R.id.recyclerDVR)
    RecyclerView rv;

    @BindView(R.id.txtTotalDOT)
    TextView txtTotalDOT;
    @BindView(R.id.txtTotalNEW)
    TextView txtTotalNEW;
    @BindView(R.id.txtTotalAbsent)
    TextView txtTotalAbsent;
    @BindView(R.id.txtTotalExecution)
    TextView txtTotalExecution;

    Context context;
    LoadingDialog loadingDialog;
    private int totalDot, totalExe, totalAbsent, totalNew;
    List<IDOTExecution> idotExecutionList = new ArrayList<>();
    FastItemAdapter<IDOTExecution> fastItemAdapter = new FastItemAdapter<>();
    @Inject
    Realm r;
    HashMap<String, Integer> absentMap = new HashMap<>();
    HashMap<String, String> absentDateMap = new HashMap<>();
    @Inject
    RequestServices requestServices;

    Map<String, List<NewDoctorDCR>> mapNewDoctorExecution = new HashMap<>();

    UserModel userModel;

    int serialNo = 1;

    public void getUserModel(){
        userModel = r.where(UserModel.class).findFirst();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DoctorWiseDOTFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_doctor_wise_dot, container, false);
        ButterKnife.bind(this, rootView);
        getUserModel();
        if (getArguments() != null) {
            month = getArguments().getInt("month");
            day = getArguments().getInt("day");
            year = getArguments().getInt("year");
            if (ConnectionUtils.isNetworkConnected(context)) {
                //syncDCRAbsent();
                requestServices.getDoctorDCRReport(context, apiServices, userModel.getUserId(), day, month, year, this);
            } else {
                ToastUtils.longToast("No Internet Connection!!");
                ((AppCompatActivity) context).onBackPressed();
            }
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    void setAbsentMap(List<AbsentReport> absentReportList){
        for (AbsentReport absentReport:absentReportList){
            absentMap.put(absentReport.getDoctorID(), absentReport.getAbsentCount());
            absentDateMap.put(absentReport.getDoctorID(), absentReport.getAbsentDates());
        }
    }


    public void setIDOTList(List<IDOTExecution> dotExecutionList){
        totalDot = 0; totalExe = 0; totalAbsent = 0; totalNew = 0;
        idotExecutionList = new ArrayList<>();
        for(IDOTExecution idotExecution:dotExecutionList){
            List<IDotModel> iDotModelList = new ArrayList<>();
            idotExecution.setNewDoctor(false);
            idotExecution.setSerialNo(serialNo);
            idotExecution.withIdentifier( (serialNo));
            serialNo++;
            int[][] dotCount = new int[62][3];
            dotCount[0] = getDOTExeCount(idotExecution.getE1());
            dotCount[1] = getDOTExeCount(idotExecution.getE2());
            dotCount[2] = getDOTExeCount(idotExecution.getE3());
            dotCount[3] = getDOTExeCount(idotExecution.getE4());
            dotCount[4] = getDOTExeCount(idotExecution.getE5());
            dotCount[5] = getDOTExeCount(idotExecution.getE6());
            dotCount[6] = getDOTExeCount(idotExecution.getE7());
            dotCount[7] = getDOTExeCount(idotExecution.getE8());
            dotCount[8] = getDOTExeCount(idotExecution.getE9());
            dotCount[9] = getDOTExeCount(idotExecution.getE10());
            dotCount[10] = getDOTExeCount(idotExecution.getE11());
            dotCount[11] = getDOTExeCount(idotExecution.getE12());
            dotCount[12] = getDOTExeCount(idotExecution.getE13());
            dotCount[13] = getDOTExeCount(idotExecution.getE14());
            dotCount[14] = getDOTExeCount(idotExecution.getE15());
            dotCount[15] = getDOTExeCount(idotExecution.getE16());
            dotCount[16] = getDOTExeCount(idotExecution.getE17());
            dotCount[17] = getDOTExeCount(idotExecution.getE18());
            dotCount[18] = getDOTExeCount(idotExecution.getE19());
            dotCount[19] = getDOTExeCount(idotExecution.getE20());
            dotCount[20] = getDOTExeCount(idotExecution.getE21());
            dotCount[21] = getDOTExeCount(idotExecution.getE22());
            dotCount[22] = getDOTExeCount(idotExecution.getE23());
            dotCount[23] = getDOTExeCount(idotExecution.getE24());
            dotCount[24] = getDOTExeCount(idotExecution.getE25());
            dotCount[25] = getDOTExeCount(idotExecution.getE26());
            dotCount[26] = getDOTExeCount(idotExecution.getE27());
            dotCount[27] = getDOTExeCount(idotExecution.getE28());
            dotCount[28] = getDOTExeCount(idotExecution.getE29());
            dotCount[29] = getDOTExeCount(idotExecution.getE30());
            dotCount[30] = getDOTExeCount(idotExecution.getE31());

            dotCount[31] = getDOTExeCount(idotExecution.getM1());
            dotCount[32] = getDOTExeCount(idotExecution.getM2());
            dotCount[33] = getDOTExeCount(idotExecution.getM3());
            dotCount[34] = getDOTExeCount(idotExecution.getM4());
            dotCount[35] = getDOTExeCount(idotExecution.getM5());
            dotCount[36] = getDOTExeCount(idotExecution.getM6());
            dotCount[37] = getDOTExeCount(idotExecution.getM7());
            dotCount[38] = getDOTExeCount(idotExecution.getM8());
            dotCount[39] = getDOTExeCount(idotExecution.getM9());
            dotCount[40] = getDOTExeCount(idotExecution.getM10());
            dotCount[41] = getDOTExeCount(idotExecution.getM11());
            dotCount[42] = getDOTExeCount(idotExecution.getM12());
            dotCount[43] = getDOTExeCount(idotExecution.getM13());
            dotCount[44] = getDOTExeCount(idotExecution.getM14());
            dotCount[45] = getDOTExeCount(idotExecution.getM15());
            dotCount[46] = getDOTExeCount(idotExecution.getM16());
            dotCount[47] = getDOTExeCount(idotExecution.getM17());
            dotCount[48] = getDOTExeCount(idotExecution.getM18());
            dotCount[49] = getDOTExeCount(idotExecution.getM19());
            dotCount[50] = getDOTExeCount(idotExecution.getM20());
            dotCount[51] = getDOTExeCount(idotExecution.getM21());
            dotCount[52] = getDOTExeCount(idotExecution.getM22());
            dotCount[53] = getDOTExeCount(idotExecution.getM23());
            dotCount[54] = getDOTExeCount(idotExecution.getM24());
            dotCount[55] = getDOTExeCount(idotExecution.getM25());
            dotCount[56] = getDOTExeCount(idotExecution.getM26());
            dotCount[57] = getDOTExeCount(idotExecution.getM27());
            dotCount[58] = getDOTExeCount(idotExecution.getM28());
            dotCount[59] = getDOTExeCount(idotExecution.getM29());
            dotCount[60] = getDOTExeCount(idotExecution.getM30());
            dotCount[61] = getDOTExeCount(idotExecution.getM31());
            int dot = 0, exe = 0, newD = 0, absent = 0;
            if(absentMap.get(idotExecution.getDoctorID()) != null){
                absent = absentMap.get(idotExecution.getDoctorID()).intValue();
            }
            for(int i = 0; i < 62; i++){
                int j = 0;
                if(i > 30){
                    j = i - 30;
                } else {
                    j = i + 1;
                }
                IDotModel iDotModel = new IDotModel();

                iDotModel.setDate(j);
                boolean isEntry = false;
                if(dotCount[i][0] == 1){
                   ++dot;
                   iDotModel.setStatus(0);
                   isEntry = true;
                }
                if(dotCount[i][1] == 1){
                    ++newD;
                    iDotModel.setStatus(3);
                    isEntry = true;
                }
                if(dotCount[i][2] == 1){
                    ++exe;
                    iDotModel.setStatus(1);
                    isEntry = true;
                }

                if(absentDateMap.get(idotExecution.getDoctorID()) != null){
                    String[] absentDate = absentDateMap.get(idotExecution.getDoctorID()).split(",");
                    for(int k = 0; k < absentDate.length; k++){
                        if(j == Integer.parseInt(absentDate[k].split("-")[0])) {
                            boolean entryExist = false;
                            for (IDotModel iDotModel1:iDotModelList){
                                if(iDotModel1.getDate() == j){
                                    entryExist = true;
                                    break;
                                }
                            }
                            if(!entryExist) {
                                iDotModel.setStatus(2);
                                isEntry = true;
                            }
                            break;
                        }
                    }
                }

                if(isEntry){
                    iDotModelList.add(iDotModel);
                }
            }

            exe -= absent;
            idotExecution.setAbsentCount(absent);
            idotExecution.setDotCount(dot);
            idotExecution.setExecutionCount(exe);
            idotExecution.setNewCount(newD);
            idotExecution.setiDotModelList(iDotModelList);
            totalDot += dot;
            totalNew += newD;
            totalAbsent += absent;
            totalExe += exe;
            idotExecutionList.add(idotExecution);
        }
    }

    public void setNewDoctorDCR(List<NewDoctorDCR> newDoctorDCRList){

        for(NewDoctorDCR newDoctorDCR:newDoctorDCRList){
            List<NewDoctorDCR> newDoctorDCRListM = mapNewDoctorExecution.get(newDoctorDCR.getDoctorID());
            if (newDoctorDCRListM == null || newDoctorDCRListM.size() <= 0) {
                newDoctorDCRListM = new ArrayList<>();
            }
            newDoctorDCRListM.add(newDoctorDCR);
            mapNewDoctorExecution.put(newDoctorDCR.getDoctorID(), newDoctorDCRListM);
        }

        for(Map.Entry<String, List<NewDoctorDCR>> map:mapNewDoctorExecution.entrySet()){
            Map<String, Integer> dateMap = new HashMap<>();
            for(NewDoctorDCR newDoctorDCR:map.getValue()){
                int count = dateMap.get(newDoctorDCR.getDate())!=null?dateMap.get(newDoctorDCR.getDate()).intValue():0;
                dateMap.put(newDoctorDCR.getDate(), ++count);
            }
            int count = dateMap.size();
            IDOTExecution idotExecution = new IDOTExecution();
            idotExecution.setDoctorID(map.getKey());
            idotExecution.setNewCount(count);
            idotExecution.setDoctorName(map.getKey());
            idotExecution.setAbsentCount(0);
            idotExecution.setDotCount(0);
            idotExecution.setExecutionCount(count);
            idotExecution.setNewDoctor(true);
            idotExecution.setSerialNo(serialNo);
            serialNo++;
            totalExe += count;
            totalNew += count;
            idotExecutionList.add(idotExecution);
        }

        txtTotalAbsent.setText(String.valueOf(totalAbsent));
        txtTotalDOT.setText(String.valueOf(totalDot));
        txtTotalExecution.setText(String.valueOf(totalExe + totalNew));
        txtTotalNEW.setText(String.valueOf(totalNew));


    }

    public List<DoctorWiseItemModel> getDoctorWiseItem(String doctorId){
        List<DoctorWiseItemModel> doctorWiseItemModelList = new ArrayList<>();
        List<NewDoctorDCR> newDoctorDCRList = mapNewDoctorExecution.get(doctorId);
        Collections.sort(newDoctorDCRList, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        for(NewDoctorDCR newDoctorDCR:newDoctorDCRList){
            DoctorWiseItemModel itemModel = new DoctorWiseItemModel();
            itemModel.setPackSize(newDoctorDCR.getPackSize());
            itemModel.setProductCode(newDoctorDCR.getProductCode());
            itemModel.setProductName(newDoctorDCR.getProductName());
            itemModel.setQuantity(String.valueOf(newDoctorDCR.getQuantity()));
            itemModel.setSetDate(newDoctorDCR.getDate());
            doctorWiseItemModelList.add(itemModel);
        }


        return doctorWiseItemModelList;
    }



    public void syncDoctorWiseItemExecution(String id, String name) {
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getDoctorWiseItem(userModel.getUserId(),
                DateTimeUtils.getMonthYear(month, year),
                id)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<DoctorWiseItemModel>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG, "onComplete doctors coverage sync");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        //refreshList();
                    }

                    @Override
                    public void onNext(final ResponseDetail<DoctorWiseItemModel> value) {
                        if (value != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {

                            if (value.getDataModelList() != null && value.getDataModelList().size() > 0) {

                                DoctorWiseItemDialogFragment doctorWiseItemDialogFragment = DoctorWiseItemDialogFragment.newInstance(id, name, value.getDataModelList());
                                doctorWiseItemDialogFragment.show(getFragmentManager(), "dialog");
                            }
                        }
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setAdapter() {
        fastItemAdapter.add(idotExecutionList);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(fastItemAdapter);
        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDOTExecution>() {
            @Override
            public boolean onClick(View v, IAdapter<IDOTExecution> adapter, IDOTExecution item, int position) {
                if(item.isNewDoctor()){
                    DoctorWiseItemDialogFragment doctorWiseItemDialogFragment = DoctorWiseItemDialogFragment.newInstance("New", item.getDoctorID(), getDoctorWiseItem(item.getDoctorID()));
                    doctorWiseItemDialogFragment.show(getFragmentManager(), "dialog");
                } else if(item.getExecutionCount() > 0){
                    syncDoctorWiseItemExecution(item.getDoctorID(), item.getDoctorName());
                }
                return false;
            }
        });
        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDOTExecution>() {
            @Override
            public boolean filter(IDOTExecution item, CharSequence constraint) {

                if (item.getDoctorName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    return false;
                } else return true;

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.global_report_menu, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search...");
        //searchView.setQueryHint(getString(R.string.search_hint_dot));

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (fastItemAdapter != null)
                    fastItemAdapter.getItemAdapter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (fastItemAdapter != null)
                    fastItemAdapter.getItemAdapter().filter(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }


    public int[] getDOTExeCount(String text){
        int[] dotCount = new int[3]; //0 - dot, 1- new,  2 - exe
        if(!text.contains(".")){
            if(text.equalsIgnoreCase("DPE") || text.equalsIgnoreCase("DE") || text.equalsIgnoreCase("PE")){
                dotCount[0] = 1;
                dotCount[2] = 1;
            } else if(text.equalsIgnoreCase("D") || text.equalsIgnoreCase("P") || text.equalsIgnoreCase("DP")){
                dotCount[0] = 1;
            } else if(text.equalsIgnoreCase("E")){
                dotCount[1] = 1;
            }
        }
        return dotCount;
    }


    @Override
    public void getSS(DoctorDCRResponse ddResponse) {
        setAbsentMap(ddResponse.getAbsentReportList());
        setIDOTList(ddResponse.getIdotExecutionList());
        setNewDoctorDCR(ddResponse.getNewDoctorDCRList());
        setAdapter();

    }

    @Override
    public void onError() {

    }
}
