package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRProductModel;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.reports.model.ProductExecution;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils.getToday;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class DCRDoctorListFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    List<DoctorsModel> doctorsModels;
    @Inject
    Realm r;

    int dcrCount = 0;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;



    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;

    Context context;
    int month, year;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DCRDoctorListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_doctor_dcr, container, false);
        ButterKnife.bind(this, rootView);
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

    @OnClick(R.id.flColorInfo)
    void onClickColorInfo(){
        DialogFragment dialogFragment = new ColorInfoDCRDialog();
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "color_info");
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
            List<DayDot> dotList = getDotExeList(dm.getId(), dateModel);
            dvrIDoctor.setDotList(dotList);
            dvrIDoctor.withIdentifier(i);
            if(dcrCount > 0) {
                dvrIDoctors.add(dvrIDoctor);
            }
            dcrCount = 0;
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

        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDoctorItem>() {
            @Override
            public boolean onClick(View v, IAdapter<IDoctorItem> adapter, IDoctorItem item, int position) {
                if(item.getDotList().size() > 0) {
                    ExecutionDialogFragment newFragment = ExecutionDialogFragment.newInstance(item.getName(), getProductExeList(item.getDotList()));
                    newFragment.show(getFragmentManager(), "dialog");
                }
                return false;
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


    public List<DayDot> getDotExeList(String docId, DateModel dateModel){
        List<DayDot> dotExeList = new ArrayList<>();
        long idG = Long.valueOf(year+DateTimeUtils.getMonthNumber(month)+"000");
        long idL = Long.valueOf(year+DateTimeUtils.getMonthNumber(month)+DateTimeUtils.getMonthNumber(dateModel.getDay())+"0");;
        List<DVRDoctorRealm> dvrDoctorList = r.where(DVRDoctorRealm.class)
                .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, docId)
                .lessThan(DVRDoctorRealm.COL_DVR_LOCAL_ID, idL)
                .greaterThan(DVRDoctorRealm.COL_DVR_LOCAL_ID, idG)
                .sort(DVRDoctorRealm.COL_DVR_LOCAL_ID)
                .findAll();
        if(dvrDoctorList != null && dvrDoctorList.size() > 0){
            for(DVRDoctorRealm dvrDoctorRealm:dvrDoctorList){
                long id = dvrDoctorRealm.getDvrLocalId() % 1000;
                id = id / 10;
                int day = (int) id;
                String date = DateTimeUtils.getMonthNumber(day) +"-"+ DateTimeUtils.getMonthNumber(month)+"-"+ year;
                DCRModel dcrModel = r.where(DCRModel.class)
                        .equalTo(DCRModel.COL_DID, docId)
                        .equalTo(DCRModel.COL_SEND_DATE, date)
                        .findFirst();
                if(dcrModel != null){
                    boolean isAbsent = dcrModel.getStatus().contains(StringConstants.STATUS_ABSENT);
                    if(!isAbsent){
                        dcrCount++;
                    }

                    DayDot dayShift = new DayDot();
                    dayShift.setWeekDay(formatDate(year, month, day));
                    dayShift.setDay(day);
                    dayShift.setNew(false);
                    dayShift.setDcrId(dcrModel.getId());
                    dayShift.setAbsent(isAbsent);
                    dotExeList.add(dayShift);
                } else {
                    DayDot dayShift = new DayDot();
                    dayShift.setWeekDay(formatDate(year, month, day));
                    dayShift.setDay(day);
                    dayShift.setNew(false);
                    dayShift.setDcrId(0);
                    dayShift.setAbsent(true);
                    dotExeList.add(dayShift);
                }
            }
        }
        for(int i = 1; i <= dateModel.getLastDay(); i++) {
            DateModel dateModel1 = new DateModel(i, month, year, 0, dateModel.getLastDay());
            NewDCRModel newDCRModel = r.where(NewDCRModel.class)
                    .equalTo(NewDCRModel.COL_DOCTOR_ID, docId)
                    .equalTo(NewDCRModel.COL_DATE, DateTimeUtils.getDayMonthYear(dateModel1))
                    .findFirst();
            if( newDCRModel != null){
                dcrCount++;
                DayDot dayShift = new DayDot();
                dayShift.setWeekDay(formatDate(year, month, i));
                dayShift.setDay(i);
                dayShift.setAbsent(false);
                dayShift.setNew(true);
                dayShift.setDcrId(newDCRModel.getId());
                dotExeList.add(dayShift);
            }

        }
        return dotExeList;
    }


    public static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return DateTimeUtils.WEEK_DAY_1[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public List<ProductExecution> getProductExeList(List<DayDot> dayDotList){
        Map<String, Integer> products = new HashMap<>();

        for(DayDot dayDot:dayDotList){
            if(dayDot.isNew()){
                List<NewDCRProductModel> newDCRProductModels = r.where(NewDCRProductModel.class)
                        .equalTo(NewDCRProductModel.COL_NEW_DCR_ID, dayDot.getDcrId())
                        .findAll();
                for(NewDCRProductModel newDCRProductModel:newDCRProductModels){
                    if(products.containsKey(newDCRProductModel.getProductID())) {
                        int qty = products.get(newDCRProductModel.getProductID()) + newDCRProductModel.getCount();
                        products.put(newDCRProductModel.getProductID(), qty);
                    } else {
                        products.put(newDCRProductModel.getProductID(), newDCRProductModel.getCount());
                    }
                }
            } else {
                if(!dayDot.isAbsent()){
                    List<DCRProductModel> dcrProductModels = r.where(DCRProductModel.class)
                            .equalTo(DCRProductModel.COL_DCR_ID, dayDot.getDcrId())
                            .findAll();
                    for(DCRProductModel dcrProductModel:dcrProductModels){
                        if(products.containsKey(dcrProductModel.getProductID())){
                            int qty = products.get(dcrProductModel.getProductID()) + dcrProductModel.getQuantity();
                            products.put(dcrProductModel.getProductID(), qty);
                        } else {
                            products.put(dcrProductModel.getProductID(), dcrProductModel.getQuantity());
                        }

                    }
                }
            }
        }

        List<ProductExecution> productExecutions = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : products.entrySet()){
            String productName = getProductName(entry.getKey());
            Integer quantity = entry.getValue();
            ProductExecution productExecution = new ProductExecution();
            productExecution.setProductName(productName);
            productExecution.setCount(quantity);
            if(quantity > 0 && !TextUtils.isEmpty(productName)){
                productExecutions.add(productExecution);
            }
        }
        return productExecutions;
    }

    public String getProductName(String prodKey){
        ProductModel productModel = r.where(ProductModel.class)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_CODE, prodKey)
                .findFirst();
        if(productModel != null){
            return productModel.getName()+"("+productModel.getPackSize()+")";
        } else {
            return "";
        }
    }


}
