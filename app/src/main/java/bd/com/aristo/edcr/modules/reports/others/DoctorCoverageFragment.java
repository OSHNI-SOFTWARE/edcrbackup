package bd.com.aristo.edcr.modules.reports.others;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.reports.model.DoctorCoverage;
import bd.com.aristo.edcr.networking.APIServices;
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

public class DoctorCoverageFragment extends Fragment{

    public static final String TAG = "DVREveningFragment";


    @Inject
    APIServices apiServices;
    @Inject
    UserModel userModel;
    @Inject
    Realm r;
    int month, year;
    private CompositeDisposable mCompositeDisposable;
    @BindView(R.id.rv)
    RecyclerView rv;
    DoctorCoverage doctorCoverage = null;
    final FastItemAdapter<DoctorCoverageModel> fastAdapter = new FastItemAdapter<>();
    Context context;
    LoadingDialog loadingDialog;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public DoctorCoverageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_report_doctor_coverage, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        if(getArguments() != null){
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");
            if (ConnectionUtils.isNetworkConnected(context)) {
                syncMarketDVRList();
            } else {
                ToastUtils.longToast("No Internet Connection!!");
                ((AppCompatActivity) context).onBackPressed();
            }
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }

        return rootView;
    }


    public void syncMarketDVRList(){
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getDoctorCoverage(userModel.getUserId(),
                String.valueOf(year),
                DateTimeUtils.getMonthNumber(month))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<DoctorCoverage>>() {
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
                    public void onNext(final ResponseDetail<DoctorCoverage> value) {
                        if (value!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){

                            if(value.getDataModelList() != null && value.getDataModelList().size() > 0){
                                doctorCoverage = value.getDataModelList().get(0);
                                refreshList();
                            }
                        }
                    }
                }));
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshList(){
        fastAdapter.add(getDoctorCoverage());
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(fastAdapter);
    }

    public List<DoctorCoverageModel> getDoctorCoverage(){
        List<DoctorCoverageModel> doctorCoverageModelList = new ArrayList<>();
        DoctorCoverageModel doctorCoverageModel = null;
        int i =1;
        /*
         * 1. TotalDoctor,
         * 2. TotalPlannedDoctor,
         * 3. PlannedPer,
         * 4. TotalDoctorVisited,
         * 5. DoctorCoveragePer,
         * 6. TotalNoOfDOTPlan,
         * 7. TotalExecutionOfDOTPlan,
         * 8. TotalVisitOfDOTPlannedDoctorWithoutPlan,
         * 9. PlanExecutionPer,
         * 10. TotalVisitOfUnplannedDoctor,
         * 11. TotalNoOfNonDOTDoctorVisited,
         * 12. TotalExecutionOfNewDoctor,
         * 13. TotalVisitOfNewDoctor,
         * 14. TotalVisitOfInterDoctor
         */
        if(doctorCoverage != null){

            //TotalDoctor 1
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Doctor");
            doctorCoverageModel.setValue(doctorCoverage.getTotalDoctor());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalPlannedDoctor 2
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Planned Doctor");
            doctorCoverageModel.setValue(doctorCoverage.getTotalPlannedDoctor());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //PlannedPer 3
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Planned");
            doctorCoverageModel.setValue(doctorCoverage.getPlanned()+"%");
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalDoctorVisited 4
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Doctor Visited");
            doctorCoverageModel.setValue(doctorCoverage.getTotalDoctorVisited());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //DoctorCoveragePer 5
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Doctor Coverage");
            doctorCoverageModel.setValue(doctorCoverage.getDoctorCoverage()+"%");
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalNoOfDOTPlan 6
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total no of DOT Plan");
            doctorCoverageModel.setValue(doctorCoverage.getTotalNoOfDOTPlan());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalExecutionOfDOTPlan 7
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Execution of DOT Plan");
            doctorCoverageModel.setValue(doctorCoverage.getTotalExecutionOfDOTPlan());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalVisitOfDOTPlannedDoctorWithoutPlan 8
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Visit of DOT Planned Doctor without Plan");
            doctorCoverageModel.setValue(doctorCoverage.getTotalVisitOfDOTPlannedDoctorWithoutPlan());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //PlanExecutionPer 9
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Plan Execution Percentage");
            doctorCoverageModel.setValue(doctorCoverage.getPlanExecution()+"%");
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalVisitOfUnplannedDoctor 10
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Visit Of Unplanned Doctor");
            doctorCoverageModel.setValue(doctorCoverage.getTotalVisitOfUnplannedDoctor());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalNoOfNonDOTDoctorVisited 11
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total No Of Non DOT Doctor Visited");
            doctorCoverageModel.setValue(doctorCoverage.getTotalNoOfNonDOTDoctorVisited());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalExecutionOfNewDoctor 12
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Execution Of NewDoctor");
            doctorCoverageModel.setValue(doctorCoverage.getTotalExecutionOfNewDoctor());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalVisitOfNewDoctor 13
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Visit Of New Doctor");
            doctorCoverageModel.setValue(doctorCoverage.getTotalVisitOfNewDoctor());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);

            //TotalVisitOfInterDoctor 14
            doctorCoverageModel = new DoctorCoverageModel();
            doctorCoverageModel.setCategory("Total Visit Of Intern Doctor");
            doctorCoverageModel.setValue(doctorCoverage.getTotalVisitOfInterDoctor());
            doctorCoverageModel.withIdentifier(i++);
            doctorCoverageModelList.add(doctorCoverageModel);
        }
        return doctorCoverageModelList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_coverage, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
