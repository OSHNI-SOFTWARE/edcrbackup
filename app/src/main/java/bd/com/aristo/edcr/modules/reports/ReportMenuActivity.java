package bd.com.aristo.edcr.modules.reports;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.HomeActivity;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.ui.ReportMainMenuModel;
import bd.com.aristo.edcr.utils.RedirectUtils;
import bd.com.aristo.edcr.utils.TempData;
import bd.com.aristo.edcr.utils.constants.MainMenuConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportMenuActivity extends AppCompatActivity {

    Point activityWH;
    LinearLayout root;
    Intent intent                                       = null;


    //dependency injections
    @Inject
    App context;

    @Inject
    List<ReportMainMenuModel> mainMenuModels;

    //view injections
    @BindView(R.id.mainMenu)
    RecyclerView mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        App.getComponent().inject(this);

        // Get the layout id
        root                                            = (LinearLayout) findViewById(R.id.root);
        root.post(new Runnable() {
            public void run() {
                MainMenuConstants.getInstance().setActivityWH(ReportMenuActivity.this);   //get activity width height
                setupMenuGrids();
            }
        });
    }

    private void setupMenuGrids(){
        //init colors

        String[] colors                                 = getResources().getStringArray(R.array.menuColors);
        TempData.MAIN_MENU_BG_COLORS                    = new int[colors.length];
        for(int i = 0; i < colors.length; i++){
            TempData.MAIN_MENU_BG_COLORS[i]             = Color.parseColor(colors[i]);
        }

        //init fast adapter
        final FastItemAdapter<ReportMainMenuModel> fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(mainMenuModels);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ReportMainMenuModel>() {
            @Override
            public boolean onClick(View v, IAdapter<ReportMainMenuModel> adapter, ReportMainMenuModel item, int position) {
                //Toast.makeText(MainMenuActivity.this, "pos: " + position, Toast.LENGTH_SHORT).show();
                if(position == 0){
                    RedirectUtils.go(context, HomeActivity.class, true, "HOME"); // home
                } else if(position == 1) {
                    RedirectUtils.go(context, ReportActivity.class, false, "SAMPLE_STATEMENT");
                } else if(position == 2) {
                    RedirectUtils.go(context, ReportActivity.class, false, "DOCTOR_COVERAGE");
                } else if(position == 3) {
                    RedirectUtils.go(context, ReportActivity.class, false, "DOCTOR_DCR");
                } else if(position == 4) {
                    RedirectUtils.go(context, ReportActivity.class, false, "DOCTOR_NO_DCR");
                } else if(position == 5){
                    RedirectUtils.go(context, ReportActivity.class, false, "DCR_SUMMARY");// wp
                } else if(position == 6){
                    RedirectUtils.go(context, ReportActivity.class, false, "ACCOMPANY");
                    //ToastUtils.longToast("Abolished Report...");
                } else if(position == 7){
                    RedirectUtils.go(context, ReportActivity.class, false, "PSC");
                    //RedirectUtils.go(context, ReportActivity.class, false, "ACCOMPANY"); // doctor coverage
                } else if(position == 8){
                    //RedirectUtils.go(context, ReportActivity.class, false, "PSC"); //  sample statement
                }
                return true;
            }
        });

        //set layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        mainMenu.setLayoutManager(layoutManager);
        mainMenu.setAdapter(fastItemAdapter);
    }
}
