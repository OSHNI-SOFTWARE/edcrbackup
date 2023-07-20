package bd.com.aristo.edcr.modules.dss;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/11/17.
 */

public class DSSFragment extends Fragment {

    @BindView(R.id.dssList)
    RecyclerView dssRecyclerView;
    @BindView(R.id.btnDone)
    ATextView btnDone;
    @BindView(R.id.rgMorEve)
    RadioGroup rgMorEve;

    @BindView(R.id.morning)
    RadioButton morning;

    @BindView(R.id.evening)
    RadioButton evening;

    @BindView(R.id.totalTV)
    ATextView totalTV;

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    boolean isMorning = true;


    DSSAdapterSectionRecycler dssAdapterRecycler;
    public DateModel dateModel;

    List<DSSModel> dssModelList = new ArrayList<>();

    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public DSSFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_dss_product, container, false);
        ButterKnife.bind(this, rootView);
        assert getArguments() != null;
        dateModel = (DateModel) getArguments().getSerializable("dateModel");
        setUpList(isMorning);
        rgMorEve.setOnCheckedChangeListener((group, checkedId) -> {
            isMorning = checkedId == R.id.morning;
            setUpList(isMorning);

        });

        ((Activity) context).setTitle("DSS for "+dateModel.getDay() +" "+ DateTimeUtils.getMonthForInt(dateModel.getMonth()));

        return rootView;
    }

    public void setUpList(boolean isMorning){
        dssModelList.clear();
        dssModelList = WPUtils.getDSSList(r, isMorning, dateModel);

        generateList();

    }

    @OnClick(R.id.btnDone)
    public void done() {
        ((Activity) context).onBackPressed();
    }

   public void generateList(){

        int count = 0;
        for(DSSModel dssModel:dssModelList){
            count += dssModel.getCount();
        }

       String productCount = dssModelList.size() +"/ <span style=\"color:#01991f;\">"+ count +"</span> ";
       totalTV.setText(Html.fromHtml(productCount));

       //setLayout Manager
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
       dssRecyclerView.setLayoutManager(linearLayoutManager);
       dssRecyclerView.setHasFixedSize(true);



       //Create a List of Child DataModel
       List<DSSModel> selectedDssList = new ArrayList<>();
       List<DSSModel> sampleDssList = new ArrayList<>();
       List<DSSModel> giftDssList = new ArrayList<>();

       for (DSSModel dssModel:dssModelList){
           // 0 for Selective, 1 for Sample, 2 for Gift
           if (dssModel.getFlag()==0){
               selectedDssList.add(dssModel);
           }else if (dssModel.getFlag()==1){
               sampleDssList.add(dssModel);
           }else {
               giftDssList.add(dssModel);
           }

       }

       //Create a List of Section DataModel implements Section
       List<SectionHeader> sections = new ArrayList<>();
       sections.add(new SectionHeader(selectedDssList, "Selected"));
       sections.add(new SectionHeader(sampleDssList, "Sample"));
       sections.add(new SectionHeader(giftDssList, "Gift"));

       dssAdapterRecycler = new DSSAdapterSectionRecycler(context, sections);
       dssRecyclerView.setAdapter(dssAdapterRecycler);

   }




}
