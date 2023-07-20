package bd.com.aristo.edcr.modules.pwds;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.modules.reports.model.PWDSReport;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class PWDSListFragment extends Fragment {

    private static final String TAG = "PWDSReportFragment";
    @Inject
    Realm r;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    @BindView(R.id.cardBottom)
    CardView cardBottom;


    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    DateModel dateModel = new DateModel();

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public PWDSListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor, container, false);
        ButterKnife.bind(this, rootView);
        cardBottom.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
        dateModel.setMonth(getArguments().getInt("month"));
        dateModel.setYear(getArguments().getInt("year"));
        setupList();
        setTitle();

        return rootView;
    }


    public void setTitle(){
        ((Activity) context).setTitle("PWDS for "+DateTimeUtils.getFullMonthForInt(dateModel.getMonth()));
    }


    private void setupList() {
        refreshList();
    }





    public void refreshList(){


       final FastItemAdapter<PWDSReport> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(getPWDSReport());
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(false);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<PWDSReport>() {
            @Override
            public boolean onClick(View v, IAdapter<PWDSReport> adapter, PWDSReport item, int position) {
                PWDSUtilsModel pwdsUtilsModel = new PWDSUtilsModel(item.getpCode(), item.getProductName(), item.getCount(), position);
                Fragment fragment = new DoctorsFragment();
                Bundle b = new Bundle();
                b.putSerializable("pwdsUtils", pwdsUtilsModel);
                b.putSerializable("dateModel", dateModel);
                fragment.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("pwds_doctors").commit();
                return false;
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);


    }


    public List<PWDSReport> getPWDSReport(){
        List<PWDSReport> pwdsReportList = new ArrayList<>();
        List<ProductModel> productModelList = PWDSUtils.getPWDSProducts(r, dateModel.getYear(), dateModel.getMonth());
        int i = 0;
        if(productModelList != null && productModelList.size() > 0){
            for(ProductModel productModel: productModelList){
                PWDSReport pwdsReport = new PWDSReport();
                pwdsReport.setProductName(productModel.getName());
                pwdsReport.setProduct(productModel.getName()+"("+productModel.getPackSize()+")"+" [Total Qty."+productModel.getQuantity()+"]");
                pwdsReport.setDoctorList(getPWDSDoctors(productModel.getCode()));
                pwdsReport.setpCode(productModel.getCode());
                pwdsReport.setCount(getPWDSDoctorsCount(productModel.getCode()));
                pwdsReport.withIdentifier(i);
                pwdsReportList.add(pwdsReport);
                i++;
            }
        }

        return pwdsReportList;
    }

    public String getPWDSDoctors(String code){
        StringBuilder doctors = new StringBuilder();
        List<PWDSModel> pwdsModels = r.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_PRODUCT_ID, code)
                .equalTo(PWDSModel.COL_MONTH, dateModel.getMonth())
                .equalTo(PWDSModel.COL_YEAR, dateModel.getYear())
                .findAll();
        if(pwdsModels != null && pwdsModels.size() > 0){
            int i = 1;
            String prefix = "";
            for(PWDSModel pwdsModel:pwdsModels){
                String doctorName = "";
                DoctorsModel doctorsModel = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, pwdsModel.getDoctorID()).findFirst();
                if(doctorsModel != null){
                    doctorName = prefix+i + ". " + doctorsModel.getName() + " [" + doctorsModel.getId() + "]";
                } else {
                    doctorName = prefix+i+ ". Unknown";
                }
                doctors.append(doctorName);
                prefix = "\n";
                i++;
            }
        }

        return doctors.toString();
    }

    public int getPWDSDoctorsCount(String code){
        StringBuilder doctors = new StringBuilder();
        List<PWDSModel> pwdsModels = r.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_PRODUCT_ID, code)
                .equalTo(PWDSModel.COL_MONTH, dateModel.getMonth())
                .equalTo(PWDSModel.COL_YEAR, dateModel.getYear())
                .findAll();
        if(pwdsModels != null && pwdsModels.size() > 0){
           return pwdsModels.size();
        }

        return 0;
    }


    @Override
    public void onPause() {
        super.onPause();
        //onCancel();
    }
}
