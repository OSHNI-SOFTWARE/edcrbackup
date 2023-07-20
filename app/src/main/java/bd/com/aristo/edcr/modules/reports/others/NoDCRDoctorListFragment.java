package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
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
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils.getToday;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class NoDCRDoctorListFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    List<DoctorsModel> doctorsModels;
    @Inject
    Realm r;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    @BindView(R.id.cardBottom)
    CardView cardBottom;


    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;

    Context context;
    int month, year;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public NoDCRDoctorListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor, container, false);
        ButterKnife.bind(this, rootView);
        cardBottom.setVisibility(View.GONE);
        setHasOptionsMenu(false);
        if(getArguments() != null){
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");
            setupList();
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }

        return rootView;
    }


    public void setTitle(long count){
        ((Activity) context).setTitle("Doctors for "+DateTimeUtils.getMonthForInt(month)+"("+count+")");
    }


    private void setupList() {
        doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .sort(DoctorsModel.COL_NAME)
                .findAll();
        refreshList();
    }

    public void refreshList(){
        DateModel dateModel = getToday();
        List<IDoctorItem> dvrIDoctors = new ArrayList<>();
        int i = 10;
        for(DoctorsModel dm: doctorsModels){
            IDoctorItem dvrIDoctor = new IDoctorItem();
            dvrIDoctor.setId(dm.getId());
            dvrIDoctor.setName(dm.getName());
            dvrIDoctor.setSpecial(dm.getSpecial());
            dvrIDoctor.setDegree(dm.getDegree());
            dvrIDoctor.setmLoc(dm.getMorningLoc());
            dvrIDoctor.seteLoc(dm.getEveningLoc());
            dvrIDoctor.setDotList(new ArrayList<DayDot>());
            dvrIDoctor.withIdentifier(i);
            boolean isDCRExist = isDCRExist(dm.getId(), dateModel);
            if(!isDCRExist) {
                dvrIDoctors.add(dvrIDoctor);
            }
            i++;
        }
       final FastItemAdapter<IDoctorItem> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dvrIDoctors);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDoctorItem>() {
            @Override
            public boolean filter(IDoctorItem item, CharSequence constraint) {
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

    @Override
    public void onPause() {
        super.onPause();
    }


    public boolean isDCRExist(String docId, DateModel dateModel){
        boolean isDCR = false;
        boolean isNewDCR = false;
        DCRModel dcrModel = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_DID, docId)
                .equalTo(DCRModel.COL_MONTH, month)
                .equalTo(DCRModel.COL_YEAR, year)
                .findFirst();
        if(dcrModel != null){
            isDCR = true;
        }

        for(int i = 1; i <= dateModel.getLastDay(); i++) {
            DateModel dateModel1 = new DateModel(i, month, year, 0, dateModel.getLastDay());
            NewDCRModel newDCRModel = r.where(NewDCRModel.class)
                    .equalTo(NewDCRModel.COL_DOCTOR_ID, docId)
                    .equalTo(NewDCRModel.COL_DATE, DateTimeUtils.getDayMonthYear(dateModel1))
                    .findFirst();
            if( newDCRModel != null){
                isNewDCR = true;
                break;
            }

        }
        return isDCR||isNewDCR;
    }


}
