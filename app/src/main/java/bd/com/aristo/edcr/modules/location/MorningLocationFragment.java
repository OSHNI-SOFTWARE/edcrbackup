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

public class MorningLocationFragment extends Fragment {

    @BindView(R.id.rvLocation)
    RecyclerView rvLocation;

    Context context;
    List<String> location;

    @Inject
    Realm r;

    DateModel nextDateModel;
    List<LocationReport> locationReportList = new ArrayList<>();

    public MorningLocationFragment() {
    }

    @SuppressLint("ValidFragment")
    public MorningLocationFragment(List<String> location, DateModel nextDateModel) {
        this.nextDateModel = nextDateModel;
        this.location = location;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_location_tab, container, false);
        ButterKnife.bind(this, rootView);
        refreshList();
        return rootView;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

    public void getLocationWiseDoctors(){
        for(String loc:location){
            LocationReport locationReport = new LocationReport();
            locationReport.setLocation(loc);
            locationReport.setDoctorList(getLocationWiseDoctor(loc));
            locationReportList.add(locationReport);
        }

    }

    public String getLocationWiseDoctor(String loc){

        StringBuilder doctors = new StringBuilder();
        List<DoctorsModel> doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, nextDateModel.getMonth())
                .equalTo(DoctorsModel.COL_YEAR, nextDateModel.getYear())
                .equalTo(DoctorsModel.COL_MORNING_LOC, loc)
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
