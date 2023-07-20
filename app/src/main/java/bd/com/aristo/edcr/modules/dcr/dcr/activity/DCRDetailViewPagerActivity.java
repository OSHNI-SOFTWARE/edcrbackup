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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.dcr.model.IPlanExeModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by monir.sobuj on 6/5/17.
 */
public class DCRDetailViewPagerActivity extends AppCompatActivity {
    @BindView(R.id.rvPlanExe)
    RecyclerView rvPlanExe;
    static List<IPlanExeModel> dcrPlanList = new ArrayList<>();
    static String date1;


    public static Intent start(Context context, List<IPlanExeModel> planExeList, String date){
        dcrPlanList = planExeList;
        date1 = date;
        return new Intent(context, DCRDetailViewPagerActivity.class);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcr_plan_exe);
        ButterKnife.bind(this);
        setTitle("DCR for "+formatDate());
        setAdapter();
    }

    public void setAdapter(){
        FastItemAdapter<IPlanExeModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dcrPlanList);
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

    public String formatDate() {
        String[] day = date1.split("-");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.valueOf(day[2]), Integer.valueOf(day[1]) - 1, Integer.valueOf(day[0]));
        Date dateCon = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EE, d MMM");
        String hireDate = sdf.format(dateCon);
        return hireDate;
    }


}
