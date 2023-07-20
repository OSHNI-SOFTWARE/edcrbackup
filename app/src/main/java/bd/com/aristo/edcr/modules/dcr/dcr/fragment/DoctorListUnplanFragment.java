package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRDoctorUnplanModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class DoctorListUnplanFragment extends Fragment {

    @Inject
    Realm r;
    @BindView(R.id.txtToday)
    ATextView txtToday;
    @BindView(R.id.rvDoctors)
    RecyclerView rv;
    public DateModel dateModel;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DoctorListUnplanFragment() {
    }
    public static DoctorListUnplanFragment newInstance(){

        return new DoctorListUnplanFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_today_doctor, container, false);
        ButterKnife.bind(this, rootView);
        dateModel = DCRUtils.getToday();
        txtToday.setText(""+dateModel.getDay()+
                " "+DateTimeUtils.getMonthForInt(dateModel.getMonth())
                +", "+dateModel.getYear());
        refreshList();
        return rootView;
    }

    public void refreshList(){
        List<DCRDoctorUnplanModel> dcrDoctors = DCRUtils.getDCRDoctorsUnplanned(r, DoctorListViewPager.shift);
        FastItemAdapter<DCRDoctorUnplanModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dcrDoctors);
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DCRDoctorUnplanModel>() {
            @Override
            public boolean onClick(View v, IAdapter<DCRDoctorUnplanModel> adapter, final DCRDoctorUnplanModel item, int position) {

                Log.e("DoctorListFragment", ""+item.getId());
                //final Fragment fragment = new DCRViewPager();
                DCRUtils.DCR_IS_MORNING = item.isMorning();
                DCRUtils.SHIFT = item.isMorning()? StringConstants.MORNING:StringConstants.EVENING;
                DCRUtils.DOCTOR_ID = item.getId();
                DCRUtils.DOCTOR_NAME = item.getName();
                if(item.getId().contains("I")){
                    ToastUtils.displayAlert(((AppCompatActivity) context), "Enter detail of intern and create plan from work plan!!");
                } else {
                    Fragment fragment = new DCRViewPager();
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("dcr_view_pager_fragment").commit();

                }

                return false;
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(fastAdapter);
    }



}
