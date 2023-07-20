package bd.com.aristo.edcr;

import android.graphics.Color;
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

import bd.com.aristo.edcr.models.ui.MainMenuModel;
import bd.com.aristo.edcr.modules.bill.fragment.BillStatementActivity;
import bd.com.aristo.edcr.modules.dvr.fragment.DVRActivity;
import bd.com.aristo.edcr.modules.location.DoctorLocationActivity;
import bd.com.aristo.edcr.modules.reports.ReportMenuActivity;
import bd.com.aristo.edcr.utils.RedirectUtils;
import bd.com.aristo.edcr.utils.TempData;
import bd.com.aristo.edcr.utils.constants.MainMenuConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends AppCompatActivity {

    LinearLayout root;


    //dependency injections
    @Inject
    App context;

    @Inject
    List<MainMenuModel> mainMenuModels;

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
                MainMenuConstants.getInstance().setActivityWH(MainMenuActivity.this);   //get activity width height
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
        final FastItemAdapter<MainMenuModel> fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(mainMenuModels);
        fastItemAdapter.withSelectable(true);
        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<MainMenuModel>() {
            @Override
            public boolean onClick(View v, IAdapter<MainMenuModel> adapter, MainMenuModel item, int position) {
                //Toast.makeText(MainMenuActivity.this, "pos: " + position, Toast.LENGTH_SHORT).show();
                if(position == 0){
                    RedirectUtils.go(MainMenuActivity.this, DoctorLocationActivity.class, false, "location"); // location
                } else if(position == 1) {
                    RedirectUtils.go(context, HostActivity.class, false, "tp"); // Tour Plan
                } else if(position == 2){
                    RedirectUtils.go(context, DVRActivity.class, false, "dvr"); // D V R
                } else if(position == 3){
                    RedirectUtils.go(context, HostActivity.class, false, "pwds"); // P W D S
                } else if(position == 4){
                    RedirectUtils.go(context, HostActivity.class, false, "gwds"); // G W D S
                } else if(position == 5){
                    RedirectUtils.go(context, HostActivity.class, false, "wp"); //  Work Plan
                } else if(position == 6){
                    RedirectUtils.go(context, HostActivity.class, false, "dss"); //  Day Sample Summary
                } else if(position == 7){
                    RedirectUtils.go(context, ReportMenuActivity.class, false, "ss"); //  Sample Statement
                } else if(position == 8){
                    RedirectUtils.go(context, BillStatementActivity.class, false, "bill"); //  Bill Statement
                    //ToastUtils.shortToast(StringConstants.UNDER_DEV_MSG);
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
