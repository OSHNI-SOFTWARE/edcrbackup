package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.model.WPModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class WorkPlanSummaryFragment extends Fragment {

    private static final String TAG = "WorkPlanSummaryFragment";
    @Inject
    Realm r;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    @BindView(R.id.cardBottom)
    CardView cardBottom;


    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    DateModel dateModel = DCRUtils.getToday();
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public WorkPlanSummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor, container, false);
        ButterKnife.bind(this, rootView);
        cardBottom.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
        setupList();
        setTitle();

        return rootView;
    }


    public void setTitle(){
        ((Activity) context).setTitle("Work Plan Summary for "+DateTimeUtils.getMonthForInt(dateModel.getMonth()));
    }


    private void setupList() {
        refreshList();
    }





    public void refreshList(){


       final FastItemAdapter<IWorkPlanModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(getWorkPlanSummary());
        fastAdapter.withSelectable(false);
        fastAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);


    }


    public List<IWorkPlanModel> getWorkPlanSummary(){
        List<IWorkPlanModel> iWorkPlanModelArrayList = new ArrayList<>();
        for (int i = 1; i< dateModel.getLastDay() + 1; i++){
            String morningCount = "Morning";
            String eveningCount = "Evening";
            StringBuilder morningList = new StringBuilder();
            StringBuilder eveningList = new StringBuilder();
            List<WPModel> morningWP = r.where(WPModel.class)
                    .equalTo(WPModel.COL_DAY, i)
                    .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                    .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                    .equalTo(WPModel.COL_IS_MORNING, true)
                    .findAll();
            List<WPModel> eveningWP = r.where(WPModel.class)
                    .equalTo(WPModel.COL_DAY, i)
                    .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                    .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                    .equalTo(WPModel.COL_IS_MORNING, false)
                    .findAll();
            if(morningWP != null && morningWP.size() > 0){
                List<String> doctorList = new ArrayList<>();
                for(WPModel wpModel:morningWP){
                    doctorList.add(wpModel.getDoctorID());
                }
                Set<String> tempList = new HashSet<>(doctorList);
                doctorList.clear();
                doctorList.addAll(tempList);
                morningCount = morningCount+"("+doctorList.size()+")";
                int j = 1;
                String prefix = "";
                for(String doctor:doctorList){
                    DoctorsModel doctorsModel = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, doctor).findFirst();
                    if(doctorsModel != null) {
                        morningList.append(prefix+j+"."+doctorsModel.getName() + "[" + doctor + "]");
                    } else {
                        morningList.append(prefix+j+"."+"Name not Found" + "[" + doctor + "]");
                    }
                    j++;
                    prefix = "\n";
                }
            } else {
                morningCount = morningCount+"(0)";
                morningList.append("No work plan");
            }


            if(eveningWP != null && eveningWP.size() > 0){
                List<String> doctorList = new ArrayList<>();
                for(WPModel wpModel:eveningWP){
                    doctorList.add(wpModel.getDoctorID());
                }
                Set<String> tempList = new HashSet<>(doctorList);
                doctorList.clear();
                doctorList.addAll(tempList);
                eveningCount = eveningCount+"("+doctorList.size()+")";
                int j = 1;
                String prefix = "";
                for(String doctor:doctorList){
                    DoctorsModel doctorsModel = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, doctor).findFirst();
                    if(doctorsModel != null) {
                        eveningList.append(prefix+j+"."+doctorsModel.getName() + "[" + doctor + "]");
                    } else {
                        eveningList.append(prefix+j+"."+"Name not Found" + "[" + doctor + "]");
                    }
                    j++;
                    prefix = "\n";
                }
            } else {
                eveningCount = eveningCount+"(0)";
                eveningList.append("No work plan");
            }

            IWorkPlanModel iWorkPlanModel = new IWorkPlanModel();
            iWorkPlanModel.setEveningCount(eveningCount);
            iWorkPlanModel.setMorningCount(morningCount);
            iWorkPlanModel.setMorningLoc(morningList.toString());
            iWorkPlanModel.setEveningLoc(eveningList.toString());
            iWorkPlanModel.setId(DateTimeUtils.getMonthNumber(i));
            iWorkPlanModel.withIdentifier(i);

            if(!TextUtils.isEmpty(morningList) || !TextUtils.isEmpty(eveningList)){
                iWorkPlanModelArrayList.add(iWorkPlanModel);
            }

        }

        return iWorkPlanModelArrayList;
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
