package bd.com.aristo.edcr.modules.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 7/11/17.
 */

public class NoLocationFragment extends Fragment {

    private static final String TAG = "EveningLocationFragment";


    @BindView(R.id.rvLocation)
    RecyclerView rvLocation;
    List<String> location = new ArrayList<>();
    Context context;

    @Inject
    Realm r;

    DateModel nextDateModel;
    List<LocationReport> locationReportList = new ArrayList<>();

    public NoLocationFragment() {
    }
    @SuppressLint("ValidFragment")
    public NoLocationFragment(DateModel nextDateModel) {
        this.nextDateModel = nextDateModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);

        View rootView = inflater.inflate(R.layout.fragment_location_tab, container, false);
        ButterKnife.bind(this, rootView);
        location.add("No Location");
        refreshList();
        return rootView;
    }






    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void refreshList() {
        getLocationWiseDoctors();
        FastItemAdapter<LocationReport> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(locationReportList);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(true);
        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvLocation.setLayoutManager(layoutManager);
        rvLocation.setAdapter(fastAdapter);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void getLocationWiseDoctors(){
        for(String loc:location){
            LocationReport locationReport = new LocationReport();
            locationReport.setLocation(loc);
            locationReport.setDoctorList(getLocationWiseDoctor());
            locationReportList.add(locationReport);
        }

    }

    public String getLocationWiseDoctor(){
        StringBuilder doctors = new StringBuilder();
        List<DoctorsModel> doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, nextDateModel.getMonth())
                .equalTo(DoctorsModel.COL_YEAR, nextDateModel.getYear())
                .equalTo(DoctorsModel.COL_EVENING_LOC, "")
                .equalTo(DoctorsModel.COL_MORNING_LOC, "")
                .findAll();

        int i = 1;
        String prefix = "";
        for(DoctorsModel dm:doctorsModels){

            String doctorName = "";
            if(dm != null){
                doctorName = prefix+i + ". " + dm.getName() + " [" + dm.getId() + "]";
            } else {
                doctorName = prefix+i+ ". Unknown";
            }
            doctors.append(doctorName);
            prefix = "\n";
            i++;
        }


        return doctors.toString();
    }
}
