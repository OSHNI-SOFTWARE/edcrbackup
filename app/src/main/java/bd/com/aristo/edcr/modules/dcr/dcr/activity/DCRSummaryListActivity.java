package bd.com.aristo.edcr.modules.dcr.dcr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.IPlanExeModel;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */
public class DCRSummaryListActivity extends AppCompatActivity {
    @BindView(R.id.rvPlanExe)
    RecyclerView rvPlanExe;
    @BindView(R.id.txtDCRCount)
    ATextView txtDCRCount;
    @BindView(R.id.txtNewDCRCount)
    ATextView txtNewDCRCount;
    @BindView(R.id.txtAbsentCount)
    ATextView txtAbsentCount;
    @Inject
    Realm r;
    static List<IPlanExeModel> planExeModels= new ArrayList<>();
    static int monthNum, yearNum;


    public static void start(Context context, List<IPlanExeModel> iPlanExeModels, int month, int year) {
        planExeModels= new ArrayList<>();
        monthNum = month;
        yearNum = year;
        Collections.sort(iPlanExeModels, new Comparator<IPlanExeModel>() {
            public int compare(IPlanExeModel o1, IPlanExeModel o2) {

                return o2.getShift().compareTo(o1.getShift());
            }
        });
        Collections.sort(iPlanExeModels, new Comparator<IPlanExeModel>() {
            public int compare(IPlanExeModel o1, IPlanExeModel o2) {

                return o2.getDate().compareTo(o1.getDate());
            }
        });


        planExeModels.addAll(iPlanExeModels);
        context.startActivity(new Intent(context, DCRSummaryListActivity.class));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcr_summary);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        setTitle("DCR List");
        setAdapter();
        int absentCount = 0;
        txtNewDCRCount.setText(""+DCRUtils.getMonthlyNewDcrCount(r, monthNum, yearNum));
        List<DCRModel> dcrModelsMonth = DCRUtils.getDCRListMonthForSummary(r, monthNum, yearNum);

        for(DCRModel dcrModel:dcrModelsMonth){
            if(dcrModel.getStatus().equalsIgnoreCase(StringConstants.STATUS_ABSENT)){
                absentCount++;
            }
        }
        txtDCRCount.setText(""+(dcrModelsMonth.size() - absentCount));
        txtAbsentCount.setText(""+absentCount);
    }

    public void setAdapter() {
        FastItemAdapter<IPlanExeModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(planExeModels);
        fastAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvPlanExe.setLayoutManager(layoutManager);
        rvPlanExe.setAdapter(fastAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //btnOk();
            onBackPressed();
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
